package com.example.anton.wificonnect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Anton on 06.06.2016.
 */

public class SharingActivity  extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.shar_layout);

        final String ipserver = "192.168.1.156";

        Runnable runnable = new Runnable() {
            public void run() {
                TextView textStatus = (TextView)findViewById(R.id.textStatus);
            URL url;
            HttpURLConnection conn;
            BufferedReader rd;
            String line;
            String result = "";
            try {
                url = new URL("http://"+ipserver+":3000/api");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                try {

                    int status = conn.getResponseCode();

                    InputStreamReader input;

                    if(status >= HttpStatus.SC_BAD_REQUEST) {
                        input = new InputStreamReader(conn.getErrorStream());
                        Log.d("666","что то не так");
                    }
                    else
                        input = new InputStreamReader(conn.getInputStream());


                    rd =new BufferedReader(input);
                    while ((line = rd.readLine()) != null) {
                        Log.d("666","read");
                        result += line;
                    }
                    rd.close();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } finally {
                    conn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

                Message msg = handler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("Key", result);
                msg.setData(bundle);
                handler.sendMessage(msg);

        Log.d("sdcsdcs",result);

            }
        };
        Thread thread = new Thread(runnable);
        thread.start();



        Button getshar = (Button)findViewById(R.id.buttonSendQuery);
        getshar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String  res=((EditText)findViewById(R.id.editSendQuery)).getText().toString();
                Runnable runnable = new Runnable() {
                    public void run() {
                        TextView textStatus = (TextView)findViewById(R.id.textStatus);
                        URL url;
                        HttpURLConnection conn;
                        BufferedReader rd;
                        String line;
                        String result = "";
                       Log.d("88888",res);

                        try {
                            url = new URL("http://"+ipserver+":3000/api/wifi/"+res);
                            conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            try {
                                int status = conn.getResponseCode();
                                InputStreamReader input;
                                if(status >= HttpStatus.SC_BAD_REQUEST) {
                                    //input = new InputStreamReader(conn.getErrorStream());
                                    Message msg = handler.obtainMessage();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("Key", "Error");
                                    msg.setData(bundle);
                                    handler.sendMessage(msg);
                                    return;
                                }
                                else
                                    input = new InputStreamReader(conn.getInputStream());

                                rd =new BufferedReader(input);
                                while ((line = rd.readLine()) != null) {
                                    result += line;
                                }
                                rd.close();

                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } finally {
                                conn.disconnect();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }



                        Message msg = getResStr.obtainMessage();
                        Bundle bundle = new Bundle();
                        bundle.putString("Key", result);
                        msg.setData(bundle);
                        getResStr.sendMessage(msg);

                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();
            }
        });


//        Log.d("0","-> " + getIntent().getExtras().getStringArrayList("val").get(0));

        Button send = (Button)findViewById(R.id.buttonSend);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayList<String> arr = getIntent().getExtras().getStringArrayList("val");
                Random r = new Random();
                final int id = r.nextInt(1000)+5;
                Runnable runnable = new Runnable() {
                    public void run() {

                        URL url;
                        HttpURLConnection conn;
                        BufferedReader rd;
                        String line;
                        String result = "";

                        for (int i=0;i<arr.size();i++) {
                            result = "";
                            try {
                                url = new URL("http://"+ipserver+":3000/api/wifi/"+id+"?"+arr.get(i));
                                conn = (HttpURLConnection) url.openConnection();
                                conn.setRequestMethod("POST");
                                try {
                                    int status = conn.getResponseCode();
                                    InputStreamReader input;
                                    if (status >= HttpStatus.SC_BAD_REQUEST) {
                                        input = new InputStreamReader(conn.getErrorStream());
                                    } else
                                        input = new InputStreamReader(conn.getInputStream());

                                    rd = new BufferedReader(input);
                                    while ((line = rd.readLine()) != null) {
                                        result += line;
                                    }
                                    rd.close();

                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } finally {
                                    conn.disconnect();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }




                        }
                        Message msg = getPostStr.obtainMessage();
                        Bundle bundle = new Bundle();
                        bundle.putString("Key", ""+id);
                        msg.setData(bundle);
                        getPostStr.sendMessage(msg);
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();
            }
        });


        Button ext = (Button)findViewById(R.id.buttonSharExit);
        ext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SharingActivity.this, MainActivity.class);

                intent.putExtra("getwifi", msgg);

                startActivity(intent);
            }
        });

        EditText edi = (EditText)findViewById(R.id.editSendQuery);
        edi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edi1 = (EditText)findViewById(R.id.editSendQuery);
                edi1.setText("");
            }
        });

    }

    private String msgg="";

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String s = bundle.getString("Key");
            TextView textStatus = (TextView)findViewById(R.id.textStatus);
            textStatus.setText(s);
        }
    };

    Handler getResStr = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String s = bundle.getString("Key");
            msgg = s;
        }
    };

    Handler getPostStr = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String s = bundle.getString("Key");
            TextView textStatus = (TextView)findViewById(R.id.editGetShared);
            textStatus.setText(s);
        }
    };

    private static String read(InputStream instream) {
        StringBuilder sb = null;
        try {
            sb = new StringBuilder();
            BufferedReader r = new BufferedReader(new InputStreamReader(
                    instream));
            for (String line = r.readLine(); line != null; line = r.readLine()) {
                sb.append(line);
            }

            instream.close();

        } catch (IOException e) {
        }
        return sb.toString();

    }
}