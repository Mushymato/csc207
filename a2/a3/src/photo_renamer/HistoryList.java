package photo_renamer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
/**
 * JPanel containing table of history entries. Handles name reversion.
 */
public class HistoryList extends JPanel {

	private static final long serialVersionUID = PhotoRenamer.serialVersionUID;

	/** Table of history entries*/
	private JTable logTable;
	/** Column namess*/
	private Vector<String> colName = new Vector<String>();
	/** Log model used by logTable.*/
	private DefaultTableModel logModel = new DefaultTableModel() {
		private static final long serialVersionUID = PhotoRenamer.serialVersionUID;

		/** Disable editing. */
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};
	
	/** Initialize new HistoryList*/
	public HistoryList() {
		this.setLayout(new GridBagLayout());
		this.setAlignmentX(LEFT_ALIGNMENT);
		this.setBorder(BorderFactory.createTitledBorder("History"));

		logTable = new JTable(logModel);

		colName.add("Time");
		colName.add("Name");
		updateTable();

		JScrollPane scrollPane = new JScrollPane(logTable);
		JButton revert = new JButton("Revert to selected");
		revert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int idx = logTable.getSelectedRow();
				if (idx != -1) {
					PhotoRenamer.getCurrentImg().revertName(logModel.getRowCount() - 1 - idx);
					PhotoRenamer.alert();
				}
			}
		});
		
		this.add(revert, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(scrollPane, new GridBagConstraints(0, 1, 1, 6, 1, 6, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
	}

	/** Update info in logTable*/
	public void updateTable() {
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
