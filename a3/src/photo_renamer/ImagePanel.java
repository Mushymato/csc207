package photo_renamer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Visually display the specified image in a Panel.
 */
public class ImagePanel extends JPanel {
	/* Serial Version ID */
	private static final long serialVersionUID = PhotoRenamer.serialVersionUID;
	/** Image currently loaded */
	private BufferedImage img;

	/**
	 * Make a new empty image panel.
	 */
	public ImagePanel() {
		super();
		this.setVisible(true);
	}

	/**
	 * Change displayed image.
	 */
	public void changeImage() {
		try {
			img = ImageIO.read(PhotoRenamer.getCurrentImg().getImageFile());
			this.repaint();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int pW = this.getWidth(), pH = this.getHeight(), iW = img.getWidth(), iH = img.getHeight();

		double scale;
		if (iW > iH) {
			scale = (double) (pW) / (double) (iW);
		} else {
			scale = (double) (pH) / (double) (iH);
		}
		int x = (int) ((pW - iW * scale) / 2);
		int y = (int) ((pH - iH * scale) / 2);
		g.drawImage(img, x, y, (int) (iW * scale), (int) (iH * scale), this);
	}
}
