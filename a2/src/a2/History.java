package a2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Map;
import java.util.TreeMap;

public class History {

	private TreeMap<Timestamp, String> log = new TreeMap<Timestamp, String>();
	private String logPath = "data/";

	/**
	 * Initialize
	 * 
	 * @param img
	 */
	History(Image img) {
		logPath += img.imageName();
		logPath += ".log";
		String line = "";
		String vbar = "|";
		BufferedReader br = null;
		try {
			// Attempt to read from file
			br = new BufferedReader(new FileReader(logPath));
			while ((line = br.readLine()) != null) {
				int div = line.indexOf(vbar);
				Timestamp time = Timestamp.valueOf(line.substring(0, div));
				String name = line.substring(div + 1);
				log.put(time, name);
			}
		} catch (FileNotFoundException e) {
			this.newChange(img.getName());
			this.writeLog();
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

	public void newChange(String change) {
		Timestamp currTime = new Timestamp(System.currentTimeMillis());
		while (log.containsKey(currTime)) {
			currTime.setTime(currTime.getTime() + 1);
		}
		log.put(currTime, change);
		this.writeLog();
	}

	public String unChange() {
		Timestamp currTime = new Timestamp(System.currentTimeMillis());
		while (log.containsKey(currTime)) {
			currTime.setTime(currTime.getTime() + 1);
		}
		String revert = log.lastEntry().getValue();
		log.put(currTime, revert);
		this.writeLog();
		return revert;
	}

	public String unChange(Timestamp time) {
		Timestamp currTime = new Timestamp(System.currentTimeMillis());
		while (log.containsKey(currTime)) {
			currTime.setTime(currTime.getTime() + 1);
		}
		try {
			String revert = log.get(time);
			log.put(currTime, revert);
			this.writeLog();
			return revert;
		} catch (NullPointerException e) {
			return null;
		}
	}

	public void writeLog() {
		String vbar = "|";
		PrintWriter wr = null;
		try {
			wr = new PrintWriter(logPath, "UTF-8");
			for (Map.Entry<Timestamp, String> line : log.entrySet()) {
				wr.write(line.getKey().toString());
				wr.write(vbar);
				wr.write(line.getValue());
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
