/*
 *  Name: Cole Gotelli
 *  Student ID: 2268217
 *  Email: gotel100@mail.chapman.edu
 *  Course: CPSC 380
 *  Project 3: Rate Monotonic Scheduler
 */

import java.util.*;

class RMS 
{
	public static void main(String[] args) {

	}
}

class Scheduler extends Thread
{
	Tasks task1;
	Tasks task2;
	Tasks task3;
	Tasks task4;

	public Scheduler(Tasks t1, Tasks t2, Tasks t3, Tasks t4) {
		task1 = t1;
		task2 = t2;
		task3 = t3;
		task4 = t4;
	}
}

class Tasks extends Thread
{
	
	public void doWork() {
		int[][] matrix = new int[10][10];
		Arrays.fill(matrix, 1);

		for(int i = 0; i < 5; i++) {
			matrix[i][0] = matrix[i][0]*matrix[i][0];
			matrix[i][1] = matrix[i][1]*matrix[i][1];
			matrix[i][2] = matrix[i][2]*matrix[i][2];
			matrix[i][3] = matrix[i][3]*matrix[i][3];
			matrix[i][4] = matrix[i][4]*matrix[i][4];
			matrix[i][5] = matrix[i][5]*matrix[i][5];
			matrix[i][6] = matrix[i][6]*matrix[i][6];
			matrix[i][7] = matrix[i][7]*matrix[i][7];
			matrix[i][8] = matrix[i][8]*matrix[i][8];
			matrix[i][9] = matrix[i][9]*matrix[i][9];

			matrix[i+5][0] = matrix[i+5][0]*matrix[i+5][0];
			matrix[i+5][1] = matrix[i+5][1]*matrix[i+5][1];
			matrix[i+5][2] = matrix[i+5][2]*matrix[i+5][2];
			matrix[i+5][3] = matrix[i+5][3]*matrix[i+5][3];
			matrix[i+5][4] = matrix[i+5][4]*matrix[i+5][4];
			matrix[i+5][5] = matrix[i+5][5]*matrix[i+5][5];
			matrix[i+5][6] = matrix[i+5][6]*matrix[i+5][6];
			matrix[i+5][7] = matrix[i+5][7]*matrix[i+5][7];
			matrix[i+5][8] = matrix[i+5][8]*matrix[i+5][8];
			matrix[i+5][9] = matrix[i+5][9]*matrix[i+5][9];
		}
	}
}