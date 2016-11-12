package a2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.TreeMap;

/**
 * Records name changes of an Image. Writes these changes to a .log file.
 * Implements closable.
 */
public class History implements Closeable {

	/** A Map that maps a Timestamp to a name change (String). */
	private TreeMap<Timestamp, String> log = new TreeMap<Timestamp, String>();
	/** The files in which changes are recorded. */
	protected File logFile;
	/** A BufferedWriter kept open as long as this instance is open. */
	private BufferedWriter bw;
	/** Name of the Image this History instance is associated with. */
	private String imgName;

	/**
	 * Initialize a new History object for Image img. The constructor will store
	 * the name of img, and if there exists a corresponding .log file in log
	 * directory defined in PhotoRenamer the constructor will attempt to read
	 * from this existing file. Otherwise, a new file is created, with log
	 * recording the current name of img as the first change.
	 * 
	 * @param img
	 *            The Image object to be associated with this instance of
	 *            History.
	 */
	History(Image img) {
		this.imgName = img.imageName(); // store name
		String logPath = PhotoRenamer.dataDirPath + this.imgName + ".log";
		logFile = new File(logPath);
		// check if file exists at logPath
		FileWriter fw;
		if (!logFile.exists()) {
			// No file, make new file and add current image name as first change
			try {
				logFile.createNewFile();
				fw = new FileWriter(logFile, true);
				bw = new BufferedWriter(fw);
				this.newChange(img.getName());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// File exists, make writer and reader
			try {
				fw = new FileWriter(logFile, true);
				bw = new BufferedWriter(fw);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String line;
			String vbar = "|";
			BufferedReader br = null;
			try {
				// Attempt to read from file
				br = new BufferedReader(new FileReader(logFile));
				while ((line = br.readLine()) != null) {
					int div = line.indexOf(vbar);
					Timestamp time = Timestamp.valueOf(line.substring(0, div));
					String name = line.substring(div + 1);
					log.put(time, name);
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
	}

	/**
	 * Helper function which converts a Timestamp and a string to the format of
	 * a single ling in the .log file.
	 * 
	 * @param time
	 *            A Timestamp
	 * @param change
	 *            A change represented by a String
	 * @return A formatted line for a .log file
	 */
	private static String line(Timestamp time, String change) {
		String vbar = "|";
		return time.toString() + vbar + change + "\n";
	}

	/**
	 * Add a newChange to History. Put change into HashMap<Timestamp, String>
	 * log. Then write change to logFile.
	 * 
	 * @param change
	 *            Change to be recorded to logFile.
	 */
	protected void newChange(String change) {
		Timestamp currTime = new Timestamp(System.currentTimeMillis());
		while (log.containsKey(currTime)) {
			currTime.setTime(currTime.getTime() + 1);
		}
		log.put(currTime, change);
		try {
			bw.write(History.line(currTime, change));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reverts to the int nth most recent change. Does not delete
	 * log entries, just adds a new one with the reverting change.
	 * The bounds of n is (2, log.size). 
	 * If n is out of bounds, revert to the very first change
	 * 
	 * @param n
	 * 		Number of changes ago.
	 * @return the reverted change, or null if no change occurs at time.
	 */
	protected String unChange(int n){
		if(n < 2 || n > log.size()){
			n = log.size();
		}
		for (Timestamp change : log.descendingKeySet()) {
			n -= 1;
			if(n == 0){
				return this.unChange(change);
			}
		}
		return null;
	}

	/**
	 * Reverts to the change made at the specified Timestamp. Does not delete
	 * log entries, just adds a new one with the reverting change.
	 * 
	 * @param time
	 *            The specified time.
	 * @return the reverted change, or null if no change occurs at time
	 */
	protected String unChange(Timestamp time) {
		String revert = log.get(time);
		if (revert == null) {
			return null;
		}
		this.newChange(revert);
		return revert;
	}
	
	@Override
	public void close() {
		log = null;
		logFile = null;
		imgName = null;
		if (bw != null) {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
