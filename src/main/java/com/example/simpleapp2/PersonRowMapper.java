package com.example.simpleapp2;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonRowMapper implements RowMapper<Person> {
    @Override
    public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
        Person person = new Person();
        person.setFullname(rs.getString("FULLNAME"));
        person.setBIRTH(rs.getDate("BIRTH"));
        person.setSEX(rs.getString("SEX"));

        return person;
    }
}
