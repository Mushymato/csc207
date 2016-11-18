package photo_renamer;

import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import backend.*;

/**
 * Step pls finish this.
 * TODO:
 * 	Directory selection.
 * 	Add all images in dir (PRWrapper.addImagesInDir())
 *  List images in dir 
 *  Optional: Thumbnail
 */
public class ImageList extends JPanel {

	private static final long serialVersionUID = PhotoRenamer.serialVersionUID;
	JButton dirSelect;
	JScrollPane imgPane;
	JList<Image> imgList;

	ImageList() {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		//this.setPreferredSize(PhotoRenamer.size);
		
		JFileChooser dirChooser = new JFileChooser();
		dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		dirSelect = new JButton("Choose Directory");
		
		imgPane = new JScrollPane();
		imgList = new JList<Image>();
		
		imgPane.add(imgList);
		this.add(dirSelect);
		this.add(imgPane);
	}

	@Override
	public void setPreferredSize(Dimension preferredSize) {
		super.setPreferredSize(new Dimension((int) (preferredSize.getWidth() * 0.2), (int) preferredSize.getHeight()));
	}

}
