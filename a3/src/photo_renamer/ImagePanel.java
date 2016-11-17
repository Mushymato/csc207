package photo_renamer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import backend.Image;

@SuppressWarnings("serial")
/**
 * Visually display the specified image in a Panel. Do not serialize.
 */
public class ImagePanel extends JPanel {

	private BufferedImage img;

	public ImagePanel(Image imageObj) {
		try {
			img = ImageIO.read(imageObj.getImageFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
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
