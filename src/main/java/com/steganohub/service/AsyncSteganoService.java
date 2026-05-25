package com.steganohub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.concurrent.CompletableFuture;

@Service
public class AsyncSteganoService {

    @Autowired
    private LsbSteganographyService lsbService;

    @Async("taskExecutor")
    public CompletableFuture<byte[]> encodeAsync(BufferedImage image, String message) {
        byte[] result = lsbService.encodeMessage(image, message);
        return CompletableFuture.completedFuture(result);
    }

    @Async("taskExecutor")
    public CompletableFuture<String> decodeAsync(BufferedImage image) {
        String result = lsbService.decodeMessage(image);
        return CompletableFuture.completedFuture(result);
    }
}