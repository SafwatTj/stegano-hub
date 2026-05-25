package com.steganohub.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "operations")
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String username;
    private String messageLength;
    private String imageName;
    private LocalDateTime timestamp;
    private String status;

    public Operation() {}

    public Operation(String type, String username, String messageLength, String imageName, String status) {
        this.type = type;
        this.username = username;
        this.messageLength = messageLength;
        this.imageName = imageName;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    // Getter und Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getMessageLength() { return messageLength; }
    public void setMessageLength(String messageLength) { this.messageLength = messageLength; }

    public String getImageName() { return imageName; }
    public void setImageName(String imageName) { this.imageName = imageName; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}