package com.example.backend.service;

import com.example.backend.utils.DataSourceConfig;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

@Component
public class QRCodeService {

	private static final Logger LOGGER = Logger.getLogger(DataSourceConfig.class.getName());

	private final UserService userService;

	@Autowired
	public QRCodeService(UserService userService) {
		this.userService = userService;
	}

	public String createQRCode(String client, int userId, int activityID, String activityName) throws WriterException, IOException {

		// generate code
		String code = userService.generateEventCode(userId, activityID);

		String qrCodeFormat = "PNG";
		String path = "src\\main\\resources\\qrcodes\\";
		String data = "Here's my private activity code: " + code;
		String fileName = activityName + "_" + client + ".png";

		BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 300, 300);
		MatrixToImageWriter.writeToPath(matrix, qrCodeFormat, new File(path + fileName).toPath());

		LOGGER.info("QR CODE created!");

		return code;
	}

	public void drawPhoto(String userName, String eventName, int userId) throws IOException {

		String qrCodePath = "src\\main\\resources\\qrcodes\\" + eventName + "_" + userName + ".png";
		String numberTemplatePath = "src\\main\\resources\\layout\\template.png";
		String imageOutputPath = "src\\main\\resources\\layout\\" + eventName + "_" + userName + ".png";
		final BufferedImage image = ImageIO.read(new File(numberTemplatePath));

		final BufferedImage qrCode = ImageIO.read(new File(qrCodePath));
		Image resultingImage = qrCode.getScaledInstance(150, 150, Image.SCALE_DEFAULT);
		BufferedImage outputImage = new BufferedImage(150, 150, BufferedImage.TYPE_INT_RGB);
		outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);

		Graphics g = image.getGraphics();

		drawName(image, g, eventName);
		drawNumber(image, g, userId);

		g.drawImage(outputImage, 690, 655, null);

		g.dispose();

		ImageIO.write(image, "png", new File(imageOutputPath));
	}

	private static void drawName(BufferedImage image, Graphics g, String eventName) {
		// NAME
		Font nameBasefont = g.getFont().deriveFont(40f);
		FontMetrics metrics;
		FontMetrics nameRuler = g.getFontMetrics(nameBasefont);
		GlyphVector nameVector = nameBasefont.createGlyphVector(nameRuler.getFontRenderContext(), eventName);

		Shape nameOutline = nameVector.getOutline(0, 0);

		double expectedWidth = nameOutline.getBounds().getWidth();
		double expectedHeight = nameOutline.getBounds().getHeight();

		boolean textFits = image.getWidth() >= expectedWidth && image.getHeight() >= expectedHeight;

		if (!textFits) {
			double widthBasedFontSize1 = (nameBasefont.getSize2D() * image.getWidth()) / expectedWidth;
			double heightBasedFontSize1 = (nameBasefont.getSize2D() * image.getHeight()) / expectedHeight;

			double newFontSize = Math.min(widthBasedFontSize1, heightBasedFontSize1);
			Font newFont1 = nameBasefont.deriveFont(nameBasefont.getStyle(), (float) newFontSize);

			metrics = g.getFontMetrics(newFont1);
			g.setFont(newFont1);
		} else {
			metrics = g.getFontMetrics(nameBasefont);
			g.setFont(nameBasefont);
		}

		int positionX = (image.getWidth() - metrics.stringWidth(eventName)) / 2;
		g.setColor(Color.WHITE);
		g.drawString(eventName, positionX, 210);
	}

	private static void drawNumber(BufferedImage image, Graphics g, int userId) {
		// NUMBER
		String eventNumber = userId + "";
		Font numberBasefont = g.getFont().deriveFont(220f);
		FontMetrics numberMetrics;
		FontMetrics numberRuler = g.getFontMetrics(numberBasefont);
		GlyphVector numberVector = numberBasefont.createGlyphVector(numberRuler.getFontRenderContext(), eventNumber);

		Shape nameOutline = numberVector.getOutline(0, 0);

		double expectedWidth = nameOutline.getBounds().getWidth();
		double expectedHeight = nameOutline.getBounds().getHeight();

		boolean textFits = image.getWidth() >= expectedWidth && image.getHeight() >= expectedHeight;

		if (!textFits) {
			double widthBasedFontSize1 = (numberBasefont.getSize2D() * image.getWidth()) / expectedWidth;
			double heightBasedFontSize1 = (numberBasefont.getSize2D() * image.getHeight()) / expectedHeight;

			double newFontSize = Math.min(widthBasedFontSize1, heightBasedFontSize1);
			Font newFont1 = numberBasefont.deriveFont(numberBasefont.getStyle(), (float) newFontSize);

			numberMetrics = g.getFontMetrics(newFont1);
			g.setFont(newFont1);
		} else {
			numberMetrics = g.getFontMetrics(numberBasefont);
			g.setFont(numberBasefont);
		}

		int positionX = (image.getWidth() - numberMetrics.stringWidth(eventNumber)) / 2;
		int positionY = (image.getHeight() - numberMetrics.getHeight()) / 2 + numberMetrics.getAscent() + 80;

		g.setColor(Color.BLACK);
		g.drawString(eventNumber, positionX, positionY);
	}
}
