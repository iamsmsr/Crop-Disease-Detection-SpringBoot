package com.smsr.CropDiseaseDetection;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class ImageProcessingService {

    private static final String apiUrl = "https://crop.kindwise.com/api/v1/identification";
    private static final String apiKey = "50uQWkH5U7xym2o7g7Bb15FX02MLsk6QD7MbrcV5o1BAa3YwQH";

    public String processImage(String base64Image) {
        try {
            return sendToExternalAPI(base64Image);
        } catch (Exception e) {
            System.err.println("Error while processing image: " + e.getMessage());
            e.printStackTrace();
            return "Failed to process image";
        }
    }

    private String sendToExternalAPI(String base64Image) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        // Prepare the request body with a JSON structure
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("images", Collections.singletonList(base64Image));  // Wrap Base64 image in a list

        // Set the headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Api-Key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create an HttpEntity to send the request body and headers
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            // Send the POST request to the external API
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, requestEntity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();  // Return the external API's response as-is
            } else {
                // Log if there is an error
                System.err.println("API Response Error: " + response.getStatusCode() + " - " + response.getBody());
                return "Error calling external API: " + response.getBody();
            }
        } catch (Exception e) {
            System.err.println("Exception while calling API: " + e.getMessage());
            throw new Exception("Failed to communicate with the external API.", e);
        }
    }
}

