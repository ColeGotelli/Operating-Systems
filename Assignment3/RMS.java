/*
 *  Name: Cole Gotelli
 *  Student ID: 2268217
 *  Email: gotel100@mail.chapman.edu
 *  Course: CPSC 380
 *  Project 3: Rate Monotonic Scheduler
 */

import java.util.*;
import java.time.Clock;

class RMS 
{
	Semaphore scheduleSema = new Semaphore(5);
	Semaphore locks = new Semaphore{new Semaphore(1), new Semaphore(1), new Semaphore(1), new Semaphore(1)};

	int[] runs = {100,200,400,1600}; //Num times each thread should run
	int[] miss = {0,0,0,0}; //Num tasks not completed
	int[] finished = {0,0,0,0}; //Num tasks completed
	int[] times = {1,2,4,16}; // Amount of time thread has to complete
	int[] loops = {1,2,4,16}; // Num times a thread runs doWork()

	public static void main(String[] args) {

		scheduleSema.aquire(5);

		if(args[1].equals("1")) {
			System.out.println("Nominal Case:");
		}
		else if(args[1].equals("2")) {
			System.out.println("T2 Overrun Case:");
			loops[1] = 50000;
		}
		else {
			System.out.println("Nominal Case:");
		}

		scheduleSema.release(1); //Releases a permit for the scheduler
	}
}

class Scheduler extends Thread
{
	Tasks task1;
	Tasks task2;
	Tasks task3;
	Tasks task4;

	private Clock clock;

	public Scheduler(Tasks t1, Tasks t2, Tasks t3, Tasks t4) {
		task1 = t1;
		task2 = t2;
		task3 = t3;
		task4 = t4;

		t1.setPriority(10);
		t2.setPriority(9);
		t3.setPriority(8);
		t4.setPriority(7);
	}

	public void run() {

		for(int i = 0; i < 100; ++i) {
			task1.doWork();
			finished[0]++;
		}
		miss[0] = runs[0]-finished[0];

		for(int j = 0; j < 200; ++j) {
			task1.doWork();
			finished[1]++;
		}
		miss[1] = runs[1]-finished[1];

		for(int k = 0; k < 400; ++k) {
			task1.doWork();
			finished[2]++;
		}
		miss[2] = runs[2]-finished[2];

		for(int m = 0; m < 1600; ++m) {
			task4.doWork();
			finished[3]++;
		}
		miss[3] = runs[3]-finished[3];


		System.out.println("Tasks Expected to Run: " + runs[0] + ", " + runs[1] + ", " + runs[2] + ", " + runs[3]);
		System.out.println("Tasks Actually Run: " + finished[0] + ", " + finished[1] + ", " + finished[2] + ", " + finished[3]);
		System.out.println("Tasks Expected to Run: " + miss[0] + ", " + miss[1] + ", " + miss[2] + ", " + miss[3]);
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