package com.example.jeremy.lab4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class  MainActivity extends AppCompatActivity {

    private Button task1,task2,task3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        task1 = findViewById(R.id.btn_1);
        task2 = findViewById(R.id.btn_2);
        task3 = findViewById(R.id.btn_3);

        task1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchTask1(view);
            }
        });

        task2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchTask2(view);
            }
        });

        task3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchTask3(view);
            }
        });
    }

    private void switchTask1(View view) {
        Intent intent = new Intent(this, Task1Activity.class);
        startActivity(intent);
    }

    private void switchTask2(View view) {
        Intent intent = new Intent(this, Task2Activity.class);
        startActivity(intent);
    }

    private void switchTask3(View view) {
        Intent intent = new Intent(this, Task3Activity.class);
        startActivity(intent);
    }


}
