    package com.example.joanna.lab1;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {

    TextView txt;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    Button clickMeBtn = (Button) findViewById(R.id.button);
    txt = (TextView) findViewById(R.id.textView);
        clickMeBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                myClick(v);
            }
        });
    }



    public void myClick(View v){
        //
        count++;
        txt.setText(String.valueOf(count));
    }
}
