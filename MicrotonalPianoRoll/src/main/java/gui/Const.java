package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class Const {
	
	static final Dimension KEY_SIZE_VERTICAL = new Dimension(100, 32);
	private static final Color KEYS_BASE_COLOR = new Color(255, 255, 255);
	static final Border KEYS_BORDER = BorderFactory.createLineBorder(Color.BLACK, 1);
	static final int KEYS_ICON_SIZE = 22;
	static final int KEYS_ICON_PLACEMENT = 12;
	
	static final Dimension KEY_SIZE_HORIZONTAL = new Dimension(KEY_SIZE_VERTICAL.height, KEY_SIZE_VERTICAL.width);
	static final Color[] KEYS_COLORS = {KEYS_BASE_COLOR, null, null, null, null};
	static final Image ICON_VERTICAL;
	static final Image ICON_HORIZONTAL;
	static {
		try {
			// Rotate clockwise the vertical icon by 90 degrees
			BufferedImage image = ImageIO.read(new File("key.png"));
			AffineTransform tx = new AffineTransform();
			tx.rotate(Math.PI / 2, image.getWidth() / 2, image.getHeight() / 2);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BICUBIC);
			ICON_HORIZONTAL = image.getScaledInstance(KEYS_ICON_SIZE, KEYS_ICON_SIZE, Image.SCALE_SMOOTH);
			ICON_VERTICAL = op.filter(image, null).getScaledInstance(KEYS_ICON_SIZE, KEYS_ICON_SIZE, Image.SCALE_SMOOTH);
			// Different states have same color but darker
			for (int i = 1; i < KEYS_COLORS.length; i++) {
				KEYS_COLORS[i] = KEYS_COLORS[i - 1].darker();
			}
		} 
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
