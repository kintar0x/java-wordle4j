package ru.yandex.practicum;

import java.util.*;

public class WordleDictionary {
    private final List<String> words;
    private final Set<String> wordSet;
    private final Random random;

    public WordleDictionary(List<String> words) {
        if (words == null || words.isEmpty()) {
            throw new IllegalArgumentException("Словарь не может быть пустым");
        }
        this.words = new ArrayList<>(words);
        this.wordSet = new HashSet<>(words);
        this.random = new Random();
    }

    public int size() {
        return words.size();
    }

    public List<String> getWords() {
        return new ArrayList<>(words);
    }

    public boolean isValidWord(String word) {
        if (word == null) {
            return false;
        }
        String normalized = WordleGame.normalize(word);
        if (normalized.length() != WordleGame.WORD_LENGTH) {
            return false;
        }
        return wordSet.contains(normalized);
    }

    public String getHint(Set<Character> usedWrongLetters,
                          Set<Character> requiredLetters,
                          Map<Integer, Character> exactPositions,
                          List<String> history) {

        if (usedWrongLetters == null) {
            usedWrongLetters = Collections.emptySet();
        }
        if (requiredLetters == null) {
            requiredLetters = Collections.emptySet();
        }
        if (exactPositions == null) {
            exactPositions = Collections.emptyMap();
        }
        if (history == null) {
            history = Collections.emptyList();
        }

        List<String> allValid = new ArrayList<>();

        for (String word : words) {
            boolean isValid = true;
            Set<Character> wordChars = new HashSet<>();
            for (char c : word.toCharArray()) {
                wordChars.add(c);
            }

            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);

                if (exactPositions.containsKey(i) && exactPositions.get(i) != c) {
                    isValid = false;
                    break;
                }

                if (usedWrongLetters.contains(c)) {
                    isValid = false;
                    break;
                }
            }

            if (isValid) {
                for (char c : requiredLetters) {
                    if (!wordChars.contains(c)) {
                        isValid = false;
                        break;
                    }
                }
            }

            if (isValid) {
                allValid.add(word);
            }
        }

        if (allValid.isEmpty()) {
            return words.get(random.nextInt(words.size()));
        }

        List<String> fresh = new ArrayList<>();
        for (String word : allValid) {
            if (!history.contains(word)) {
                fresh.add(word);
            }
        }

        List<String> pool = fresh.isEmpty() ? allValid : fresh;
        return pool.get(random.nextInt(pool.size()));
    }

    public String compareWords(String guess, String secret) {
        if (guess == null || secret == null) {
            throw new IllegalArgumentException("Слова не могут быть пустыми строками");
        }

        if (guess.length() != secret.length()) {
            throw new IllegalArgumentException("Слова должны быть одинаковой длины");
        }

        char[] result = new char[guess.length()];
        Map<Character, Integer> secretCounts = new HashMap<>();

        for (char c : secret.toCharArray()) {
            secretCounts.put(c, secretCounts.getOrDefault(c, 0) + 1);
        }

        for (int i = 0; i < guess.length(); i++) {
            char g = guess.charAt(i);
            char s = secret.charAt(i);

            if (g == s) {
                result[i] = '+';
                secretCounts.put(g, secretCounts.get(g) - 1);
            }
        }

        for (int i = 0; i < guess.length(); i++) {
            char g = guess.charAt(i);

            if (result[i] == '+') {
                continue;
            }

            int count = secretCounts.getOrDefault(g, 0);
            if (count > 0) {
                result[i] = '^';
                secretCounts.put(g, count - 1);
            } else {
                result[i] = '-';
            }
        }

        return new String(result);
    }
}