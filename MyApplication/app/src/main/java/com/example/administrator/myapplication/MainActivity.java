package com.example.administrator.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import static java.lang.Boolean.FALSE;

public class MainActivity extends Activity implements CompoundButton.OnCheckedChangeListener {
    private EditText user_name;
    private EditText user_pass;
    private TextView user_point ;
    private CheckBox cbRememberPassword;
    private CheckBox cbAutoLogin;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private Handler handler = new Handler(){
    private int process = 0;
    @Override
    public void handleMessage(Message msg) {
    switch(msg.what){
    case 1://登陆成功
    user_point.setText("登陆成功");//在主线程中更新UI界面
    break;
    case 0://提示下载完成
    user_point.setText("登陆失败");//在主线程中更新UI界面
    break;
        default:
            break;
        }
    }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //去除标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.log);

        //获得实例对象
        sp = PreferenceManager.getDefaultSharedPreferences(this);

        cbRememberPassword = (CheckBox)findViewById(R.id.remember_password);
        cbAutoLogin = (CheckBox)findViewById(R.id.auto_login);

        //设置checkbox监听
        cbRememberPassword.setOnCheckedChangeListener(this);
        cbAutoLogin.setOnCheckedChangeListener(this);


        Button btn_login = (Button) this.findViewById(R.id.login);
        final EditText user_name = (EditText) this.findViewById(R.id.username);
        final EditText user_pass = (EditText) this.findViewById(R.id.password);
        boolean isRemember = sp.getBoolean("remember_password",FALSE);
        if(isRemember){
            String account = sp.getString("account","");
            String password = sp.getString("password","");
            user_name.setText(account);
            user_pass.setText(password);
            cbRememberPassword.setChecked(true);
        }

        Handler handler;
        // 定义与服务器通信的子线程
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = user_name.getText().toString();
                String password = user_pass.getText().toString();
                if(!((account.equals("123"))&&(password.equals("456")))){
                    Toast.makeText(MainActivity.this,"密码错误请重新输入",Toast.LENGTH_SHORT).show();

                }
                else{
                    if(cbRememberPassword.isChecked()){
                        editor.putBoolean("remember_password",true);
                        editor.putString("account",account);
                        editor.putString("password",password);
                        Toast.makeText(MainActivity.this,"登录成功，正在读取用户数据...",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        editor.clear();
                    }
                    editor.commit();
                }
               // new login().start();
                //Intent intent = new Intent(MainActivity.this, menu.class);
                //startActivity(intent);
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()){
            case R.id.auto_login:
                Toast.makeText(this,isChecked ? "自动登录":"不自动登录",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.remember_password:
                Toast.makeText(this,isChecked ? "记住密码":"不记住密码",
                        Toast.LENGTH_SHORT).show();
                break;

        }
    }


    class login extends Thread{
        public void run(){
            Socket socket = null;
            user_name = (EditText) findViewById(R.id.username);
           // cardNumStr = cardNumAuto.getText().toString();
            user_pass = (EditText) findViewById(R.id.password);
            user_point = (TextView) findViewById(R.id.point);
            //PrintWriter out = null;
            //BufferedReader in = null;
            try{
                socket = new Socket("23.83.239.175",9998);
                OutputStream out = socket.getOutputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String line = null;
                line = in.readLine();
                if (line.equals("OK"))
                    handler.sendEmptyMessage(1);
                else
                    handler.sendEmptyMessage(0);
                String data  = user_name.getText().toString() + "\n" + user_pass.getText().toString();
              //  String data  = cardNumAuto.getText().toString() + "\n" + user_pass.getText().toString();
                out.write(data.getBytes("UTF-8"));
                out.flush();
                in.close();
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


