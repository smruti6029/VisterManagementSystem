package com.app.serviceImpl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.app.Dao.ConfigurationDao;
import com.app.dto.MeetingDto;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

@Component
public class QrcodeGenerate {

//	@Value("${project.qr}")
//	private String qrUrl;

	@Autowired
	private ConfigurationDao configurationDao;
//	
//	void show()
//	{
//		System.out.println(excelDir +"   url generate ");
//	}

	public BufferedImage generateQrCode(MeetingDto meetingDto) throws IOException {

		String qrUrl = configurationDao.getByKey("QR_IP").getValue();

//		 QrcodeGenerate obj =new QrcodeGenerate();  obj.show();
//		 String data = ServletUriComponentsBuilder.fromCurrentContextPath()
//			        .path("/api/meeting/getbyid/")
//			        .path("id", meetingDto.getId())
//			        .toUriString();

		String data = qrUrl + meetingDto.getVisitor().getId();

//		 String data = ServletUriComponentsBuilder.fromCurrentContextPath()
//			        .path("/api/meeting/meeting-details/")
//			        .path(Integer.toString(meetingDto.getVisitor().getId()))
//			        .toUriString();

		String path = "s.jpg";

		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 500, 500);
			MatrixToImageWriter.writeToPath(bitMatrix, "jpg", Paths.get(path));
			System.out.println("QR code generated successfully");

			// Read the generated image and return it as a BufferedImage
			BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
			return image;
		} catch (WriterException e) {
			e.printStackTrace();
			return null;
		}
	}

}
