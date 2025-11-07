package com.example.project_reactnative.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "wards")
@Data
public class Ward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection
    @CollectionTable(name = "ward_merged_from", joinColumns = @JoinColumn(name = "ward_id"))
    @Column(name = "merged_name")
    private List<String> mergedFrom;

    @ManyToOne
    @JoinColumn(name = "province_id", referencedColumnName = "id")
    private Province province;
}

