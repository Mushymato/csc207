package backend;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

/** Manages an image file and tag add/remove operations */
public class Image implements Closeable {
	/** Location of the image file */
	private File imgFile;
	/** Tags associated with this image file */
	protected HashSet<String> imgTags = new HashSet<String>();
	/** Change log associated with this image file */
	private History imgHistory;

	/**
	 * Create new Image object using a path
	 * 
	 * @param path
	 *            Path to the image file
	 * @throws FileNotFoundException
	 *             If file does not exist or file does not have a extension
	 */
	protected Image(String path) throws FileNotFoundException, IllegalArgumentException {
		imgFile = new File(path);
		// Only initialize Image if file exist, file is a File, and file name
		// contains a '.'
		if (imgFile.exists()) {
			String[] types = ImageIO.getReaderFileSuffixes();
			boolean isImage = false;
			for (int i = 0; i < types.length; i++) {
				if (path.endsWith(types[i])) {
					isImage = true;
					break;
				}
			}
			if (isImage) {
				this.imgHistory = new History(this);
				this.updateTags();
			} else {
				throw new IllegalArgumentException(imgFile.getName() + " is not an image file.");
			}
		} else {
			throw new FileNotFoundException(imgFile.getName() + " does not exist.");
		}
	}

	protected Image(File img) throws FileNotFoundException, IllegalArgumentException {
		if (img.exists()) {
			String[] types = ImageIO.getReaderFileSuffixes();
			boolean isImage = false;
			for (int i = 0; i < types.length; i++) {
				if (img.getAbsolutePath().endsWith(types[i])) {
					isImage = true;
					break;
				}
			}
			if (isImage) {
				this.imgFile = img;
				this.imgHistory = new History(this);
				this.updateTags();
			} else {
				throw new IllegalArgumentException(img.getName() + " is not an image file.");
			}
		} else {
			throw new FileNotFoundException(img.getName() + " does not exist.");
		}
	}

	/**
	 * Reads Tags from the name of imgFile. Based Tags.PREFIX. It is assumed
	 * that tags are contained after the first instance of Tags.PREFIX. Update
	 * any
	 */
	private void updateTags() {
		String dot = ".";
		String tagsInName = imgFile.getName();
		if (!tagsInName.contains(Tags.PREFIX)) {
			for (String tag : imgTags) {
				Tags.removeTag(tag);
			}
			imgTags = new HashSet<String>();
		} else {
			try {
				tagsInName = tagsInName.substring(tagsInName.indexOf(Tags.PREFIX) + 1, tagsInName.indexOf(dot));
				List<String> newTags = Arrays.asList(tagsInName.split(Tags.PREFIX));
				for (int i = 0; i < newTags.size(); i++) {
					newTags.set(i, newTags.get(i).trim());
					if (!imgTags.contains(newTags.get(i))) {
						// Add any tags not already in imgTags
						Tags.newTag(newTags.get(i));
						imgTags.add(newTags.get(i));
					}
				}
				Iterator<String> it = imgTags.iterator();
				while (it.hasNext()) {
					String tag = it.next();
					if (!newTags.contains(tag)) {
						// Remove any tags that will no longer be tagged to this
						// image
						Tags.removeTag(tag);
						it.remove();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Get the name of the image.
	 * 
	 * @return Name of the image file
	 */
	public String getName() {
		return imgFile.getName();
	}

	/**
	 * Get canonical path, and return it if it exists. Otherwise return the
	 * absolute path.
	 * 
	 * @return Path the to imgFile.
	 */
	public String getPath() {
		try {
			return imgFile.getCanonicalPath();
		} catch (IOException e) {
			return imgFile.getAbsolutePath();
		}
	}

	/**
	 * Get the name of this image without tags and file extensions
	 * 
	 * @return
	 */
	protected String imageName() {
		String name = imgFile.getName();
		try {
			return name.substring(0, name.indexOf(Tags.PREFIX));
		} catch (IndexOutOfBoundsException e) {
			try {
				return name.substring(0, name.indexOf("."));
			} catch (IndexOutOfBoundsException eh) {
				return name;
			}
		}
	}

	/**
	 * Insert a tag into the name of the name of the image. Add the tag to the
	 * set of tags associated with this image.
	 * 
	 * @param tag
	 *            The tag to be added to this Image.
	 * @return true if a tag was successfully added to this Image.
	 */
	public boolean addTag(String tag) {
		if (imgTags.add(tag)) {
			String dot = "\\.";
			String path = this.getPath();
			String[] nameAndExtension = path.split(dot);
			String newName = nameAndExtension[0] + Tags.PREFIX + tag + "." + nameAndExtension[1];
			Tags.newTag(tag);
			File newFile = new File(newName);
			boolean success = imgFile.renameTo(newFile);
			if (success) {
				imgFile = newFile;
				imgHistory.newChange(this.getName());
			}
			return success;
		} else {
			return false;
		}
	}

	/**
	 * Remove a tag from the name of the image, remove it from the set of tags
	 * associated with this image. Does nothing if this image does not have the
	 * specified tag.
	 * 
	 * @param tag
	 *            The tag to be removed from this Image.
	 * @return true if a tag was successfully added to this Image, false if
	 *         removal unsuccessful.
	 */
	public boolean delTag(String tag) {
		if (imgTags.contains(tag)) {
			String newName = this.getPath().replaceFirst(Tags.PREFIX + tag, "");
			imgTags.remove(tag);
			File newFile = new File(newName);
			if (imgFile.renameTo(newFile)) {
				Tags.removeTag(tag);
				imgFile = newFile;
				imgHistory.newChange(this.getName());
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Revert the image's name to n changes ago. The current change is
	 * considered 0 change ago, the change before is 1 changes ago, the first
	 * undone change is -1, etc.
	 * 
	 * @param n
	 * @return true iff reversion successful
	 */
	public boolean revertName(int n) {
		String name = null;
		if (n < 0) {
			name = imgHistory.reChange(n);
		} else if (n > 0) {
			name = imgHistory.unChange(n);
		} else {
			return true;
		}
		name = imgHistory.unChange(n);
		if (name != null) {
			File newFile = new File(imgFile.getParentFile().getAbsolutePath() + "\\" + name);
			if (imgFile.renameTo(newFile)) {
				imgFile = newFile;
				this.updateTags();
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Remove all tags from the image name, does nothing if there are no tags.
	 * 
	 * @return true iff revert successful.
	 */
	public boolean revertToOriginal() {
		String name = this.getPath();
		if (name.contains(Tags.PREFIX) && name.contains(".")) {
			name = name.substring(0, name.indexOf(Tags.PREFIX)) + name.substring(name.indexOf("."));
		} else { // no tags
			return true;
		}
		File newFile = new File(name);
		imgFile.renameTo(newFile);
		if (imgFile.renameTo(newFile)) {
			imgHistory.newChange(name);
			imgFile = newFile;
			this.updateTags();
			return true;
		} else {
			return false;
		}
	}

	public Map<Timestamp, String> getLog() {
		return this.imgHistory.getLog();
	}

	public Map<Timestamp, String> getRedo() {
		return this.imgHistory.getRedo();
	}
	
	public File getImageFile(){
		return this.imgFile;
	}
	
	public boolean deleteLog(){
		return this.imgHistory.delete();
	}

	public List<String> getTags(){
		return new ArrayList<String>(imgTags);
	}
	
	@Override
	public void close() {
		imgFile = null;
		imgTags = null;
		imgHistory.close();
	}
}
