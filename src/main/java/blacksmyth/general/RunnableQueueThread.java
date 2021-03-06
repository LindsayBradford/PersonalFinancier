/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.general;

import java.util.LinkedList;

public class RunnableQueueThread extends Thread {
  private static final int SLEEP_PERIOD = 200;
  
  private LinkedList<Runnable> runnableQueue;
  
  private static final RunnableQueueThread INSTANCE = new RunnableQueueThread();
  
  private RunnableQueueThread() {
    super();
    this.runnableQueue = new LinkedList<Runnable>();
  }
  
  public static RunnableQueueThread getInstance() {
    return INSTANCE;
  }
  
  public synchronized void push(Runnable runnable) {
    this.runnableQueue.addLast(runnable);
  }

  public void run() {
    while (true) {
      try {
        sleep(SLEEP_PERIOD);
      } catch (Exception e) {}
      processQueue();
    }
  }
  
  private synchronized void processQueue() {
    while (this.runnableQueue.size() > 0) {
      Runnable popped = this.runnableQueue.removeFirst();
      popped.run();
    }
  }
}
