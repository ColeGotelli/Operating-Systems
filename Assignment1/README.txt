Name: Cole Gotelli
Student ID: 2268217
Email: gotel100@mail.chapman.edu
Course: CPSC 380

Project 1: Sudoku Validator

Description:
The Sudoku Validator Program is a program that aims to check a 9x9 Sudoku board for errors utilizing threads. The program checks the rows of the board and upon finding an error, it sends the location of the error to another thread. This thread determines whether the specified value also has an error in its column. If it does the correct value is calculated and indicated to the user.


Instructions:
After navigating to the correct folder use the following commands:
- 'javac Sudoku.java'
- 'java Sudoku [FILENAME]' (Example: java Sudoku test1.txt)