package photo_renamer;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import backend.*;

/**
 * Main photo renamer class.
 */
public class PhotoRenamer {
	public static final long serialVersionUID = 5165545549804727342L;
	
	private static final JFrame frame = new JFrame("PhotoRenamer");
	private static final ImageList imgList = new ImageList();
	private static final ImagePanel imgPanel = new ImagePanel();
	private static final ImageOptions imgOptions = new ImageOptions();
	private static final HistoryList histList = new HistoryList();

	/** The current selected Image*/
	private static Image currentImg = null;

	/** Return the current selected Image*/
	protected static Image getCurrentImg() {
		return currentImg;
	}

	/** Set currentImh and notify components to update information accordingly*/
	protected static void setCurrentImg(Image newImg) {
		if (newImg != null) {
			PhotoRenamer.currentImg = newImg;
			alert();
		}
	}
	
	/** Update data of all components */
	protected static void alert(){
		imgOptions.updateInfo();
		histList.updateTable();
		imgList.updateList();
		imgPanel.changeImage();
	}

	public static void main(String[] args) {
//		// Instantiate components
//		PRWrapper pr = new PRWrapper();
//
//		// Placeholder
//		List<Image> p = pr.listImage();
//		setCurrentImg(p.get(4));

		// Set size
		Dimension frameSize = Toolkit.getDefaultToolkit().getScreenSize();
		frameSize.setSize((int) (frameSize.width * 0.5), (int) (frameSize.height * 0.5));
		frame.setMinimumSize(frameSize);
		frame.setPreferredSize(frameSize);
		Dimension sideSize = new Dimension((int)(frameSize.width * 0.25), frameSize.height);
		imgOptions.setPreferredSize(sideSize);
		imgList.setPreferredSize(sideSize);
		Dimension histSize = new Dimension(frameSize.width, (int)(frameSize.height * 0.1));
		histList.setPreferredSize(histSize);
		
		// Layout & components
		frame.setLayout(new GridBagLayout());
		frame.add(imgList, new GridBagConstraints	(0, 0, 1, 3, 1, 1, GridBagConstraints.EAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		frame.add(imgPanel, new GridBagConstraints	(1, 0, 2, 2, 5, 5, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		frame.add(imgOptions, new GridBagConstraints(3, 0, 1, 3, 1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));		
		frame.add(histList, new GridBagConstraints	(1, 2, 2, 1, 1, 1, GridBagConstraints.SOUTH, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		
		// Listeners
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				imgList.pr.close();
			}
		});
		// Boilerplate
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
}
