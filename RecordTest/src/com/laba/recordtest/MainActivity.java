package com.laba.recordtest;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.laba.recordtest.Mp3Recorder.State;

public class MainActivity extends Activity implements OnClickListener{
	private static final String TAG = "Record";
    private static final String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/compressed.mp3"; 
    
	private Button btnRecordStart;
	private Button btnRecordPause;
	private Button btnRecordStop;
	private Button btnPlay;

	private Mp3Recorder mp3Recorder = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		btnRecordStart = $(R.id.btn_record_start);
		btnRecordPause = $(R.id.btn_record_pause);
		btnRecordStop = $(R.id.btn_record_stop);
		btnPlay = $(R.id.btn_play);
		btnPlay.setOnClickListener(this);
		btnRecordStart.setOnClickListener(this);
		btnRecordPause.setOnClickListener(this);
		btnRecordStop.setOnClickListener(this);
		
		mp3Recorder = new Mp3Recorder();
		mp3Recorder.setmMaxDuration(60);//60s
		mp3Recorder.setOutputFile(fileName);
		mp3Recorder.setOnMaxDurationReachedListener(new OnMaxDurationReached() {
			
			@Override
			public void onMaxDurationReached() {
				Toast.makeText(MainActivity.this, "最长录音1分钟", Toast.LENGTH_LONG).show();
				mp3Recorder.stop();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public <T extends View> T $(int id){
		return (T)findViewById(id);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_play:
			File file = new File(fileName);
			if(file.exists()){
				playRecord();
			}else{
				Toast.makeText(this, "请先录音", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btn_record_start:
			mp3Recorder.prepare();
			mp3Recorder.startRecording();
			
			Log.e(TAG, "record started");
			Toast.makeText(this, "录音开始", Toast.LENGTH_SHORT).show();
			btnRecordStart.setEnabled(false);
			break;
		case R.id.btn_record_pause:
			if(mp3Recorder.getRecorderState() == State.PAUSED){
				mp3Recorder.resume();
				btnRecordPause.setText("暂停录音");
				Toast.makeText(this, "已恢复", Toast.LENGTH_SHORT).show();
				Log.e(TAG, "record started");
			}else if(mp3Recorder.getRecorderState() == State.RECORDING){
				mp3Recorder.pause();
				Toast.makeText(this, "已暂停", Toast.LENGTH_SHORT).show();
				btnRecordPause.setText("恢复录音");
				Log.e(TAG, "record paused");
			}
			
			break;
		case R.id.btn_record_stop:
			mp3Recorder.stop();
			Log.e(TAG, "record stoped");
			Toast.makeText(this, "已停止录音", Toast.LENGTH_SHORT).show();
			btnRecordStart.setEnabled(true);
			break;
		default:
			break;
		}
	}

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void playRecord() {
		File file = new File(fileName);
		if (file.exists()) {
			MediaPlayer mp=new MediaPlayer();
			try {
				mp.setDataSource(fileName); // 设置数据源
				mp.prepare();
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
			mp.start();
		}
	}
}
