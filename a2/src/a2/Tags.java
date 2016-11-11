package a2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

/**
 * Manage known tags and track what tags have been used for which images Consist
 * solely of static attributes and methods, should not be instantiated
 */
public class Tags {

	/** Prefix of a tag */
	public static final String PREFIX = "@";
	/** Set of tags */
	private static HashSet<String> tagSet = new HashSet<String>();
	/** Location of the tags file */
	private static File tagFile;

	/**
	 * Load tags from the tags file Read and add each line as a tag. Each line
	 * is one single tag
	 */
	public static void open() {
		tagFile = new File(PhotoRenamer.tagsPath);
		String line = "";
		if (!tagFile.exists()) {
			try {
				tagFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		BufferedReader br = null;
		try {
			// Attempt to read from file
			br = new BufferedReader(new FileReader(tagFile));
			/* Tags.tagSetPath = tagSetPath; */
			while ((line = br.readLine()) != null) {
				// Parse each line to a tag
				tagSet.add(line.trim());
			}
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
		return tagSet.add(tag);
	}

	public HashSet<String> getTags() {
		return new HashSet<String>(tagSet);
	}

	public static void writeTags(){
		FileWriter fw;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(tagFile);
			bw = new BufferedWriter(fw);
			for (String tag : tagSet) {
				try {
					bw.write(tag);
					bw.write("\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(bw != null){
				try {
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
