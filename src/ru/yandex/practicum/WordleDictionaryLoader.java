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
                String word = WordleGame.normalize(line);
                if (word.length() == WordleGame.WORD_LENGTH) {
                    words.add(word);
                }
            }
        } catch (IOException e) {
            throw new DictionaryLoadException(
                    String.format("Ошибка чтения файла: %s", filename), e
            );
        }

        if (words.isEmpty()) {
            throw new DictionaryLoadException(
                    String.format("Словарь пуст или не нашлось слов длиной в %d символов", WordleGame.WORD_LENGTH)
            );
        }

        return words;
    }

    public List<String> getDictionary() {
        return new ArrayList<>(dictionary);
    }
}