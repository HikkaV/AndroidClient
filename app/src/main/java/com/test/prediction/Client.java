package com.test.prediction;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.net.URLEncoder;
public class Client extends AsyncTask<Void, Void, String> {


    private String response = "";
    private TextView textResponse;
    private byte data [];
    private String dstAddress;
    private int dstPort;

    Client( String dstAddress , int dstPort,TextView textResponse, byte data []) {

        this.textResponse = textResponse;
        this.data = data;
        this.dstAddress = dstAddress;
        this.dstPort = dstPort;
    }

    @Override
    protected String doInBackground(Void... arg0) {

        Socket soc=null;

        try {
            soc = new Socket(dstAddress, dstPort);

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
                soc.close();
                soc.getOutputStream().flush();

                Log.i("s", data.length+"");

                Log.i("sent", "sent image");
            }
            catch (IOException e){
                Log.i("Can't send data", e.getMessage());
            }
            /*
             * notice: inputStream.read() will block if no data return
             */
            BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            String msg= URLDecoder.decode(in.readLine(), "UTF-8");
            if (msg.equals("Got image")){
                Log.w("FUCK", "FUCK");
                BufferedReader f= new BufferedReader(new InputStreamReader(soc.getInputStream()));
                String o= URLDecoder.decode(f.readLine(), "UTF-8");
                Log.i("got", o);
             }
            BufferedReader op = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            this.response=URLDecoder.decode(op.readLine(), "UTF-8");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
        }

        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        textResponse.setText(s);
    }
}