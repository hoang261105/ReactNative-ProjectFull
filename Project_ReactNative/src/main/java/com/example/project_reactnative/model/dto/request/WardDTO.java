package com.example.project_reactnative.model.dto.request;

import com.example.project_reactnative.util.StringListDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.List;

@Data
public class WardDTO {
    private String name;

    @JsonDeserialize(using = StringListDeserializer.class)
    private List<String> mergedFrom;
}
