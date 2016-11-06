package a2;

import java.io.File;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;

// Image will not be serialized
@SuppressWarnings("serial")
/** Manages an image file and tag add/remove operations */
public class Image extends File {
	/** Tags associated with this image file */
	private HashSet<String> tags;
	/** Change log associated with this image file */
	private History log;

	/**
	 * Create new Image object using a path
	 * 
	 * @param path
	 *            Path to the image file
	 */
	public Image(String path) {
		super(path);
		this.log = new History(this);
		this.updateTags();
	}
	
	private void updateTags(){
		String dot = ".";
		String tagsInName = this.getName();
		try {
			tagsInName = tagsInName.substring(tagsInName.indexOf(Tags.PREFIX) + 1, tagsInName.indexOf(dot));
			tags = new HashSet<String>(Arrays.asList(tagsInName.split(Tags.PREFIX)));
		} catch (IndexOutOfBoundsException e) {
			tags = new HashSet<String>();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the name of this image without tags
	 * 
	 * @return
	 */
	public String imageName() {
		String name = this.getName();
		return name.substring(0, name.indexOf(Tags.PREFIX));
	}

	public boolean newTag(String tag) {
		if (tags.add(tag)) {
			String dot = ".";
			String newName = this.getName();
			newName = newName.substring(0, newName.indexOf(dot)) + Tags.PREFIX + tag;
			// TODO: Update History
			Tags.tagSet.add(tag);
			return this.renameTo(new File(newName));
		} else {
			return false;
		}
	}

	public boolean delTag(String tag) {
		if (tags.contains(tag)) {
			String dot = ".";
			String newName = this.getName();
			newName = newName.substring(0, newName.indexOf(dot));
			newName.replaceFirst(Tags.PREFIX + tag, "");
			// TODO: Update History
			tags.remove(tag);
			return this.renameTo(new File(newName));
		} else {
			return false;
		}
	}

	public boolean revertName() {
		String name = log.unChange();
		if (name != null) {
			this.renameTo(new File(name));
			this.updateTags();
			return true;
		} else {
			return false;
		}
	}
	
	public boolean revertName(Timestamp time){
		String name = log.unChange(time);
		if (name != null){
			this.renameTo(new File(name));
			this.updateTags();
			return true;
		} else {
			return false;
		}
	}
}
