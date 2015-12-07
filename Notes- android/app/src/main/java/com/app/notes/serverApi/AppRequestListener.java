package com.app.notes.serverApi;

public interface AppRequestListener {
	
	public static final int REQUEST_STARTED=1;
	
	public static final int REQUEST_FAILED=2;
	
	public static final int REQUEST_COMPLETED=3;

	/**
	 * Callback Client Request Started
	 * 
	 * @param <T>
	 */
	public <T> void onRequestStarted(BaseTask<T> request);

	/**
	 * Callback Client Request Completed
	 */
	public <T> void onRequestCompleted(BaseTask<T> request);

	/**
	 * Callback Client Request Failed
	 */
	public <T> void onRequestFailed(BaseTask<T> request);

}
