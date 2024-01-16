package com.app.serviceImpl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.app.Dao.MeetingDao;
import com.app.Dao.UserDao;
import com.app.dto.MeetingDto;
import com.app.dto.RoomDto;
import com.app.dto.UpdateMeetingDto;
import com.app.dto.UserDto;
import com.app.emun.MeetingStatus;
import com.app.entity.Meeting;
import com.app.entity.User;
import com.app.security.JwtHelper;
import com.app.service.MeetingService;
import com.app.service.PdfService;

@Service
public class PdfServiceImpl implements PdfService {

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private MeetingDao meetingDao;

	@Autowired
	private MeetingService meetingService;

	@Autowired
	private JwtHelper jwtHelper;

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private QrcodeGenerate qrcodeGenerate;

	@Override
	public byte[] generateVisitorPassPDF(MeetingDto meetingDto) throws Exception {
		

		Meeting meeting = meetingDao.getById(meetingDto.getId());
		
		Resource templateImageResource = resourceLoader.getResource("classpath:/pass_red.png");

		InputStream templateImageInputStream = templateImageResource.getInputStream();

		int dpi = 72;

		float widthInInches = 6.5f;
		float heightInInches = 10f;

		float widthInPoints = widthInInches * dpi;
		float heightInPoints = heightInInches * dpi;

		try (PDDocument document = new PDDocument()) {
			PDRectangle customPageSize = new PDRectangle(widthInPoints, heightInPoints);
			PDPage page = new PDPage(customPageSize);
			document.addPage(page);

			PDImageXObject pdImage = PDImageXObject.createFromByteArray(document,
					IOUtils.toByteArray(templateImageInputStream), "template");

			try (PDPageContentStream contentStream = new PDPageContentStream(document, page, AppendMode.APPEND, true,
					true)) {
				contentStream.drawImage(pdImage, 0, 0, widthInPoints, heightInPoints);

				contentStream.beginText();
				contentStream.setNonStrokingColor(1f, 1f, 1f);
				contentStream.setFont(PDType1Font.HELVETICA_OBLIQUE, 20);
				contentStream.newLineAtOffset(50, 250);

				// String imageUrl = meetingDto.getVisitor().getImageUrl();

				MultipartFile imageFile = convertUrlToMultipartFile(meetingDto.getVisitor().getImageUrl());

				contentStream.newLineAtOffset(0, -40);
				contentStream.showText("Phone Number  :  " + meetingDto.getVisitor().getPhoneNumber());

				contentStream.newLineAtOffset(0, -40);
				contentStream.showText("Meeting Host   :  " + meetingDto.getUser().getFirstName() + " "
						+ meetingDto.getUser().getLastName());

				contentStream.newLineAtOffset(0, -40);
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
				String formattedStartTime = dateFormat.format(new Date());

				// Calculate the next day
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.add(Calendar.DAY_OF_MONTH, 1); // Add one day

				SimpleDateFormat nextDayFormat = new SimpleDateFormat("dd/MM/YYYY");
				String formattedEndTime = nextDayFormat.format(calendar.getTime());
				contentStream.showText("Pass validity    :   " + formattedStartTime);

				contentStream.newLineAtOffset(0, -40);
				if (meetingDto.getRoom() != null) {
					contentStream.showText("Room              :  " + meetingDto.getRoom().getRoomName());
				} else {
					contentStream.showText("Room              :  Room not assigned yet.");
				}

				contentStream.endText();

				contentStream.beginText();
				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 34);
				contentStream.newLineAtOffset(50, 320);

				contentStream.showText(meetingDto.getVisitor().getName());
				contentStream.newLineAtOffset(0, -40);
				contentStream.showText("          VISITOR");

				contentStream.endText();

				// Move the image drawing outside the text block
				// byte[] imageBytes =
				// convertUrlToByteArray(meetingDto.getVisitor().getImageUrl());
				int photoX = 130;
				int photoY = 385;
				int photoWidth = 220;
				int photoHeight = 220;
				int borderWidth = 2;
				int radius = photoWidth / 2;

				if (imageFile != null) {
					try (PDPageContentStream imageContentStream = new PDPageContentStream(document, page,
							AppendMode.APPEND, true, true)) {

						imageContentStream.setStrokingColor(0, 0, 0);
						imageContentStream.setLineWidth(borderWidth);

						// Create a circular clipping path
						float centerX = photoX + radius;
						float centerY = photoY + radius;

						float magicNumber = 0.551784f; // Magic number for Bezier curves

						imageContentStream.moveTo(centerX + radius, centerY);
						imageContentStream.curveTo(centerX + radius, centerY + radius * magicNumber,
								centerX + radius * magicNumber, centerY + radius, centerX, centerY + radius);
						imageContentStream.curveTo(centerX - radius * magicNumber, centerY + radius, centerX - radius,
								centerY + radius * magicNumber, centerX - radius, centerY);
						imageContentStream.curveTo(centerX - radius, centerY - radius * magicNumber,
								centerX - radius * magicNumber, centerY - radius, centerX, centerY - radius);
						imageContentStream.curveTo(centerX + radius * magicNumber, centerY - radius, centerX + radius,
								centerY - radius * magicNumber, centerX + radius, centerY);
						imageContentStream.closePath();
						imageContentStream.clip(); // Set the current path as the clipping path

						// Draw a rectangular border
						imageContentStream.addRect(photoX - borderWidth, photoY - borderWidth,
								photoWidth + 2 * borderWidth, photoHeight + 2 * borderWidth);
						imageContentStream.closeAndStroke();

						// Load and display the image within the circular clipping path
						PDImageXObject photoImage = PDImageXObject.createFromByteArray(document, imageFile.getBytes(),
								"photo");
						imageContentStream.drawImage(photoImage, photoX, photoY, photoWidth, photoHeight);
					}
				}

				MultipartFile comapanyImg = null;

				if (meeting.getEmployee().getCompany().getLogo() != null) {

					comapanyImg = convertUrlToMultipartFile(meeting.getEmployee().getCompany().getLogo());

				}

				int comapanyImgX = 280;
				int comapanyImgY = 620;
				int comapanyImgWidth = 160;
				int comapanyImgHeight = 70;
				int comapanyImgborderWidth = 1;

				if (comapanyImg != null) {
					try (PDPageContentStream imageContentStream = new PDPageContentStream(document, page,
							AppendMode.APPEND, true, true)) {

						
						
					
						imageContentStream.setNonStrokingColor(173f / 255f, 216f / 255f, 230f / 255f); 
						imageContentStream.fillRect(comapanyImgX - comapanyImgborderWidth,
						        comapanyImgY - comapanyImgborderWidth,
						        comapanyImgWidth + 2 * comapanyImgborderWidth,
						        comapanyImgHeight + 2 * comapanyImgborderWidth);
						
						
						imageContentStream.setStrokingColor(0, 0, 0);
						imageContentStream.setLineWidth(borderWidth);

						imageContentStream.addRect(comapanyImgX - comapanyImgborderWidth,
								comapanyImgY - comapanyImgborderWidth, comapanyImgWidth + 2 * comapanyImgborderWidth,
								comapanyImgHeight + 2 * comapanyImgborderWidth);
						imageContentStream.closeAndStroke();

						PDImageXObject photoImage = PDImageXObject.createFromByteArray(document, comapanyImg.getBytes(),
								"photo");
						imageContentStream.drawImage(photoImage, comapanyImgX, comapanyImgY, comapanyImgWidth,
								comapanyImgHeight);

					}
				}

				BufferedImage qrCodeImage = qrcodeGenerate.generateQrCode(meetingDto);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(qrCodeImage, "png", baos);
				byte[] qrCodeImageData = baos.toByteArray();

				PDImageXObject qrCodeImageXObject = PDImageXObject.createFromByteArray(document, qrCodeImageData,
						"QRCodeImage");
				int qrCodeX = 45; // Adjust the X position
				int qrCodeY = 580; // Adjust the Y position
				int qrCodeWidth = 110; // Adjust the width
				int qrCodeHeight = 110; // Adjust the height
				

				// Draw a black border around the QR code
				contentStream.setStrokingColor(0, 0, 0); // Black color
				contentStream.setLineWidth(borderWidth);
				contentStream.addRect(qrCodeX - borderWidth, qrCodeY - borderWidth, qrCodeWidth + 2 * borderWidth, qrCodeHeight + 2 * borderWidth);
				contentStream.closeAndStroke();
				

				contentStream.drawImage(qrCodeImageXObject, qrCodeX, qrCodeY, qrCodeWidth, qrCodeHeight);
				

			}

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

			document.save(byteArrayOutputStream);

			return byteArrayOutputStream.toByteArray();

		} catch (IOException e) {

			e.printStackTrace();

			return new byte[0];
		}
	}

	public MultipartFile convertUrlToMultipartFile(String imageUrl) {
		try {
			URL url = new URL(imageUrl);

			RestTemplate restTemplate = new RestTemplate();

			ResponseEntity<Resource> response = restTemplate.getForEntity(url.toURI(), Resource.class);

			Resource resource = response.getBody();

			InputStream inputStream = resource.getInputStream();

			// Use Apache Commons FileUpload to create a MultipartFile
			DiskFileItem fileItem = (DiskFileItem) new DiskFileItem("file", "image/jpeg", false, resource.getFilename(),
					(int) resource.contentLength(), new File(""));
			fileItem.getOutputStream().write(IOUtils.toByteArray(inputStream));

			MultipartFile multipartFile = new CommonsMultipartFile(fileItem);

			return multipartFile;
		} catch (URISyntaxException | IOException e) {
			System.out.println("Img not found !! ");
			return null;
		}catch (HttpClientErrorException.NotFound notFoundException) {
	        System.out.println("Image not found at URL: " + imageUrl);
	        return null;
		}

	}

	private byte[] convertUrlToByteArray(String imageUrl) throws IOException {
		URL url = new URL(imageUrl);
		try (InputStream in = url.openStream()) {
			return IOUtils.toByteArray(in);
		}
	}

}
