package com.example.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Person {
    public int id;
    public String name;
    public String gender;
    public LocalDate birthDate;
    public Division division;
    public int salary;

    public Person(int id, String name, String gender, LocalDate birthDate, Division division, int salary) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.division = division;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", birthDate=" + birthDate +
                ", division=" + division +
                ", salary=" + salary +
                '}';
    }
}



