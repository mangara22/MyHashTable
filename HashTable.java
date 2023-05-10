/*
    Written by: Michael Angara, started in March 2023
    What is this?: A basic implementation of a HashTable with a "console"
*/
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.text.DecimalFormat;
public class HashTable<T> {
    private int elemCount;
    private int tableSize;
    private Node<T>[] hashTable;
    private T firstAdded;
    private T recentAdded;
    private static final DecimalFormat dfRound = new DecimalFormat("0.00000"); //for rounding load factor

    public HashTable(int length){
        this.hashTable = new Node[length];
        this.elemCount = 0;
        this.tableSize = length;
    }

    public double getCurrentLoad() {return (double)this.elemCount / this.tableSize;}

    public int getTableSize() {return this.tableSize;}

    public Node<T>[] getHashTable() {return hashTable;}

    public int getElemCount() {return elemCount;}

    public T getFirstAdded() {return firstAdded;}

    public T getRecentAdded() {return recentAdded;}

    public int hash(T element){
        if(element == null) return -1;
        String result = element.toString();
        int hashcode = 0;
        for(int i = 0; i < result.length(); i++) {
            hashcode = (98317 * result.charAt(i) + result.length());
            hashcode += hashcode << 6 + hashcode << 16 - hashcode;
            hashcode += hashcode << 5 * i;
        }
        if(hashcode < 0){
            hashcode *= -1;
        }
        return hashcode % this.tableSize; //make sure hashcode is within table bounds
    }

    public int containsElem(T element){
        if(element == null) return -1;
        int idx = hash(element);
        Node<T> key = hashTable[idx];
        for(; key != null; key = key.getNext()){
            if(key.getData().equals(element)){ //found the element
                return idx;
            }
        }
        return -1; //did not find element
    }

    public void add(T element){
        if(element == null || this.containsElem(element) != -1){ //check for null or duplicates
            return;
        }
        if(this.elemCount == 0) this.firstAdded = element;
        this.recentAdded = element;
        int idx = hash(element);
        Node<T> key = hashTable[idx];
        Node<T> newNode = new Node<>(element, null);
        if(key == null) hashTable[idx] = newNode; //first node to add
        else{
            for(; key.getNext() != null; key = key.getNext()){}
            key.setNext(newNode); //chaining
        }
        this.elemCount++;
        if(this.getCurrentLoad() > 0.75){
            System.out.println("Load factor of " + dfRound.format(this.getCurrentLoad()) + " is too big! Resizing...");
            this.resize(this.tableSize * 3);
        }
    }

    public void remove(T element){
        if(element == null) return;
        int result = this.hash(element);
        Node<T> tail = hashTable[result];
        Node<T> ptr = tail.getNext();
        if(tail.getData().equals(element)){ //need to determine where to point next
            if(ptr == null){
                hashTable[result] = null; //set tail to null, only one node at that index to remove
                this.elemCount--;
                return;
            }
            this.elemCount--;
            hashTable[result] = ptr; //point to ptr and not tail
        }
        else{ //find the element to remove in the linked list
            for(; !ptr.getData().equals(element); tail = ptr, ptr = ptr.getNext()) {}
            tail.setNext(ptr.getNext());
            this.elemCount--;
        }
    }

    public boolean saveToFile(String fileName){
        PrintWriter p;
        try{
            p = new PrintWriter(fileName);
        }
        catch(Exception e){
            System.out.println("Error with saving to " + fileName);
            return false;
        }
        p.print(this); //use HashTable's toString() to write (print) to file
        p.close();
        return true;
    }

    public void resize(int newSize){
        HashTable h2 = new HashTable(newSize);
        for(int i = 0; i < this.tableSize; i++){
            Node<T> key = this.hashTable[i];
            if(key == null) h2.hashTable[i] = null;
            else{
                for(; key != null; key = key.getNext()) {h2.add(key.getData());}
            }
        }
        //reassign "this" variables to the "h2" objects
        this.elemCount = h2.elemCount;
        this.tableSize = h2.tableSize;
        this.hashTable = h2.hashTable;
    }

    public String toString(){
        StringBuilder result = new StringBuilder(); //using StringBuilder instead of string concatenation
        for(int i = 0; i < this.tableSize; i++){
            result.append("[").append(i).append("]").append(": ");
            Node<T> hashPtr = hashTable[i];
            if(hashPtr == null) result.append("NULL");
            else{
                for(; hashPtr != null; hashPtr = hashPtr.getNext()){
                    if(hashPtr.getNext() == null){
                        result.append(hashPtr.getData());
                    }
                    else{
                        result.append(hashPtr.getData()).append("-->");
                    }
                }
            }
            result.append("\n");
        }
        result.append("Number of elements: ").append(this.elemCount).append("\n");
        result.append("HashTable size: ").append(this.tableSize).append("\n");
        result.append("Load factor: ").append(dfRound.format(this.getCurrentLoad())).append("\n");
        result.append("First element added: ").append("<"+this.firstAdded+">").append("\n");
        result.append("Most recent element added: ").append("<"+this.recentAdded+">").append("\n");
        return result.toString();
    }

    public void clear(){
        this.hashTable = new Node[this.tableSize];
        this.elemCount = 0;
        this.firstAdded = null;
        this.recentAdded = null;
    }

    public static void runConsole(){
        Scanner s = new Scanner(System.in);
        String[] command;
        System.out.println("----HashTable Console----\nCommands: ");
        System.out.println("    hashcode <element> : prints the hashcode for the given element");
        System.out.println("    contains <element> : prints the index for the element");
        System.out.println("    add <element> : inserts the element into the HashTable");
        System.out.println("    remove <element> : removes the element from the HashTable");
        System.out.println("    save <filename.txt> : saves the HashTable to the given .txt file");
        System.out.println("    print : prints the HashTable");
        System.out.println("    clear : clears the current HashTable");
        System.out.println("    help : prints out the commands again");
        System.out.println("    quit : exits the console");
        try {
            System.out.print("Please enter a size for your HashTable: ");
            int length = s.nextInt();
            HashTable h1 = new HashTable(length);
            System.out.print("HashTable>> ");
            inputLoop: //switch label
            while (s.hasNext()) {
                command = s.nextLine().split(" ");
                switch (command[0]) {
                    case "hashcode":
                        System.out.println("<" + command[1] + ">'s hashcode is: " + h1.hash(command[1]));
                        break;
                    case "contains":
                        if (h1.containsElem(command[1]) == -1) {
                            System.out.println("<" + command[1] + "> could not be found");
                        } else {
                            System.out.println("<" + command[1] + "> is at index: " + h1.containsElem(command[1]));
                        }
                        break;
                    case "add":
                        if (h1.containsElem(command[1]) != -1) {
                            System.out.println("Duplicate element found, did not add");
                        } else {
                            h1.add(command[1]);
                            System.out.println("<" + command[1] + ">" + " was added");
                        }
                        break;
                    case "remove":
                        if (h1.containsElem(command[1]) != -1){
                            h1.remove(command[1]);
                            System.out.println("<" + command[1] + "> was successfully removed");
                        } else {
                            System.out.println("<" + command[1] + "> not found, cannot remove");
                        }
                        break;
                    case "save":
                        if (h1.saveToFile(command[1])){
                            System.out.println("HashTable was successfully saved to <" + command[1] + ">");
                        }
                        else{
                            System.out.println("HashTable could not be saved");
                        }
                        break;
                    case "print":
                        System.out.println("Printing the HashTable...");
                        System.out.println(h1);
                        break;
                    case "clear":
                        h1.clear();
                        System.out.println("Cleared the HashTable");
                        break;
                    case "help":
                        System.out.print("Here are the commands...\n");
                        System.out.println("----HashTable Console----\nCommands: ");
                        System.out.println("    hashcode <element> : prints the hashcode for the given element");
                        System.out.println("    contains <element> : prints the index for the element");
                        System.out.println("    add <element> : inserts the element into the HashTable");
                        System.out.println("    remove <element> : removes the element from the HashTable");
                        System.out.println("    save <filename.txt> : saves the HashTable to the given .txt file");
                        System.out.println("    print : prints the HashTable");
                        System.out.println("    clear : clears the current HashTable");
                        System.out.println("    help : prints out the commands again");
                        System.out.println("    quit : exits the console");
                        break;
                    case "quit":
                        break inputLoop;
                }
                System.out.print("HashTable>> ");
            }
            System.out.println("Exiting the Console...");
        }
        catch(InputMismatchException e){ //if user doesn't enter an int for HashTable length
            System.out.println("Please enter a number for the length!");
        }
        catch(ArrayIndexOutOfBoundsException e){ //only one argument for two argument command
            System.out.println("Invalid number of arguments!");
        }
    }
}
