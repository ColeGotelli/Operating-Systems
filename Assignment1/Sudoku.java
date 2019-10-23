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

			Data data = new Data();

			//Start Thread Here
			RowChecker row = new RowChecker(grid, data);
			ColumnChecker col = new ColumnChecker(grid, data);
			Fixer fixer = new Fixer(grid, data);

			row.start();
			col.start();
			fixer.start();

			//System.out.println(row.getState());
			//System.out.println(col.getState());
			//System.out.println(fixer.getState());

			row.join();
			col.join();
			fixer.join();
			

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
	int[] middle;
	int[] toFix;
	LinkedList<int[]> fixQueue = new LinkedList<int[]>();
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
							data.middle = temp;
							System.out.println("Found " +grid[i][j]+ "s at: " + i + ", " + j + " and "+ i + ", " + k);
							data.queue.put(temp); //Push an array of indexes to the queue
							Thread.sleep(5);
						}
					}
				}
				data.done++;
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
		System.out.println("Made it out Row");
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
				for(int i = 0; i < 9; ++i) {
					if(grid[temp[0]][temp[1]] == grid[i][temp[1]]) {
						System.out.println("I think this pinpoints: " + grid[i][temp[1]]);
						System.out.println("Found at (" + i + ", " + temp[1] + ")");
						int[] fixDex = {temp[0],temp[1]};
						data.toFix = fixDex;
						data.fixQueue.add(fixDex);
						Thread.sleep(5);
					}
					else if(grid[temp[0]][temp[2]] == grid[i][temp[2]]) {
						System.out.println("I think this pinpoints: " + grid[i][temp[2]]);
						System.out.println("Found at (" + i + ", " + temp[2] + ")");
						int[] fixDex = {temp[0], temp[2]};
						data.toFix = fixDex;
						data.fixQueue.add(fixDex);
						Thread.sleep(5);
					}
				}
			}
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Made it out Col");
	}
}



class Fixer extends Thread 
{
	int[][] grid = new int[9][9];
	private Data data;

	//Create a constructor to pass the array to other threads
	public Fixer(int[][] table, Data dt) {
		grid = table;
		data = dt;
	}

	public void run() {
		fixTable();
		printTable();
	}

	public void fixTable() {
		try {
			Thread.sleep(500);
			int[] temp = data.toFix;
			System.out.println("Just grabbed " + temp[0] + ", " + temp[1]);
			int gridSum = 0;
			boolean[] subNumbers = new boolean[9];
			/*SETS IT TO 44 FOR SOME REASON*/
			if(temp[0] < 3) {
				gridSum = 0;
				System.out.println("Do we even try 0?");
				if (temp[1] < 3) {
					for(int i = 0; i < 3; ++i) {
						gridSum += grid[i][i];
						gridSum += grid[i][i+1];
						gridSum += grid[i][i+2];

					}
					int remainder = 45-gridSum;
					grid[temp[0]][temp[1]] += remainder;
				}
				else if (temp[1] >= 3 && temp[1] < 6) {

					subNumbers[grid[3][0]] = true;
					subNumbers[grid[3][1]] = true;
					subNumbers[grid[3][2]] = true;
					subNumbers[grid[4][0]] = true;
					subNumbers[grid[4][1]] = true;
					subNumbers[grid[4][2]] = true;
					subNumbers[grid[5][0]] = true;
					subNumbers[grid[5][1]] = true;
					subNumbers[grid[5][2]] = true;
					for(int i = 0; i < 9; ++i) {
						if(subNumbers[i] == false) {
							grid[temp[0]][temp[1]] = i;
						}
					}
					Arrays.fill(subNumbers, Boolean.FALSE);
					System.out.println("Fixed");
				}
				else if (temp[1] > 6) {
					for(int i = 0; i < 3; ++i) {
						gridSum += grid[i][i+3];
						gridSum += grid[i][i+4];
						gridSum += grid[i][i+5];

						int remainder = 45-gridSum;
						grid[temp[0]][temp[1]] += remainder;
					}
					
				}
			}
			else if(temp[0] >= 3 && temp[0] < 6) {
				System.out.println("Do we even try 1?");
				if (temp[1] < 3) {
					subNumbers[grid[0][3]] = true;
					subNumbers[grid[0][4]] = true;
					subNumbers[grid[0][5]] = true;
					subNumbers[grid[1][3]] = true;
					subNumbers[grid[1][4]] = true;
					subNumbers[grid[1][5]] = true;
					subNumbers[grid[2][3]] = true;
					subNumbers[grid[2][4]] = true;
					subNumbers[grid[2][5]] = true;
					for(int i = 0; i < 9; ++i) {
						if(subNumbers[i] == false) {
							grid[temp[0]][temp[1]] = i;
						}
					}
					Arrays.fill(subNumbers, Boolean.FALSE);
					System.out.println("Fixed");
				}
				else if (temp[1] >= 3 && temp[1] < 6) {
					subNumbers[grid[3][3]] = true;
					subNumbers[grid[3][4]] = true;
					subNumbers[grid[3][5]] = true;
					subNumbers[grid[4][3]] = true;
					subNumbers[grid[4][4]] = true;
					subNumbers[grid[4][5]] = true;
					subNumbers[grid[5][3]] = true;
					subNumbers[grid[5][4]] = true;
					subNumbers[grid[5][5]] = true;
					for(int i = 0; i < 9; ++i) {
						if(subNumbers[i] == false) {
							grid[temp[0]][temp[1]] = i;
						}
					}
					Arrays.fill(subNumbers, Boolean.FALSE);
					System.out.println("Fixed");
				}
				else if (temp[1] > 6) {
					subNumbers[grid[6][3]] = true;
					subNumbers[grid[6][4]] = true;
					subNumbers[grid[6][5]] = true;
					subNumbers[grid[7][3]] = true;
					subNumbers[grid[7][4]] = true;
					subNumbers[grid[7][5]] = true;
					subNumbers[grid[8][3]] = true;
					subNumbers[grid[8][4]] = true;
					subNumbers[grid[8][5]] = true;
					for(int i = 0; i < 9; ++i) {
						if(subNumbers[i] == false) {
							grid[temp[0]][temp[1]] = i;
						}
					}
					Arrays.fill(subNumbers, Boolean.FALSE);
					System.out.println("Fixed");
				}
			}
			else if(temp[0] > 6) {
				System.out.println("Do we even try 2?");
				if (temp[1] < 3) {
					for(int i = 0; i < 3; ++i) {
						gridSum += grid[6+i][i];
						gridSum += grid[6+i][i+1];
						gridSum += grid[6+i][i+2];

					}
					int remainder = 45-gridSum;
					grid[temp[0]][temp[1]] += remainder;
				}
				else if (temp[1] >= 3 && temp[1] < 6) {
					for(int i = 0; i < 3; ++i) {
						gridSum += grid[6+i][i+3];
						gridSum += grid[6+i][i+4];
						gridSum += grid[6+i][i+5];

					}
					int remainder = 45-gridSum;
					grid[temp[0]][temp[1]] += remainder;
				}
				else if (temp[1] > 6) {
					for(int i = 0; i < 3; ++i) {
						gridSum += grid[6+i][i+6];
						gridSum += grid[6+i][i+7];
						gridSum += grid[6+i][i+8];

					}
					int remainder = 45-gridSum;
					grid[temp[0]][temp[1]] += remainder;
				}
			}
			System.out.println("Made it out fixer");

		}
		catch(Exception e) {}
	}

	//This code prints out the grid how an actual Sudoku grid looks
	public void printTable() {
		try {
			Thread.sleep(3000);
		}
		catch(Exception e) {}

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
