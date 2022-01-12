package com.example.testedit;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Loading extends AsyncTask<Void, Void, String> {
    JSch jsch = new JSch();
    Properties prop;
    Session session;
    ChannelShell channelShell;
    ChannelSftp sftpChannel;
    String Result;


    Loading(String user, String password, String ipadress, int port) {
        try {
            session = jsch.getSession(user, ipadress, port);
        } catch (JSchException e) {
            e.printStackTrace();
        }
        session.setPassword(password);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    private String outUrls;
    private String inUrls;
    private String startComend;
    private String message;

    public void StartComand(String StartComend) {
        this.startComend = StartComend;
    }

    public void setCopySend(String inUrls, String outUrls) {
        this.inUrls = inUrls;
        this.outUrls = outUrls;
    }

    public void StopConnect() {
        channelShell.disconnect();
        session.disconnect();
    }

    public void setMessage(String message) {
        this.message = message;

    }

    public String getResult() {
        return Result;
    }



    @Override
    protected String doInBackground(Void... voids) {

        String str = "";
        try {
            /**https://sourceforge.net/p/jsch/mailman/message/26387731/*/
            prop = new Properties();
            prop.put("StrictHostKeyChecking", "no");
            session.setConfig(prop);
            session.connect();

            sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
            //sftpChannel.put(inUrls, outUrls);
            //  lsFolderCopy(Environment.getExternalStorageDirectory().toString() + "/python/","home/kali/documents/python/",sftpChannel);
            //SftpATTRS attrs = null;
            //lsFolderCopy(Environment.getExternalStorageDirectory().toString() + "/python/prog/", "/home/kali/python/", sftpChannel);
            lsFolderCopy(inUrls, outUrls, sftpChannel);
/*
            File dir = new File(Environment.getExternalStorageDirectory().toString() + "/python/");
            /** если объект представляет каталог
            final String[] sDirList = dir.list();

            for (int i = 0; i < sDirList.length; i++) {
                File f1 = new File(Environment.getExternalStorageDirectory().toString() + "/python/"  + File.separator + sDirList[i]);
                if (f1.isFile()) {
                    sftpChannel.put(Environment.getExternalStorageDirectory().toString() + "/python/" +sDirList[i], "/home/kali/python/" + sDirList[i]);
                }

            }*/
            channelShell = (ChannelShell) session.openChannel("shell");
            InputStream inputStream = channelShell.getInputStream();

            channelShell.connect();
            OutputStream outputStream = channelShell.getOutputStream();
            java.io.PrintWriter printWriter = new PrintWriter(outputStream);
            message = startComend;


            try {

                List<String> token = new ArrayList<String>();
                token.add("([\\[?2004])+l");// Заменяеть в строке [?2004l
                token.add("([\\[?2004])+h");// Заменяеть в строке [?2004h
                token.add("([\\[?2004])+l+[ \\v\\r\\n\\f]");// Заменяеть в строке [?2004h с пробелом
                token.add("([\\[?2004])+h+[ \\v\\r\\n\\f]");// Заменяеть в строке [?2004h с пробелом
                token.add("\u001B\\[[;\\d]*m");// Заменяеть в строке [35;1m  [m
                String patterns = TextUtils.join("|", token);


                List<String> tokens = new ArrayList<String>();
                tokens.add("line");
                tokens.add("Traceback");
                tokens.add("NameError");
                tokens.add("SyntaxError");

                String patternString = "\\b(" + TextUtils.join("|", tokens) + ")\\b";
                Pattern pattern = Pattern.compile(patternString);

                while (true) {

                    if (channelShell.isClosed()) {
                        channelShell.connect();
                    }
                    try {
                        /**если message > 0 то мы отпровляем на сервер*/
                        if (message.length() > 0) {
                            printWriter.println(message);
                            printWriter.flush();

                            download.ClearText();

                            message = null;
                            Thread.sleep(100);
                        }

                    } catch (Exception e) {
                    }

                    while (inputStream.available() > 0) {
                        byte[] tmp = new byte[8196];
                        int i = inputStream.read(tmp, 0, 8196);
                        String s = new String(tmp, 0, i);
                        if (s.indexOf("--More--") >= 0) {
                            outputStream.write((" ").getBytes());
                            outputStream.flush();
                        }

                        str = str + s.replaceAll(patterns, "");
                        download.sendText(str);

                        Matcher matcher = pattern.matcher(str);
                        if (message != null) {
                            message = null;
                            //  SaveInfo(s);
                        }
                        if (matcher.find()) {
                            Result = Result + s;
                            //download.SetResult(Return);
                        } else {
                            Result = "Excellent";
                            matcher.reset();

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

    private static void lsFolderCopy(String sourcePath, String destPath, ChannelSftp sftpChannel) throws SftpException, FileNotFoundException {
        File localFile = new File(sourcePath);

        if (localFile.isFile()) {
            //copy if it is a file
            sftpChannel.cd(destPath);
            if (!localFile.getName().startsWith("."))
                sftpChannel.put(new FileInputStream(localFile), localFile.getName(), ChannelSftp.OVERWRITE);
        } else {
            File[] files = localFile.listFiles();

            if (files != null && files.length > 0 && !localFile.getName().startsWith(".")) {

                sftpChannel.cd(destPath);
                SftpATTRS attrs = null;

                //check if the directory is already existing
                try {
                    attrs = sftpChannel.stat(destPath + "/" + localFile.getName());
                } catch (Exception e) {

                }

                //else create a directory
                if (attrs != null) {

                } else {

                    sftpChannel.mkdir(localFile.getName());
                }


                for (int i = 0; i < files.length; i++) {

                    lsFolderCopy(files[i].getAbsolutePath(), destPath + "/" + localFile.getName(), sftpChannel);

                }

            }
        }

    }
}