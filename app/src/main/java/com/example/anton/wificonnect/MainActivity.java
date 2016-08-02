package com.example.anton.wificonnect;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {



    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static ReceiverModel mReceiver;
    private boolean inAdd;

    private static WiFiListModel mWifiListALL ;
    private static WiFiListModel mWifiListSAVE  = new WiFiListModel();
    private static WiFiListModel mWifiList = new WiFiListModel();
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private MainActivity getInstatse()
    {
        return  this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWifiListALL = new WiFiListModel();
        mReceiver = new ReceiverModel(this,mWifiListALL);

        try {
            String ssid = "";
            String passw = "", ssidCon="", passwCon="", mod="", getwifi="";

            ssid = getIntent().getExtras().getString("ssid");
            passw = getIntent().getExtras().getString("pass");

            ssidCon = getIntent().getExtras().getString("ssidConn");
            passwCon = getIntent().getExtras().getString("passConn");
            mod = getIntent().getExtras().getString("mod");

            if (ssidCon!="" && mod!=null)
            {
                if (mod.equals("conn"))
                    mReceiver.tryConnectToWifi(ssidCon,passwCon);

                if (mod.equals("del"))
                    mWifiListSAVE.delWiFi(ssidCon);

                if (mod.equals("edit")) {

                    Log.d("88888888888",getIntent().getExtras().getString("wifiid") + " -> " + ssidCon);
                    mWifiListSAVE.setWiFi(getIntent().getExtras().getString("wifiid"), ssidCon, passwCon);

                }
            }

            getwifi = getIntent().getExtras().getString("getwifi");

            if (getwifi!="" && getwifi!=null)
            {
                JSONArray jsonArray = new JSONArray(getwifi);
                for (int i = 0; i < jsonArray.length(); i++) {
                   // String str=jsonArray.getString(i); // Если это массив строк
                    JSONObject obj=jsonArray.getJSONObject(i); //Если это объект
                    String str2=obj.getString("SSID"); // Получаем строку из объекта
                    String str3=obj.getString("MAC");
                    String str4=obj.getString("PASSWORD");
                    mWifiListSAVE.putWiFi(str2,str3,str4);
                }
            }

            mWifiListSAVE.putWiFi(ssid, passw);

        }
        catch (Exception e)
        {
            Log.d("e",e.toString());
        }

        inAdd = false;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(MainActivity.this, WiFiActivity.class);
                inAdd = true;
                startActivity(intent);
            }
        });

    }


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
            Intent intent = new Intent(MainActivity.this, SharingActivity.class);

            intent.putExtra("val", mWifiListSAVE.getArrayStringsToPost());

            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }




    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {

        }




        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


        private HashMap<String, Object> getCurrentSsid(Context context) {
            HashMap<String, Object> ssid = new HashMap<String, Object>();

            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (networkInfo.isConnected()) {
                final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                if (connectionInfo != null) {
                    ssid.put(WiFiListModel.TITLE,connectionInfo.getSSID());
                    ssid.put(WiFiListModel.MAC,connectionInfo.getMacAddress());
                    ssid.put(WiFiListModel.DESCRIPTION,connectionInfo.getIpAddress());
                }
            }
            else
            {
                ssid.put(WiFiListModel.TITLE,"NoConnected");
                ssid.put(WiFiListModel.MAC,"");
                ssid.put(WiFiListModel.DESCRIPTION,"");
            }
            return ssid;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

           // TextView textView = (TextView) rootView.findViewById(R.id.section_label);
           // textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

            Log.d("------------","this - view " + getArguments().getInt(ARG_SECTION_NUMBER));

            ListView listView = (ListView)rootView.findViewById(R.id.section_label);

            switch (getArguments().getInt(ARG_SECTION_NUMBER))
            {
                case 0:


                    break;
                case 1:





                    mWifiList = mReceiver.getmWifiList();


                    SimpleAdapter adapter = new SimpleAdapter(getContext(), mWifiList.WiFiList(),
                            R.layout.list_item, new String[]{mWifiList.TITLE, mWifiList.DESCRIPTION, mWifiList.ICON},
                            new int[]{R.id.text1, R.id.text2, R.id.img});

                    listView.setAdapter(adapter);

                    break;
                case 2:


                    WiFiListModel mWifi = mWifiListSAVE;



                    HashMap<String, Object> t = getCurrentSsid(getContext());

                    if (t!=null)
                    {
                        if (!mWifi.seachWiFiSSID(t.get(WiFiListModel.TITLE).toString()))
                        mWifi.putWiFi(t);


                    }

                    SimpleAdapter ad = new SimpleAdapter(getContext(), mWifi.WiFiList(),
                            R.layout.list_item_mac, new String[]{mWifi.TITLE, mWifi.DESCRIPTION, mWifi.ICON, mWifi.MAC},
                            new int[]{R.id.text1, R.id.text2, R.id.img,R.id.mac_text});

                    listView.setAdapter(ad);

                    listView.setOnItemClickListener(itemClickListener);

                    break;


                case 3:


                    break;
            }





            return rootView;
        }

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    HashMap<String, Object> itemHashMap = (HashMap<String, Object>) parent.getItemAtPosition(position);
                    String titleItem = itemHashMap.get(WiFiListModel.TITLE).toString();
                    String passItem = itemHashMap.get(WiFiListModel.DESCRIPTION).toString();
                    String mac = itemHashMap.get(WiFiListModel.MAC).toString();

                    //   int imageItem = (int)itemHashMap.get(WiFiListModel.ICON);
                    // Intent intent1;
                    // intent1 = new Intent().setClass(getContext(), WiFiActivity.class);



                Intent intent = new Intent(getContext(), SetWiFiActivity.class);

                intent.putExtra("ssid", titleItem);
                intent.putExtra("pass", passItem);
                intent.putExtra("mac", mac);



                startActivity(intent);

                Log.d("------------","clic");
                }
                catch (Exception e)
                {}

            }
        };


    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Wi-Fi";
                case 1:
                    return "All Wi-Fi";
                case 2:
                    return "Sharing";
            }
            return null;
        }
    }
}

/*
    void writeFile(String s) {
        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput("file.txt", MODE_PRIVATE)));
            // пишем данные

            for (HashMap<String, Object> t :mWiFiList) {
                if (!t.get(TITLE).equals(s))
                    bw.write(t.get(TITLE)+" " + t.get(DESCRIPTION) + "\n");
                Log.d("записываем", t.get(TITLE)+" " + t.get(DESCRIPTION));
            }


            // закрываем поток
            bw.close();
            Log.d("0", "Файл записан");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void readFile() {
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput("file.txt")));
            String str = "";
            // читаем содержимое
            HashMap<String, Object> hm;
            while ((str = br.readLine()) != null) {
                Log.d("0", str);


                String[] res = str.split(" ");

                hm = new HashMap<>();
                hm.put(TITLE, res[0]);
                hm.put(DESCRIPTION, res[1]);
                hm.put(ICON, R.drawable.inf8);
                mWiFiList.add(hm);

            }


            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
