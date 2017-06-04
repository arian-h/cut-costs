package com.boot.cut_costs.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

public class ImageUtils {
	
	//TODO: fix the imagePath
	private final static String imagePath = "/";
    private final static Random random = new SecureRandom();

	public static String decodeBase64AndSaveImage(String data) throws IOException {
		String base64Image = data.split(",")[1];
		byte[] imageBytes = DatatypeConverter.parseBase64Binary(base64Image);
		BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
		String imageId = generateRandomKey(15);
		File imageFile = new File(imagePath + File.separator + imageId);
		ImageIO.write(img, "png", imageFile);
		return imageId;
	}
    
    private static String generateRandomKey(int length) {
        return String.format("%"+length+"s", new BigInteger(length*5/*base 32,2^5*/, random)
            .toString(32)).replace('\u0020', '0');
    }
}
