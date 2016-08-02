package com.example.anton.wificonnect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Anton on 03.06.2016.
 */
public class WiFiActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Log.d("sdcsdc","11111111111111111111111111111111111eeeeeee");

        setContentView(R.layout.wifi_layout);

        Button b = (Button)findViewById(R.id.SAVE);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WiFiActivity.this, MainActivity.class);

                EditText userEditText = (EditText)findViewById(R.id.editText);
                EditText pass = (EditText)findViewById(R.id.editText2);

                intent.putExtra("ssid", userEditText.getText().toString());
                intent.putExtra("pass", pass.getText().toString());

                startActivity(intent);

            }
        });

    }

}
