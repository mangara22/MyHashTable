import org.junit.jupiter.api.*;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HashTableTest {
    private HashTable hashTable;
    private Random random;
    private StringBuilder string;
    private int iterations, wordLength;

    @BeforeEach
    void setUp() {
        hashTable = new HashTable(250);
        random = new Random();
        string = new StringBuilder();
        iterations = random.nextInt(500); //add iteration amount of words
        wordLength = random.nextInt(10); //add wordLength letter words long
    }

    @Test
    @DisplayName("Hash & Contains Test")
    @Order(1)
    void hashAndContainsTest(){
        for(int i = 0; i < iterations; i++){
            for(int j = 0; j < wordLength; j++){
                string.append((char) random.nextInt(26));
            }
            String word = string.toString();
            hashTable.add(word); //add word so that we can use contains to see if it was actually added
            assertEquals(hashTable.hash(word), hashTable.containsElem(word), "an added word isn't in the right spot!");
        }
        assertEquals(-1, hashTable.hash(null), "can't hash a null element!");
        assertEquals(-1, hashTable.containsElem(null), "can't check if a null element is in the HashTable!");
        assertEquals(-1, hashTable.containsElem("test"), "found an element that isn't meant to be in the HashTable!");
    }

    @Test
    @DisplayName("Remove Test")
    @Order(2)
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

    @Test
    @DisplayName("Resize & Load Factor Test")
    @Order(3)
    void resizeAndLoadTest(){
        for(int i = 0; i < iterations; i++){
            for(int j = 0; j < wordLength; j++){
                string.append((char) random.nextInt(26));
            }
            String word = string.toString();
            hashTable.add(word);
        }
        if(hashTable.getCurrentLoad() > 0.75){
            hashTable.resize(hashTable.getTableSize() * 3);
        }
        assertTrue(hashTable.getCurrentLoad() < 0.75, "load factor is still too big!");
    }

    @Test
    @DisplayName("Clear Test")
    @Order(4)
    void clearTest(){
        for(int i = 0; i < iterations; i++){
            for(int j = 0; j < wordLength; j++){
                string.append((char) random.nextInt(26));
            }
            String word = string.toString();
            hashTable.add(word);
        }
        hashTable.clear();
        for(int k = 0; k < hashTable.getTableSize(); k++){
            assertNull(hashTable.getHashTable()[k], "HashTable not null!");
        }
        assertEquals(0, hashTable.getElemCount(), "HashTable doesn't have 0 elements!");
    }
}
