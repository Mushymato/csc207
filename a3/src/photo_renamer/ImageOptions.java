package photo_renamer;

import java.awt.Dimension;
import java.sql.Timestamp;
import java.util.Map.Entry;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import backend.Image;

public class ImageOptions extends JTabbedPane {

	private static final long serialVersionUID = PhotoRenamer.serialVersionUID;

	private JPanel infoPanel;
	private JList<Entry<Timestamp, String>> logList;

	public ImageOptions() {
		super();
		infoPanel = new JPanel();
		logList = new JList<Entry<Timestamp, String>>();
		this.add("Properties", infoPanel);
		this.add("History", logList);

		//this.setPreferredSize(PhotoRenamer.size);

		this.setVisible(true);
	}

	@Override
	public void setPreferredSize(Dimension preferredSize) {
		super.setPreferredSize(new Dimension((int) (preferredSize.getWidth() * 0.2), (int) preferredSize.getHeight()));
	}

	public void changeImage(Image imageObj) {
	}
}
