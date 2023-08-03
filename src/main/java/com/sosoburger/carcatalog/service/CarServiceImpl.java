package com.sosoburger.carcatalog.service;

import com.sosoburger.carcatalog.dao.CarDAO;
import com.sosoburger.carcatalog.exception.AlreadyExists;
import com.sosoburger.carcatalog.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImpl implements CarService {
    @Autowired
    CarRepository carRepository;

    @Override
    public CarDAO save(CarDAO carDAO) throws AlreadyExists {
        /*
        Проверка на уже существующий гос. номер,
        кроме тех случаев когда id машины совпадает(при редактировании, например)
        */
        if (
                carRepository.findByNumber(carDAO.getNumber()) != null
                        &&
                        !carRepository.findByNumber(carDAO.getNumber()).getId().equals(carDAO.getId())) {
            throw new AlreadyExists("Машина с таким номером уже существует");
        }
        return carRepository.save(carDAO);
    }

    @Override
    public List<CarDAO> getAll() {
        return carRepository.findAll();
    }

    @Override
    public void delete(CarDAO carDAO) {
        carRepository.delete(carDAO);
    }

    @Override
    public List<CarDAO> findBy(
            String model,
            String brand,
            String category,
            String number,
            String releaseYear,
            String type,
            String trailer) {
        return carRepository.findByModelContainingIgnoreCaseAndBrandContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndNumberContainingIgnoreCaseAndReleaseYearContainingIgnoreCaseAndTypeContainingIgnoreCaseAndTrailerContainingIgnoreCase(
                model,
                brand,
                category,
                number,
                releaseYear,
                type,
                trailer
        );
    }
}
