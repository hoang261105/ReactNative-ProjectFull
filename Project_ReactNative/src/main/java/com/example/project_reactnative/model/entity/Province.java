package com.example.project_reactnative.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "provinces")
@Data
public class Province {
    @Id
    private Long id;

    private String provinceName;

    private String imageURL;

    @ElementCollection
    @CollectionTable(name = "province_license_plates", joinColumns = @JoinColumn(name = "province_id"))
    @Column(name = "plate")
    private List<String> licensePlates;

    @OneToMany(mappedBy = "province", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ward> wards = new ArrayList<>();
}
