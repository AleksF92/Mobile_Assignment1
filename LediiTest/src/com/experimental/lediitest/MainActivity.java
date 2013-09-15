package com.experimental.lediitest;

import java.util.Random;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1) public class MainActivity extends Activity {
	
	//Global variables used by many functions
	public int clicks = -1;
	public int low = -1;
	public int high = -1;
	public MySQLiteHelper dbHelper;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Initialize the database class
        dbHelper = new MySQLiteHelper(this);
        
        //Load statistics from the database
        int[] dbResults = dbHelper.getStats();
        
        //Set values to match database
        clicks = dbResults[0];
        low = dbResults[1];
        high = dbResults[2];
        
        //Update the interface
    	updateStatistics();
    }
    
    protected void onStop() {
    	super.onStop();
    	
    	//Save current statistics to database
    	dbHelper.updateStats(clicks, low, high);
    }
    
    protected void onPause() {
    	super.onPause();
    	
    	//Save current statistics to database
    	dbHelper.updateStats(clicks, low, high);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void onClearStats(View view) {
    	//Get refrence to all views
    	TextView txt_Record = (TextView) findViewById(R.id.txt_Record);
    	TextView txt_Lowest = (TextView) findViewById(R.id.txt_Lowest);
    	TextView txt_Highest = (TextView) findViewById(R.id.txt_Highest);
    	
    	//Reset data
    	clicks = -1;
    	low = -1;
    	high = -1;
    	
    	//Save current statistics to database
    	dbHelper.updateStats(clicks, low, high);
    	
    	//Clear any possible states
    	txt_Record.setVisibility(4);
    	txt_Highest.setTextColor(Color.RED);
    	txt_Lowest.setTextColor(Color.RED);
    	
    	
    	//Update the interface
    	updateStatistics();
    }
    
    public void updateStatistics() {
    	//Get refrence to all views
    	TextView txt_Clicks = (TextView) findViewById(R.id.txt_Clicks);
    	TextView txt_Lowest = (TextView) findViewById(R.id.txt_Lowest);
    	TextView txt_Highest = (TextView) findViewById(R.id.txt_Highest);
    	
    	if (clicks == -1) {
    		//No clicks, no data expected
    		txt_Clicks.setText("Clicks: ?");
    		txt_Lowest.setText("Lowest: ?");
    		txt_Highest.setText("Highest: ?");
    	} else {
    		//Clicks found, set to loaded data
    		txt_Clicks.setText("Clicks: " + clicks);
    		txt_Lowest.setText("Lowest: " + low + "%");
    		txt_Highest.setText("Highest: " + high + "%");
    	}
    }
    
    public void setRandomStats() {
    	//Get refrence to all views
    	ImageView bar_Health = (ImageView) findViewById(R.id.img_BarStat1);
    	ImageView bar_Speed = (ImageView) findViewById(R.id.img_BarStat2);
    	ImageView bar_Attack = (ImageView) findViewById(R.id.img_BarStat3);
    	ImageView bar_Ability = (ImageView) findViewById(R.id.img_BarStat4);
    	ImageView bar_Overall = (ImageView) findViewById(R.id.img_BarOverall);
    	ImageView bar_Gray = (ImageView) findViewById(R.id.img_BarGray1);
    	TextView txt_Record = (TextView) findViewById(R.id.txt_Record);
    	TextView txt_Lowest = (TextView) findViewById(R.id.txt_Lowest);
    	TextView txt_Highest = (TextView) findViewById(R.id.txt_Highest);
    	TextView txt_Percent1 = (TextView) findViewById(R.id.txt_Stat1Percent);
    	TextView txt_Percent2 = (TextView) findViewById(R.id.txt_Stat2Percent);
    	TextView txt_Percent3 = (TextView) findViewById(R.id.txt_Stat3Percent);
    	TextView txt_Percent4 = (TextView) findViewById(R.id.txt_Stat4Percent);
    	TextView txt_Percent5 = (TextView) findViewById(R.id.txt_OverallPercent);
    	
    	//Generate random numbers
    	Random dice = new Random();
    	int roll1 = dice.nextInt(bar_Gray.getWidth() + 1);
    	int roll2 = dice.nextInt(bar_Gray.getWidth() + 1);
    	int roll3 = dice.nextInt(bar_Gray.getWidth() + 1);
    	int roll4 = dice.nextInt(bar_Gray.getWidth() + 1);
    	int roll5 = (roll1 + roll2 + roll3 + roll4) / 4;
    	
    	//Calculate percentages
    	int score1 = (roll1 * 100) / bar_Gray.getWidth();
    	int score2 = (roll2 * 100) / bar_Gray.getWidth();
    	int score3 = (roll3 * 100) / bar_Gray.getWidth();
    	int score4 = (roll4 * 100) / bar_Gray.getWidth();
    	int score5 = (roll5 * 100) / bar_Gray.getWidth();
    	
    	//Request to change layout
    	bar_Health.requestLayout();
    	bar_Speed.requestLayout();
    	bar_Attack.requestLayout();
    	bar_Ability.requestLayout();
    	bar_Overall.requestLayout();
    	
    	//Change image colors based on percentages
    	if (score1 >= 75) {
    		bar_Health.setImageResource(R.drawable.bar_green);
    	} else if (score1 >= 50) {
    		bar_Health.setImageResource(R.drawable.bar_yellow);
    	} else if (score1 >= 25) {
    		bar_Health.setImageResource(R.drawable.bar_orange);
    	} else {
    		bar_Health.setImageResource(R.drawable.bar_red);
    	}
    	
    	if (score2 >= 75) {
    		bar_Speed.setImageResource(R.drawable.bar_green);
    	} else if (score2 >= 50) {
    		bar_Speed.setImageResource(R.drawable.bar_yellow);
    	} else if (score2 >= 25) {
    		bar_Speed.setImageResource(R.drawable.bar_orange);
    	} else {
    		bar_Speed.setImageResource(R.drawable.bar_red);
    	}
    	
    	if (score3 >= 75) {
    		bar_Attack.setImageResource(R.drawable.bar_green);
    	} else if (score3 >= 50) {
    		bar_Attack.setImageResource(R.drawable.bar_yellow);
    	} else if (score3 >= 25) {
    		bar_Attack.setImageResource(R.drawable.bar_orange);
    	} else {
    		bar_Attack.setImageResource(R.drawable.bar_red);
    	}
    	
    	if (score4 >= 75) {
    		bar_Ability.setImageResource(R.drawable.bar_green);
    	} else if (score4 >= 50) {
    		bar_Ability.setImageResource(R.drawable.bar_yellow);
    	} else if (score4 >= 25) {
    		bar_Ability.setImageResource(R.drawable.bar_orange);
    	} else {
    		bar_Ability.setImageResource(R.drawable.bar_red);
    	}
    	
    	if (score5 >= 75) {
    		bar_Overall.setImageResource(R.drawable.bar_green);
    	} else if (score5 >= 50) {
    		bar_Overall.setImageResource(R.drawable.bar_yellow);
    	} else if (score5 >= 25) {
    		bar_Overall.setImageResource(R.drawable.bar_orange);
    	} else {
    		bar_Overall.setImageResource(R.drawable.bar_red);
    	}
    	
    	//Set image widths
    	bar_Health.getLayoutParams().width = roll1;
    	bar_Speed.getLayoutParams().width = roll2;
    	bar_Attack.getLayoutParams().width = roll3;
    	bar_Ability.getLayoutParams().width = roll4;
    	bar_Overall.getLayoutParams().width = roll5;
    	
    	//Update statistics
    	txt_Percent1.setText(score1 + "%");
    	txt_Percent2.setText(score2 + "%");
    	txt_Percent3.setText(score3 + "%");
    	txt_Percent4.setText(score4 + "%");
    	txt_Percent5.setText(score5 + "%");
    	
    	//Clear any possible states
    	txt_Record.setVisibility(4);
    	txt_Highest.setTextColor(Color.RED);
    	txt_Lowest.setTextColor(Color.RED);
    	
    	//Make sure first result always is better
    	if (clicks == -1) {
    		clicks = 0;
    		low = 101;
    	}
    	
    	//Check if new result is better
    	clicks += 1;
    	if (score5 < low) {
    		low = score5;
    		txt_Record.setVisibility(0);
    		txt_Lowest.setTextColor(Color.WHITE);
    	}
    	if (score5 > high) {
    		high = score5;
    		txt_Record.setVisibility(0);
    		txt_Highest.setTextColor(Color.WHITE);
    	}
    	
    	//Update the interface
    	updateStatistics();
    }
    
    public void onRandomize(View view) {
    	setRandomStats();
    }
    
    public void onSwapToInternet(View view) {
    	//Open new intent and go to InternetActivity
    	Intent myIntent = new Intent(view.getContext(), com.experimental.lediitest.InternetActivity.class);
    	startActivityForResult(myIntent, 0);
    }
    
}