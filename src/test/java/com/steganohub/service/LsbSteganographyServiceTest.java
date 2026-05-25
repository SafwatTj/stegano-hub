package com.steganohub.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LsbSteganographyServiceTest {

    private final LsbSteganographyService service = new LsbSteganographyService();

    private BufferedImage createTestImage(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    @Test
    public void testEncodeAndDecode() {
        BufferedImage image = createTestImage(100, 100);
        String originalMessage = "Hallo Welt! 123 äöüß";

        byte[] encodedImage = service.encodeMessage(image, originalMessage);

        BufferedImage decodedImage = null;
        try {
            decodedImage = ImageIO.read(new ByteArrayInputStream(encodedImage));
        } catch (Exception e) {
            fail("Fehler beim Lesen des kodierten Bildes");
        }

        String decodedMessage = service.decodeMessage(decodedImage);
        assertEquals(originalMessage, decodedMessage);
    }

    @Test
    public void testMessageZuLang() {
        BufferedImage image = createTestImage(10, 10);
        String longMessage = "A".repeat(1000);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.encodeMessage(image, longMessage);
        });

        assertTrue(exception.getMessage().contains("Nachricht zu lang"));
    }

    @Test
    public void testLeereNachricht() {
        BufferedImage image = createTestImage(50, 50);
        String emptyMessage = "";

        byte[] encodedImage = service.encodeMessage(image, emptyMessage);

        BufferedImage decodedImage = null;
        try {
            decodedImage = ImageIO.read(new ByteArrayInputStream(encodedImage));
        } catch (Exception e) {
            fail("Fehler beim Lesen");
        }

        String decodedMessage = service.decodeMessage(decodedImage);
        assertEquals("", decodedMessage);
    }
}