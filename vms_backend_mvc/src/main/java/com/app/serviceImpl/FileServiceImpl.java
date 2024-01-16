package com.app.serviceImpl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.app.Dao.ConfigurationDao;
import com.app.service.FileService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@PropertySource("classpath:application.properties")
@Service
public class FileServiceImpl implements FileService {

	@Autowired
	private ConfigurationDao configurationDao;

	@Value("${file.save.url}")
	private String saveImage;

	@Value("${imageUrlToken}")
	private String tokenForsave;

	private static final String MAX_FILE_SIZE = "10MB";
	private static final String MAX_REQUEST_SIZE = "10MB";
	private static final String PROJECT_IMAGE_PATH = "images/";

	@Override
	public String uploadImage(String path, MultipartFile file) throws IOException {

		String baseUrl = configurationDao.getByKey("BASE_URL").getValue();
//		
//		System.out.println(baseUrl);

		String name = file.getOriginalFilename();

		// abc.png

		// random name generate file
		String randomID = UUID.randomUUID().toString();

		String fileName1 = randomID.concat(name.substring(name.lastIndexOf(".")));

		// fullpath
		String filePath = path + File.separator + fileName1;

		// create folder if not created
		File f = new File(path);
		if (!f.exists()) {
			f.mkdir();
		}

		// file copy

		Files.copy(file.getInputStream(), Paths.get(filePath));

//		 String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//	                .path("/com/image/")
//	                .path(fileName1)
//	                .toUriString();

		String fileDownloadUri = baseUrl + "/com/image/" + fileName1;
//		
		return fileDownloadUri;
	}

	@Override
	public InputStream getResource(String path, String fileName) throws FileNotFoundException {

		String fullPath = path + File.separator + fileName;

		InputStream is = new FileInputStream(fullPath);

		// db logic to return inputstream
		return is;
	}

	@Override
	public String uploadImage(MultipartFile image) {

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);

			byte[] fileBytes = image.getBytes();
			MultipartInputStreamFileResources fileResource = new MultipartInputStreamFileResources(fileBytes,
					image.getOriginalFilename());

			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("files", fileResource);
			body.add("token", tokenForsave);

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.postForEntity(saveImage, requestEntity, String.class);
			if (response.getStatusCode().value() == 200) {
				String responseBody = response.getBody();
				JsonObject jsonData = new Gson().fromJson(responseBody, JsonObject.class);
				if (jsonData.has("data")) {
					JsonObject dataJson = jsonData.get("data").getAsJsonObject();

					if (dataJson.has("fileUrls")) {
						JsonArray fileUrlsArray = dataJson.getAsJsonArray("fileUrls");
						String fileUrl = fileUrlsArray.get(0).getAsString();
						return fileUrl;

					}

				}

			}
			return null;

		} catch (Exception e) {
		}
		return "";
	}

	static class MultipartInputStreamFileResources extends ByteArrayResource {
		private final String filename;

		public MultipartInputStreamFileResources(byte[] byteArray, String filename) {
			super(byteArray);
			this.filename = filename;
		}

		@Override
		public String getFilename() {
			return this.filename;
		}
	}

//	@Override
//	public String getFileUrl(ByteArrayInputStream file) {
//
//		String fileUrl = null;
//		try {
//			System.out.println(saveImage);
//			String url = saveImage;
//
//			// create an instance of RestTemplate
//			RestTemplate restTemplate = new RestTemplate();
//
//			// create headers
//			HttpHeaders headers = new HttpHeaders();
//			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//			// request body parameters
//			LinkedMultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
//			map.add("files", new MultipartInput(file, "meeting.xlsx"));
//
//			// send POST request
//			ResponseEntity<String> response = null;
//			String responseBody = null;
//
//			map.add("token", tokenForsave);
//			HttpEntity<LinkedMultiValueMap<Object, Object>> entity = new HttpEntity<>(map, headers);
//			response = restTemplate.postForEntity(url, entity, String.class);
//			responseBody = response.getBody();
//
//			if (response.getStatusCode().value() == 200) {
//				responseBody = response.getBody();
//				JsonObject jsonData = new Gson().fromJson(responseBody, JsonObject.class);
//				if (jsonData.has("data")) {
//					JsonObject dataJson = jsonData.get("data").getAsJsonObject();
//
//					if (dataJson.has("fileUrls")) {
//						JsonArray fileUrlsArray = dataJson.getAsJsonArray("fileUrls");
//						fileUrl = fileUrlsArray.get(0).getAsString();
//						return fileUrl;
//
//					}
//				}
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return fileUrl; // You might want to return the actual file URL if available in the response.
//	}
//
//	static class MultipartInput extends InputStreamResource {
//
//		private final String filename;
//
//		MultipartInput(InputStream inputStream, String filename) {
//			super(inputStream);
//			this.filename = filename;
//		}
//
//		@Override
//		public String getFilename() {
//			return this.filename;
//		}
//
//		@Override
//		public long contentLength() throws IOException {
//			return -1; // we do not want to generally read the whole stream into memory ...
//		}
//	}

}
