package com.vms2.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class Fileservice {

	public static String uploadImage(String path, MultipartFile file) throws IOException {

		String name = file.getOriginalFilename();

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
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/image/")
				.path(fileName1).toUriString();

		return fileDownloadUri;

	}
	
	
	public static InputStream getResource(String path, String fileName) throws FileNotFoundException {
	   String fullPath = path+File.separator+fileName;
		
		InputStream is = new FileInputStream(fullPath);
		
		return is;
	}

}
