package photo_renamer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;

import backend.*;

public class PhotoRenamer {
	public static final long serialVersionUID = 5165545549804727342L;
	public static Dimension size = Toolkit.getDefaultToolkit().getScreenSize();

	public static void main(String[] args) {
		// Set default size
		size.setSize((int) (size.getWidth() * 0.66), (int) (size.getHeight() * 0.66));

		// Instantiate components
		JFrame frame = new JFrame("PhotoRenamer");
		ImageList imgList = new ImageList();
		ImagePanel imgPanel = new ImagePanel();
		ImageOptions imgOptions = new ImageOptions();
		PRWrapper pr = new PRWrapper();

		// Placeholder
		List<Image> p = pr.listImage();
		imgPanel.changeImage(p.get(4));

		// Set size
		frame.setMinimumSize(size);
		frame.setPreferredSize(size);

		// Add components
		frame.add(imgList, BorderLayout.WEST);
		frame.add(imgPanel, BorderLayout.CENTER);
		frame.add(imgOptions, BorderLayout.EAST);

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
