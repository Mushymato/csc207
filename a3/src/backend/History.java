package backend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Records name changes of an Image. Writes these changes to a .log file.
 * Implements closable.
 */
public class History implements Closeable {

	/** A Map that maps a Timestamp to a name change (String). */
	private TreeMap<Timestamp, String> log = new TreeMap<Timestamp, String>(Collections.reverseOrder());
	/**
	 * Maps a Timestamp to a name change (String), retains undone log entries
	 */
	private TreeMap<Timestamp, String> redoLog = new TreeMap<Timestamp, String>();
	/** The files in which changes are recorded. */
	private String logFilePath;
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
	protected History(Image img) {
		this.imgName = img.imageName(); // store name
		this.logFilePath = PRWrapper.dataDirPath + this.imgName + ".log";
		File logFile = new File(logFilePath);
		// Make new file if no file exists
		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.newChange(img.getName());
			this.writeLog();
		} else {
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
		redoLog = new TreeMap<Timestamp, String>();
	}

	/**
	 * Reverts to the nth most recent change. Moves all changes between current
	 * and nth most recent change to redoLog. The bounds of n is (1,
	 * log.size). If n is out of bounds, revert to the very first change.
	 * 
	 * @param n
	 *            Number of changes to undo.
	 * @return the reverted change, or null if log is empty
	 */
	protected String unChange(int n) {
		if (n < 1 || n > log.size()) {
			n = log.size();
		}
		Iterator<Entry<Timestamp, String>> it = log.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Timestamp, String> current = it.next();
			if (n == 0) {
				this.newChange(current.getValue());
				return current.getValue();
			}
			n -= 1;
			redoLog.entrySet().add(current);
			it.remove();
		}
		return null;
	}

	/**
	 * Redo n changes previously undone by unChange(). Moves the redone changes
	 * from redoLog to log. The bounds of n is (-redoLog.size, -1). If n is out
	 * of bounds, revert to the most recent change by time in redoLog
	 * 
	 * @param n
	 *            Number of changes to redo.
	 * @return the reverted change, or null if redoLog is empty
	 */
	protected String reChange(int n) {
		if (n > -1 || n < -redoLog.size()) {
			n = -redoLog.size();
		}
		Iterator<Entry<Timestamp, String>> it = redoLog.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Timestamp, String> current = it.next();
			n += 1;
			if (n == 0) {
				return current.getValue();
			}
			log.entrySet().add(current);
			it.remove();
		}
		return null;
	}

	// /**
	// * Reverts to the change made at the specified Timestamp. Does not delete
	// * log entries, just adds a new one with the reverting change.
	// *
	// * @param time
	// * The specified time.
	// * @return the reverted change, or null if no change occurs at time
	// */
	// private String getLogEntry(Timestamp time) {
	// String logEntry = log.get(time);
	// if (logEntry == null) {
	// return null;
	// }
	// return logEntry;
	// }

	/**
	 * Record all History log entries to the .log file stored at logFilePath
	 */
	private void writeLog() {
		BufferedWriter bw = null;
		try {
			FileWriter fw = new FileWriter(this.logFilePath);
			bw = new BufferedWriter(fw);
			for (Entry<Timestamp, String> entry : this.log.entrySet()) {
				try {
					bw.write(History.line(entry.getKey(), entry.getValue()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			// do nothing
		} catch (IOException e) {
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
	 * Clear all history entries and delete the log file
	 * 
	 * @return
	 */
	protected boolean delete() {
		File logFile = new File(logFilePath);
		if (logFile.delete()) {
			log = new TreeMap<Timestamp, String>();
			return true;
		} else {
			return false;
		}
	}

	protected Map<Timestamp, String> getLog() {
		return this.log;
	}

	protected Map<Timestamp, String> getRedo() {
		return this.redoLog;
	}

	@Override
	public void close() {
		this.writeLog();
		log = null;
		redoLog = null;
		logFilePath = null;
		imgName = null;
	}
}
