import java.util.Scanner;
import java.io.File;
import java.util.concurrent.SynchronousQueue;

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

			Data data = new Data();

			//Start Thread Here
			RowChecker row = new RowChecker(grid, data);
			ColumnChecker col = new ColumnChecker(grid, data);

			row.start();
			col.start();

			//Fixer fixer = new Fixer(grid);
			//fixer.start();
			//row.join();
			//col.join();
			//fixer.join();

		}
		catch(Exception e) {
			System.out.println(e);
		}

	}
}

class Data
{
	//A synchronous queue allows one thread to insert data, blocks it, and notifies the
	//other thread that takes that data from the queue
	final SynchronousQueue<int[]> queue = new SynchronousQueue<int[]>();
	int done = 0;
}


class RowChecker extends Thread 
{
	int[][] grid = new int[9][9];
	private Data data;

	//Create a constructor to pass the array to other threads
	public RowChecker(int[][] table, Data dt) {
		grid = table;
		data = dt;
	}

	public void run() {
		checkRow();
	}

	public void checkRow() {
		int[] temp = {0, 0, 0};
		try {		
			for(int i = 0; i < 9; ++i) {
				for(int j = 0; j < 9; ++j) {
					for(int k = j + 1; k < 9; ++k) {
						if(grid[i][j] == grid[i][k]) {
							temp[0] = i;
							temp[1] = j;
							temp[2] = k;
							System.out.println("Found errors at: " + i + ", " + j + " and "+ i + ", " + k);
							data.queue.put(temp); //Push an array of indexes to the queue
						}
					}
				}
				data.done++;
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
}



class ColumnChecker extends Thread 
{
	int[][] grid = new int[9][9];
	Data data;

	//Create a constructor to pass the array to other threads
	public ColumnChecker(int[][] table, Data dt) {
		grid = table;
		data = dt;
	}

	public void run() {
		checkCol();

	}

	public void checkCol() {
		try {
			while(data.done < 9) {
				int[] temp = data.queue.take(); //Pulls the array of indexes from the queue
				System.out.println("Taken");
				for(int j = 0; j < temp.length; ++j) {
					System.out.println(temp[j]);
				}
			}
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
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
