package com.genai.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class Controller {

    @Autowired
    private ImageService imageService;


    @PostMapping("/api/images/replace-background")
    public ResponseEntity<String> replaceBackground(
            @RequestParam("image") MultipartFile image,
            @RequestParam("outputType") String outputType,
            @RequestParam("modification") String modification) {

        try {
            // Save the uploaded file to a temporary location
            Path tempDir = Files.createTempDirectory("");
            File tempFile = tempDir.resolve(image.getOriginalFilename()).toFile();
            image.transferTo(tempFile);

            // Call the service to replace the background
            String response = imageService.replaceImageBackground(tempFile.getAbsolutePath(), outputType, modification);

            // Clean up the temporary file
            Files.deleteIfExists(tempFile.toPath());
            Files.deleteIfExists(tempDir);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }
}
