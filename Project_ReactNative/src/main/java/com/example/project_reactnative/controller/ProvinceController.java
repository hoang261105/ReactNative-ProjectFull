package com.example.project_reactnative.controller;

import com.example.project_reactnative.model.dto.response.APIResponse;
import com.example.project_reactnative.model.dto.response.ProvinceResponse;
import com.example.project_reactnative.model.entity.Province;
import com.example.project_reactnative.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/provinces")
public class ProvinceController {
    @Autowired
    private ProvinceService provinceService;

    @GetMapping("/import")
    public String importProvinces() {
        provinceService.importProvincesFromAPI();
        return "Import provinces successfully!";
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<ProvinceResponse>> getProvinceById(@PathVariable Long id){
        return new ResponseEntity<>(new APIResponse<>(true, "Lấy chi tiết thành công!", provinceService.getProvinceById(id)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<ProvinceResponse>>> getAllProvinces() {
        return new ResponseEntity<>(new APIResponse<>(true, "Lấy danh sách tỉnh thành thành công!", provinceService.getAllProvinces()), HttpStatus.OK);
    }
}
