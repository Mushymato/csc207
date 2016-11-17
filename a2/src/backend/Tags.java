package backend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manage known tags and track what tags have been used for which images Consist
 * solely of static attributes and methods, should not be instantiated
 */
public class Tags {

	/** Prefix of a tag */
	public static final String PREFIX = "@";
	/** Map of tags and usage count */
	private static HashMap<String, Integer> tagMap = new HashMap<String, Integer>();
	/** Location of the tags file */
	private static File tagFile;

	/**
	 * Load tags from the tags file Read and add each line as a tag. Each line
	 * is a tag and the usage count of the tag.
	 */
	public static void load() {
		tagFile = new File(PRWrapper.dataDirPath + "tags.data");
		if (!tagFile.exists()) {
			try {
				tagFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		String line = "";
		BufferedReader br = null;
		try {
			// Attempt to read from file
			br = new BufferedReader(new FileReader(tagFile));
			while ((line = br.readLine()) != null) {
				// Parse each line to a tag
				tagMap.put(line, 0);
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
	 * Add a new tag to tagMap if there are no tags of the same name. Otherwise,
	 * increase tag usage count by 1.
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
	public static void removeTag(String tag) {
		if (tagMap.containsKey(tag)) {
			tagMap.put(tag, tagMap.get(tag) - 1);
			if (tagMap.get(tag) == 0) {
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
	 * Return a map sorted by value such that the most used tag is the first
	 * element of the Map followed by the second most used, etc.
	 * Obtained from: http://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values-java
	 * 
	 * @return Map sorted by tag usage count.
	 */
	public static Map<String, Integer> getTagUsage() {
		return tagMap.entrySet() // Compare entries
				.stream() // Get sequential stream
				.sorted(// Compare each entry in reverse order
						Map.Entry.comparingByValue(Collections.reverseOrder())) 
				// Collect sorted results back into a Map
				.collect(Collectors.toMap( 
						// Key is still key, value is still value
						Map.Entry::getKey, 
						// :: operator allows passing methods as parameters
						Map.Entry::getValue, 
						// Merge function
						(e1, e2) -> e1, 
						// new LinkedHashMap to recieve
						LinkedHashMap::new 
		));
	}

	/**
	 * Write all the tags to the tag file.
	 */
	public static void writeTags() {
		FileWriter fw;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(tagFile);
			bw = new BufferedWriter(fw);
			for (String tag : tagMap.keySet()) {
				try {
					bw.write(tag);
					bw.write("\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			// do nothing
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
