package com.boot.cut_costs.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import com.boot.cut_costs.exception.BadRequestException;

public class CommonUtils {

	//TODO: fix the file path
	private final static String imagePath = File.separator + "home"
			+ File.separator + "arian" + File.separator + "cut_costs_media"
			+ File.separator + "image" + File.separator + "profile";
	
    private final static Random random = new SecureRandom();

	public static String decodeBase64AndSaveImage(String data) throws IOException, BadRequestException {
		try {
			if (data == null) {
				return null;
			}
			String base64Image = data.split(",")[1];
			byte[] imageBytes = DatatypeConverter.parseBase64Binary(base64Image);
			BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
			String imageId = generateRandomKey(15);
			File imageFile = new File(imagePath + File.separator + imageId);
			ImageIO.write(img, "png", imageFile);
			return imageId;
		} catch (Exception e) {
			throw new BadRequestException("Bad image data passed");
		}
	}
	
	private static String generateRandomKey(int length) {
        return String.format("%"+length+"s", new BigInteger(length*5/*base 32,2^5*/, random)
            .toString(32)).replace('\u0020', '0');
    }
	
	/**
	 * Convert string to long
	 * @return
	 */
	public static long convertId(String id) {
		try {
			return Long.parseLong(id);
		} catch (NumberFormatException e) {
			throw new BadRequestException("Bad id(" + id + ") passed");
		}
	}
}
