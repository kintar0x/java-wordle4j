package ru.yandex.practicum;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class WordleDictionaryLoader {

    private final List<String> dictionary;

    public WordleDictionaryLoader(String filename) throws DictionaryLoadException {
        this.dictionary = loadWordsFromFile(filename);
    }

    private List<String> loadWordsFromFile(String filename) throws DictionaryLoadException {
        List<String> words = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String word = line.trim().toLowerCase().replace("ё", "е");
                if (word.length() == 5) {
                    words.add(word);
                }
            }
        } catch (IOException e) {
            throw new DictionaryLoadException("Ошибка чтения файла: " + filename, e);
        }

        if (words.isEmpty()) {
            throw new DictionaryLoadException("Словарь пуст или не нашлось слов длиной в 5 символов");
        }

        return words;
    }

    public List<String> getDictionary() {
        return new ArrayList<>(dictionary);
    }
}