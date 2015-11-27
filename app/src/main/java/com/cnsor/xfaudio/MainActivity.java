package com.cnsor.xfaudio;


import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.sncor.xfaudio.util.JsonParser;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	private Button Start;  
	private Button Stop;  
	private SpeechRecognizer mlat;
	private SpeechSynthesizer mTts;
	private TextView result;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Start = (Button) this.findViewById(R.id.record);  
        Stop = (Button) this.findViewById(R.id.stopRecord);  
        result = (TextView)this.findViewById(R.id.textView2);
        Start.setOnClickListener(new audioListener());
        Stop.setOnClickListener(new audioListener());
        //初始化讯飞APPID
        SpeechUtility.createUtility(MainActivity.this, SpeechConstant.APPID +"=56516737");  
        initSpeechRecognizer();
        initSpeechSynthesizer(); //对识别语音后的文本进行语音合成  
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings || super.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	 /**
     * <p>标题: MainActivity</p>
     * <p>类描述: </p>
     * 作者:Administrator
     * <p>创建时间：2015年11月22日 上午11:27:56</p>
     */
    private void initSpeechRecognizer(){
    	mlat = SpeechRecognizer.createRecognizer(MainActivity.this, null);
    	mlat.setParameter(SpeechConstant.DOMAIN, "iat");
    	mlat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
    	//mlat.setParameter(SpeechConstant.ASR_PTT, "0");
    	mlat.setParameter(SpeechConstant.ACCENT, "mandarin");
    	//mlat.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
    }
	
    private void initSpeechSynthesizer(){
    	mTts = SpeechSynthesizer.createSynthesizer(MainActivity.this, null);
    	mTts.setParameter(SpeechConstant.VOICE_NAME, "vixx");//设置发音人  
    	mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速  
    	mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100  
    	mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端  
    	//设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”  
    	//保存在SD卡需要在AndroidManifest.xml添加写SD卡权限  
    	//如果不需要保存合成音频，注释该行代码  
    	//mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");    
    }
    
    private RecognizerListener mRecoListener = new RecognizerListener(){
		private StringBuffer resultTemp = new StringBuffer();
		@Override
		public void onBeginOfSpeech() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onEndOfSpeech() {
			// TODO Auto-generated method stub
			result.setText(resultTemp.toString());
			resultTemp.setLength(0);
			//3.开始合成 
			mTts.startSpeaking(result.getText().toString(), mSynListener);
		}

		@Override
		public void onError(SpeechError arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onResult(RecognizerResult arg0, boolean arg1) {
			// TODO Auto-generated method stub
			Log.d("语音识别结果为：", arg0.getResultString());
			resultTemp = new StringBuffer();
			resultTemp.append(JsonParser.parseIatResult(arg0.getResultString().toString()));


		}

		@Override
		public void onVolumeChanged(int arg0, byte[] arg1) {
			// TODO Auto-generated method stub
			
		}


    	   
	};
	
	private  SynthesizerListener mSynListener = new SynthesizerListener(){

		@Override
		public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onCompleted(SpeechError arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSpeakBegin() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSpeakPaused() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSpeakProgress(int arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSpeakResumed() {
			// TODO Auto-generated method stub
			
		}
		
	};
    
	class audioListener implements OnClickListener {   
         @Override  
         public void onClick(View v) {  
             if (v == Start) {  
             	if(mRecoListener == null){
             		System.out.println("创建mRecoListener对象错误");
             	}
             	// 开启音频文件写入线程  
                mlat.startListening(mRecoListener);
             	
             }  
             if (v == Stop) {  
                 mlat.stopListening();  
             }  
   
         }  
   
     }
}
