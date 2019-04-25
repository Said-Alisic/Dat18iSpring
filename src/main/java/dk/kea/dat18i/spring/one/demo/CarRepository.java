package dk.kea.dat18i.spring.one.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Repository
public class CarRepository {


    @Autowired // Handle this field and create the object that needs to be created
    private JdbcTemplate jdbc;



    // find specific car in table cars from database icar by id
    public Car findCar(int id) {
        // Create query for sql and parse an object into class
        SqlRowSet rs = jdbc.queryForRowSet("SELECT * FROM cars WHERE id = " + id);
        Car car = new Car();
        while (rs.next()) {
            car.setId(rs.getInt("id"));
            car.setBrand(rs.getString("brand"));
            car.setColor(rs.getString("color"));
            car.setMaxSpeed(rs.getDouble("max_speed"));
            car.setReg(rs.getString("reg"));

        }

        return car;
    }

    // Find all cars from database icar table cars
    public List<Car> findAllCars() {
        SqlRowSet rs = jdbc.queryForRowSet("SELECT * FROM cars");
        List<Car> carList = new ArrayList<>();
        while (rs.next()) {
            Car car = new Car();
            car.setId(rs.getInt("id"));
            car.setBrand(rs.getString("brand"));
            car.setColor(rs.getString("color"));
            car.setMaxSpeed(rs.getDouble("max_speed"));
            car.setReg(rs.getString("reg"));
            carList.add(car);
        }
        return carList;
    }

    // Adding a car to the MySQL database with JDBCtemplate.update
    public void saveCar(Car car) {
        String query = String.format("INSERT INTO cars (reg, brand, color, max_speed) " +
                                     " VALUES ('%s', '%s', '%s', '%s')",
                 car.getReg(), car.getBrand(), car.getColor(), car.getMaxSpeed());
        jdbc.update(query);


//
//        String query2 = String.format("INSERT INTO cars" +
//                        " VALUES (NULL, '%s', '%s', '%s', '%s')",
//                car.getReg(), car.getBrand(), car.getColor(), car.getMaxSpeed());
//
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//        jdbc.update((
//                new PreparedStatementCreator() {
//            @Override
//            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
//                PreparedStatement ps =
//                        connection.prepareStatement(query2, new String[] {"id"});
//                ps.setString(1,car.getReg());
//                ps.setString(2,car.getBrand());
//                ps.setString(3,car.getColor());
//                ps.setDouble(4,car.getMaxSpeed());
//                return ps;
//            }
//        }), keyHolder);
//        System.out.println(keyHolder.getKey());


    }

    // Alex's version with PreparedStatement
    public Car insert(Car car) {

        PreparedStatementCreator ps = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO cars VALUES (null, ?, ?, ?, ?)", new String[]{"id"});
                ps.setString(1, car.getReg());
                ps.setString(2, car.getBrand());
                ps.setString(3, car.getColor());
                ps.setDouble(4, car.getMaxSpeed());

                return ps;
            }
        };

        KeyHolder keyholder = new GeneratedKeyHolder();
        jdbc.update(ps, keyholder);
        car.setId(keyholder.getKey().intValue());
        return car;

    }

    // Deleting a car inside the MySQL database with JDBCtemplate.update
    public void deleteCar(Car car) {
        String query = "DELETE FROM cars WHERE id = " + car.getId();
        jdbc.update(query);
    }

    // Editing a car inside the MySQL database with JDBCtemplate.update
    public void editCar(Car car1, Car car2) {
        String query = String.format(("UPDATE cars "
                                   + "SET reg = '%s', "
                                   + "brand = '%s', "
                                   + "color = '%s', "
                                   + "max_speed = '%s' "
                                   + "WHERE id = %s"),
                car1.getReg(), car1.getBrand(), car1.getColor(), car1.getMaxSpeed(), car2.getId());
        jdbc.update(query);
    }



}
