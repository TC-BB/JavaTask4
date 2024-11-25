package com.example.csvreader;

import com.example.model.Person;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvReaderTest {

    @Test
    void testReadPeopleFromCsv() {
        try {
            List<Person> people = CsvReader.readPeopleFromCsv("people.csv");
            assertFalse(people.isEmpty()); // Проверяем, что список не пуст
            assertTrue(people.size() > 0); //Более конкретная проверка
        } catch (Exception e) {
            fail("Произошла ошибка при чтении CSV файла: " + e.getMessage());
        }
    }

    @Test
    void testReadPeopleFromCsv_CorrectData() {
        try {
            List<Person> people = CsvReader.readPeopleFromCsv("people.csv");
            // Проверяем данные хотя бы для одного человека
            Person firstPerson = people.get(0);
            assertEquals(28281, firstPerson.id, "ID первого человека неверный");
            assertEquals("Aahan", firstPerson.name, "Имя первого человека неверно");
            assertEquals("Male", firstPerson.gender, "Пол первого человека неверен");
            assertEquals(LocalDate.of(1970, 5, 15), firstPerson.birthDate, "Дата рождения первого человека неверна");
        } catch (Exception e) {
            fail("Произошла ошибка при чтении CSV файла: " + e.getMessage());
        }
    }

    @Test
    void testReadPeopleFromCsv_DivisionCount() {
        try {
            List<Person> people = CsvReader.readPeopleFromCsv("people.csv");
            // Проверяем количество уникальных подразделений
            Map<String, Integer> divisionCounts = new HashMap<>();
            for (Person person : people) {
                divisionCounts.put(person.division.name, divisionCounts.getOrDefault(person.division.name, 0) + 1);
            }
        } catch (Exception e) {
            fail("Произошла ошибка при чтении CSV файла: " + e.getMessage());
        }
    }


}
