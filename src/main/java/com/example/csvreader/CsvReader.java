package com.example.csvreader;

import com.example.model.Division;
import com.example.model.Person;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CsvReader {

    public static List<Person> readPeopleFromCsv(String csvFilePath) throws Exception {
        List<Person> people = new ArrayList<>();
        AtomicInteger divisionCounter = new AtomicInteger(1); // Используем AtomicInteger для атомарного инкремента

        Map<String, Division> divisions = new HashMap<>();

        try (InputStream in = CsvReader.class.getClassLoader().getResourceAsStream(csvFilePath);
             Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
             CSVReader csvReader = new CSVReaderBuilder(reader)
                     .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                     .build()) {

            if (csvReader == null) {
                throw new FileNotFoundException(csvFilePath);
            }
            String[] nextLine;
            csvReader.readNext(); // Пропускаем заголовок
            while ((nextLine = csvReader.readNext()) != null) {
                try {
                    int id = Integer.parseInt(nextLine[0]);
                    String name = nextLine[1];
                    String gender = nextLine[2];
                    LocalDate birthDate = LocalDate.parse(nextLine[3], DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                    String divisionName = nextLine[4];
                    int salary = Integer.parseInt(nextLine[5]);

                    Division division = divisions.computeIfAbsent(divisionName, dn -> {
                        Division newDivision = new Division(divisionCounter.getAndIncrement(), dn); // Используем getAndIncrement для атомарного инкремента
                        return newDivision;
                    });

                    Person person = new Person(id, name, gender, birthDate, division, salary);
                    people.add(person);
                } catch (NumberFormatException e) {
                    System.err.println("Ошибка преобразования числа в строке: " + String.join(",", nextLine) + ". Ошибка: " + e.getMessage());
                } catch (DateTimeParseException e) {
                    System.err.println("Ошибка преобразования даты в строке: " + String.join(",", nextLine) + ". Ошибка: " + e.getMessage());
                } catch (IndexOutOfBoundsException e) {
                    System.err.println("Неправильный формат строки: " + String.join(",", nextLine) + ". Ошибка: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return people;
    }

    public static void main(String[] args) {
        try {
            List<Person> people = readPeopleFromCsv("people.csv");
            people.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
