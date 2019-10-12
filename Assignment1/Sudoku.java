import java.util.Scanner;
import java.io.File;

class Sudoku 
{
	public static void main(String[] args) {
		try {

			//Reads the file and coverts strings to ints and puts it in an array
			Scanner file = new Scanner(new File(args[0]));
			file.useDelimiter(",");

			int[][] grid = new int[9][9];

			while(file.hasNextLine()) {
				for(int i = 0; i < 9; ++i) {
					for(int j = 0; j < 9; ++j) {
						String temp = file.next();
						grid[i][j] = Integer.parseInt(temp.replaceAll("[\\D]", ""));
					}
				}
			}

			//Start Thread Here

			Fixer fixer = new Fixer(grid);
			fixer.start();
			fixer.join();

		}
		catch(Exception e) {
			System.out.println(e);
		}

	}
}

class RowChecker extends Thread 
{
	int[][] grid = new int[9][9];

	//Create a constructor to pass the array to other threads
	public RowChecker(int[][] table) {
		grid = table;
	}

	public void run() {

	}
}



class ColumnChecker extends Thread 
{
	int[][] grid = new int[9][9];

	//Create a constructor to pass the array to other threads
	public ColumnChecker(int[][] table) {
		grid = table;
	}

	public void run() {
		
	}
}



class Fixer extends Thread 
{
	int[][] grid = new int[9][9];

	//Create a constructor to pass the array to other threads
	public Fixer(int[][] table) {
		grid = table;
	}

	public void run() {

		//printTable();
	}

	//This code prints out the grid how an actual Sudoku grid looks
	public void printTable() {
		for(int i = 0; i < 9; ++i) {
			for(int j = 0; j < 9; ++j) {
				System.out.print(grid[i][j] + " ");
				if(j%3 == 2 && j != 8) {
					System.out.print("| ");
				}

			}
			System.out.println();
			if(i%3 == 2 && i < 8) {
				System.out.print("---------------------");
				System.out.println();
			}
		}
	}
}
