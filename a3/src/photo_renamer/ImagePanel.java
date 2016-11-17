package photo_renamer;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import backend.Image;

/**
 * Visually display the specified image in a Panel.
 */
public class ImagePanel extends JPanel {

	private static final long serialVersionUID = PhotoRenamer.serialVersionUID;
	private BufferedImage img;

	public ImagePanel(Image imageObj) {
		try {
			img = ImageIO.read(imageObj.getImageFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setLayout(new BorderLayout());
		this.add(this, BorderLayout.EAST);
		this.setVisible(true);
	}

	public void switchImage(Image imageObj){
		try {
			img = ImageIO.read(imageObj.getImageFile());
			this.repaint();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, this);
	}
}
