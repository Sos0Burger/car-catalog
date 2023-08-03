package com.sosoburger.carcatalog.repository;

import com.sosoburger.carcatalog.dao.CarDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<CarDAO, Integer> {
    CarDAO findByNumber(String number);

    //Поиск по всем полям :)
    List<CarDAO> findByModelContainingAndBrandContainingAndCategoryContainingAndNumberContainingAndReleaseYearContainingAndTypeContainingAndTrailerContaining(
            String model,
            String brand,
            String category,
            String number,
            String releaseYear,
            String type,
            String trailer);

}
