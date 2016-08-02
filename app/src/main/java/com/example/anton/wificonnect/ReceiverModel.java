package com.example.anton.wificonnect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

/**
 * Created by Anton on 03.06.2016.
 */
public class ReceiverModel{
    private WifiManager wifiManager;
    private WifiConfiguration wifiConfig;
    private WifiReceiver wifiResiver;
    private boolean wifiEnabled;
    private boolean isClick = false;
    private MainActivity mCon;
    private WiFiListModel mWifiList;

    private boolean canConnect = false;
    private String mSsid, mPassword;

    public void init() {
        wifiConfig = new WifiConfiguration();

        wifiManager = (WifiManager)mCon.getSystemService(Context.WIFI_SERVICE);
        wifiEnabled = wifiManager.isWifiEnabled();

    }


    public ReceiverModel(MainActivity _Con,WiFiListModel t) {
        Log.d("0","ReceiverModel");


        mWifiList = new WiFiListModel();
        wifiResiver = new WifiReceiver();

        mCon = _Con;

        mWifiList=t;
        init();

        if(!wifiEnabled) {
            wifiManager.setWifiEnabled(true);
        }

        scheduleSendLocation();// скан вай фай
    }

    /*
     * Подключаемся к wifi указаному в edit text
     * */
    public void scheduleSendLocation() {

       // Log.d("0","Сейчас буде регестреировать ресивер");
        mCon.registerReceiver(wifiResiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
       // Log.d("0","Зарегали успешно!!");
        wifiManager.startScan();
       // Log.d("0","Начали сканиварование");
    }

    public WifiReceiver getWifiResiver(){return wifiResiver;}
    public WiFiListModel getmWifiList(){return mWifiList;}

    protected void onPause() {
       // mCon.unregisterReceiver(wifiResiver);
    }

    public void onResume() {
          //  mCon.registerReceiver(wifiResiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    public void tryConnectToWifi(String s, String p)
    {
        canConnect = true;
        mPassword = p;
        mSsid = s;
    }

    public class WifiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context c, Intent intent) {


            List<ScanResult> results = wifiManager.getScanResults();

            Log.d("0","Нашл " + results.size() + " точек)");

            for (final ScanResult ap : results) {

                if (!mWifiList.seachWiFiSSID(ap.SSID.toString()))
                    mWifiList.putWiFi(ap.SSID.toString(),ap.capabilities);

                if(canConnect && ap.SSID.toString().trim().equals(mSsid)) {
                    // дальше получаем ее MAC и передаем для коннекрта, MAC получаем из результата
                    //здесь мы уже начинаем коннектиться
                    Log.d("0","Пытаемся законнектится к вай фай");
                    wifiConfig.BSSID = ap.BSSID;
                    wifiConfig.priority = 1;
                    wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                    if (!mPassword.equals("") && mPassword!=null) {
                        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                        wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                        wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);

                        wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                        wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

                        wifiConfig.status = WifiConfiguration.Status.ENABLED;
                        wifiConfig.preSharedKey = "\"" + mPassword + "\"";

                    }
                    else
                    {
                        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                        wifiConfig.status = WifiConfiguration.Status.ENABLED;
                    }


                    //получаем ID сети и пытаемся к ней подключиться,
                    int netId = wifiManager.addNetwork(wifiConfig);
                    wifiManager.saveConfiguration();
                    //если вайфай выключен то включаем его
                    wifiManager.enableNetwork(netId, true);
                    //если же он включен но подключен к другой сети то перегружаем вайфай.
                    wifiManager.reconnect();
                    Log.d("0","ЧТО ТО СДЕЛАНО1");

                    canConnect = false;
                    break;
                }
            }
        }
    }


}
