package com.steganohub.service;

import org.springframework.stereotype.Service;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

@Service
public class LsbSteganographyService {

    private static final byte[] DELIMITER = new byte[8]; // 8 null bytes

    public byte[] encodeMessage(BufferedImage image, String message) {
        byte[] messageBytes = (message + new String(DELIMITER, StandardCharsets.UTF_8)).getBytes(StandardCharsets.UTF_8);
        int messageLength = messageBytes.length;
        int maxBytes = (image.getWidth() * image.getHeight() * 3) / 8;

        if (messageLength > maxBytes) {
            throw new IllegalArgumentException("Nachricht zu lang für dieses Bild");
        }

        int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        int byteIndex = 0;
        int bitIndex = 7;

        for (int i = 0; i < pixels.length && byteIndex < messageLength; i++) {
            int pixel = pixels[i];
            int r = (pixel >> 16) & 0xFF;
            int g = (pixel >> 8) & 0xFF;
            int b = pixel & 0xFF;

            if (byteIndex < messageLength) {
                r = (r & 0xFE) | ((messageBytes[byteIndex] >> bitIndex) & 1);
                bitIndex--;
                if (bitIndex < 0) {
                    bitIndex = 7;
                    byteIndex++;
                }
            }

            if (byteIndex < messageLength) {
                g = (g & 0xFE) | ((messageBytes[byteIndex] >> bitIndex) & 1);
                bitIndex--;
                if (bitIndex < 0) {
                    bitIndex = 7;
                    byteIndex++;
                }
            }

            if (byteIndex < messageLength) {
                b = (b & 0xFE) | ((messageBytes[byteIndex] >> bitIndex) & 1);
                bitIndex--;
                if (bitIndex < 0) {
                    bitIndex = 7;
                    byteIndex++;
                }
            }

            pixels[i] = (pixel & 0xFF000000) | (r << 16) | (g << 8) | b;
        }

        BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        resultImage.setRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            javax.imageio.ImageIO.write(resultImage, "png", baos);
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Speichern des Bildes", e);
        }
        return baos.toByteArray();
    }

    public String decodeMessage(BufferedImage image) {
        int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        ByteArrayOutputStream messageBytes = new ByteArrayOutputStream();
        int currentByte = 0;
        int bitPosition = 7;

        for (int pixel : pixels) {
            int r = (pixel >> 16) & 0xFF;
            int g = (pixel >> 8) & 0xFF;
            int b = pixel & 0xFF;

            currentByte = (currentByte << 1) | (r & 1);
            bitPosition--;
            if (bitPosition < 0) {
                messageBytes.write(currentByte);
                currentByte = 0;
                bitPosition = 7;
            }

            currentByte = (currentByte << 1) | (g & 1);
            bitPosition--;
            if (bitPosition < 0) {
                messageBytes.write(currentByte);
                currentByte = 0;
                bitPosition = 7;
            }

            currentByte = (currentByte << 1) | (b & 1);
            bitPosition--;
            if (bitPosition < 0) {
                messageBytes.write(currentByte);
                currentByte = 0;
                bitPosition = 7;
            }

            // Prüfe auf Delimiter (8 null bytes)
            byte[] bytes = messageBytes.toByteArray();
            if (bytes.length >= 8) {
                boolean foundDelimiter = true;
                for (int i = 0; i < 8; i++) {
                    if (bytes[bytes.length - 8 + i] != 0) {
                        foundDelimiter = false;
                        break;
                    }
                }
                if (foundDelimiter) {
                    byte[] messageWithoutDelimiter = new byte[bytes.length - 8];
                    System.arraycopy(bytes, 0, messageWithoutDelimiter, 0, messageWithoutDelimiter.length);
                    return new String(messageWithoutDelimiter, StandardCharsets.UTF_8);
                }
            }
        }

        throw new RuntimeException("Keine Nachricht im Bild gefunden");
    }
}