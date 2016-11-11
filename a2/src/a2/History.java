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
import java.util.Map;

public class History implements Closeable {

	private HashMap<Timestamp, String> log = new HashMap<Timestamp, String>();
	private File logFile;
	/* A BufferedWriter kept open as long as this instance is open */
	private BufferedWriter bw;
	private String imgName;

	/**
	 * Initialize
	 * 
	 * @param img
	 */
	History(Image img) {
		this.imgName = img.imageName();
		String logPath = PhotoRenamer.logDir + this.imgName + ".log";
		logFile = new File(logPath);
		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
				this.newChange(img.getName());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			String line = "";
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
			for (Map.Entry<Timestamp, String> line : log.entrySet()) {
				bw.write(History.line(line.getKey(), line.getValue()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String line(Timestamp time, String change) {
		String vbar = "|";
		return time.toString() + vbar + change + "\n";
	}

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
		if (bw != null) {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
