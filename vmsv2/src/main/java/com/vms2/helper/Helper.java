package com.vms2.helper;

import org.springframework.web.multipart.MultipartFile;



public class Helper {
	
	
	public static Boolean CheckImage(MultipartFile image)

	{
	String contentType = image.getContentType();
	

			boolean isImage = contentType != null && (
			        contentType.startsWith("image/jpeg") ||
			        contentType.startsWith("image/png") ||
			        contentType.startsWith("image/gif") ||
			        contentType.startsWith("image/bmp") ||
			        contentType.startsWith("image/tiff") ||
			        contentType.startsWith("image/webp") ||
			        contentType.startsWith("image/svg+xml")
			);
			return isImage;

	}

}
