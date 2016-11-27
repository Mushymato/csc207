package photo_renamer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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
	private JList<Image> imgList;
	protected PRWrapper pr;

	ImageList() {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		pr = new PRWrapper();
		
		JFileChooser dirChooser = new JFileChooser();
		dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		JButton dirSelect = new JButton("Choose Directory");
		dirSelect.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		JButton showAll = new JButton("Select Image");
		showAll.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});

		
		JScrollPane imgPane = new JScrollPane(imgList);
		imgList = new JList<Image>();
		
		this.add(imgPane);
		this.add(dirSelect);
		this.add(showAll);
	}
	
	public void updateList() {
	}	
}
