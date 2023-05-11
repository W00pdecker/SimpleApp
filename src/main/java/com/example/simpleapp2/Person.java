package com.example.simpleapp2;

import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

@Data
public class Person {
    private String fullname;
    private Date BIRTH;
    private String SEX;

    public Person() {
    }
    public Person(String fullname, Date BIRTH, String SEX) {
        this.fullname = fullname;
        this.BIRTH = BIRTH;
        this.SEX = SEX;
    }
    @Override
    public String toString() {
        return String.format(
                "%s, birth: %tD, sex: %s, age: %d",
                fullname, BIRTH, SEX, getAge());
    }

    public int getAge() {
        LocalDate birthLocalDate = BIRTH.toLocalDate();
        LocalDate today = LocalDate.now();
        Period period = Period.between(birthLocalDate, today);
        return period.getYears();
    }


}
