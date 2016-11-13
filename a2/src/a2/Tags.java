package a2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * Manage known tags and track what tags have been used for which images Consist
 * solely of static attributes and methods, should not be instantiated
 */
public class Tags {

	/** Prefix of a tag */
	public static final String PREFIX = "@";
	/** Map of tags and usage count*/
	private static HashMap<String, Integer> tagMap = new HashMap<String, Integer>();
	/** Location of the tags file */
	private static File tagFile;

	/**
	 * Load tags from the tags file Read and add each line as a tag. 
	 * Each line is a tag and the usage count of the tag.
	 */
	public static void load() {
		tagFile = new File(PhotoRenamer.dataDirPath + "tags.data");
		if (!tagFile.exists()) {
			try {
				tagFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		String line = "";
		String vbar = "|";
		BufferedReader br = null;
		try {
			// Attempt to read from file
			br = new BufferedReader(new FileReader(tagFile));
			while ((line = br.readLine()) != null) {
				// Parse each line to a tag and tag count
				tagMap.put(line.substring(0, line.indexOf(vbar)), new Integer(line.substring(line.indexOf(vbar) + 1)));
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
	 * Add a new tag to tagMap if there are no tags of the same name. 
	 * Otherwise, increase tag usage count by 1.
	 * 
	 * @param tag
	 */
	public static void newTag(String tag) {
		if (tagMap.containsKey(tag)) {
			tagMap.put(tag, tagMap.get(tag) + 1);
		} else {
			tagMap.put(tag, 1);
		}
	}
	
	/**
	 * Lower the tag usage count by 1. If the usage count is 0, remove the tag.
	 * 
	 * @param tag
	 */
	public static void removeTag(String tag){
		if (tagMap.containsKey(tag)){
			tagMap.put(tag, tagMap.get(tag) - 1);
			if(tagMap.get(tag) == 0){
				tagMap.remove(tag);
			}
		}
	}

	/**
	 * Return a copy of all the tags in tagMap.
	 * 
	 * @return HashSet of all the tags.
	 */
	public static HashSet<String> getTags() {
		return new HashSet<String>(tagMap.keySet());
	}
	
	/**
	 * Return a map sorted by value such that the most used tag is the first element of the Map followed by the second most used, etc.
	 * @return Map sorted by tag usage count.
	 */
	public static Map<String, Integer> getTagUsage(){
	    return tagMap.entrySet() // Compare entries
	              .stream() // Get sequential stream
	              .sorted(Map.Entry.comparingByValue(Collections.reverseOrder())) //Compare each entry in reverse order
	              .collect(Collectors.toMap( // Collect sorted results back into a Map
	                Map.Entry::getKey, //Key is still key, value is still value
	                Map.Entry::getValue, // :: operator allows passing methods as parameters
	                (e1, e2) -> e1, // Merge function
	                LinkedHashMap::new // new LinkedHashMap to recieve
	              ));
	}

	/**
	 * Write all the tags to the tag file.
	 */
	public static void writeTags() {
		FileWriter fw;
		BufferedWriter bw = null;
		String vbar = "|";
		try {
			fw = new FileWriter(tagFile);
			bw = new BufferedWriter(fw);
			for (Entry<String, Integer> tag : tagMap.entrySet()) {
				try {
					bw.write(tag.getKey());
					bw.write(vbar);
					bw.write(tag.getValue());
					bw.write("\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
