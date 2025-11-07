package com.example.project_reactnative.service;

import com.example.project_reactnative.model.dto.request.ProvinceDTO;
import com.example.project_reactnative.model.dto.request.WardDTO;
import com.example.project_reactnative.model.dto.response.ProvinceResponse;
import com.example.project_reactnative.model.entity.Province;
import com.example.project_reactnative.model.entity.Ward;
import com.example.project_reactnative.repository.ProvinceRepository;
import com.example.project_reactnative.repository.WardRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProvinceServiceImp implements ProvinceService {
    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private WardRepository wardRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void importProvincesFromAPI() {
        String url = "https://vietnamlabs.com/api/vietnamprovince";
        RestTemplate restTemplate = new RestTemplate();

        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);

            JsonNode root = objectMapper.readTree(jsonResponse);

            JsonNode dataNode = root.get("data");

            ProvinceDTO[] provinceDTOs = objectMapper.readValue(dataNode.toString(), ProvinceDTO[].class);

            Arrays.stream(provinceDTOs).forEach(dto -> {
                Province province = new Province();
                province.setId(Long.parseLong(dto.getId()));
                province.setProvinceName(dto.getProvinceName());
                province.setLicensePlates(dto.getLicensePlates());

                provinceRepository.saveAndFlush(province);

                if (dto.getWards() != null) {
                    dto.getWards().forEach((WardDTO wardDTO) -> {
                        Ward ward = new Ward();
                        ward.setName(wardDTO.getName());
                        ward.setMergedFrom(wardDTO.getMergedFrom());
                        ward.setProvince(province);
                        wardRepository.save(ward);
                    });
                }
            });

            System.out.println("✅ Import thành công " + provinceDTOs.length + " tỉnh/thành từ API.");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Lỗi khi import dữ liệu tỉnh/thành: " + e.getMessage());
        }
    }

    public List<ProvinceResponse> getAllProvinces() {
        List<Province> provinces = provinceRepository.findAll();
        return provinces.stream().map(this::convertToResponse).toList();
    }

    public ProvinceResponse getProvinceById(Long id) {
        Province province = provinceRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Không tìm thấy tỉnh thành!"));
        return convertToResponse(province);
    }

    public ProvinceResponse convertToResponse(Province province) {
        return new ProvinceResponse(
                province.getId(),
                province.getProvinceName(),
                province.getImageURL()
        );
    }

}
