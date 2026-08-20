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
            writer.printf("Ответ: %s%n", game.getAnswer());

            System.out.printf("Угадайте слово из %d букв! У вас %d попыток.%n",
                    WordleGame.WORD_LENGTH, WordleGame.MAX_ATTEMPTS);
            System.out.println("Введите слово или нажмите Enter для подсказки.");

            while (game.getSteps() > 0) {
                System.out.printf("Осталось ходов: %d%n", game.getSteps());
                System.out.print(">> ");
                String input = scanner.nextLine();

                if (input.isBlank()) {
                    String hint = game.getHint();
                    String result = game.makeMove(hint);
                    System.out.printf("Подсказка: %s%n", hint);
                    System.out.printf("Результат: %s%n", result);
                    writer.printf("Подсказка: %s -> %s%n", hint, result);

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

                try {
                    String normalizedInput = WordleGame.normalize(input);
                    String result = game.makeMove(normalizedInput);
                    writer.printf("Игрок: %s -> %s%n", normalizedInput, result);

                    if (result == null) {
                        System.out.println("❌ Ошибка ввода");
                        writer.printf("Ошибка: %s%n", normalizedInput);
                        continue;
                    }

                    if (normalizedInput.equals(game.getAnswer())) {
                        System.out.println("🎉 Победа!");
                        writer.println("Игра завершена: ПОБЕДА");
                        break;
                    }

                    if (game.getSteps() == 0) {
                        System.out.printf("Поражение. Загадано слово: %s%n", game.getAnswer().toUpperCase());
                        writer.printf("Игра завершена: ПОРАЖЕНИЕ. Ответ: %s%n", game.getAnswer());
                        break;
                    }

                    System.out.printf("Результат: %s%n", result);

                } catch (InvalidWordException | WordNotInDictionaryException e) {
                    System.out.printf("❌ %s%n", e.getMessage());
                    writer.printf("Ошибка: %s%n", e.getMessage());
                }
            }

            System.out.println("Игра окончена. Спасибо за игру!");
            writer.println("Программа завершила работу");

        } catch (DictionaryLoadException e) {
            System.out.printf("Ошибка загрузки словаря: %s%n", e.getMessage());
        } catch (IOException e) {
            System.out.printf("Ошибка работы с файлом лога: %s%n", e.getMessage());
        } catch (Exception e) {
            System.out.printf("Непредвиденная ошибка: %s%n", e.getMessage());
        }
    }
}