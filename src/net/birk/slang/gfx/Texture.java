package net.birk.slang.gfx;

import net.birk.slang.SourceLoc;
import net.birk.slang.ir.value.IrUserdata;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Texture extends IrUserdata {

	public static final int userType = IrUserdata.genUserType();

	private BufferedImage image;

	public Texture(String path, SourceLoc location) {
		super(userType, location);
		try {
			image = ImageIO.read(new File(path));
		} catch (IOException e) {
			image = null;
		}
	}

	public BufferedImage getImage() {
		return image;
	}

}
