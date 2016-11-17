package photo_renamer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import backend.*;

public class PhotoRenamer {
	public static final long serialVersionUID = 5165545549804727342L;

	public static void main(String[] args) {
		JFrame frame = new JFrame("PhotoRenamer");
		JFileChooser dirChooser = new JFileChooser();
		ImagePanel imgPanel = new ImagePanel();
		PRWrapper pr = new PRWrapper();

		frame.setSize(new Dimension(600, 400));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				pr.close();
			}
		});

		dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		List<Image> p = pr.listImage();
		imgPanel.switchImage(p.get(0));
		
		frame.add(imgPanel, BorderLayout.CENTER);
				
		frame.pack();
	    frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
