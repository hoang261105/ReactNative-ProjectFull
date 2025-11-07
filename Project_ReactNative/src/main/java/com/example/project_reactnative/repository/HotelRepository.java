package com.example.project_reactnative.repository;

import com.example.project_reactnative.model.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> getHotelsByProvince_Id(Long provinceId);
    List<Hotel> findByProvinceIdIn(List<Long> provinceIds);
}
