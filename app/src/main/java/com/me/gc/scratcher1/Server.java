package com.me.gc.scratcher1;

import android.content.Context;
import android.os.Build;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Server {
    private Context context;
    private ArrayMap<String,String> deviceInfo = new ArrayMap<>();
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private String deviceuid;
    private String hashkey;
    private Integer transactionid;

    public Server(Context c){
        context = c;
        deviceInfo.put("model", android.os.Build.MODEL);
        deviceInfo.put("brand", Build.BRAND);
        deviceInfo.put("device", Build.DEVICE);
        deviceInfo.put("buildid", Build.ID);
        deviceInfo.put("manufacturer", Build.MANUFACTURER);
        deviceInfo.put("user", Build.USER);
        deviceInfo.put("product", Build.PRODUCT);
        deviceInfo.put("releaseversion", Build.VERSION.RELEASE);
        Integer sdkInt = android.os.Build.VERSION.SDK_INT;
        deviceInfo.put("sdkversion", sdkInt.toString());
        transactionid = null;
        hashkey = "";
        deviceuid = "";
    }

    //ping server and save new deviceuid and new hashkey
    private final Runnable createNewDeviceUID = new Runnable() {
        public void run() {
            Hashing hashing = new Hashing();
            String hash = "";
            try {
                hash = hashing.sha1(deviceInfo.get("brand") + "."
                        + deviceInfo.get("model") + "." + deviceInfo.get("device"));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            MediaType MEDIA_TYPE =
                    MediaType.parse("application/json");

            JSONObject postdata = new JSONObject();
            try {
                //currentlu data is unsed serverside
                postdata.put("model", deviceInfo.get("model"));
                postdata.put("brand", deviceInfo.get("brand"));
                postdata.put("device", deviceInfo.get("device"));
                postdata.put("buildid", deviceInfo.get("buildid"));
                postdata.put("manufacturer", deviceInfo.get("manufacturer"));
                postdata.put("user", deviceInfo.get("user"));
                postdata.put("product", deviceInfo.get("product"));
                postdata.put("releaseversion", deviceInfo.get("releaseversion"));
                postdata.put("sdkversion", deviceInfo.get("sdkversion"));
                postdata.put("hash",hash);
            } catch(JSONException e){
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            RequestBody body = RequestBody.create(MEDIA_TYPE,
                    postdata.toString());
            Request request = new Request.Builder()
                    .addHeader("Content-Type", "application/json")
                    .url("https://scratcherserver.herokuapp.com/api/create")
                    .post(body)
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                Response response = client.newCall(request).execute();
                if(response.isSuccessful()){
                    String jsonData = response.body().string();
                    Log.d("berttest","jsonData string is:" + jsonData);
                    try {
                        JSONObject jsonObject = new JSONObject(jsonData);
                        String newdeviceuid = jsonObject.getString("deviceuid");
                        Log.d("berttest", "deviceuid:"+newdeviceuid);
                        String newhk = jsonObject.getString("hk");
                        //create new file start
                        //creates new file and new entry even if exists
                        String filename = "startdust";
                        String fileContents = newdeviceuid + "-" + newhk;
                        FileOutputStream outputStream;
                        try {
                            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
                            outputStream.write(fileContents.getBytes());
                            outputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //create new file end
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private final Runnable playAd = new Runnable() {
        public void run() {
            Hashing hashing = new Hashing();
            String hash = "";
            try {
                hash = hashing.sha1(deviceuid + hashkey + deviceInfo.get("model"));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            MediaType MEDIA_TYPE =
                    MediaType.parse("application/json");
            JSONObject postdata = new JSONObject();
            try {
                //currentlu data is unsed serverside
                postdata.put("model", deviceInfo.get("model"));
                postdata.put("brand", deviceInfo.get("brand"));
                postdata.put("device", deviceInfo.get("device"));
                postdata.put("buildid", deviceInfo.get("buildid"));
                postdata.put("manufacturer", deviceInfo.get("manufacturer"));
                postdata.put("user", deviceInfo.get("user"));
                postdata.put("product", deviceInfo.get("product"));
                postdata.put("releaseversion", deviceInfo.get("releaseversion"));
                postdata.put("sdkversion", deviceInfo.get("sdkversion"));
                postdata.put("deviceuid", deviceuid);
                postdata.put("hash", hash);
            } catch(JSONException e){
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            RequestBody body = RequestBody.create(MEDIA_TYPE,
                    postdata.toString());
            Request request = new Request.Builder()
                    .addHeader("Content-Type", "application/json")
                    .url("https://scratcherserver.herokuapp.com/api/playad")
                    .post(body)
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                Response response = client.newCall(request).execute();
                if(response.isSuccessful()){
                    String jsonData = response.body().string();
                    Log.d("berttest","playad jsonData string is:" + jsonData);
                    try {
                        JSONObject jsonObject = new JSONObject(jsonData);
                        transactionid = jsonObject.getInt("transactionid");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    //check if deviceuid exists, if not ping server
    public void create(){
        try {
            FileInputStream userInputStream = context.openFileInput("startdust");
            InputStreamReader inputStreamReader = new InputStreamReader(userInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String lineData = bufferedReader.readLine();
            Log.d("berttest","lineData length is:" + lineData.length());
            int fileLength = lineData.length();
            if(fileLength>90 && fileLength<100){
                //deviceuid exists do nothing
                String[] parts = lineData.split("-");
                deviceuid = parts[0]; // 004
                hashkey = parts[1]; // 034556
                Log.d("berttest", "deviceuid exists and is:" + deviceuid + " hashkey:" + hashkey);
            }else{
                executorService.submit(createNewDeviceUID);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            executorService.submit(createNewDeviceUID);
        } catch (IOException e) {
            e.printStackTrace();
            executorService.submit(createNewDeviceUID);
        }
    }

    public void playAd(){
        executorService.submit(playAd);
    }

}
