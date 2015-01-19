package com.laba.recordtest;

import java.io.File;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;


public class Mp3Recorder implements Callback{
	private int mMaxDuration;// 最长录音时间，单位：秒(s)
	private String outputFilePath;
	private AudioRecorder audioRecorder = null;
	private int state = State.UNINITIALIZED;
	private Handler mHandler;
	private OnMaxDurationReached onMaxDurationReachedListener;

	public class State {
		public static final int UNINITIALIZED = -1;
		public static final int INITIALIZED = 0;
		public static final int PREPARED = 1;
		public static final int RECORDING = 2;
		public static final int PAUSED = 3;
		public static final int STOPPED = 4;
	}
	
	private Runnable r = new Runnable() {
		
		@Override
		public void run() {
			if(state != State.STOPPED){
				onMaxDurationReachedListener.onMaxDurationReached();
			}
		}
	};
	
	/**
	 * TODO 要考虑使用单例模式
	 */
	public Mp3Recorder(){
		mHandler = new Handler(this);
	}
	
	public int getmMaxDuration() {
		return mMaxDuration;
	}

	public void setmMaxDuration(int mMaxDuration) {
		this.mMaxDuration = mMaxDuration;
	}
	
	public void setOutputFile(String path){
		this.outputFilePath = path;
	}
	
	public void setOnMaxDurationReachedListener(
			OnMaxDurationReached onMaxDurationReachedListener) {
		this.onMaxDurationReachedListener = onMaxDurationReachedListener;
	}
	
	public void prepare(){
		audioRecorder = new AudioRecorder(new File(outputFilePath));
		audioRecorder.start();
		state = State.PREPARED;
	}
	
	public void startRecording(){
		if(state == State.PREPARED){
			audioRecorder.startRecording();
			state = State.RECORDING;
			mHandler.removeCallbacks(r);
			mHandler.postDelayed(r, mMaxDuration * 1000 - audioRecorder.getDuration());
		}
	}
	
	public void pause(){
		audioRecorder.pauseRecord();
		state = State.PAUSED;
	}
	
	public void resume(){
		audioRecorder.resumeRecord();
		state = State.RECORDING;
	}
	
	public void stop(){
		audioRecorder.stopRecord();
		state = State.STOPPED;
		mHandler.removeCallbacks(r);
	}
	
	public int getRecorderState(){
		return state;
	}
	
	public void reset(){
		if(null == audioRecorder){
			prepare();
			return;
		}
		if(null != audioRecorder && state != State.STOPPED){
			stop();
		}
		audioRecorder = null;
		prepare();
	}
	
	private void setLocation(float latitude, float longitude){
		
	}
	

	@Override
	public boolean handleMessage(Message msg) {
		return false;
	}
}
