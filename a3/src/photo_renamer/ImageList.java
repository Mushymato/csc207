package photo_renamer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		
		JFileChooser dirChooser = new JFileChooser();
		dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		JButton dirSelect = new JButton("Choose Directory");
		dirSelect.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		JButton showAll = new JButton("Show All Images");
		showAll.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});

		
		imgPane = new JScrollPane();
		imgList = new JList<Image>();
		
		imgPane.add(imgList);
		this.add(imgPane);
		this.add(dirSelect);
		this.add(showAll);
	}

}
