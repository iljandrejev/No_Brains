package com.example.ilja.nobrains;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private static final String TAG  = "NoBrains";
    private String[] mathLine = new String[3];

    private boolean dotLogic = true;
    private boolean isOperatorSet = false;
    private boolean canSend = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            mathLine = savedInstanceState.getStringArray("mathLine");
            dotLogic = savedInstanceState.getBoolean("dotLogic");
            isOperatorSet = savedInstanceState.getBoolean("isOpertorSet");
            canSend = savedInstanceState.getBoolean("canSend");
        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView mathResult = (TextView) findViewById(R.id.mathLineResult);

        registerReceiver(receiver,new IntentFilter("com.example.Brains_Math_Response"));

    }



    public void getMathResult (View view){

        Intent intent = new Intent();
        intent.setAction("com.example.Nobrains_Math_Request");
        intent.putExtra("mathLine",mathLine);
        intent.putExtra("num1",mathLine[0]);
        intent.putExtra("num2",mathLine[2]);
        intent.putExtra("operator",mathLine[1]);
        sendBroadcast(intent);
    }
    
    private void updateView(){
        TextView mathLine = (TextView) findViewById(R.id.mathLine);
       String line = null;
        String firstNumber = this.mathLine[0];
        if(firstNumber != null){
            line = firstNumber;

            if(this.mathLine[1] != null){
                line = line + " " + this.mathLine[1];

                if(this.mathLine[2] != null){
                    line = line+ " " + this.mathLine[2];
                }

            }

        }

        mathLine.setText(line);
    }

    public void setNumber(View view){
        Button button = (Button) view;
        String value = button.getText().toString();
        Log.i("MathNumber","Value: "+ value);

        if(!isOperatorSet){
            if(value.equals("0")){
                if (getFirstNumber() == null){
                    mathLine[0] = "0";
                    Log.i("MathNumber","71FirstNum: " + mathLine[0]);
                }else if (getFirstNumber().equals("0")){
                    mathLine[0] = "0";
                    Log.i("MathNumber","74FirstNu2m: " + mathLine[0]);
                }else{
                    setFirstNumber(value);
                }

            }else{
                if(getFirstNumber()== null){
                    mathLine[0] = value;
                }else if(getFirstNumber().equals("0")){
                    mathLine[0] = value;
                    Log.i("MathNumber","82FirstNum: " + mathLine[0]);
                }else{
                    setFirstNumber(value);
                }
            }
        }else{
            if(value.equals("0")){
                if (getSecondNumber() == null){
                    mathLine[2] = "0";
                    Log.i("MathNumber","71SecondNum: " + mathLine[2]);
                }else if (getSecondNumber().equals("0")){
                    mathLine[2] = "0";
                    Log.i("MathNumber","74SecondNu2m: " + mathLine[2]);
                }else{
                    setSecondNumber(value);
                }

            }else{
                if(getSecondNumber()== null){
                    mathLine[2] = value;
                }else if(getSecondNumber().equals("0")){
                    mathLine[2] = value;
                    Log.i("MathNumber","82SecondNum: " + mathLine[2]);
                }else{
                    setSecondNumber(value);
                }
            }
            canSend=true;
        }

        

        updateView();
    }

    public void putDot( View view){
        Log.i("Dot","Puting dot action");
        if(dotLogic==true){
            if(!isOperatorSet){
                if(mathLine[0]!= null){
                    mathLine[0] = mathLine[0] + ".";
                }else{
                    mathLine[0]="0.";
                }

            }else{
                if(mathLine[2]!= null){
                    mathLine[2] = mathLine[2] + ".";
                }else{
                    mathLine[2]= "0.";
                }

            }

        }
        dotLogic = false;
        updateView();
    }

    public void setOperator(View view){
        Button button = (Button) view;
        String value = button.getText().toString();
        if(mathLine[2]== null || !isOperatorSet){
            mathLine[1]=value;
            Log.i("MathOperator","Operator: " + value);
            isOperatorSet = true;
            dotLogic = true;
        }
        updateView();
    }

    public void setFirstNumber(String number){
        mathLine[0] = mathLine[0] + number;
        Log.i("MathNumber","94FirstNum: " + mathLine[0]);
    }

    public String getFirstNumber(){
        return mathLine[0];
    }

    public void setSecondNumber(String number){
        mathLine[2] = mathLine[2] + number;
        Log.i("MathNumber","155SecondNum: " + mathLine[2]);
    }

    public String getSecondNumber(){
        return mathLine[2];
    }

    public void cleanMathLine(View view){
        mathLine[0]="0";
        mathLine[1]=null;
        mathLine[2]=null;
        dotLogic=true;

        isOperatorSet= false;
        canSend=false;
        TextView result = (TextView)findViewById(R.id.mathLineResult);
        result.setText("");
        updateView();
    }


    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            TextView mathResult = (TextView) findViewById(R.id.mathLineResult);
            Log.d(TAG,"Lauched nobrains receiver");
            Bundle extras = intent.getExtras();
            if(extras != null){

                String result = extras.getString("mathResult");

                Log.d(TAG,"Result response " + result);
                mathResult.setText(result);
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void notification(){
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_webapplogo)
                        .setContentTitle("Brains sent response")
                        .setContentText("Hello World!");
        int mNotificationId = 001;
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    protected void onSaveInstanceState(Bundle icicle) {
        super.onSaveInstanceState(icicle);
        icicle.putStringArray("mathLine", mathLine);
        icicle.putBoolean("dotLogic",dotLogic);
        icicle.putBoolean("isOperatorSet",isOperatorSet);
        icicle.putBoolean("canSend",canSend);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (BuildConfig.DEBUG) { Log.d(TAG, "onStart called"); }
        // The activity is about to become visible.
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (BuildConfig.DEBUG) { Log.d(TAG, "onResume called"); }
        // The activity has become visible (it is now "resumed").
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (BuildConfig.DEBUG) { Log.d(TAG, "onPause called"); }
        // Another activity is taking focus (this activity is about to be "paused").
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (BuildConfig.DEBUG) { Log.d(TAG, "onStop called"); }
        // The activity is no longer visible (it is now "stopped")
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (BuildConfig.DEBUG) { Log.d(TAG, "onDestroy called"); }
        // The activity is about to be destroyed.
    }
}
