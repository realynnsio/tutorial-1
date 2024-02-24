package id.ac.ui.cs.advprog.eshop.service;
import id.ac.ui.cs.advprog.eshop.model.Car;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CarService {
    public Car create(Car car);
    public List<Car> findAll();
    Car findById(String carId);
    public void update(String carId, Car car);
    public void deleteCarById(String carId);
}
