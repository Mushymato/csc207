package photo_renamer;

import javax.swing.JList;
import javax.swing.JScrollPane;

import backend.*;

public class ImageList extends JScrollPane{

	private static final long serialVersionUID = PhotoRenamer.serialVersionUID;
	JList<Image> imgList;
	
	ImageList(){
	}
}
