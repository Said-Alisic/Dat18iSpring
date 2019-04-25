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
public class OwnerRepository {

    @Autowired // Handle this field and create the object that needs to be created
    private JdbcTemplate jdbc;

    // Find all cars from database icar table cars
    public List<Owner> findAllOwners() {
        SqlRowSet rs = jdbc.queryForRowSet("SELECT * FROM owners");
        List<Owner> ownerList = new ArrayList<>();
        while (rs.next()) {
            Owner owner = new Owner();
            owner.setId(rs.getInt("id"));
            owner.setFirstName(rs.getString("first_name"));
            owner.setLastName(rs.getString("last_name"));
            owner.setPhoneNo(rs.getString("phone_no"));
            ownerList.add(owner);
        }
        return ownerList;
    }

    // Adding a car to the MySQL database with JDBCtemplate.update
    public void saveOwner(Owner owner) {
        String query = String.format("INSERT INTO owners (first_name, last_name, phone_no) " +
                        " VALUES ('%s', '%s', '%s')",
                owner.getFirstName(), owner.getLastName(), owner.getPhoneNo());
        jdbc.update(query);


    }

    // Alex's version with PreparedStatement
    public Owner insertOwner(Owner owner) {

        PreparedStatementCreator ps = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO owners VALUES (null, ?, ?, ?)", new String[]{"id"});
                ps.setString(1, owner.getFirstName());
                ps.setString(2, owner.getLastName());
                ps.setString(3, owner.getPhoneNo());

                return ps;
            }
        };

        KeyHolder keyholder = new GeneratedKeyHolder();
        jdbc.update(ps, keyholder);
        owner.setId(keyholder.getKey().intValue());
        return owner;

    }

    // Deleting a car inside the MySQL database with JDBCtemplate.update
    public void deleteOwner(Owner owner) {
        String query1 = "DELETE FROM owned_cars WHERE owner_id = " + owner.getId();
        jdbc.update(query1);
        String query2 = "DELETE FROM owners WHERE id = " + owner.getId();
        jdbc.update(query2);
    }

    // Editing a car inside the MySQL database with JDBCtemplate.update
    public void editOwner(Owner owner, int id) {
        String query = String.format(("UPDATE owners "
                        + "SET first_name = '%s', "
                        + "last_name = '%s', "
                        + "phone_no = '%s' "
                        + "WHERE id = %s"),
                owner.getFirstName(), owner.getLastName(), owner.getPhoneNo(), id);
        jdbc.update(query);
    }

}
