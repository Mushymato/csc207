package photo_renamer;

import java.sql.Timestamp;
import java.util.Map.Entry;

import javax.swing.JList;
import javax.swing.JPanel;

public class HistoryList extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = PhotoRenamer.serialVersionUID;
	
	private JList<Entry<Timestamp, String>> logList;
	
	HistoryList(){
		logList = new JList<Entry<Timestamp, String>>();
		logList.getSelectionModel();
	}
	
	public void changeImage() {
		PhotoRenamer.getCurrentImg();
	}

}
