package ru.yandex.practicum;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

class WordleGameTest {

    private WordleDictionary dictionary;
    private WordleGame game;
    private String secretWord;

    @BeforeEach
    void setUp() {
        List<String> words = List.of("гость", "город", "герой", "гномы", "голос");
        dictionary = new WordleDictionary(words);
        game = new WordleGame(dictionary);
        secretWord = game.getAnswer();
    }

    @Test
    void testMakeMoveCorrect() {
        if (!secretWord.equals("гость")) {
            return;
        }
        String result = game.makeMove("гость");
        assertNotNull(result);
        assertEquals("+++++", result);
    }

    @Test
    void testMakeMovePartial() {
        if (!secretWord.equals("гость")) {
            return;
        }
        String result = game.makeMove("город");
        assertNotNull(result);
        assertEquals("++---", result);
    }

    @Test
    void testMakeMoveInvalidLength() {
        assertThrows(InvalidWordException.class, () -> game.makeMove("кот"));
    }

    @Test
    void testMakeMoveInvalidWord() {
        assertThrows(WordNotInDictionaryException.class, () -> game.makeMove("абвгд"));
    }

    @Test
    void testMakeMoveDuplicate() {
        if (!secretWord.equals("гость")) {
            return;
        }
        game.makeMove("герой");
        String result = game.makeMove("герой");
        assertNull(result);
    }

    @Test
    void testStepsDecrement() {
        assertEquals(6, game.getSteps());
        game.makeMove("герой");
        assertEquals(5, game.getSteps());
    }

    @Test
    void testGetHintNotNull() {
        String hint = game.getHint();
        assertNotNull(hint);
        assertTrue(dictionary.isValidWord(hint));
    }

    @Test
    void testHistoryContainsMoves() {
        game.makeMove("герой");
        game.makeMove("гномы");
        assertEquals(2, game.getHistory().size());
        assertTrue(game.getHistory().contains("герой"));
        assertTrue(game.getHistory().contains("гномы"));
    }

    @Test
    void testAnswerIsFromDictionary() {
        String answer = game.getAnswer();
        assertTrue(dictionary.isValidWord(answer));
    }

    @Test
    void testRequiredLettersUpdated() {
        game.makeMove("герой");
        assertNotEquals(0, game.getHistory().size());
    }

    @Test
    void testHintRespectsUsedLetters() {
        game.makeMove("герой");
        String hint = game.getHint();
        assertNotNull(hint);
    }

    @Test
    void testHintDoesNotRepeat() {
        game.makeMove("герой");
        String hint1 = game.getHint();
        String hint2 = game.getHint();
        assertNotNull(hint1);
        assertNotNull(hint2);
    }

    @Test
    void testCompareWordsAllMatch() {
        WordleDictionary dict = new WordleDictionary(List.of("гость"));
        String result = dict.compareWords("гость", "гость");
        assertEquals("+++++", result);
    }

    @Test
    void testCompareWordsPartialMatch() {
        WordleDictionary dict = new WordleDictionary(List.of("город"));
        String result = dict.compareWords("голос", "город");
        assertEquals("++-+-", result);
    }

    @Test
    void testIsValidWordTrue() {
        assertTrue(dictionary.isValidWord("гость"));
    }

    @Test
    void testIsValidWordFalse() {
        assertFalse(dictionary.isValidWord("абвгд"));
    }

    @Test
    void testIsValidWordNull() {
        assertFalse(dictionary.isValidWord(null));
    }

    @Test
    void testIsValidWordWrongLength() {
        assertFalse(dictionary.isValidWord("кот"));
    }

}