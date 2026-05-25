package com.steganohub.controller;

import com.steganohub.service.AsyncSteganoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/async/stegano")
public class AsyncSteganoController {

    @Autowired
    private AsyncSteganoService asyncService;

    @PostMapping(value = "/encode", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CompletableFuture<ResponseEntity<byte[]>> encodeAsync(
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam("message") String message) {
        try {
            if (!imageFile.getContentType().equals("image/png")) {
                return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
            }

            if (message.length() > 1000) {
                return CompletableFuture.completedFuture(ResponseEntity.status(413).build());
            }

            BufferedImage image = ImageIO.read(imageFile.getInputStream());

            return asyncService.encodeAsync(image, message)
                    .thenApply(result -> ResponseEntity.ok()
                            .contentType(MediaType.IMAGE_PNG)
                            .body(result));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());
        }
    }

    @PostMapping(value = "/decode", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CompletableFuture<ResponseEntity<String>> decodeAsync(
            @RequestParam("image") MultipartFile imageFile) {
        try {
            if (!imageFile.getContentType().equals("image/png")) {
                return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
            }

            BufferedImage image = ImageIO.read(imageFile.getInputStream());

            return asyncService.decodeAsync(image)
                    .thenApply(ResponseEntity::ok);
        } catch (Exception e) {
            return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());
        }
    }
}