package photo_renamer;

import java.awt.Dimension;
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
	/* Serial Version ID */
	private static final long serialVersionUID = PhotoRenamer.serialVersionUID;
	/** Image currently loaded */
	private BufferedImage img;

	/**
	 * Make a new empty image panel.
	 */
	public ImagePanel() {
		super();
		//this.setPreferredSize(PhotoRenamer.size);
		this.setVisible(true);
	}
	
	@Override
	public void setPreferredSize(Dimension preferredSize) {
		super.setPreferredSize(new Dimension((int) (preferredSize.getWidth() * 0.6), (int) preferredSize.getHeight()));
	}

	/**
	 * Change displayed image.
	 * 
	 * @param imageObj
	 *            New Image to be displayed in this panel.
	 */
	public void changeImage(Image imageObj) {
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
		
//		if (pW < iW || pH < iH) {
//			double scale;
//			if (iW > iH) {
//				scale = (double) (pW) / (double) (iW);
//			} else {
//				scale = (double) (pH) / (double) (iH);
//			}
//			int x = (int) ((pW - iW * scale) / 2);
//			int y = (int) ((pH - iH * scale) / 2);
//			g.drawImage(img, x, y, (int) (iW * scale), (int) (iH * scale), this);
//		} else {
//			int x = (this.getWidth() - img.getWidth()) / 2;
//			int y = (this.getHeight() - img.getHeight()) / 2;
//			g.drawImage(img, x, y, this);
//		}
	}
}
