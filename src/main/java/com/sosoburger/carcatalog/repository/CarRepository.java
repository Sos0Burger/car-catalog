package com.sosoburger.carcatalog.repository;

import com.sosoburger.carcatalog.dao.CarDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<CarDAO, Integer> {
    CarDAO findByNumber(String number);
}
