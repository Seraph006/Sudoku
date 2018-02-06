package com.sudoku006;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	
	public static final String key1 = "SDK2017192907094420rvmu4g8q5paek";
	public static final String key2 = "SDK20161629040641z7snyxkrbndasty";
	public static final String key3 = "SDK20141005101101zvhnqr5vyanz6uy";
	public static final String keySet[] = new String[] {key1, key2, key3};
	private Button Spin = null;
	private onClickListener listener = null;
	private static MainActivity mainActivity = null;
	private TextView Score2 = null;
	private int Score = 0;
	private Button SC = null;
	private boolean restartFlag = true;
	
	public void restartApplication() {  
        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());  
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
        startActivity(intent);  
    } 
	
	public MainActivity() {
		// TODO Auto-generated constructor stub
		this.mainActivity = this;
	}
	
	public static MainActivity getMainActivity() {
		return mainActivity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Score2 = (TextView) findViewById(R.id.Score2);
		SC = (Button)findViewById(R.id.SC);
		Spin=(Button) findViewById(R.id.Spin);
		SC.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(restartFlag){
					listener.checkIfComlete();		
					}else{
					SC.setText("Submit");
					setRestartFlag(true);
					listener.startGame();
					}
			}
		});
		Spin.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listener.changeColor();
			}
		});
		setScore(0);
	}
	
	public void setOnClickListener(onClickListener listener){
		this.listener = listener;
	}
	
	public int getScore(){
		return Score;
	}
	
	public void setScore(int score){
		this.Score = score;
		Score2.setText(score+"");
	}
	
	public Button getSCButton(){
		return SC;
	}
	
	public boolean getRestartFlag(){
		return restartFlag;
	}
	
	public void setRestartFlag(boolean bool){
		this.restartFlag = bool;
	}
	
}
