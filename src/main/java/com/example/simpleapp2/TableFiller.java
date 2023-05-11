package com.example.simpleapp2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Component
public class TableFiller {
    private static final int BATCH_SIZE = 1000;
    private static final int TOTAL_RECORDS = 10000000;
    private static final String[] maleNames = new String[] {"Alexander", "Boris", "Daniil", "Gennady", "Evgeny", "George", "Igor", "Leonid", "Mikhail", "Nikolai"};
    private static final String[] maleSurnames = new String[] {"Pavlov", "Elkin", "Alchibaev", "Tarkovsky", "Karpov", "Pushkin", "Molchanov", "Parfenov", "Safonenkov", "Romanov"};
    private static final String[] femaleNames = new String[] {"Alexandra", "Bella", "Daria", "Galina", "Evgeniya", "Fatima", "Ilona", "Larisa", "Mariya", "Nelly"};
    private static final String[] femaleSurnames = new String[] {"Charina", "Rappoport", "Kogan", "Albina", "Chetvertak", "Davlyatshina", "Davydova", "Semenova", "Novik", "Uvarova"};


    private JdbcTemplate jdbcTemplate;

    public TableFiller(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public void insertBatch(List<Person> persons) throws SQLException {
        jdbcTemplate.batchUpdate(
                "INSERT INTO Persons (fullname, birth, sex) "
                        + "VALUES (?, ?, ?)", new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Person person = persons.get(i);
                        ps.setString(1, person.getFullName());
                        ps.setDate(2, person.getBirth());
                        ps.setString(3, person.getSex());
                    }

                    public int getBatchSize() {
                        return persons.size();
                    }
                });
    }


    public void fillTable() throws InterruptedException {
        for (int i = 0; i < TOTAL_RECORDS; i += BATCH_SIZE) {
            try {
                List<Person> persons = new ArrayList<>();
                for (int j = 0; j < BATCH_SIZE; j++) {

                    String sex = generateSex();
                    persons.add(new Person(generateRandomName(sex), generateRandomBirthDate(), sex));

                }
                insertBatch(persons);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < 100; i++) {
            try {
                List<Person> persons = new ArrayList<>();
                persons.add(new Person("Felix " + maleSurnames[ThreadLocalRandom.current().nextInt(maleSurnames.length)], generateRandomBirthDate(), "m"));
                insertBatch(persons);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private String generateRandomName(String sex) {
        if (sex.equals("m")) {
            return /* maleNames[ThreadLocalRandom.current().nextInt(maleNames.length)]*/(char)(ThreadLocalRandom.current().nextInt(26) + 'A') + "oris " + maleSurnames[ThreadLocalRandom.current().nextInt(maleSurnames.length)];
        }
        else return femaleNames[ThreadLocalRandom.current().nextInt(femaleNames.length)] + " " + femaleSurnames[ThreadLocalRandom.current().nextInt(femaleSurnames.length)];
    }

    private String generateSex() {
        return (ThreadLocalRandom.current().nextBoolean()) ? "m" : "w";
    }
    private Date generateRandomBirthDate() {
        LocalDate start = LocalDate.of(1930, 1, 1);
        LocalDate end = LocalDate.of(2010, 12, 31);
        long startEpochDay = start.toEpochDay();
        long endEpochDay = end.toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(startEpochDay, endEpochDay);
        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
        return java.sql.Date.valueOf(randomDate);
    }
}