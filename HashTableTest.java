import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HashTableTest {
    private HashTable hashTable;
    private Random random;
    private StringBuilder string;
    private int iterations, wordLength;

    @BeforeEach
    void setUp() {
        hashTable = new HashTable(10);
        random = new Random();
        string = new StringBuilder();
        iterations = random.nextInt(50); //add iteration amount of words
        wordLength = random.nextInt(5); //add wordLength letter words long
    }

    @Test
    @DisplayName("Hash & Contains Test")
    void hashAndContainsTest(){
        for(int i = 0; i < iterations; i++){
            for(int j = 0; j < wordLength; j++){
                string.append((char) random.nextInt(26));
            }
            String word = string.toString();
            hashTable.add(word); //add word so that we can use contains to see if it was actually added
            assertEquals(hashTable.hash(word), hashTable.containsElem(word), "an added word isn't in the right spot!");
        }
    }

    @Test
    @DisplayName("Remove Test")
    void removeTest() {
        String[] wordsArray = new String[iterations]; //array of added words
        int randIdx = random.nextInt(iterations); //random index out of wordsArray
        for (int i = 0; i < iterations; i++) {
            for (int j = 0; j < wordLength; j++) {
                string.append((char) random.nextInt(26));
            }
            String word = string.toString();
            wordsArray[i] = word;
            hashTable.add(word);
        }
        String removeWord = wordsArray[randIdx];
        hashTable.remove(removeWord); //remove a word at a random index
        assertEquals(-1, hashTable.containsElem(removeWord), "word not successfully removed!");
    }
}
