package com.example.anton.wificonnect;

import android.util.Log;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Anton on 03.06.2016.
 */
public class WiFiListModel {
    private ArrayList<HashMap<String, Object>> mWiFiList;
    public static final String TITLE = "Name";
    public static final String DESCRIPTION = "Password";
    public static final String ICON = "icon";
    public static final String MAC = "Macadress";

    public ArrayList<HashMap<String, Object>> WiFiList() {return mWiFiList;}

    public WiFiListModel() {
        mWiFiList = new ArrayList<HashMap<String, Object>>();
    }


    public void putWiFi(HashMap<String, Object> t) {
        mWiFiList.add(t);
    }
    public void putWiFi(String ssid, String password)
    {
        if (ssid==null) return;
        if (password==null) password ="No password";
        HashMap<String, Object> hm;
        hm = new HashMap<>();
        hm.put(TITLE, ssid);
        hm.put(DESCRIPTION, password);
        hm.put(ICON, R.drawable.inf8);
        hm.put(MAC, "");
        mWiFiList.add(hm);
    }

    public boolean seachWiFiSSID(String s)
    {
        for (HashMap<String, Object> t: mWiFiList) {
            if (t.get(TITLE).equals(s))
                return true;
        }
        return false;
    }

    public String[] getStringsToPost()
    {
        String[] res = new String[mWiFiList.size()];
        int i =0;
        for (HashMap<String, Object> t: mWiFiList) {
            res[i] = "SSID="+t.get(TITLE)+"&PASSWORD="+t.get(DESCRIPTION)+"&MAC="+t.get(MAC);
            Log.d("0","Created " + res[i]);
        }
        return res;
    }

    public ArrayList<String> getArrayStringsToPost()
    {
        ArrayList<String> res = new ArrayList<>();
        int i =0;
        for (HashMap<String, Object> t: mWiFiList) {
            res.add("SSID="+t.get(TITLE)+"&PASSWORD="+t.get(DESCRIPTION)+"&MAC="+t.get(MAC));
            Log.d("0","Created ");
        }
        return res;
    }

    public void setWiFi(String s, String ssid, String pass)
    {
        for (int i = 0; i < mWiFiList.size(); i++) {
            if (mWiFiList.get(i).get(TITLE).equals(s))
            {
                if (ssid==null) return;
                mWiFiList.remove(i);
                /*if (pass==null) pass ="No password";
                HashMap<String, Object> hm =new HashMap<>();
                hm.put(TITLE, ssid);hm.put(DESCRIPTION, pass);hm.put(ICON, R.drawable.inf8);
                hm.put(MAC, "");
                mWiFiList.add(i,hm);*/putWiFi(ssid,pass);

                Log.d("888888","ну тип поменяли");
            }
        }
    }

    public void delWiFi(String ssid)
    {
        for (int i = 0; i < mWiFiList.size(); i++) {
            if (mWiFiList.get(i).get(TITLE).equals(ssid))
                mWiFiList.remove(i);
        }
    }

    public void putWiFi(String ssid, String password, String mac)
    {
        if (ssid==null) return;
        if (password==null) password ="No password";
        HashMap<String, Object> hm;
        hm = new HashMap<>();
        hm.put(TITLE, ssid);
        hm.put(DESCRIPTION, password);
        hm.put(ICON, R.drawable.inf8);
        hm.put(MAC, mac);
        mWiFiList.add(hm);
    }

    public void clear() {
        mWiFiList = new ArrayList<HashMap<String, Object>>();
    }

}
