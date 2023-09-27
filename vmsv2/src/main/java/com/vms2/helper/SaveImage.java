//package com.vms2.helper;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.URL;
//
//public class SaveImage {
//	
//	
//	
//	if (!imageFile.isEmpty()) {
//        try {
//             Create the directory if it doesn't exist
//            File imageDir = new File(IMAGE_DIRECTORY);
//            if (!imageDir.exists()) {
//                imageDir.mkdirs();
//            }
//
//            // Get the original filename of the uploaded image
//            String originalFileName = imageFile.getOriginalFilename();
//
//            // Construct the file path where you want to save the uploaded image
//            String filePath = IMAGE_DIRECTORY + File.separator + originalFileName;
//
//            // Copy the content of the uploaded image to the specified location
//            Path destinationPath = new File(filePath).toPath();
//            Files.copy(imageFile.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
//
//            System.out.println("Image uploaded successfully to: " + filePath);
//        } catch (IOException e) {
//            e.printStackTrace();
//            // Handle the exception (e.g., log or return an error response)
//        }
//    } else {
//        // Handle the case where the uploaded image is empty
//    }
//
//    // Redirect or return a response as needed
//    return "redirect:/";
//
//}
