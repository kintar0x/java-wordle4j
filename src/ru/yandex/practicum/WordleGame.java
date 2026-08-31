package ru.yandex.practicum;

import java.util.*;

public class WordleGame {

    public static final int WORD_LENGTH = 5;
    public static final int MAX_ATTEMPTS = 6;

    private final WordleDictionary dictionary;
    private final String answer;
    private int steps = MAX_ATTEMPTS;
    private final Set<Character> usedWrongLetters;
    private final Set<Character> requiredLetters;
    private final Map<Integer, Character> exactPositions;
    private final List<String> history;

    public WordleGame(WordleDictionary dictionary) {
        if (dictionary == null || dictionary.size() == 0) {
            throw new IllegalArgumentException("Словарь не может быть пустым");
        }
        this.dictionary = dictionary;
        this.usedWrongLetters = new HashSet<>();
        this.requiredLetters = new HashSet<>();
        this.exactPositions = new HashMap<>();
        this.history = new ArrayList<>();
        this.answer = dictionary.getWords().get(new Random().nextInt(dictionary.size()));
    }

    public String getAnswer() {
        return answer;
    }

    public int getSteps() {
        return steps;
    }

    public List<String> getHistory() {
        return history;
    }

    public static String normalize(String input) {
        if (input == null) {
            return null;
        }
        return input.trim().toLowerCase().replace('ё', 'е');
    }

    public String makeMove(String guess) {
        String normalized = normalize(guess);

        if (normalized == null || normalized.length() != WORD_LENGTH) {
            throw new InvalidWordException(
                    String.format("Слово должно быть из %d букв", WORD_LENGTH)
            );
        }

        if (!dictionary.isValidWord(normalized)) {
            throw new WordNotInDictionaryException("Слова нет в словаре");
        }

        if (history.contains(normalized)) {
            throw new WordNotInDictionaryException(
                    String.format("Вы уже вводили слово: %s", normalized)
            );
        }

        String result = dictionary.compareWords(normalized, answer);

        for (int i = 0; i < normalized.length(); i++) {
            char c = normalized.charAt(i);
            char res = result.charAt(i);

            if (res == '+') {
                exactPositions.put(i, c);
            } else if (res == '^') {
                requiredLetters.add(c);
            } else if (res == '-') {
                usedWrongLetters.add(c);
            }
        }

        history.add(normalized);
        steps--;

        return result;
    }

    public String getHint() {
        return dictionary.getHint(usedWrongLetters, requiredLetters, exactPositions, history);
    }
}