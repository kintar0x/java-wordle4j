package ru.yandex.practicum;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Wordle {

    public static void main(String[] args) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("wordle.log", StandardCharsets.UTF_8));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Вас приветствует игра Wordle!");
            writer.println("Программа начала работу");

            WordleDictionaryLoader loader = new WordleDictionaryLoader("words_ru.txt");
            WordleDictionary dictionary = new WordleDictionary(loader.getDictionary());
            writer.println("Словарь загружен");

            WordleGame game = new WordleGame(dictionary);
            writer.println("Игра создана");
            writer.println("Ответ: " + game.getAnswer());

            System.out.println("Угадайте слово из 5 букв! У вас 6 попыток.");
            System.out.println("Введите слово или нажмите Enter для подсказки.");

            while (game.getSteps() > 0) {
                System.out.println("Осталось ходов: " + game.getSteps());
                System.out.print(">> ");
                String input = scanner.nextLine();

                if (input.isEmpty()) {
                    String hint = game.getHint();
                    String result = game.makeMove(hint);
                    System.out.println("Подсказка: " + hint);
                    System.out.println("Результат: " + result);
                    writer.println("Подсказка: " + hint + " -> " + result);

                    if (result == null) {
                        continue;
                    }

                    if (hint.equals(game.getAnswer())) {
                        System.out.println("🎉 Победа!");
                        writer.println("Игра завершена: ПОБЕДА");
                        break;
                    }
                    continue;
                }

                String result = game.makeMove(input);
                writer.println("Игрок: " + input + " -> " + result);

                if (result == null) {
                    System.out.println("Ошибка ввода");
                    writer.println("Ошибка: " + input);
                    continue;
                }

                if (input.equals(game.getAnswer())) {
                    System.out.println("Победа!");
                    writer.println("Игра завершена: ПОБЕДА");
                    break;
                }

                if (game.getSteps() == 0) {
                    System.out.println("Поражение. Загадано слово: " + game.getAnswer().toUpperCase());
                    writer.println("Игра завершена: ПОРАЖЕНИЕ. Ответ: " + game.getAnswer());
                    break;
                }

                System.out.println("Результат: " + result);
            }

            System.out.println("Игра окончена. Спасибо за игру!");
            writer.println("Программа завершила работу");

        } catch (DictionaryLoadException e) {
            System.out.println("Ошибка загрузки словаря: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Ошибка работы с файлом лога: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Непредвиденная ошибка: " + e.getMessage());
        }
    }
}