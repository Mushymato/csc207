package photo_renamer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ImageOptions extends JPanel {

	private static final long serialVersionUID = PhotoRenamer.serialVersionUID;

	private JLabel imgName;
	private JButton addTag;
	private JList<String> listTags;

	public ImageOptions() {
		super();

		this.setLayout(new GridBagLayout());
		this.setAlignmentX(LEFT_ALIGNMENT);

		imgName = new JLabel("Img Name");
		imgName.setHorizontalAlignment(JLabel.CENTER);
		addTag = new JButton("Add new Tag");
		addTag.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		listTags = new JList<String>();
		JScrollPane listTagsScroll = new JScrollPane(listTags);
		

		this.add(imgName, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.EAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(addTag, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.EAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(listTagsScroll, new GridBagConstraints(0, 2, 1, 1, 1, 1, GridBagConstraints.EAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		this.setVisible(true);
	}

	public void changeImage() {
		if (PhotoRenamer.getCurrentImg() != null) {
			this.imgName.setText(PhotoRenamer.getCurrentImg().getName());
			List<String> imgTags = PhotoRenamer.getCurrentImg().getTags();
			imgTags.add("");
			this.listTags.setListData(imgTags.toArray(new String[imgTags.size()]));
		}
	}
}
