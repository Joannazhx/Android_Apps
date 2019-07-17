package com.example.jeremy.lab9;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Task1Activity extends AppCompatActivity {
    Button btnSave;
    TextView textView;
    EditText editText;
    Context context ;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_task1 );
        textView = findViewById( R.id.txtOutput );
        textView.setText( "" );
        editText = findViewById( R.id.editText );
        btnSave = findViewById( R.id.btnSave );

        final String dateTimeKey = "com.example.app.datetime";

        sharedPref = this.getSharedPreferences(dateTimeKey, Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        btnSave.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = String.valueOf( editText.getText() );
//                Log.e("text", string);
                editor.putString( dateTimeKey, input);
                editor.commit();

                editText.setText("");
                String output = sharedPref.getString(dateTimeKey, "foo");
                textView.setText( output );
            }
        } );

        try {
            String string = sharedPref.getString(dateTimeKey, "foo");
            textView.setText( string );
        } catch(Exception e) {

        }

    }
}