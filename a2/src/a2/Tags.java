package a2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;

/**
 * Manage known tags and track what tags have been used for which images Consist
 * solely of static attributes and methods, should not be instantiated
 */
public class Tags {

	/** Prefix of a tag */
	public static String PREFIX = "@";
	/** Set of tags */
	private static HashSet<String> tagSet = new HashSet<String>();
	/** Location of the tags file */
	private static String tagSetPath = "data/tags";

	/**
	 * Load tags from the tags file Read and add each line as a tag. Each line is
	 * one single tag
	 * 
	 * @param path
	 *            Path of the .tag file
	 */
	public static void loadTags(/*String tagSetPath*/){
		String line = "";
		// Reader to load file
		BufferedReader br = null;
		try {
			// Attempt to read from file
			br = new BufferedReader(new FileReader(tagSetPath));
			/*Tags.tagSetPath = tagSetPath;*/
			while ((line = br.readLine()) != null) {
				// Parse each line to a tag
				tagSet.add(line.trim());
			}
		} catch (FileNotFoundException e){
			Tags.writeTagList();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Make sure to close the reader
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Add a new tag to tagMap if there are no tags of the same name
	 * 
	 * @param tag
	 * @return true if new tag successfully added
	 */
	public static boolean addTag(String tag) {
		if(tagSet.add(tag)){
			Tags.writeTagList();
			return true;
		} else {
			return false;
		}
	}
	
	public static HashSet<String> getTags(){
		return new HashSet<String>(tagSet);
	}

	/**
	 * Write tags to a .tag file
	 */
	public static void writeTagList() {
		PrintWriter wr = null;
		try {
			wr = new PrintWriter(tagSetPath, "UTF-8");
			for (String tag : tagSet) {
				wr.write(tag);
				wr.write("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (wr != null) {
				try {
					wr.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
