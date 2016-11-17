package photo_renamer;

import javax.swing.*;

public class PhotoRenamer {
	JFrame appFrame;
	JFileChooser dirChooser;
	ImagePanel imgPanel;
	
	
	PhotoRenamer(){		
		// declare directory chooser
		dirChooser = new JFileChooser();
		dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	}
}
