package com.example.projectmanagementplatform.file;
import com.example.projectmanagementplatform.analytics.Overview;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class ServiceExport{

    private final ObjectMapper objectMapper;

    public ServiceExport(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    public byte[] exportProject(Overview overview) throws JsonProcessingException
    {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(overview);
    }
}
