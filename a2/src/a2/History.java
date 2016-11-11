package a2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
/** 
 * Records name changes of an Image. Writes these changes to a .log file. Implements closable.
 */
public class History implements Closeable {

	/** A HasMap that maps a Timestamp to a name change (String). */
	private HashMap<Timestamp, String> log = new HashMap<Timestamp, String>();
	/** The files in which changes are recorded. */
	private File logFile;
	/** A BufferedWriter kept open as long as this instance is open. */
	private BufferedWriter bw;
	/** Name of the Image this History instance is associated with. */
	private String imgName;

	/**
	 * Initialize a new History object for Image img. The constructor will store the name
	 * of img, and if there exists a corresponding .log file in log directory defined in
	 * PhotoRenamer the constructor will attempt to read from this existing file. Otherwise,
	 * a new file is created, with log recording the current name of img as the first change. 
	 * 
	 * @param img
	 * 		The Image object to be associated with this instance of History.
	 */
	History(Image img) {
		this.imgName = img.imageName(); // store name
		String logPath = PhotoRenamer.logDir + this.imgName + ".log";
		logFile = new File(logPath);
		// check if file exists at logPath
		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
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
		FileWriter fw;
		try {
			fw = new FileWriter(logFile);
			bw = new BufferedWriter(fw);
			//TODO: Better logic
			this.newChange(img.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/** 
	 * Helper function which converts a Timestamp and a string to the format of a single ling in
	 * the .log file.
	 * 
	 * @param time
	 * 		A Timestamp
	 * @param change
	 * 		A change represented by a String
	 * @return A formatted line for a .log file
	 */
	private static String line(Timestamp time, String change) {
		String vbar = "|";
		return time.toString() + vbar + change + "\n";
	}
	/** 
	 * Add a newChange to History. Put change into HashMap<Timestamp, String> log. Then write change
	 * to logFile. 
	 * 
	 * @param change
	 * 		Change to be recorded to logFile.
	 */
	public void newChange(String change) {
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
	 * Reverts to the second most recent change. Does not delete the change
	 * TODO: fix this shit. should be second most change
	 * 
	 * @return
	 */
	public String unChange() {
		Timestamp currTime = new Timestamp(System.currentTimeMillis());
		while (log.containsKey(currTime)) {
			currTime.setTime(currTime.getTime() + 1);
		}

		Timestamp mostRecent = new Timestamp(0);
		for (Timestamp time : log.keySet()) {
			if (time.compareTo(mostRecent) > 0) {
				mostRecent = time;
			}
		}
		String revert = log.get(mostRecent);

		log.put(currTime, revert);
		try {
			bw.write(History.line(currTime, revert));
			return revert;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	/** 
	 * Reverts to the change made at the specified Timestamp. Does not delete log entries, just adds
	 * a new one with the reverting change.
	 * 
	 * @param time
	 * 		The specified time. 
	 * @return the reverted change, or null if no change occurs at time
	 */
	public String unChange(Timestamp time) {
		String revert = log.get(time);
		if (revert == null) {
			return null;
		}
		Timestamp currTime = new Timestamp(System.currentTimeMillis());
		while (log.containsKey(currTime)) {
			currTime.setTime(currTime.getTime() + 1);
		}
		log.put(currTime, revert);
		try {
			bw.write(History.line(currTime, revert));
			return revert;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	/** Moves the log file to logDir as specified by PhotoRenamer.
	 * 
	 * @return true iff logFile is successfully moved 
	 */
	public boolean moveLog() {
		String logPath = PhotoRenamer.logDir + this.imgName + ".log";
		File newLocation = new File(logPath);
		try {
			this.logFile.renameTo(newLocation);
			return true;
		} catch (Exception e) {
			return false;
		}
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
