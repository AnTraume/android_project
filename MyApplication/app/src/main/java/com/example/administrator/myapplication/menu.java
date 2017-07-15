package com.example.administrator.myapplication;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.*;

import android.app.Activity;
import android.app.Notification;
import android.app.Notification.MessagingStyle.Message;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/7/7.
 */

public class menu  extends Activity{
    TextView t;
    Handler handler = new Handler()
    {
        public void handleMessage(android.os.Message msg) {
            if(msg.what==0x123)
            {
                t.setText("更新后的TextView");
            }
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.light);
        Button light_Button = (Button) this.findViewById(R.id.switch_craft);
        final TextView light_status = (TextView) this.findViewById(R.id.light_status);
        light_status.setText("获取状态");
        light_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ServerThread().start();
            }
        });
    }
    class ServerThread extends Thread{
        public void run(){
                Socket socket = null;
                //PrintWriter out = null;
                //BufferedReader in = null;
                try{
                    socket = new Socket("23.83.239.175",9999);
                    OutputStream out = socket.getOutputStream();
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    //in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    //out = new PrintWriter(socket.getOutputStream(),true);
                    out.write("Just for test".getBytes("UTF-8"));
                    out.flush();
                    in.close();
                    out.flush();
                    out.close();
                    socket.close();
                }
                catch (UnknownHostException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
    }
}
