package com.smsr.CropDiseaseDetection;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/api/v1")
public class ImageController {

    @Autowired
    private ImageProcessingService imageProcessingService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestBody Map<String, Object> request) {
        try {
            // Extract the 'images' list from the request
            List<String> images = (List<String>) request.get("images");

            if (images == null || images.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No images found in the request");
            }

            // Extract the first image from the list
            String base64Image = images.get(0);

            // Call the service layer to process the image and get the result
            String apiResponse = imageProcessingService.processImage(base64Image);

            // Send the external API's response back to Postman
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);

        } catch (Exception e) {
            System.err.println("Error handling the image: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing image");
        }
    }
}
