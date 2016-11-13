package a2;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

/** Manages an image file and tag add/remove operations */
public class Image implements Closeable {
	/** Location of the image file */
	private File imgFile;
	/** Tags associated with this image file */
	protected HashSet<String> tags;
	/** Change log associated with this image file */
	protected History log;

	/**
	 * Create new Image object using a path
	 * 
	 * @param path
	 *            Path to the image file
	 * @throws FileNotFoundException
	 * 			If file does not exist or file does not have a extension
	 */
	protected Image(String path) throws FileNotFoundException {
		imgFile = new File(path);
		// Only initialize Image if file exist, file is a File, and file name
		// contains a '.'
		if (imgFile.exists()) {
			if (imgFile.isFile() && imgFile.getName().contains(".")) {
				this.log = new History(this);
				this.updateTags();
			} else {
				throw new FileNotFoundException(path + " is not an image file.");
			}
		} else {
			throw new FileNotFoundException(path + " does not exist.");
		}
	}

	/**
	 * Reads Tags from the name of imgFile. Based Tags.PREFIX. It is assumed
	 * that tags are contained after the first instance of Tags.PREFIX.
	 */
	private void updateTags() {
		String dot = ".";
		String tagsInName = imgFile.getName();
		if (!tagsInName.contains(Tags.PREFIX)) {
			tags = new HashSet<String>();
		} else {
			try {
				tagsInName = tagsInName.substring(tagsInName.indexOf(Tags.PREFIX) + 1, tagsInName.indexOf(dot));
				String[] tagsArray = tagsInName.split(Tags.PREFIX);
				for (int i = 0; i < tagsArray.length; i++) {
					tagsArray[i] = tagsArray[i].trim();
					Tags.newTag(tagsArray[i]);
				}
				tags = new HashSet<String>(Arrays.asList(tagsArray));
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
	protected String getName() {
		return imgFile.getName();
	}

	/**
	 * Get canonical path, and return it if it exists. Otherwise return the
	 * absolute path.
	 * 
	 * @return Path the to imgFile.
	 */
	protected String getPath() {
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
	 * Insert a tag into the name of the name of the image. Add the tag to the set of tags associated with this image.
	 * 
	 * @param tag
	 *            The tag to be added to this Image.
	 * @return true if a tag was successfully added to this Image.
	 */
	protected boolean addTag(String tag) {
		if (tags.add(tag)) {
			String dot = "\\.";
			String path = this.getPath();
			String[] nameAndExtension = path.split(dot);
			String newName = nameAndExtension[0] + Tags.PREFIX + tag + "." + nameAndExtension[1];
			Tags.newTag(tag);
			File newFile = new File(newName);
			boolean success = imgFile.renameTo(newFile);
			if (success) {
				imgFile = newFile;
				log.newChange(this.getName());
			}
			return success;
		} else {
			return false;
		}
	}
	/**
	 * Remove a tag from the name of the image, remove it from the set of tags associated with this image.
	 * Does nothing if this image does not have the specified tag.
	 * @param tag
	 * 		The tag to be removed from this Image.
	 * @return true if a tag was successfully added to this Image, false if removal unsuccessful.
	 */
	protected boolean delTag(String tag) {
		if (tags.contains(tag)) {
			String newName = this.getPath().replaceFirst(Tags.PREFIX + tag, "");
			tags.remove(tag);
			File newFile = new File(newName);
			if (imgFile.renameTo(newFile)) {
				Tags.removeTag(tag);
				imgFile = newFile;
				log.newChange(newName);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * Revert the image's name to n changes ago. 
	 * The current change is considered 1 change ago, the change before is 2 changes ago, and so on.
	 * @param n
	 * @return true iff reversion successful
	 */
	protected boolean revertName(int n) {
		String name = log.unChange(n);
		if (name != null) {
			File newFile = new File(imgFile.getParentFile().getAbsolutePath() + "\\" + name);
			if (imgFile.renameTo(newFile)) {
				log.newChange(name);
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
	protected boolean revertToOriginal() {
		String name = this.getPath();
		if (name.contains(Tags.PREFIX) && name.contains(".")) {
			name = name.substring(0, name.indexOf(Tags.PREFIX)) + name.substring(name.indexOf("."));
		} else { // no tags
			return true;
		}
		File newFile = new File(name);
		imgFile.renameTo(newFile);
		if (imgFile.renameTo(newFile)) {
			log.newChange(name);
			imgFile = newFile;
			this.updateTags();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void close() {
		imgFile = null;
		tags = null;
		log.close();
	}
}
