package a2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

public class Tags {

	/** Prefix of a tag */
	static String PREFIX = "@";
	/** Maps tag to set of images with that tag */
	private HashMap<String, HashSet<String>> tagMap = new HashMap<String, HashSet<String>>();
	/** Location of the .tag file */
	private String tagMapPath;

	/**
	 * Initialize Tags from a .tag file Read and add each line as a tag
	 * 
	 * @param path
	 *            Path of the .tag file
	 */
	Tags(String path) {
		String colon = ":";
		String comma = ",";
		String line = "";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(path));
			this.tagMapPath = path;
			while ((line = br.readLine()) != null) {
				int div = line.indexOf(colon);
				if (div != -1) {
					String tag = line.substring(0, div);
					HashSet<String> imgPaths = new HashSet<String>(Arrays.asList(line.substring(div).split(comma)));
					tagMap.put(tag, imgPaths);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
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
	public boolean addTag(String tag) {
		if (tagMap.containsKey(tag)) {
			return false;
		} else {
			tagMap.put(tag, new HashSet<String>());
			return true;
		}
	}

	/**
	 * Add a new image to a existing tag in tagMap
	 * 
	 * @param img
	 *            the Image object to be tagged
	 * @return true if image successfully tagged
	 */
	public boolean tagImage(String tag, Image img) {
		if (tagMap.containsKey(tag)) {
			try {
				tagMap.get(tag).add(img.getCanonicalPath());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns an string array containing all the tags. Any changes to the array
	 * will not be reflected.
	 * 
	 * @return string[] tags
	 */
	public String[] getTags() {
		return (String[]) tagMap.keySet().toArray();
	}
	
	public HashSet<String> getTags(Image img){
		HashSet<String> imgTags = new HashSet<String>();
		try{
			String path = img.getCanonicalPath();
			for (Map.Entry<String, HashSet<String>> tag : tagMap.entrySet()) {
				if(tag.getValue().contains(path)){
					imgTags.add(tag.getKey());
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}

	public void writeTagList() {
		String colon = ":";
		String comma = ",";
		PrintWriter wr = null;
		try {
			wr = new PrintWriter(tagMapPath, "UTF-8");
			for (Map.Entry<String, HashSet<String>> tag : tagMap.entrySet()) {
				wr.write(tag.getKey());
				wr.write(colon);
				int count = tag.getValue().size() - 1;
				for (String img : tag.getValue()) {
					wr.write(img);
					if(count > 0){
						wr.write(comma);
						count -= 1;
					}
				}
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
