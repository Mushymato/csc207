package photo_renamer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import backend.Image;

/**
 * Visually display the specified image in a Panel.
 */
public class ImagePanel extends JPanel {

	private static final long serialVersionUID = PhotoRenamer.serialVersionUID;
	private BufferedImage img;

	public ImagePanel(){
		this.setBorder(BorderFactory.createLoweredBevelBorder());
		this.setVisible(true);
		this.setPreferredSize(new Dimension(600, 400));
	}
	
	public ImagePanel(Image imageObj) {
		this.setBorder(BorderFactory.createLoweredBevelBorder());
		try {
			img = ImageIO.read(imageObj.getImageFile());
			Dimension size = new Dimension(img.getWidth(), img.getHeight());
			this.setPreferredSize(size);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
