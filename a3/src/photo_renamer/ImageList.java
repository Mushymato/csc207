package photo_renamer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
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
 */
public class ImageList extends JPanel {

	private static final long serialVersionUID = PhotoRenamer.serialVersionUID;
	private JList<Image> imgList;
	private File currentDir;
	protected PRWrapper pr;

	ImageList() {
		super();
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createTitledBorder("Images"));
		pr = new PRWrapper();
		
		JFileChooser dirChooser = new JFileChooser();
		dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		JButton dirSelect = new JButton("Choose Directory");
		dirSelect.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int res = dirChooser.showOpenDialog(null);
				if(res == JFileChooser.APPROVE_OPTION){
					currentDir = dirChooser.getSelectedFile();
					updateList();
				}
			}
		});
		JButton selectImage = new JButton("Select Image");
		selectImage.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				PhotoRenamer.setCurrentImg(imgList.getSelectedValue());
			}
			
		});

		imgList = new JList<Image>();
		JScrollPane imgPane = new JScrollPane(imgList);
		this.updateList();
		this.add(imgPane, new GridBagConstraints(0, 0, 1, 1, 1, 20, GridBagConstraints.EAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(dirSelect, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.EAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(selectImage, new GridBagConstraints(0, 2, 1, 1, 1, 1, GridBagConstraints.EAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
	}
	
	public void updateList() {
		if(currentDir != null){
			this.setBorder(BorderFactory.createTitledBorder(currentDir.getName()));
			List<Image> imagesInDir = pr.addImagesInDir(currentDir);
			this.imgList.setListData(imagesInDir.toArray(new Image[imagesInDir.size()]));;
		} else {
			this.setBorder(BorderFactory.createTitledBorder("All Images"));
			List<Image> imagesInDir = pr.listImage();
			this.imgList.setListData(imagesInDir.toArray(new Image[imagesInDir.size()]));;
		}
	}	
}
