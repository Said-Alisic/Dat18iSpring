package dk.kea.dat18i.spring.one.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CarController {


    @Autowired // Handle this field and create the object that needs to be created
    private JdbcTemplate jdbc;

    @Autowired
    private CarRepository carRepo;

    private static List<Car> carList = new ArrayList<>(
//            Arrays.asList(
//                        new Car("1234", "BMW", "pink", 399.99),
//                        new Car("4567", "VW", "red", 452),
//                        new Car("7823", "Tesla", "orange", 130)
//            )
    );

        // from arraylist data
//    @GetMapping("/cars")
//    public String car(Model model){
//        model.addAttribute("cars", carList);
//        model.addAttribute("newCar", new Car());
//        return "show-car";
//    }

    // from MySQL database  data
    @GetMapping("/cars")
    public String car(Model model){
        carList = carRepo.findAllCars();
        model.addAttribute("cars", carList);
        model.addAttribute("newCar", new Car());
        return "show-car";
    }

    // View of editing cars
    @GetMapping("/cars/edit/{index}")
    public String handleEditCar(@PathVariable int index, Model model) {
        Car editCar = carList.get(index);
        model.addAttribute("index", index);
        model.addAttribute("editCar", editCar);
        return "edit-car";
    }



    // Delete car from MySQL database  and redirect back to /mycar
    @GetMapping(value = "/cars/delete/{index}")
    public String handleDeleteCar(@PathVariable int index) {
//        carList.remove(index);
        carRepo.deleteCar(carList.get(index));
        return "redirect:/cars";
    }

    // Add car to MySQL database
    // Can also use @PostMapping which is short for the @RequestMapping with RequestMethod.POST
    @PostMapping(value = "/cars")
    public String handleAddCar(@ModelAttribute Car car) {
        //carList.add(car);
        carRepo.saveCar(car);

        return "redirect:/cars";
    }

    @PostMapping(value = "/savecar")
    public String saveCar(@ModelAttribute Car car) {
        car = carRepo.insert(car);
        return "redirect:/cars";
    }


    // Edit car from MySQL database and redirect back to /mycar
    @PostMapping(value = "/cars/edit/{index}")
    public String handleEditCar(@PathVariable int index, @ModelAttribute Car car) {
//        carList.set(index, car);
        carRepo.editCar(car, carList.get(index));
        return "redirect:/cars";
    }






//
////    for testing purposes only
//    @GetMapping("/carv")
//    @ResponseBody       // don't want to point to a view but just display something
//    public Car showCar() {
//        Car car = carRepo.findCar(1);
//        return car;
//    }
//
//
//    // Johan's testing with different ID's
//    @GetMapping("/carv/{id}")
//    @ResponseBody
//    public Car showCar(@PathVariable int id){
//        return carRepo.findCar(id);
//    }




}
