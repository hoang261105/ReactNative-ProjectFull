package com.example.project_reactnative.service;

import com.example.project_reactnative.model.dto.response.ProvinceResponse;

import java.util.List;

public interface ProvinceService {
    void importProvincesFromAPI();
    List<ProvinceResponse> getAllProvinces();
    ProvinceResponse getProvinceById(Long id);
}
