package com.genai.image;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ImageService {

    @Value("${api.token}")
    private String apiToken;

    @Value("${api.url}")
    private String apiUrl;

    public String replaceImageBackground(String imagePath, String outputType, String modification) {
        File imageFile = new File(imagePath);

        HttpResponse<String> response = Unirest.post(apiUrl)
                .header("Authorization", "Bearer " + apiToken)
                .field("existing_image", imageFile)
                .field("output_type", outputType)
                .field("modification", modification)
                .asString();

        if (response.getStatus() == 200) {
            return response.getBody();
        } else {
            throw new RuntimeException(
                    "Failed to replace image background: " + response.getStatus() + " - " + response.getBody());
        }
    }
}
