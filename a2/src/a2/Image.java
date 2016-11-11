package a2;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;

/** Manages an image file and tag add/remove operations */
public class Image implements Closeable{
	/** Location of the image file*/ 
	private File imgFile;
	/** Tags associated with this image file */
	public HashSet<String> tags;
	/** Change log associated with this image file */
	private History log;

	/**
	 * Create new Image object using a path
	 * 
	 * @param path
	 *            Path to the image file
	 */
	public Image(String path) throws FileNotFoundException{
		imgFile = new File(path);
		if (imgFile.exists()){
			this.log = new History(this);
			this.updateTags();
		} else {
			throw new FileNotFoundException(path+ " does not exist.");
		}
	}

	/**
	 * Reads Tags from the name of imgFile. Based Tags.PREFIX. It is assumed that tags are contained
	 * after the first instance of Tags.PREFIX.
	 */
	private void updateTags() {
		String dot = ".";
		String tagsInName = imgFile.getName();
		try {
			tagsInName = tagsInName.substring(tagsInName.indexOf(Tags.PREFIX) + 1, tagsInName.indexOf(dot));
			tags = new HashSet<String>(Arrays.asList(tagsInName.split(Tags.PREFIX)));
			for (String tag : tags) {
				this.addTag(tag.trim());
			}
		} catch (StringIndexOutOfBoundsException e) {
			tags = new HashSet<String>();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getName(){
		return imgFile.getName();
	}
	
	public String getPath(){
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
	public String imageName() {
		String name = imgFile.getName();
		try {
			return name.substring(0, name.indexOf(Tags.PREFIX));
		} catch (StringIndexOutOfBoundsException e) {
			try {
				return name.substring(0, name.indexOf("."));
			} catch (StringIndexOutOfBoundsException eh) {
				return name;
			}
		}
	}

	public boolean addTag(String tag) {
		if (tags.add(tag)) {
			String dot = "\\.";
			String path =  this.getPath();
			String[] nameAndExtension = path.split(dot);
			String newName = nameAndExtension[0] + Tags.PREFIX + tag + "." + nameAndExtension[1];
			Tags.addTag(tag);
			File newFile = new File(newName);
			boolean success = imgFile.renameTo(newFile);
			if(success){
				imgFile = newFile;
				log.newChange(this.getName());
			}
			return success;
		} else {
			return false;
		}
	}

	public boolean delTag(String tag) {
		if (tags.contains(tag)) {
			String newName = this.getPath();
			newName.replaceFirst(Tags.PREFIX + tag, "");
			tags.remove(tag);
			File newFile = new File(newName);
			if(imgFile.renameTo(newFile)){
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

	public boolean revertName(int n) {
		String name = log.unChange(n);
		if (name != null) {
			File newFile = new File(name);
			imgFile.renameTo(newFile);
			if(imgFile.renameTo(newFile)){
				log.newChange(name);
				imgFile = newFile;
				this.updateTags();
				return true;
			}else{
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean revertName(Timestamp time) {
		String name = log.unChange(time);
		if (name != null) {
			File newFile = new File(name);
			imgFile.renameTo(newFile);
			if(imgFile.renameTo(newFile)){
				imgFile = newFile;
				log.newChange(name);
				this.updateTags();
				return true;
			}else{
				return false;
			}
		} else {
			return false;
		}
	}
	
	@Override
	public void close(){
		imgFile = null;
		tags = null;
		log.close();
	}
}
