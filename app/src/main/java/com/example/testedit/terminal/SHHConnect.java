package com.example.testedit.terminal;

import android.content.Context;
import android.os.AsyncTask;

import com.example.testedit.MainInterface;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Properties;

public class SHHConnect extends AsyncTask<Void, Void, String> {
    JSch jsch = new JSch();
    Properties prop;
    Session session;
    ChannelShell channelShell;
    ChannelSftp sftpChannel;
    MainInterface mainInterface;
    private String message;

    public SHHConnect(Context content, String user, String password, String ipAddress, int port) {
        try {
            mainInterface = (MainInterface) content;
            session = jsch.getSession(user, ipAddress, port);
            session.setPassword(password);
        } catch (JSchException e) {

        }
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    protected String doInBackground(Void... voids) {

        String str = "";
        try {
            prop = new Properties();
            prop.put("StrictHostKeyChecking", "no");
            session.setConfig(prop);
            session.connect();
            channelShell = (ChannelShell) session.openChannel("shell");
            InputStream inputStream = channelShell.getInputStream();
            channelShell.connect();
            OutputStream outputStream = channelShell.getOutputStream();
            java.io.PrintWriter printWriter = new PrintWriter(outputStream);
            try {
                while (true) {
                    if (channelShell.isClosed()) {
                        channelShell.connect();
                    }
                    else if (message.length() > 0) {
                        printWriter.println(message);
                        printWriter.flush();
                        message = null;
                        Thread.sleep(100);
                    }


                    while (inputStream.available() > 0) {
                        byte[] tmp = new byte[8196];
                        int i = inputStream.read(tmp, 0, 8196);
                        String s = new String(tmp, 0, i);
                        if (s.indexOf("--More--") >= 0) {
                            outputStream.write((" ").getBytes());
                            outputStream.flush();
                        }

                        str = str + s;

                        if (message != null) {
                            message = null;
                        }
                    }

                }
            } catch (Exception e) {
            }
            outputStream.close();
            inputStream.close();
            channelShell.disconnect();
            session.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
