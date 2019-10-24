/*
 *  Name: Cole Gotelli
 *  Student ID: 2268217
 *  Email: gotel100@mail.chapman.edu
 *  Course: CPSC 380
 *  Project 1: Sudoku Validator
 */

import java.util.*;
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

			System.out.println("Welcome to the Sudoku Validator! \n");

			Data data = new Data();

			//Initialize Threads Here
			RowChecker row = new RowChecker(grid, data);
			ColumnChecker col = new ColumnChecker(grid, data);
			Fixer fixer = new Fixer(grid, data);

			//Start Threads Here
			row.start();
			col.start();
			fixer.start();

			row.join();
			col.join();
			fixer.join();
			
			//Print out results of the checks and what the grid should look like
			int[] realQuick = data.fixQueue.getFirst();
			realQuick[0]++;
			realQuick[1]++;
			System.out.println("An error was found at: " + realQuick[0] + ", " + realQuick[1]);
			System.out.println("The correct answer should be " + grid[realQuick[0]-1][realQuick[1]-1]);
			System.out.println("The fixed table should look like this: \n");

			//This code prints out the grid how an actual Sudoku grid looks
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
			System.out.println();

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
	final LinkedList<int[]> fixQueue = new LinkedList<int[]>(); //To keep track of the indexes that contain errors
}


class RowChecker extends Thread 
{
	int[][] grid = new int[9][9];
	private Data data;

	//Constructor to pass the array to other threads
	public RowChecker(int[][] table, Data dt) {
		grid = table;
		data = dt;
	}

	public void run() {
		checkRow();
	}

	public void checkRow() {
		int[] temp = {0, 0, 0}; //Array to store the row and 2 columns with errors
		try {		
			for(int i = 0; i < 9; ++i) {
				for(int j = 0; j < 9; ++j) {
					for(int k = j + 1; k < 9; ++k) {
						if(grid[i][j] == grid[i][k]) {
							temp[0] = i;
							temp[1] = j;
							temp[2] = k;
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

	//Constructor to pass the array to other threads
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
				for(int i = 0; i < 9; ++i) {
					if(grid[temp[0]][temp[1]] == grid[i][temp[1]]) { //Loop thru the column that contains an error and check if the values appears again in the column
						int[] fixDex = {temp[0],temp[1]};
						data.fixQueue.addLast(fixDex); //If the value appears twice in the column, out the index in the queue
					}
					else if(grid[temp[0]][temp[2]] == grid[i][temp[2]]) {
						int[] fixDex = {temp[0], temp[2]};
						data.fixQueue.addLast(fixDex);
					}
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
	private Data data;

	//Constructor to pass the array to other threads
	public Fixer(int[][] table, Data dt) {
		grid = table;
		data = dt;
	}

	public void run() {
		fixTable();
	}

	public void fixTable() {
		
		try {
			Thread.sleep(5);

			int[] temp = data.fixQueue.getFirst(); //Grab the. confirmed index of an error
			int gridSum = 0;

			//Determine which subgrid the error is in
			if(temp[0] < 3) {
				if (temp[1] < 3) {
					for(int i = 0; i < 3; ++i) { //Sum the values in the subgrid
						gridSum += grid[i][0];
						gridSum += grid[i][1];
						gridSum += grid[i][2];

					}
					grid[temp[0]][temp[1]] += 45-gridSum; //Add the difference of expected and actual sum to the value. NOTE: this does not work if there are multiple errors in a grid
				}
				else if (temp[1] >= 3 && temp[1] < 6) {
					for(int i = 0; i < 3; ++i) {
						gridSum += grid[i][3];
						gridSum += grid[i][4];
						gridSum += grid[i][5];

					}
					grid[temp[0]][temp[1]] += 45-gridSum;
				}
				else if (temp[1] > 6) {
					for(int i = 0; i < 3; ++i) {
						gridSum += grid[i][6];
						gridSum += grid[i][7];
						gridSum += grid[i][8];

					}
					grid[temp[0]][temp[1]] += 45-gridSum;
					
				}
			}
			else if(temp[0] >= 3 && temp[0] < 6) {
				if (temp[1] < 3) {
					for(int i = 0; i < 3; ++i) {
						gridSum += grid[3+i][0];
						gridSum += grid[3+i][1];
						gridSum += grid[3+i][2];

					}
					grid[temp[0]][temp[1]] += 45-gridSum;
				}
				else if (temp[1] >= 3 && temp[1] < 6) {
					for(int i = 0; i < 3; ++i) {
						gridSum += grid[3+i][3];
						gridSum += grid[3+i][4];
						gridSum += grid[3+i][5];

					}
					grid[temp[0]][temp[1]] += 45-gridSum;
				}
				else if (temp[1] > 6) {
					for(int i = 0; i < 3; ++i) {
						gridSum += grid[3+i][6];
						gridSum += grid[3+i][7];
						gridSum += grid[3+i][8];

					}
					grid[temp[0]][temp[1]] += 45-gridSum;
				}
			}
			else if(temp[0] > 6) {
				if (temp[1] < 3) {
					for(int i = 0; i < 3; ++i) {
						gridSum += grid[6+i][0];
						gridSum += grid[6+i][1];
						gridSum += grid[6+i][2];

					}
					grid[temp[0]][temp[1]] += 45-gridSum;
				}
				else if (temp[1] >= 3 && temp[1] < 6) {
					for(int i = 0; i < 3; ++i) {
						gridSum += grid[6+i][3];
						gridSum += grid[6+i][4];
						gridSum += grid[6+i][5];

					}
					grid[temp[0]][temp[1]] += 45-gridSum;
				}
				else if (temp[1] > 6) {
					for(int i = 0; i < 3; ++i) {
						gridSum += grid[6+i][6];
						gridSum += grid[6+i][7];
						gridSum += grid[6+i][8];

					}
					grid[temp[0]][temp[1]] += 45-gridSum;
				}
			}
		}
		catch(Exception e) {}
	}
}
