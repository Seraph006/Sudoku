package com.sudoku006;

import android.content.Context;
import android.text.TextPaint;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Card extends FrameLayout{
	
	private TextView label;
	private int num = 0;

	public Card(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		label = new TextView(getContext());
		label.setTextSize(16);;
		label.setGravity(Gravity.CENTER);
		label.setSingleLine();
		TextPaint tp = label.getPaint(); 
		tp.setFakeBoldText(true);
		LayoutParams lp = new LayoutParams(-1,-1);
		lp.setMargins(1,1,1,1);
		addView(label,lp);
	}
	
	public int getNum(){
		return num;
	}

	public void setNum(int num) {
		// TODO Auto-generated method stub
		this.num = num;
		if(num==0){
			label.setText("");
		}else{
		label.setText(num+"");}
	}
	
	public TextView getLabel(){
		return label;
	}

}
