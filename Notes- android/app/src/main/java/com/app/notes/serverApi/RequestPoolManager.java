package com.app.notes.serverApi;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Saurabh Vashisht
 * 
 */
public class RequestPoolManager {

	// Sets the amount of time an idle thread will wait for a task before
	// terminating
	private static final int KEEP_ALIVE_TIME = 1;

	// Sets the initial threadpool size to 8
	private static final int CORE_POOL_SIZE = 8;

	// Sets the maximum threadpool size to 8
	private static final int MAXIMUM_POOL_SIZE = 8;

	// A queue of Runnables for the download pool
	private final BlockingQueue<Runnable> workQueue;

	// A queue of Application tasks. Tasks are handed to a ThreadPool.
	// private final Queue<PhotoTask> taskWorkQueue;

	// A managed pool of background decoder threads
	private final ThreadPoolExecutor threadPool;

	private final RejectedExecutionHandler mRejectionHandler;

	private static RequestPoolManager sInstance;

	public static RequestPoolManager getInstance() {
		if (sInstance == null) {
			sInstance = new RequestPoolManager();
		}
		return sInstance;
	}

	public static void shutdownInstance() {

		if (sInstance != null) {
			sInstance.close();
			sInstance = null;
		}

	}

	private void close() {
		workQueue.clear();
		threadPool.shutdown();
	}

	public RequestPoolManager() {

		/*
		 * Creates a work queue for the pool of Thread objects used for
		 * WorkTasks, using a linked list queue that blocks when the queue is
		 * empty.
		 */
		workQueue = new LinkedBlockingQueue<Runnable>();

		// Retry Handler for Submitted Task
		mRejectionHandler = new RejectedExecutionHandler() {
			public void rejectedExecution(Runnable paramRunnable,
					ThreadPoolExecutor paramThreadPoolExecutor) {
				((BaseTaskRunnable) paramRunnable).getTask()
						.finish();
			}
		};

		/*
		 * Creates a new pool of Thread objects for the Tasks queue
		 */
		threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
				KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue, mRejectionHandler);

	}

	public void executeRequest(Runnable paramBaseTask) {
		threadPool.execute(paramBaseTask);
	}

	public void executeRequest(BaseTask<?> paramBaseTask) {
		threadPool.execute(new BaseTaskRunnable(paramBaseTask));
	}

	/**
	 * Runnable for Creating a Worker Thread
	 * 
	 * @author Ankur Parashar(ankur@finoit.com)
	 * 
	 */
	private class BaseTaskRunnable implements Runnable {
		private BaseTask<?> mTask;

		public BaseTaskRunnable(BaseTask<?> paramBaseTask) {
			this.mTask = paramBaseTask;
		}

		public BaseTask<?> getTask() {
			return this.mTask;
		}

		public void run() {
			this.mTask.processData();
		}

	}

}
