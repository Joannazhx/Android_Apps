package com.example.jeremy.lab9;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnTask1, btnTask2, btnTask3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        btnTask1 = findViewById( R.id.btnTask1 );
        btnTask1.setOnClickListener( this );
        btnTask2 = findViewById( R.id.btnTask2 );
        btnTask2.setOnClickListener( this );
        btnTask3 = findViewById( R.id.btnTask3 );
        btnTask3.setOnClickListener( this );
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btnTask1:
                intent = new Intent(this, Task1Activity.class);
                break;

            case R.id.btnTask2:
                intent = new Intent(this, Task2Activity.class);
                break;

            case R.id.btnTask3:
                intent = new Intent(this, Task3Activity.class);
                break;

            default:
                intent = new Intent(this, Task1Activity.class);
                break;
        }
        startActivity(intent);

    }
}