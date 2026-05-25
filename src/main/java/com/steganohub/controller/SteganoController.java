package com.steganohub.controller;

import com.steganohub.model.Operation;
import com.steganohub.service.LsbSteganographyService;
import com.steganohub.service.MetricsService;
import com.steganohub.service.OperationService;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.List;

@RestController
@RequestMapping("/api/stegano")
public class SteganoController {

    @Autowired
    private LsbSteganographyService steganographyService;

    @Autowired
    private MetricsService metricsService;

    @Autowired
    private OperationService operationService;

    @PostMapping(value = "/encode", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> encodeImage(
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam("message") String message) throws Exception {

        Timer.Sample sample = metricsService.startTimer();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            if (!imageFile.getContentType().equals("image/png")) {
                metricsService.incrementError();
                operationService.saveOperation("ENCODE", username, String.valueOf(message.length()),
                        imageFile.getOriginalFilename(), "FAILED - Invalid format");
                return ResponseEntity.badRequest().build();
            }

            if (message.length() > 1000) {
                metricsService.incrementError();
                operationService.saveOperation("ENCODE", username, String.valueOf(message.length()),
                        imageFile.getOriginalFilename(), "FAILED - Message too long");
                return ResponseEntity.status(413).build();
            }

            BufferedImage image = ImageIO.read(imageFile.getInputStream());
            byte[] encodedImage = steganographyService.encodeMessage(image, message);

            metricsService.incrementEncode();
            metricsService.stopTimer(sample, "encode");
            operationService.saveOperation("ENCODE", username, String.valueOf(message.length()),
                    imageFile.getOriginalFilename(), "SUCCESS");

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(encodedImage);
        } catch (Exception e) {
            metricsService.incrementError();
            operationService.saveOperation("ENCODE", username, String.valueOf(message.length()),
                    imageFile.getOriginalFilename(), "ERROR: " + e.getMessage());
            throw e;
        }
    }

    @PostMapping(value = "/decode", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> decodeImage(
            @RequestParam("image") MultipartFile imageFile) throws Exception {

        Timer.Sample sample = metricsService.startTimer();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            if (!imageFile.getContentType().equals("image/png")) {
                metricsService.incrementError();
                operationService.saveOperation("DECODE", username, "0",
                        imageFile.getOriginalFilename(), "FAILED - Invalid format");
                return ResponseEntity.badRequest().build();
            }

            BufferedImage image = ImageIO.read(imageFile.getInputStream());
            String message = steganographyService.decodeMessage(image);

            metricsService.incrementDecode();
            metricsService.stopTimer(sample, "decode");
            operationService.saveOperation("DECODE", username, String.valueOf(message.length()),
                    imageFile.getOriginalFilename(), "SUCCESS");

            return ResponseEntity.ok(message);
        } catch (Exception e) {
            metricsService.incrementError();
            operationService.saveOperation("DECODE", username, "0",
                    imageFile.getOriginalFilename(), "ERROR: " + e.getMessage());
            throw e;
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<Operation>> getHistory() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Operation> operations = operationService.getAllOperations();
        return ResponseEntity.ok(operations);
    }
}