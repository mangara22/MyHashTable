import java.io.File;
import java.io.PrintWriter;
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
        if(key == null){
            return -1;
        }
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

//    public void saveToFile(String fileName){
//
//    }

//    public int remove(){
//
//    }

    public static void runConsole(){
        Scanner s = new Scanner(System.in);
        String[] command;
        System.out.println("----HashTable Console----\nCommands: ");
        System.out.println("    hashcode <element> : prints the hashcode for the given element");
        System.out.println("    contains <element> : prints the index for the element");
        System.out.println("    add <element> : inserts the element into the HashTable");
        System.out.println("    print : prints the HashTable");
        System.out.println("    clear : clears the current HashTable");
        System.out.println("    help : prints out the commands again");
        System.out.println("    quit : exits the program");

        System.out.print("How large do you want your HashTable to be? ");
        int length = s.nextInt();
        HashTable h1 = new HashTable(length);
        System.out.print("HashTable>> ");
        while(s.hasNext()){
            command = s.nextLine().split(" ");
            if(command[0].equals("print")){
                System.out.println("Printing HashTable...\n");
                System.out.println(h1);
            }
//            if(h1.getCurrentLoad() >= h1.getLoad()){
//                System.out.println("Load factor is too big! Resizing HashTable...");
//            }
            else if(command[0].equals("clear")){
                h1.clear();
                System.out.println("Cleared the HashTable");
            }
            else if(command[0].equals("help")){
                System.out.print(" \n");
                System.out.println("----HashTable Console----\nCommands: ");
                System.out.println("    hashcode <element> : prints the hashcode for the given element");
                System.out.println("    contains <element> : prints the index for the element");
                System.out.println("    add <element> : inserts the element into the HashTable");
                System.out.println("    print : prints the HashTable");
                System.out.println("    help : prints out the commands again");
                System.out.println("    quit : exits the program");
            }
            else if(command[0].equals("quit")) {
                break;
            }
            else if(command[0].equals("add")) {
                if(h1.containsElem(command[1]) != -1){
                    System.out.println("Duplicate element found, did not add");
                }
                else{
                    h1.add(command[1]);
                    System.out.println("<" + command[1] + ">" + " was added");
                }
            }
            else if(command[0].equals("contains")){
                if(h1.containsElem(command[1]) == -1){
                    System.out.println("NOT FOUND");
                }
                else{
                    System.out.println("<" + command[1] + "'s> index is: " + h1.containsElem(command[1]));
                }
            }
            else if(command[0].equals("hashcode")){
                System.out.println("<" + command[1] + "'s> hashcode is: " + h1.hash(command[1]));
            }
//            else{
//                System.out.println("Invalid command!");
//            }
            System.out.print("HashTable>> ");
        }
        System.out.println("Exiting the Console...");
    }
}
