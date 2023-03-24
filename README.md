# MyHashTable

## Description:
A *basic* implementation of a **HashTable**, *don't expect anything too fancy*.
 - Used **Linked Lists** for *separate chaining*
 - Implemented a resize method that makes the HashTable bigger based on a *load factor of 0.75*
 - Attempted to implement *open addressing*

## Compile/Run:
`javac HashTableConsole.java` --> `java HashTableConsole`

## Assumptions:
 - The user will not type in random letters for an argument in the console
 - The user will save their HashTable to a `.txt` file *only*
 
## Inspiration:
 - Decided to make my own basic implementation of a HashTable in **Java** based on other CSCI classes.
 - **CSCI 1933 Project 5**: similar code for `add`, `hash`, `print`
 - **CSCI 2021 Project 1 Problem 3**: similar implementation of a "console" with different `commands`, except this is in **Java** and *not* in **C**

## Extra Things Used:
 - Java `Generics`
 - `Switch` Statements
 - `Try-Catch` for input error handling
 - `PrintWriter` for saving to a file
 - `StringBuilder` for printing
