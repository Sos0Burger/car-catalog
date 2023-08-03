package com.sosoburger.carcatalog.service;

import com.sosoburger.carcatalog.dao.CarDAO;
import com.sosoburger.carcatalog.exception.AlreadyExists;

import java.util.List;

public interface CarService {
    CarDAO save(CarDAO carDAO) throws AlreadyExists;

    List<CarDAO> getAll();

    void delete(CarDAO carDAO);

    List<CarDAO> findBy(
            String model,
            String brand,
            String Category,
            String number,
            String releaseYear,
            String type,
            String trailer
    );
}