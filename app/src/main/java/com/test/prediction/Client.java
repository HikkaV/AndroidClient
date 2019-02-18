package com.test.prediction;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Arrays;

public class Client extends AsyncTask<Void, Void, String> {


    private String response = "";
    @SuppressLint("StaticFieldLeak")
    private TextView textResponse;
    private byte data [];
    private String dstAddress="34.73.106.119";
    private int dstPort=80;
    private  Socket soc=null;

    void setDstPort(int dstPort) {
        this.dstPort = dstPort;
    }

    void setDstAddress(String dstAddress) {
        this.dstAddress=dstAddress;
    }

    void setData(byte[] data) {
        this.data = data;
    }

    Client(TextView textResponse){
        this.textResponse = textResponse;
    }
//    Client( String dstAddress , int dstPort,TextView textResponse, byte data []) {
//
//        this.textResponse = textResponse;
//        this.data = data;
//        this.dstAddress = dstAddress;
//        this.dstPort = dstPort;
//    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onPreExecute() {
        super.onPreExecute();


        textResponse.setText("Waiting for connection..");
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onProgressUpdate(Void... values) {
        textResponse.setText("Connected");
    }

    @Override
    protected String doInBackground(Void... arg0) {



        try {

            soc = new Socket(dstAddress, dstPort);
            publishProgress();
            OutputStream os = soc.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            String string = "predict";
            string= URLEncoder.encode(string, "UTF-8");
            bw.write(string);
            bw.flush();
            if(soc.isClosed()){
                throw new Exception("Can't send data, socket is closed");
            }
            try {

                soc.getOutputStream().write(data, 0 ,data.length);
                soc.getOutputStream().flush();
                Log.i("Sent", "sent image");
            }
            catch (IOException e){
                Log.i("Can't send data", e.getMessage());
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            String msg= URLDecoder.decode(in.readLine(), "UTF-8");
            String  m [] =msg.split(" ");
            Log.i("Got", Arrays.toString(m));
            if (m[0].equals("Got")){
                Log.d("Next Step", "waiting for the msg from server ");
                return response = m[1];
             }

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "IOException: " + e.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (soc != null) {
                try {
                    soc.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        textResponse.setText(s);
        try {
            soc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}