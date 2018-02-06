package com.sudoku006;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridLayout;

public class GameView extends GridLayout implements onClickListener{
	
	private List<Point> emptyPoints = new ArrayList<Point>();
	private static int MAX_CALL_RANDOM_ARRAY_TIMES = 233;
	private Card[][] cardsMap = new Card[9][9];
	private int currentTimes = 0;
	private int colorSymbol = 0;
	
	public GameView(Context context) {
		super(context);
		initGameView();
		// TODO Auto-generated constructor stub
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initGameView();
	}

	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initGameView();
	}
	
	private void initGameView() {
		// TODO Auto-generated method stub
		MainActivity.getMainActivity().setOnClickListener(this);
		setColumnCount(9);
		setBackgroundColor(0xffbbada0);
	}
	
	protected void onSizeChanged(int w,int h,int oldw,int oldh)
    {
    	super.onSizeChanged(w, h, oldw, oldh);
    	
    	int cardWidth = (Math.min(w,h))/9;
    	
    	addCards(cardWidth,cardWidth);
    	 	
    	startGame();
    }
	
    private void addCards(int cardWidth,int cardHeight){
    	
    	Card c;
    	
    	for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 9; x++) {
				c = new Card(getContext());
				addView(c, cardWidth, cardHeight);
				cardsMap[x][y] = c;	
				cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#999999"));
			}
		}
    	
    }
    
    @Override
    public void startGame() {
    	
    	initNumber();
    	
    	listenFresh();
    	
    	initM();
    	
        initTextColor();
    	
    	cutM();
    	
    	chooseNumber();
    	
    }
    
    public void initM(){
	        for (int row = 0; row < 9; row++) {  
	            if (row == 0) {  
	                currentTimes = 0;  
	                int[] randomArray = buildRandomArray();  
	                for (int i = 0; i < 9; i++) {
						cardsMap[0][i].setNum(randomArray[i]);
					}
	            } else {  
	                int[] randomArray = buildRandomArray();
	                for (int col = 0; col < 9; col++) {  
	                    if (currentTimes < MAX_CALL_RANDOM_ARRAY_TIMES) {  
	                        if (!isCandidateNmbFound(cardsMap, randomArray, row, col)) {
	                            for (int i = 0; i < 9; i++) {
									cardsMap[row][i].setNum(0);
								} 
	                            row -= 1;  
	                            col = 8;
	                        }  
	                    } else {
	                        row = -1;  
	                        col = 8;  
	                        initNumber();
	                    }  
	                }  
	            }  
	        }
    }
    
    private boolean isCandidateNmbFound(Card[][] cardMap, int[] randomArray, int row, int col) {  
        for (int i = 0; i < randomArray.length; i++) {
        	cardMap[row][col].setNum(randomArray[i]); 
            if (isSame(row,col)) {  
                return true;  
            }  
        }  
        return false;  
    }
    
    public int[] buildRandomArray(){
    	currentTimes++;
    	int[] randomArray = new int[9];
    	for (int i = 0; i < 9; i++) {
    		randomArray[i] = i + 1;
		}
    	Random random = new Random();
    	for (int i = 0; i < 100; i++) {
			int r1 = random.nextInt(9);
			int r2 = random.nextInt(9);
			int temp = randomArray[r1];
			randomArray[r1] = randomArray[r2];
			randomArray[r2] = temp;
		}
    	return randomArray;
    }
    
    public void initNumber(){
    	for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 9; x++) {
				cardsMap[x][y].setNum(0);
			}
		}
    }
    
    public void listenFresh(){
    	for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 9; x++) {
				cardsMap[x][y].setOnClickListener(null);
			}
		}
    }
    
    public boolean isSame(int x,int y){
    	boolean iS = true;
    	for (int yy = y,xx = 0; xx <9 ; xx++) {
			if (xx==x) {
				continue;
			}else{
				if (cardsMap[xx][yy].getNum()==cardsMap[x][yy].getNum()) {
					iS = false;break;
				}
			}
		}
    	if(iS){
    		for (int yy = 0,xx = x; yy <9 ; yy++) {
    			if (yy==y) {
    				continue;
    			}else{
    				if (cardsMap[xx][yy].getNum()==cardsMap[xx][y].getNum()) {
    					iS = false;break;
    				}
    			}
    		}
    	}
    	if(iS){
    	F:for (int yy = (y/3)*3; yy < (y/3)*3+3; yy++) {
			for (int xx = (x/3)*3; xx < (x/3)*3+3; xx++) {
				if ((xx==x)&&(yy==y)) {
					continue;
				}else{
					if (cardsMap[xx][yy].getNum()==cardsMap[x][y].getNum()) {
						iS = false;break F;
					}
				}
			}
		}
    	}
    	return iS;
    }
    
    public void initTextColor(){
    	for (int i = 0; i < 9; i++) {
			for (int j = 0; j <9; j++) {
				cardsMap[i][j].getLabel().setTextColor(Color.BLACK);
			}
		}
    }
    
    public void cutM(){
    	for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 9; x++) {
				emptyPoints.add(new Point(x,y));
			}
		}
    	int initCount = 81;
    	int cutCount = new Random().nextInt(81)+1;
    	while (cutCount!=0) {
    		Point p = emptyPoints.remove(new Random().nextInt(initCount));
    		cardsMap[p.x][p.y].setNum(0);
    		cardsMap[p.x][p.y].getLabel().setTextColor(Color.WHITE);
    		initCount--;
			cutCount--;
		}
    	emptyPoints.clear();
    }
    
    public void chooseNumber(){
    	for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 9; x++) {
				final int xx = x;
				final int yy = y;
				if (cardsMap[xx][yy].getNum()==0) {
					cardsMap[xx][yy].setOnClickListener(new OnClickListener() {				
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							cardsMap[xx][yy].setNum(cardsMap[xx][yy].getNum()+1);
							if (cardsMap[xx][yy].getNum()==10){cardsMap[xx][yy].setNum(0);
						  }
						}
					});
				}
			}
		}
    }

	@Override
	public void changeColor() {
		// TODO Auto-generated method stub
		colorSymbol++;if(colorSymbol==10){colorSymbol=0;}
		switch (colorSymbol){
		case 1:
			for (int y = 0; y < 9; y++) {
				for (int x = 0; x < 9; x++) {
					if(x>=0&&x<=2&&y>=0&&y<=2){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#999999"));
					}
					if(x>=3&&x<=5&&y>=0&&y<=2){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#8B5A00"));	
					}
					if(x>=6&&x<=8&&y>=0&&y<=2){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.CYAN);
					}
					if(x>=6&&x<=8&&y>=3&&y<=5){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#FF6600"));
					}
					if(x>=6&&x<=8&&y>=6&&y<=8){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.BLUE);	
					}
					if(x>=3&&x<=5&&y>=6&&y<=8){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#CC3399"));	
					}
					if(x>=0&&x<=2&&y>=6&&y<=8){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.RED);
					}
					if(x>=0&&x<=2&&y>=3&&y<=5){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.YELLOW);
					}
					if(x>=3&&x<=5&&y>=3&&y<=5){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.GREEN);
					}
				}
			}
		break;
		case 2:
			for (int y = 0; y < 9; y++) {
				for (int x = 0; x < 9; x++) {
					if(x>=0&&x<=2&&y>=0&&y<=2){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.YELLOW);
					}
					if(x>=3&&x<=5&&y>=0&&y<=2){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#999999"));	
					}
					if(x>=6&&x<=8&&y>=0&&y<=2){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#8B5A00"));
					}
					if(x>=6&&x<=8&&y>=3&&y<=5){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.CYAN);
					}
					if(x>=6&&x<=8&&y>=6&&y<=8){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#FF6600"));	
					}
					if(x>=3&&x<=5&&y>=6&&y<=8){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.BLUE);	
					}
					if(x>=0&&x<=2&&y>=6&&y<=8){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#CC3399"));
					}
					if(x>=0&&x<=2&&y>=3&&y<=5){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.RED);
					}
					if(x>=3&&x<=5&&y>=3&&y<=5){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.GREEN);
					}
				}
			}
		break;
		case 3:
		for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 9; x++) {
				if(x>=0&&x<=2&&y>=0&&y<=2){
					cardsMap[x][y].getLabel().setBackgroundColor(Color.RED);
				}
				if(x>=3&&x<=5&&y>=0&&y<=2){
					cardsMap[x][y].getLabel().setBackgroundColor(Color.YELLOW);	
				}
				if(x>=6&&x<=8&&y>=0&&y<=2){
					cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#999999"));
				}
				if(x>=6&&x<=8&&y>=3&&y<=5){
					cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#8B5A00"));
				}
				if(x>=6&&x<=8&&y>=6&&y<=8){
					cardsMap[x][y].getLabel().setBackgroundColor(Color.CYAN);	
				}
				if(x>=3&&x<=5&&y>=6&&y<=8){
					cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#FF6600"));	
				}
				if(x>=0&&x<=2&&y>=6&&y<=8){
					cardsMap[x][y].getLabel().setBackgroundColor(Color.BLUE);
				}
				if(x>=0&&x<=2&&y>=3&&y<=5){
					cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#CC3399"));
				}
				if(x>=3&&x<=5&&y>=3&&y<=5){
					cardsMap[x][y].getLabel().setBackgroundColor(Color.GREEN);
				}
			}
		}
		break;
		case 4:
			for (int y = 0; y < 9; y++) {
				for (int x = 0; x < 9; x++) {
					if(x>=0&&x<=2&&y>=0&&y<=2){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#CC3399"));
					}
					if(x>=3&&x<=5&&y>=0&&y<=2){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.RED);	
					}
					if(x>=6&&x<=8&&y>=0&&y<=2){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.YELLOW);
					}
					if(x>=6&&x<=8&&y>=3&&y<=5){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#999999"));
					}
					if(x>=6&&x<=8&&y>=6&&y<=8){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#8B5A00"));	
					}
					if(x>=3&&x<=5&&y>=6&&y<=8){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.CYAN);	
					}
					if(x>=0&&x<=2&&y>=6&&y<=8){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#FF6600"));
					}
					if(x>=0&&x<=2&&y>=3&&y<=5){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.BLUE);
					}
					if(x>=3&&x<=5&&y>=3&&y<=5){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.GREEN);
					}
				}
			}
	    break;
		case 5:
			for (int y = 0; y < 9; y++) {
				for (int x = 0; x < 9; x++) {
					if(x>=0&&x<=2&&y>=0&&y<=2){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.BLUE);
					}
					if(x>=3&&x<=5&&y>=0&&y<=2){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#CC3399"));	
					}
					if(x>=6&&x<=8&&y>=0&&y<=2){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.RED);
					}
					if(x>=6&&x<=8&&y>=3&&y<=5){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.YELLOW);
					}
					if(x>=6&&x<=8&&y>=6&&y<=8){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#999999"));	
					}
					if(x>=3&&x<=5&&y>=6&&y<=8){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#8B5A00"));	
					}
					if(x>=0&&x<=2&&y>=6&&y<=8){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.CYAN);
					}
					if(x>=0&&x<=2&&y>=3&&y<=5){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#FF6600"));
					}
					if(x>=3&&x<=5&&y>=3&&y<=5){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.GREEN);
					}
				}
			}
		break;
		case 6:
			for (int y = 0; y < 9; y++) {
				for (int x = 0; x < 9; x++) {
					if(x>=0&&x<=2&&y>=0&&y<=2){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#FF6600"));
					}
					if(x>=3&&x<=5&&y>=0&&y<=2){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.BLUE);	
					}
					if(x>=6&&x<=8&&y>=0&&y<=2){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#CC3399"));
					}
					if(x>=6&&x<=8&&y>=3&&y<=5){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.RED);
					}
					if(x>=6&&x<=8&&y>=6&&y<=8){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.YELLOW);	
					}
					if(x>=3&&x<=5&&y>=6&&y<=8){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#999999"));	
					}
					if(x>=0&&x<=2&&y>=6&&y<=8){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#8B5A00"));
					}
					if(x>=0&&x<=2&&y>=3&&y<=5){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.CYAN);
					}
					if(x>=3&&x<=5&&y>=3&&y<=5){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.GREEN);
					}
				}
			}
		break;
		case 7:
			for (int y = 0; y < 9; y++) {
				for (int x = 0; x < 9; x++) {
					if(x>=0&&x<=2&&y>=0&&y<=2){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.CYAN);
					}
					if(x>=3&&x<=5&&y>=0&&y<=2){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#FF6600"));	
					}
					if(x>=6&&x<=8&&y>=0&&y<=2){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.BLUE);
					}
					if(x>=6&&x<=8&&y>=3&&y<=5){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#CC3399"));
					}
					if(x>=6&&x<=8&&y>=6&&y<=8){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.RED);	
					}
					if(x>=3&&x<=5&&y>=6&&y<=8){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.YELLOW);	
					}
					if(x>=0&&x<=2&&y>=6&&y<=8){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#999999"));
					}
					if(x>=0&&x<=2&&y>=3&&y<=5){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#8B5A00"));
					}
					if(x>=3&&x<=5&&y>=3&&y<=5){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.GREEN);
					}
				}
			}
		break;
		case 8:
			for (int y = 0; y < 9; y++) {
				for (int x = 0; x < 9; x++) {
					if(x>=0&&x<=2&&y>=0&&y<=2){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#8B5A00"));
					}
					if(x>=3&&x<=5&&y>=0&&y<=2){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.CYAN);	
					}
					if(x>=6&&x<=8&&y>=0&&y<=2){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#FF6600"));
					}
					if(x>=6&&x<=8&&y>=3&&y<=5){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.BLUE);
					}
					if(x>=6&&x<=8&&y>=6&&y<=8){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#CC3399"));	
					}
					if(x>=3&&x<=5&&y>=6&&y<=8){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.RED);	
					}
					if(x>=0&&x<=2&&y>=6&&y<=8){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.YELLOW);
					}
					if(x>=0&&x<=2&&y>=3&&y<=5){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#999999"));
					}
					if(x>=3&&x<=5&&y>=3&&y<=5){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.GREEN);
					}
				}
			}
		break;
		case 9:
			for (int y = 0; y < 9; y++) {
				for (int x = 0; x < 9; x++) {
					if(x>=0&&x<=2&&y>=0&&y<=2){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#696969"));
					}
					if(x>=6&&x<=8&&y>=0&&y<=2){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#696969"));
					}
					if(x>=3&&x<=5&&y>=3&&y<=5){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#696969"));
					}
					if(x>=0&&x<=2&&y>=6&&y<=8){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#696969"));
					}
					if(x>=6&&x<=8&&y>=6&&y<=8){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#696969"));	
					}
					if(x>=3&&x<=5&&y>=0&&y<=2){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#999999"));	
					}
					if(x>=6&&x<=8&&y>=3&&y<=5){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#999999"));	
					}
					if(x>=3&&x<=5&&y>=6&&y<=8){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#999999"));		
					}
					if(x>=0&&x<=2&&y>=3&&y<=5){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#999999"));	
					}
				}
			}
		break;
		case 0:
			for (int y = 0; y < 9; y++) {
				for (int x = 0; x < 9; x++) {
					if(x>=0&&x<=2&&y>=0&&y<=2){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#999999"));
					}
					if(x>=6&&x<=8&&y>=0&&y<=2){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#999999"));
					}
					if(x>=3&&x<=5&&y>=3&&y<=5){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#999999"));
					}
					if(x>=0&&x<=2&&y>=6&&y<=8){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#999999"));
					}
					if(x>=6&&x<=8&&y>=6&&y<=8){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#999999"));	
					}
					if(x>=3&&x<=5&&y>=0&&y<=2){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#999999"));	
					}
					if(x>=6&&x<=8&&y>=3&&y<=5){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#999999"));	
					}
					if(x>=3&&x<=5&&y>=6&&y<=8){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#999999"));		
					}
					if(x>=0&&x<=2&&y>=3&&y<=5){
						cardsMap[x][y].getLabel().setBackgroundColor(Color.parseColor("#999999"));	
					}
				}
			}
		break;
		}
	}
	
	@Override
	public void checkIfComlete(){
		boolean flag1 = true;
		boolean flag2 = true;
		boolean flag3 = true;
		boolean flag4 = false;
		A:for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 8; x++) {
				for (int z = x+1; z < 9; z++) {
					if (cardsMap[x][y].getNum()==cardsMap[z][y].getNum()) {
						flag1 = false;break A;
					}
				}
			}
		}
		B:for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 8; y++) {
				for (int z = y+1; z < 9; z++) {
					if (cardsMap[x][y].getNum()==cardsMap[x][z].getNum()) {
						flag2 = false;break B;
					}
				}
			}
		}
		C:for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 9; x++) {
					if (cardsMap[x][y].getNum()==0) {
						flag3 = false;break C;	
				}
			}
		}
		if (oneCfAddM(0,0)==45
		  &&oneCfAddM(3,0)==45
		  &&oneCfAddM(6,0)==45
		  &&oneCfAddM(0,3)==45
		  &&oneCfAddM(3,3)==45
		  &&oneCfAddM(6,3)==45
		  &&oneCfAddM(0,6)==45
		  &&oneCfAddM(3,6)==45
	      &&oneCfAddM(6,6)==45
	      &&twoCfAddM(0,0)==285
		  &&twoCfAddM(3,0)==285
		  &&twoCfAddM(6,0)==285
		  &&twoCfAddM(0,3)==285
		  &&twoCfAddM(3,3)==285
		  &&twoCfAddM(6,3)==285
		  &&twoCfAddM(0,6)==285
		  &&twoCfAddM(3,6)==285
	      &&twoCfAddM(6,6)==285
	      &&threeCfAddM(0,0)==2025
		  &&threeCfAddM(3,0)==2025
		  &&threeCfAddM(6,0)==2025
		  &&threeCfAddM(0,3)==2025
		  &&threeCfAddM(3,3)==2025
		  &&threeCfAddM(6,3)==2025
		  &&threeCfAddM(0,6)==2025
		  &&threeCfAddM(3,6)==2025
	      &&threeCfAddM(6,6)==2025){
			flag4 = true;
		}
		if (flag1&&flag2&flag3&flag4) {
			MainActivity.getMainActivity().getSCButton().setText("Continue");
			MainActivity.getMainActivity().setRestartFlag(false);
			MainActivity.getMainActivity().setScore(MainActivity.getMainActivity().getScore()+1);
			AlertDialog.Builder AB = new AlertDialog.Builder(getContext());
			AB.setTitle("Congratulations!!!");
			AB.setMessage("Record : " + MainActivity.getMainActivity().getScore());
			AB.setPositiveButton("Continue",new DialogInterface.OnClickListener() {	
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					MainActivity.getMainActivity().getSCButton().setText("Submit");
					MainActivity.getMainActivity().setRestartFlag(true);
					startGame();
				}
			});
			AB.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				}
			});
			AB.show();
		}
	}
	
	public int oneCfAddM(int x,int y){
		int oneCfAddResult = 0;
		for (int yy = y; yy < y+3; yy++) {
			for (int xx = x; xx < x+3; xx++) {
			  oneCfAddResult+=cardsMap[xx][yy].getNum();
			}
		}
		return oneCfAddResult;
	}
	
	public int twoCfAddM(int x,int y){
		int twoCfAddResult = 0;
		for (int yy = y; yy < y+3; yy++) {
			for (int xx = x; xx < x+3; xx++) {
			  twoCfAddResult+=cardsMap[xx][yy].getNum()*cardsMap[xx][yy].getNum();
			}
		}
		return twoCfAddResult;
	}
	
	public int threeCfAddM(int x,int y){
		int threeCfAddResult = 0;
		for (int yy = y; yy < y+3; yy++) {
			for (int xx = x; xx < x+3; xx++) {
			  threeCfAddResult+=cardsMap[xx][yy].getNum()*cardsMap[xx][yy].getNum()*cardsMap[xx][yy].getNum();
			}
		}
		return threeCfAddResult;
	}
  
}
