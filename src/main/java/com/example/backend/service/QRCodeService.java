package com.example.backend.service;

import com.example.backend.utils.DataSourceConfig;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

@Component
public class QRCodeService {

	private static final Logger LOGGER = Logger.getLogger(DataSourceConfig.class.getName());

	public void createQRCode(String client) throws WriterException, IOException {

		String qrCodeFormat = "PNG";
		String path = "src\\main\\resources\\qrcodes\\";
		String data = "ActivityName_Ticket";
		String fileName = client + ".png";

		BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 300, 300);
		MatrixToImageWriter.writeToPath(matrix, qrCodeFormat, new File(path + fileName).toPath());

		LOGGER.info("QR CODE created!");

	}
}
