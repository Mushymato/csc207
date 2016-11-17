package photo_renamer;

import javax.swing.*;
import backend.*;

public class PhotoRenamer {
	public static final long serialVersionUID = 5165545549804727342L;

	public static void main(String[] args) {
		JFrame appFrame = new JFrame("PhotoRenamer");
		JFileChooser dirChooser = new JFileChooser();
		PRWrapper pr = new PRWrapper();
		ImagePanel imgPanel;
		
		appFrame.setSize(500, 500);
		appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		appFrame.setVisible(true);
	}
}
