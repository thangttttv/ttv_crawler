package com.az24.test;

class CounterThread extends Thread {
	public static boolean stopped = false;

	int count = 0;

	public void run() {
		while (!stopped) {
			try {
				sleep(1000);
			} catch (InterruptedException e) {
			}
			System.out.println(count++);
			
		}
	}

	public static void main(String[] args) {
		CounterThread thread = new CounterThread();
		thread.start();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
		}
		
		//CounterThread.stopped = true;
		
		thread = new CounterThread();
		thread.start();
		
		System.out.println("exit");
	}

}
