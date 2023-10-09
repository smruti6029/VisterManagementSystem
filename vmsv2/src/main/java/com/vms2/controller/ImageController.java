package com.vms2.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vms2.helper.Fileservice;
import com.vms2.helper.Helper;
import com.vms2.response.Response;

@RestController
@RequestMapping("/api/image/")
public class ImageController {

  @Value("${project.image}")
  private String path;

//	private static final String IMAGE_UPLOAD_PATH = "/home/rapidsoft/Documents/Spring_Security/vmsv2";
//
////	
//
//	String path = IMAGE_UPLOAD_PATH;

	@PostMapping("/upload")
	public ResponseEntity<?> upload(@RequestParam("image") MultipartFile image) throws IOException {

		Boolean checkImage = Helper.CheckImage(image);

		if (checkImage) {
			String fileName = Fileservice.uploadImage(path, image);
			return new ResponseEntity<>(new Response<>("Image uploaded successfully", fileName, HttpStatus.OK.value()),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(
				new Response<>("Upload Vallid Image", "Upload VAllid Image ", HttpStatus.BAD_REQUEST.value()),
				HttpStatus.BAD_REQUEST);

	}

	@GetMapping(value = "/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(@PathVariable("imageName") String imageName, HttpServletResponse response)
			throws IOException {
		System.out.println(imageName);

		try {
			InputStream resource = Fileservice.getResource(path, imageName);

			if (resource != null) {
				response.setContentType(MediaType.IMAGE_JPEG_VALUE);

				StreamUtils.copy(resource, response.getOutputStream());

			} else {

				response.setStatus(HttpServletResponse.SC_NOT_FOUND);

				response.getWriter().write("Image not found");

			}

		} catch (Exception e) {

			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

			response.getWriter().write("Error occurred while fetching the image");
		}

	}

}
