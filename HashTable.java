import java.io.File;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;
public class HashTable<T> {
    private int elemCount;
    private int tableSize;
    private Node<T>[] hashTable;
    private double load = 0.75;

    public HashTable(int length){
        this.hashTable = new Node[length];
        this.elemCount = 0;
        this.tableSize = length;
    }

    public double getCurrentLoad(){
        return (double)this.elemCount / this.tableSize;
    }

    public double getLoad(){
        return this.load;
    }

    public int hash(T element){
        String result = element.toString();
        int hashcode = 0;
        for(int i = 0; i < result.length(); i++) {
            hashcode = (193 * result.charAt(i) + result.length()) % this.tableSize;
        }
        return hashcode;
    }

    public boolean add(T element){
        int idx = hash(element);
        Node<T> key = hashTable[idx];
        if(key == null){ //first Node to add
            hashTable[idx] = new Node<>(element, null);
            this.elemCount++;
        }
        else{
            while(key.getNext() != null){ //check for duplicates
                if(key.getData().equals(element)){
                    return false;
                }
                key = key.getNext();
            }
            if(!key.getData().equals(element)){ //add, double check for duplicates at end
                key.setNext(new Node<>(element, null));
                this.elemCount++;
            }
        }
        return true;
    }

    public int containsElem(T element){
        int idx = hash(element);
        Node<T> key = hashTable[idx];
//        if(key == null){
//            return -1;
//        }
//        else if(key.getNext() == null){
//            return idx;
//        }
        while(key != null){
            if(key.getData().equals(element)){
                return idx;
            }
            key = key.getNext();
        }
        return -1;
    }

    public String toString(){
        String result = "";
        for(int i = 0; i < this.tableSize; i++){
            result += "["+i+"]" + ": ";
            Node<T> hashPtr = hashTable[i];
            if(hashPtr == null){
                result += "null";
            }
//            else if(hashPtr.getNext() == null){
//                result += hashPtr.getData() + " ";
//            }
            else{
                while(hashPtr != null){
                    if(hashPtr.getNext() == null){
                        result += hashPtr.getData() + "";
                    }
                    else{
                        result += hashPtr.getData() + "-->";
                    }
                    hashPtr = hashPtr.getNext();
                }
            }
            result += "\n";
        }
        result += "Number of elements: " + this.elemCount + "\n";
        result += "HashTable size: " + this.tableSize + "\n";
        result += "Load factor: " + this.getCurrentLoad() + "\n";
        return result;
    }

//    public void resize(int newSize){
//      double the size
//    }

    public void clear(){
        for(int i = 0; i < this.tableSize; i++){
            this.hashTable[i] = null;
        }
        this.elemCount = 0;
    }

    public boolean saveToFile(String fileName){
        PrintWriter p = null;
        try{
            p = new PrintWriter(new File(fileName));
        }
        catch(Exception e){
            System.out.println("Error with saving to " + fileName);
            return false;
        }
        p.print(this);
        p.close();
        return true;

    }

    public boolean remove(T element){
        int result = this.containsElem(element); //containsElem() either returns a -1 or the index of where the element is
        Node<T> tail = hashTable[result];
        Node<T> ptr = tail.getNext();
        if(result == -1){
            return false;
        }
        else if(tail.getData().equals(element)){
            if(ptr == null){
                hashTable[result] = null; //set tail to null, only one node at that index to remove
                return true;
            }
            hashTable[result] = ptr;
            return true;
        }
        else{ //find the element to remove in the linked list
            while(!ptr.getData().equals(element)){
                tail = ptr;
                ptr = ptr.getNext();
            }
            tail.setNext(ptr.getNext());
            return true;
        }
    }

    public static void runConsole(){
        Scanner s = new Scanner(System.in);
        String[] command;
        System.out.println("----HashTable Console----\nCommands: ");
        System.out.println("    hashcode <element> : prints the hashcode for the given element");
        System.out.println("    contains <element> : prints the index for the element");
        System.out.println("    add <element> : inserts the element into the HashTable");
        System.out.println("    remove <element> : removes the element from the HashTable");
        System.out.println("    print : prints the HashTable");
        System.out.println("    clear : clears the current HashTable");
        System.out.println("    save <filename.txt> : saves the HashTable to the given .txt file");
        System.out.println("    help : prints out the commands again");
        System.out.println("    quit : exits the program");
        try {
            System.out.print("Please enter a size for your HashTable: ");
            int length = s.nextInt();
            HashTable h1 = new HashTable(length);
            System.out.print("HashTable>> ");
            inputLoop:
            while (s.hasNext()) {
                command = s.nextLine().split(" ");
                switch (command[0]) {
                    case "hashcode":
                        System.out.println("<" + command[1] + ">'s hashcode: " + h1.hash(command[1]));
                        break;
                    case "contains":
                        if (h1.containsElem(command[1]) == -1) {
                            System.out.println("NOT FOUND");
                        } else {
                            System.out.println("<" + command[1] + ">'s index is: " + h1.containsElem(command[1]));
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
                        if (h1.remove(command[1])) {
                            System.out.println("<" + command[1] + "> was successfully removed");
                        } else {
                            System.out.println("Element could not be removed");
                        }
                        break;
                    case "print":
                        System.out.println("Printing the HashTable...\n");
                        System.out.println(h1);
                        break;
                    case "clear":
                        h1.clear();
                        System.out.println("Cleared the HashTable");
                        break;
                    case "save":
                        if (h1.saveToFile(command[1])){
                            System.out.println("HashTable was successfully saved to <" + command[1] + ">");
                        }
                        else{
                            System.out.println("HashTable could not be saved");
                        }
                        break;
//                    case "load":
//                        if(h1.getCurrentLoad() > h1.getLoad()){
//                            System.out.println("Resizing the HashTable...");
//                        }
//                        else{
//                            System.out.println("Load factor is still good!");
//                        }
                    case "help":
                        System.out.print(" \n");
                        System.out.println("----HashTable Console----\nCommands: ");
                        System.out.println("    hashcode <element> : prints the hashcode for the given element");
                        System.out.println("    contains <element> : prints the index for the element");
                        System.out.println("    add <element> : inserts the element into the HashTable");
                        System.out.println("    remove <element> : removes the element from the HashTable");
                        System.out.println("    print : prints the HashTable");
                        System.out.println("    clear : clears the current HashTable");
                        System.out.println("    save <filename.txt> : saves the HashTable to the given .txt file");
                        System.out.println("    help : prints out the commands again");
                        System.out.println("    quit : exits the program");
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
        catch(InputMismatchException e){
            System.out.println("Invalid command!");
            //if user doesn't enter an int for HashTable length or only has one argument for a two argument command
        }
    }
}
