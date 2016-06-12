package com.csclab.hc.socketsample;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import android.util.Log;

public class MainActivity extends Activity implements android.view.View.OnClickListener{
    EditText inputIP;
    Button ipSend;
    String ipAdd = "";
    int serverPort = 2000;

    Socket socket;

    private PrintWriter writer;

    Button btn;

    EditText inputNumTxt1;
    EditText inputNumTxt2;

    Button btnAdd;
    Button btnSub;
    Button btnMult;
    Button btnDiv;
    Button btnMod;
    Button btnPow;

    TextView textResult;
    Button return_button;
    String oper = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ip_page);
        inputIP = (EditText)findViewById(R.id.edIP);
        ipSend = (Button)findViewById(R.id.ipButton);

        ipSend.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                ipAdd = inputIP.getText().toString();
                jumpToIPLayout();
            }
        });
    }

    public void jumpToIPLayout() {
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(this);
        btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Client","Client Send");
                Thread t = new thread();
                t.start();
                jumpToCalLayout();
            }
        });
    }

    private void sendMsg(String msg){
        this.writer.println(msg);
        this.writer.flush();
    }

    class thread extends Thread{
        public void run(){
            try{
                socket = new Socket(ipAdd, serverPort);
                MainActivity.this.writer = new PrintWriter(new OutputStreamWriter(MainActivity.this.socket.getOutputStream()));
                sendMsg("connected");
            }catch (Exception e){
                System.out.println("Error" + e.getMessage());
            }
        }
    }

    public void jumpToCalLayout() {
        setContentView(R.layout.cal_page);

        this.inputNumTxt1 = (EditText) findViewById(R.id.etNum1);
        this.inputNumTxt2 = (EditText) findViewById(R.id.etNum2);
        this.btnAdd = (Button) findViewById(R.id.btnAdd);
        this.btnSub = (Button) findViewById(R.id.btnSub);
        this.btnMult = (Button) findViewById(R.id.btnMult);
        this.btnDiv = (Button) findViewById(R.id.btnDiv);
        this.btnMod = (Button) findViewById(R.id.btnMod);
        this.btnPow = (Button) findViewById(R.id.btnPow);

        this.btnAdd.setOnClickListener(this);
        this.btnSub.setOnClickListener(this);
        this.btnMult.setOnClickListener(this);
        this.btnDiv.setOnClickListener(this);
        this.btnMod.setOnClickListener(this);
        this.btnPow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        double num1 = 0;
        double num2 = 0;
        double result = 0;

        if (TextUtils.isEmpty(inputNumTxt1.getText().toString())
                || TextUtils.isEmpty(inputNumTxt2.getText().toString())) {
            return;
        }

        num1 = Double.parseDouble(inputNumTxt1.getText().toString());
        num2 = Double.parseDouble(inputNumTxt2.getText().toString());

        switch (v.getId()) {
            case R.id.btnAdd:
                oper = "+";
                result = num1 + num2;
                break;
            case R.id.btnSub:
                oper = "-";
                result = num1 - num2;
                break;
            case R.id.btnMult:
                oper = "*";
                result = num1 * num2;
                break;
            case R.id.btnDiv:
                oper = "/";
                result = num1 / num2;
                break;
            case R.id.btnMod:
                oper = "%";
                result = num1 % num2;
                break;
            case R.id.btnPow:
                oper = "^";
                result = Math.pow(num1, num2);
                break;
            default:
                break;
        }
        Log.v("debug","ANS "+result);
        String resultStr = Double.toString(num1) + " " + oper + " " + Double.toString(num2) + " " + "=" + " " + Double.toString(result);
        sendMsg(resultStr);
        this.jumpToResultLayout(resultStr);
    }

    public void jumpToResultLayout(String resultStr){
        setContentView(R.layout.result_page);

        this.textResult = (TextView) findViewById(R.id.textResult);
        this.return_button = (Button) findViewById(R.id.return_button);

        if (textResult != null) {
            this.textResult.setText(resultStr);
        }

        if (return_button != null) {
            this.return_button.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    MainActivity.this.jumpToCalLayout();
                }
            });
        }
    }
}
