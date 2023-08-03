package com.sosoburger.carcatalog.service;

import com.sosoburger.carcatalog.dao.CarDAO;
import com.sosoburger.carcatalog.exception.AlreadyExists;
import com.sosoburger.carcatalog.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarServiceImpl implements CarService {
    @Autowired
    CarRepository carRepository;

    @Override
    public CarDAO save(CarDAO carDAO) throws AlreadyExists {
        if(carRepository.findByNumber(carDAO.getNumber())!=null){
            throw new AlreadyExists("Машина с таким номером уже существует");
        }
        return carRepository.save(carDAO);
    }
}
