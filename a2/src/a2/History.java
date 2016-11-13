package a2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Records name changes of an Image. Writes these changes to a .log file.
 * Implements closable.
 */
public class History implements Closeable {

	/** A Map that maps a Timestamp to a name change (String). */
	private TreeMap<Timestamp, String> log = new TreeMap<Timestamp, String>();
	/** The files in which changes are recorded. */
	private File logFile;
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

		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	}

	/**
	 * Reverts to the int nth most recent change. Does not delete log entries,
	 * just adds a new one with the reverting change. The bounds of n is (2,
	 * log.size). If n is out of bounds, revert to the very first change
	 * 
	 * @param n
	 *            Number of changes ago.
	 * @return the reverted change, or null if no change occurs at time.
	 */
	protected String unChange(int n) {
		if (n < 2 || n > log.size()) {
			n = log.size();
		}
		for (Timestamp change : log.descendingKeySet()) {
			n -= 1;
			if (n == 0) {
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

	/**
	 * Record all History log entries to a file stored at logFile
	 */
	private void writeLog() {
		BufferedWriter bw = null;
		try {
			FileWriter fw = new FileWriter(this.logFile);
			bw = new BufferedWriter(fw);
			for (Entry<Timestamp, String> entry : this.log.entrySet()) {
				try {
					bw.write(History.line(entry.getKey(), entry.getValue()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Move the log file to the directory specified by PhotoRenamer.dataDirPath.
	 * Does nothing if the new directory is the same as the old.
	 * 
	 * @return true iff move successful.
	 */
	public boolean moveLog() {
		String logPath = PhotoRenamer.dataDirPath + this.imgName + ".log";
		File newLogFile = new File(logPath);
		if (logFile.renameTo(newLogFile)) {
			logFile = newLogFile;
			return true;
		} else {
			return false;
		}
	}

	public boolean delLog() {
		if (logFile.delete()) {
			log = new TreeMap<Timestamp, String>();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void close() {
		this.writeLog();
		log = null;
		logFile = null;
		imgName = null;
	}
}
