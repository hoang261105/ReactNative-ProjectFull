package com.example.project_reactnative.model.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ProvinceDTO {
    private String id;
    private String provinceName;
    private List<String> licensePlates;
    private List<WardDTO> wards;
}
