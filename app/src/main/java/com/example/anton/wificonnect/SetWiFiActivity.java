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
public class SetWiFiActivity extends Activity {

    private String mSaveSsid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.wifi_sett);

        try {
            String ssid = "";
            String passw = "";
            String mac = "";

            ssid = getIntent().getExtras().getString("ssid");
            passw = getIntent().getExtras().getString("pass");
            mac = getIntent().getExtras().getString("mac");

            mSaveSsid = new String(ssid);

            EditText t1 = (EditText)findViewById(R.id.editWiFi);
            t1.setText(ssid);
            EditText t2 = (EditText)findViewById(R.id.editPass);
            t2.setText(passw);
            EditText t3 = (EditText)findViewById(R.id.editMACaddr);
            t3.setText(mac);


            Log.d("0","ssid-> "+ssid+" passw->"+passw);

            // mWifiListSAVE =
            //mWifiListSAVE.putWiFi(ssid, passw);

        }
        catch (Exception e)
        {
            Log.d("e",e.toString());
        }


        Button b = (Button)findViewById(R.id.buttonConnect);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetWiFiActivity.this, MainActivity.class);

                EditText userEditText = (EditText)findViewById(R.id.editWiFi);
                EditText pass = (EditText)findViewById(R.id.editPass);

                intent.putExtra("ssidConn", userEditText.getText().toString());
                intent.putExtra("passConn", pass.getText().toString());
                intent.putExtra("mod", "conn");

                startActivity(intent);

            }
        });

        Button bdel = (Button)findViewById(R.id.buttonDele);

        bdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetWiFiActivity.this, MainActivity.class);

                EditText userEditText = (EditText)findViewById(R.id.editWiFi);
                EditText pass = (EditText)findViewById(R.id.editPass);

                intent.putExtra("ssidConn", userEditText.getText().toString());
                intent.putExtra("passConn", pass.getText().toString());
                intent.putExtra("mod", "del");

                startActivity(intent);

            }
        });

        Button ext = (Button)findViewById(R.id.buttonBack);

        ext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetWiFiActivity.this, MainActivity.class);

                EditText userEditText = (EditText)findViewById(R.id.editWiFi);
                EditText pass = (EditText)findViewById(R.id.editPass);

                intent.putExtra("ssidConn", userEditText.getText().toString());
                intent.putExtra("passConn", pass.getText().toString());
                intent.putExtra("mod", "edit");
                intent.putExtra("wifiid", mSaveSsid);

                startActivity(intent);

            }
        });

    }

}
