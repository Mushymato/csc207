package photo_renamer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class HistoryList extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = PhotoRenamer.serialVersionUID;

	private JTable logTable;
	static Vector<String> colName = new Vector<String>();
	private static DefaultTableModel logModel = new DefaultTableModel() {
		private static final long serialVersionUID = PhotoRenamer.serialVersionUID;

		/** Disable editing. */
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};

	public HistoryList() {
		this.setLayout(new GridBagLayout());
		this.setAlignmentX(LEFT_ALIGNMENT);
		
		logTable = new JTable(logModel);
		
		logTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		colName.add("Time");
		colName.add("Name");
		updateTable();
		
		logTable.getColumn("Time").setMaxWidth(100);
		logTable.getColumn("Name").setMaxWidth(100);
		
		JScrollPane scrollPane = new JScrollPane(logTable);

		this.add(scrollPane, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.EAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
	}

	public static void updateTable() {
		Vector<Vector<String>> logData = new Vector<Vector<String>>();
		if (PhotoRenamer.getCurrentImg() != null) {
			Iterator<Entry<Timestamp, String>> it = PhotoRenamer.getCurrentImg().getLog().entrySet().iterator();
			while (it.hasNext()) {
				Entry<Timestamp, String> curr = it.next();
				Vector<String> line = new Vector<String>();
				line.add(curr.getKey().toString());
				line.add(curr.getValue());
				logData.add(line);
			}
		}
		logModel.setDataVector(logData, colName);
	}
}
