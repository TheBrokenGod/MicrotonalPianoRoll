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
	
	static final Dimension KEY_SIZE = new Dimension(100, 32);
	static final Color KEY_MUTE_COLOR = Color.WHITE;
	static final Color KEY_PLAYING_COLOR = Color.LIGHT_GRAY;
	static final Border KEY_BORDER = BorderFactory.createLineBorder(Color.BLACK, 1);
	static final int KEY_ICON_SIZE = 18;
	static final float KEY_ICON_PLACEMENT = 0.67f;
	static final Image ICON;
	static {
		try {
			// Build key logo
			BufferedImage image = ImageIO.read(new File("key.png"));
			AffineTransform tx = new AffineTransform();
			tx.rotate(-Math.PI / 2, image.getWidth() / 2, image.getHeight() / 2);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BICUBIC);
			ICON = op.filter(image, null).getScaledInstance(KEY_ICON_SIZE, KEY_ICON_SIZE, Image.SCALE_SMOOTH);
		} 
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
