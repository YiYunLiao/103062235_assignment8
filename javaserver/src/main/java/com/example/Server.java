package com.example;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

/**
 * Created by Gary on 16/5/28.
 */
public class Server extends JFrame implements Runnable{
    private Thread thread;
    private ServerSocket servSock;
    private BufferedReader reader;
    private JLabel result;

    public Server(){
        super("Calculator Server");
        super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        super.setSize(new Dimension(300, 100));
        super.setResizable(false);
        super.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER));
        super.setVisible(true);
        this.result = new JLabel();
        super.add(this.result);

        try {
            servSock = new ServerSocket(2000);
            InetAddress IP = InetAddress.getLocalHost();
            System.out.println("IP of my system is := "+IP.getHostAddress());
            System.out.println("Waitting to connect......");
            this.result.setText("Server IP: " + IP.getHostAddress());

            thread = new Thread(this);
            thread.start();
        } catch (java.io.IOException e) {
            System.out.println("IOException :" + e.toString());
        }
    }

    @Override
    public void run(){
        try{
            Socket clntSock = servSock.accept();
            this.result.setText("Client connected!");
            this.reader = new BufferedReader(new InputStreamReader(clntSock.getInputStream()));
            while(true) {
                String msg = this.reader.readLine();
                String txt = "The result from APP is " + msg;
                this.result.setText(txt);
            }
        }
        catch(Exception e){
            //System.out.println("Error: "+e.getMessage());
        }
    }
}
