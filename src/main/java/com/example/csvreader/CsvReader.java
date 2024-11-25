// Лабораторная работа №4 по языку программирования Java. Выполнил: Фефелов Дмитрий, 3 курс, 3 группа

/**
 * Задача: Работа с коллекциями 2. Есть Два типа сущностей: человек и подразделение.
 * Дан CSV файл(архив с ним есть внутри задания), который содержит в себе информацию о людях. Нужно считать данные о людях из этого файла в список
 * В этой задаче нужно пользоваться встроенными Java  коллекциями
 * Для работы с CSV файлом рекомендую использовать библиотеку opencsv(НО можете и без нее - это на ваше усмотрение)
 * Ее можно либо скачать в виде jar  файла и подключить к проекту если не используете maven, либо подключить как maven зависимость
 * */
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

/**
 * В CsvReader создаются два основных метода: readPeopleFromCsv и main.
 * Метод readPeopleFromCsv отвечает за чтение и обработку csv файла. Выполняет функции: обработка исключений, получение потока, создание CSVReader, чтение данных.
 * А так же в этом методе обрабатываются ошибки, создаются объекты для человека и подразделения.
 * */   
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
