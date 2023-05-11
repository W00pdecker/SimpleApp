package com.example.simpleapp2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.SQLException;
import java.util.List;

@SpringBootApplication
public class SimpleApp2Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SimpleApp2Application.class, args);
    }

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    TableFiller filler;

    @Override
    public void run(String... args) {

        try {
            switch (args[0]) {
            case "1" -> createTable();
            case "2" -> addLine(args);
            case "3" -> selectUnics();
            case "4" -> {filler.fillTable();
                System.out.println("Table filled with 1M random names (and 100 not so random)");}
            case "5" -> executeOrder66();
            default -> System.out.println("Invalid argument");
        }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL exception occurred");
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("Interrupted exception occurred");
        }
    }

    private void createTable() {
        jdbcTemplate.execute("DROP TABLE Persons IF EXISTS");
        String query = "CREATE TABLE Persons" +
                "(id INTEGER not NULL AUTO_INCREMENT, " +
                " fullname VARCHAR(50), " +
                " birth DATE, " +
                " sex VARCHAR(1), " +
                "PRIMARY KEY (id))";
        jdbcTemplate.execute(query);
        System.out.println("Table PERSONS created");
    }

    private void addLine(String... args) {
        String query = "INSERT INTO Persons( FULLNAME, BIRTH, SEX) " +
                "VALUES ('" + args[0] + "', '"
                + args[1] + "', '"
                + args[2] + "')";
        jdbcTemplate.update(query);
        System.out.println("Entry added");
    }

    // selecting rows with distinct values
    private void selectUnics() throws SQLException {
        String query = "SELECT DISTINCT  FULLNAME, BIRTH, SEX FROM persons order by FULLNAME";
        List<Person> personList = jdbcTemplate.query(query, new PersonRowMapper());
        for (Person person : personList) {
            System.out.println(person.toString());
        }
    }

    private void executeOrder66() throws SQLException {
        String query = "SELECT DISTINCT  FULLNAME, BIRTH, SEX FROM PERSONS WHERE FULLNAME LIKE 'F%' AND SEX = 'm' order by FULLNAME ";
        long start = System.currentTimeMillis();
        List<Person> personList = jdbcTemplate.query(query, new PersonRowMapper());
        long end = System.currentTimeMillis();
        for (Person person : personList) {
            System.out.println(person.toString());
        }
        long duration = end - start;
        System.out.println("Execution time: " + duration + " ms");
    }
}
