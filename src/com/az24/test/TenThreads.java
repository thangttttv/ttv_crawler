package com.az24.test;

public class TenThreads {
	private static class WorkerThread extends Thread {
		int max = Integer.MIN_VALUE;
		int[] ourArray;

		public WorkerThread(int[] ourArray) {
			this.ourArray = ourArray;
		}

		// Find the maximum value in our particular piece of the array
		public void run() {
			for (int i = 0; i < ourArray.length; i++)
				max = Math.max(max, ourArray[i]);
		}

		public int getMax() {
			return max;
		}
	}

	public static void main(String[] args) throws InterruptedException {
		WorkerThread[] threads = new WorkerThread[10];
		int[][] bigMatrix = {{1,2,3,4,5,6,7,8,9},{20,21,22},{30,31,32},
				{40,41,42},{50,51,52},{60,61,62},{70,71,72},{80,81,82},
				{90,91,92},{100,101,102},};
		int max = Integer.MIN_VALUE;
		// Give each thread a slice of the matrix to work with
		for (int i = 0; i < 10; i++) {
			threads[i] = new WorkerThread(bigMatrix[i]);
			threads[i].start();
			//System.out.println("Maximum value was " + threads[i].getMax());
		}
		for (int i = 0; i < 10; i++) {
			threads[i].join();
			max = Math.max(max, threads[i].getMax());
			System.out.println("Maximum value was " + max);
		}
		System.out.println("Maximum value was " + max);
	}
}