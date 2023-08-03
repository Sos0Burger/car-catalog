package com.sosoburger.carcatalog.service;

import com.sosoburger.carcatalog.dao.CarDAO;
import com.sosoburger.carcatalog.exception.AlreadyExists;

public interface CarService {
    CarDAO save(CarDAO carDAO) throws AlreadyExists;
}