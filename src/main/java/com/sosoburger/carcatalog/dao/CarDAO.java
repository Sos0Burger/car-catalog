package com.sosoburger.carcatalog.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "cars")
public class CarDAO {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "brand")
    String brand;

    @Column(name = "model")
    String model;

    @Column(name = "category")
    String category;

    @Column(name = "number")
    String number;

    @Column(name = "release-year")
    String releaseYear;

    @Column(name = "type")
    String type;

    @Column(name = "trailer")
    String trailer;
}
