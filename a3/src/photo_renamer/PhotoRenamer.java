package photo_renamer;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import backend.*;

/**
 * Main photo renamer class.
 * 
 * @author miche
 *
 */
public class PhotoRenamer {
	public static final long serialVersionUID = 5165545549804727342L;

	private static final JFrame frame = new JFrame("PhotoRenamer");
	private static final ImageList imgList = new ImageList();
	private static final ImagePanel imgPanel = new ImagePanel();
	private static final ImageOptions imgOptions = new ImageOptions();
	private static final HistoryList histList = new HistoryList();
	private static final JTabbedPane imgSideBar = new JTabbedPane();

	private static Image currentImg = null;

	protected static Image getCurrentImg() {
		return currentImg;
	}

	protected static void setCurrentImg(Image newImg) {
		if (newImg != null) {
			PhotoRenamer.currentImg = newImg;
			ImageOptions.changeImage();
			imgPanel.changeImage();
			histList.changeImage();
		}
	}

	public static void main(String[] args) {
		// Instantiate components
		imgSideBar.add("Tags", imgOptions);
		imgSideBar.add("History", histList);

		PRWrapper pr = new PRWrapper();

		// Placeholder
		List<Image> p = pr.listImage();
		setCurrentImg(p.get(4));

		// Set size
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		size.setSize((int) (size.getWidth() * 0.5), (int) (size.getHeight() * 0.5));
		frame.setMinimumSize(size);
		frame.setPreferredSize(size);

		// Layout & components
		frame.setLayout(new GridBagLayout());
		frame.add(imgList, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.EAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		frame.add(imgPanel, new GridBagConstraints(1, 0, 1, 1, 6, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		frame.add(imgSideBar, new GridBagConstraints(2, 0, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		
		

		// Listeners
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				pr.close();
			}
		});
		// Boilerplate
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
}
