package ru.yandex.practicum;

import java.util.*;

public class WordleGame {

    private final WordleDictionary dictionary;
    private final String answer;
    private int steps = 6;
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

    public String makeMove(String guess) {
        if (guess == null || guess.length() != 5) {
            return null;
        }

        if (!dictionary.isValidWord(guess)) {
            return null;
        }

        if (history.contains(guess)) {
            return null;
        }

        String result = dictionary.compareWords(guess, answer);

        for (int i = 0; i < guess.length(); i++) {
            char c = guess.charAt(i);
            char res = result.charAt(i);

            if (res == '+') {
                exactPositions.put(i, c);
            } else if (res == '^') {
                requiredLetters.add(c);
            } else if (res == '-') {
                usedWrongLetters.add(c);
            }
        }

        history.add(guess);
        steps--;

        return result;
    }

    public String getHint() {
        return dictionary.getHint(usedWrongLetters, requiredLetters, exactPositions, history);
    }
}