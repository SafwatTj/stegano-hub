package com.steganohub.service;

import com.steganohub.model.Operation;
import com.steganohub.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationService {

    @Autowired
    private OperationRepository operationRepository;

    public void saveOperation(String type, String username, String messageLength, String imageName, String status) {
        Operation operation = new Operation(type, username, messageLength, imageName, status);
        operationRepository.save(operation);
    }

    public List<Operation> getAllOperations() {
        return operationRepository.findAll();
    }
}