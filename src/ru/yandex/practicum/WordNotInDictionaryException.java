package ru.yandex.practicum;

public class WordNotInDictionaryException extends RuntimeException {
    public WordNotInDictionaryException(String message) {
        super(message);
    }
}