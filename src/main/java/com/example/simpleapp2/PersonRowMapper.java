package com.example.simpleapp2;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonRowMapper implements RowMapper<Person> {
    @Override
    public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
        Person person = new Person();
        person.setFullName(rs.getString("FULLNAME"));
        person.setBirth(rs.getDate("BIRTH"));
        person.setSex(rs.getString("SEX"));

        return person;
    }
}
