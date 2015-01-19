package com.laba.recordtest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.czt.mp3recorder.DataEncodeThread;
import com.czt.mp3recorder.util.LameUtil;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Message;
import android.util.Log;

public class AudioRecorder extends Thread {

	private static final String TAG = "AudioRecorder";
	
	private final int sampleRates[] = {44100,22050, 11025, 8000};
	private final int configs[] = { AudioFormat.CHANNEL_IN_MONO,AudioFormat.CHANNEL_IN_STEREO}; 
	private final int formats[] = { AudioFormat.ENCODING_PCM_16BIT, AudioFormat.ENCODING_PCM_8BIT }; 

	// ======================Lame Default Settings=====================
	private static final int DEFAULT_LAME_MP3_QUALITY = 7;
	/**
	 * Encoded bit rate. MP3 file will be encoded with bit rate 128kbps
	 */
	private static final int DEFAULT_LAME_MP3_BIT_RATE = 128;
	
	private AudioRecord audioRecord = null;
	int bufsize = AudioRecord.ERROR_BAD_VALUE;
	private boolean mShouldRun = false;
	private boolean mShouldRecord = false;
	
	/**
	 * 自定义 每220帧作为一个周期，通知一下需要进行编码
	 */
	private static final int FRAME_COUNT = 220;
	private short[] mPCMBuffer;
	private DataEncodeThread mEncodeThread;
	
	private File outputFile;
	private double mDuration;//录音时间
	
	public AudioRecorder(File file) {
		outputFile = file;
	}
	
	
	public void startRecording(){
		mShouldRecord = true;
	}
	
	public void resumeRecord(){
		mShouldRecord = true;
	}
	
	public void pauseRecord(){
		mShouldRecord = false;
	}
	
	public void stopRecord(){
		mShouldRecord = false;
		mShouldRun = false;
		if(audioRecord != null){
			audioRecord.stop();
			audioRecord.release();
			audioRecord = null;
		}
		// stop the encoding thread and try to wait until the thread finishes its job
		Message msg = Message.obtain(mEncodeThread.getHandler(),
				DataEncodeThread.PROCESS_STOP);
		msg.sendToTarget();
	}
	
	private int mapFormat(int format){
		switch (format) {
		case AudioFormat.ENCODING_PCM_8BIT:
			return 8;
		case AudioFormat.ENCODING_PCM_16BIT:
			return 16;
		default:
			return 0;
		}
	}
	
	public int getDuration(){
		return (int)mDuration;
	}
	
	@Override
	public void run() {
		super.run();
		if(!isFound()){
			Log.e(TAG, "Sample rate, channel config or format not supported!");
			return;
		}
		init();
		mShouldRun = true;
		boolean oldShouldRecord = false;
		
		int bytesPerSecond = audioRecord.getSampleRate() * mapFormat(audioRecord.getAudioFormat()) / 8 * audioRecord.getChannelCount();
		mDuration = 0.0;
		while (mShouldRun) {
			if(mShouldRecord != oldShouldRecord){
				if(mShouldRecord){
					audioRecord.startRecording();
				}else{
					audioRecord.stop();
				}
				oldShouldRecord = mShouldRecord;
			}
			
			if(mShouldRecord){
				int readSize = audioRecord.read(mPCMBuffer, 0, bufsize);
				if (readSize > 0) {
					double read_ms = (1000.0 * readSize * 2) / bytesPerSecond; 
					mDuration += read_ms;
					
					if(audioRecord.getChannelCount()==1){
						mEncodeThread.addTask(mPCMBuffer, readSize);
					}else if(audioRecord.getChannelCount() == 2){
						short[] leftData = new short[readSize / 2];
						short[] rightData = new short[readSize / 2];
						for(int i = 0;i< readSize /2; i = i + 2){
							leftData[i] = mPCMBuffer[2 * i];
							if( 2 * i + 1 < readSize){
								leftData[i+1] = mPCMBuffer[2 * i + 1];
							}
							if(2 * i + 2 < readSize){
								rightData[i] = mPCMBuffer[2 * i + 2];
							}
							if(2 * i + 3 < readSize){
								rightData[i + 1] = mPCMBuffer[2 * i + 3];
							}
						}
						mEncodeThread.addTask(leftData,rightData, readSize / 2);
					}
				}
			}
		}
	}
	
	
	public boolean isRecording(){
		return mShouldRecord;
	}
	
	private void init() {
		int bytesPerFrame = audioRecord.getAudioFormat() == AudioFormat.ENCODING_PCM_16BIT ? 2
				: 1;
		int frameSize = bufsize / bytesPerFrame;
		if (frameSize % FRAME_COUNT != 0) {
			frameSize += (FRAME_COUNT - frameSize % FRAME_COUNT);
			bufsize = frameSize * bytesPerFrame;
		}
		mPCMBuffer = new short[bufsize];
		/*
		 * Initialize lame buffer
		 * mp3 sampling rate is the same as the recorded pcm sampling rate 
		 * The bit rate is 128kbps
		 */
		LameUtil.init(audioRecord.getSampleRate(), audioRecord.getChannelCount(), audioRecord.getSampleRate(), DEFAULT_LAME_MP3_BIT_RATE, DEFAULT_LAME_MP3_QUALITY);
		// Create and run thread used to encode data
		// The thread will 
		try {
			if(!outputFile.exists()){
				outputFile.createNewFile();
			}
			mEncodeThread = new DataEncodeThread(outputFile, bufsize);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		mEncodeThread.start();
		audioRecord.setRecordPositionUpdateListener(mEncodeThread, mEncodeThread.getHandler());
		audioRecord.setPositionNotificationPeriod(FRAME_COUNT);
	}
	
	/**
	 * get the available AudioRecord
	 * @return 
	 */
	private boolean isFound(){
		boolean isFound = false;
		
		int sample_rate = -1; 
	    int channel_config = -1; 
	    int format = -1; 

	    for(int x=0;!isFound &&x<formats.length;x++){
	    	format = formats[x];
	    	for(int y=0;!isFound && y<sampleRates.length;y++){
	    		sample_rate = sampleRates[y];
	    		 for (int z = 0 ; !isFound && z < configs.length ; z++) {
	    			 channel_config = configs[z];
	    			 
	    			 Log.i(TAG, "Trying to create AudioRecord use: " + format + "/" + channel_config + "/" + sample_rate);
	    			 bufsize = AudioRecord.getMinBufferSize(sample_rate, channel_config, format);
	    			 Log.i(TAG, "Bufsize: " + bufsize);
	    			 if (AudioRecord.ERROR_BAD_VALUE == bufsize) { 
	    				 Log.i(TAG, "invaild params!");
			            continue; 
			         } 
	    			 if(AudioRecord.ERROR == bufsize){
	    				 Log.i(TAG, "Unable to query hardware!");
	    				 continue;
	    			 }
	    			 
	    			 try {
						audioRecord = new AudioRecord(
								MediaRecorder.AudioSource.MIC, sample_rate,
								channel_config, format, bufsize);
						int state = audioRecord.getState();
						if (state != AudioRecord.STATE_INITIALIZED) {
							continue;
						}
					} catch (IllegalStateException e) {
						Log.i(TAG, "Failed to set up recorder!");
						audioRecord = null;
						continue;
					}
	    			isFound = true;
	    			break;
	    		 }
	    	}
	    }
	    
		return isFound;
	}
}
