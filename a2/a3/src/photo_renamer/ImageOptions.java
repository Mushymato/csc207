package photo_renamer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * JPanel containing the image name and list of tags. Handles tag addition/deletion operations.
 * PhotoRenamer.currentImage and update data appropriately
 */
public class ImageOptions extends JPanel {

	private static final long serialVersionUID = PhotoRenamer.serialVersionUID;
	
	/** List of current tags*/
	private JList<String> listTags;
	
	/** Initialize new ImageOptions*/
	public ImageOptions() {
		super();

		this.setLayout(new GridBagLayout());
		this.setAlignmentX(LEFT_ALIGNMENT);
		this.setBorder(BorderFactory.createTitledBorder("Img Name"));

		listTags = new JList<String>();
		JScrollPane listTagsScroll = new JScrollPane(listTags);
		JButton addTag;
		JButton delTag;
		addTag = new JButton("Add new Tag");
		addTag.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (PhotoRenamer.getCurrentImg() != null) {
					String tag = JOptionPane.showInputDialog("Enter a tag");
					PhotoRenamer.getCurrentImg().addTag(tag);
					PhotoRenamer.alert();
				}
			}
		});
		delTag = new JButton("Remove selected Tag");
		delTag.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (PhotoRenamer.getCurrentImg() != null) {
					String tag = listTags.getSelectedValue();
					PhotoRenamer.getCurrentImg().delTag(tag);
					PhotoRenamer.alert();
				}
			}
		});

		this.add(listTagsScroll, new GridBagConstraints(0, 0, 1, 1, 1, 20, GridBagConstraints.EAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(addTag, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.EAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(delTag, new GridBagConstraints(0, 2, 1, 1, 1, 1, GridBagConstraints.EAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		this.setVisible(true);
	}

	/** Update imgName and imgTags to reflect currentImg*/
	public void updateInfo() {
		if (PhotoRenamer.getCurrentImg() != null) {
			this.setBorder(BorderFactory.createTitledBorder(PhotoRenamer.getCurrentImg().imageName()));
			List<String> imgTags = PhotoRenamer.getCurrentImg().getTags();
			listTags.setListData(imgTags.toArray(new String[imgTags.size()]));
		}
	}	
}
