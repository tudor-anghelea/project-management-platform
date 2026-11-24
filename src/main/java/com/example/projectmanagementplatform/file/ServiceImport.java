package com.example.projectmanagementplatform.file;
import com.example.projectmanagementplatform.analytics.Overview;
import com.fasterxml.jackson.core.JsonProcessingException;
import tools.jackson.databind.ObjectMapper;

public class ServiceImport {

    private final ObjectMapper objectMapper;

    public ServiceImport(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    public Overview importOverview(byte[] json) throws JsonProcessingException {

        return objectMapper.readValue(json, Overview.class);
    }
}
