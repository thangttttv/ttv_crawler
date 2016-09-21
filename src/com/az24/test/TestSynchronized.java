package com.az24.test;

public class TestSynchronized {
	  int taskID;

	  public synchronized void performATask(int val) {
	    taskID = val;
	    print("before: " + taskID);
	    try {
	      Thread.sleep(4000);
	    } catch (InterruptedException x) {
	    }
	    print("after: " + taskID);
	  }

	  public static void print(String msg) {
	    System.out.println(Thread.currentThread().getName() + ": " + msg);
	  }

	  public static void main(String[] args) throws Exception{
	    final TestSynchronized tus = new TestSynchronized();

	    Runnable runA = new Runnable() {
	      public void run() {
	        tus.performATask(1);
	      }
	    };

	    Thread ta = new Thread(runA, "threadA");
	    ta.start();

//	    Thread.sleep(2000);

	    Runnable runB = new Runnable() {
	      public void run() {
	        tus.performATask(2);
	      }
	    };

	    Thread tb = new Thread(runB, "threadB");
	    tb.start();
	  }
	}