/*
 *  Name: Cole Gotelli
 *  Student ID: 2268217
 *  Email: gotel100@mail.chapman.edu
 *  Course: CPSC 380
 *  Project 2: SeeSaw Simulator
 */

import java.util.concurrent.*;

class SeeSaw 
{
	public static void main(String[] args) {

		Semaphore semaphore1 = new Semaphore(1);
		Semaphore semaphore2 = new Semaphore(1);
		Shared shared = new Shared();
		Simulator fred = new Simulator("A", shared, semaphore1, semaphore2);
		Simulator wilma = new Simulator("B", shared, semaphore1, semaphore2);
		try {
			System.out.println("Fred\t|\tWilma");

			
			fred.start();
			wilma.start();

			fred.join();
			wilma.join();
		}
		catch (Exception e) {}

	}
}

class Shared {
	float fHeight = 1;
	float wHeight = 7;
}

class Simulator extends Thread
{
	String threadName;
	Shared s;
	Semaphore sem1;
	Semaphore sem2;
	int count = 0;

	public Simulator(String name, Shared shared, Semaphore sm1, Semaphore sm2) {
		threadName = name;
		s = shared;
		sem1 = sm1;
		sem2 = sm2;
	}

	public void run() {
		while (count < 10) {
			if(threadName.equals("A")) { //Fred is about to go up
				fredSee();
			}
			else if(threadName.equals("B")) { //Wilma is about to go up
				wilmaSaw();
			}

			count++;
		}
	}

	public void fredSee() {
		try {
			sem1.acquire(); //Grab the semaphore to allow fred's thread to change the height
			System.out.println("---------------------");
			
			while(s.wHeight > 1) {
				System.out.println(s.fHeight + "\t|\t" + s.wHeight);
				s.fHeight += 1;
				s.wHeight -= 1;
			}
			sem1.release();
			Thread.sleep(1000); //Sleep this thread after an execution to help synchronize threads
		}
		catch (Exception e) {}
		
	}

	public void wilmaSaw() {
		try {
			Thread.sleep(1000); //Sleep this thread initially to help synchronize threads
			System.out.println("---------------------");
			sem2.acquire(); //Grab the semaphore to allow wilma's thread to change the height
			
			while(s.fHeight > 1) {
				System.out.println(s.fHeight + "\t|\t" + s.wHeight);
				s.wHeight += 1.5;
				s.fHeight -= 1.5;
				
			}
			sem2.release();
		}
		catch (Exception e) {}

	}
}
