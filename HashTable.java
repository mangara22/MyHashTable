/*
    Written by: Michael Angara, March 2023
    Inspiration from: CSCI 1933 Project 5, CSCI 2021 Project 1 Problem 3
    What is this?: A basic implementation of a HashTable and a "console"
*/
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;
public class HashTable<T> {
    private int elemCount;
    private int tableSize;
    private Node<T>[] hashTable;

    public HashTable(int length){
        this.hashTable = new Node[length];
        this.elemCount = 0;
        this.tableSize = length;
    }

    public double getCurrentLoad(){
        return (double)this.elemCount / this.tableSize; //convert to double due to integer division
    }

    public int hash(T element){
        String result = element.toString();
        int hashcode = 0;
        for(int i = 0; i < result.length(); i++) {
            hashcode = (193 * result.charAt(i) + result.length()) % this.tableSize; //use modulus to make sure number is within tableSize
        }
        return hashcode;
    }

    public int containsElem(T element){
        int idx = hash(element);
        Node<T> key = hashTable[idx];
        while(key != null){
            if(key.getData().equals(element)){ //found the element
                return idx;
            }
            key = key.getNext();
        }
        return -1; //did not find element
    }

    public void add(T element){
        int idx = hash(element);
        Node<T> key = hashTable[idx];
        if(key == null){ //first Node to add
            hashTable[idx] = new Node<>(element, null);
            this.elemCount++;
        }
        else{
            while(key.getNext() != null){ //check for duplicates
                if(key.getData().equals(element)){
                    return;
                }
                key = key.getNext();
            }
            if(!key.getData().equals(element)){ //add, double check for duplicates at end
                key.setNext(new Node<>(element, null));
                this.elemCount++;
            }
        }
    }

    public void remove(T element){
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
            while(!ptr.getData().equals(element)){
                tail = ptr;
                ptr = ptr.getNext();
            }
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
        HashTable h2 = new HashTable(newSize); //make a new HashTable object
        for(int i = 0; i < this.tableSize; i++){
            Node<T> key = this.hashTable[i];
            if(key == null){
                h2.hashTable[i] = null;
            }
            else{
                while(key != null){
                    h2.add(key.getData());
                    key = key.getNext();
                }
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
            if(hashPtr == null){
                result.append("NULL");
            }
            else{
                while(hashPtr != null){
                    if(hashPtr.getNext() == null){
                        result.append(hashPtr.getData());
                    }
                    else{
                        result.append(hashPtr.getData()).append("-->");
                    }
                    hashPtr = hashPtr.getNext();
                }
            }
            result.append("\n");
        }
        result.append("Number of elements: ").append(this.elemCount).append("\n");
        result.append("HashTable size: ").append(this.tableSize).append("\n");
        result.append("Load factor: ").append(this.getCurrentLoad()).append("\n");
        return result.toString();
    }

    public void clear(){
        for(int i = 0; i < this.tableSize; i++){
            hashTable[i] = null;
        }
        this.elemCount = 0;
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
        System.out.println("    load : resizes the HashTable if the load factor is too big");
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
                    case "load":
                        if(h1.getCurrentLoad() > 0.75){
                            System.out.println("Load factor is too big! Resizing the HashTable...");
                            h1.resize(h1.tableSize * 3);
                            System.out.println("Printing the new HashTable...");
                            System.out.println(h1);
                        }
                        else{
                            System.out.println("Load factor is less than 0.75, still good!");
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
                        System.out.print(" \n");
                        System.out.println("----HashTable Console----\nCommands: ");
                        System.out.println("    hashcode <element> : prints the hashcode for the given element");
                        System.out.println("    contains <element> : prints the index for the element");
                        System.out.println("    add <element> : inserts the element into the HashTable");
                        System.out.println("    remove <element> : removes the element from the HashTable");
                        System.out.println("    save <filename.txt> : saves the HashTable to the given .txt file");
                        System.out.println("    load : resizes the HashTable if the load factor is too big");
                        System.out.println("    print : prints the HashTable");
                        System.out.println("    clear : clears the current HashTable");
                        System.out.println("    help : prints out the commands again");
                        System.out.println("    quit : exits the console");
                        break;
                    case "quit":
                        break inputLoop;
//                    default:
//                        System.out.println("Invalid command!");
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
