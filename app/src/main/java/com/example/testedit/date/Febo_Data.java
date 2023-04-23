package com.example.testedit.date;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.testedit.connect.Connect;
import com.example.testedit.connect.Protocol;
import com.example.testedit.pythonInpreter.Common;
import com.example.testedit.setting.Setting;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Febo_Data {
    private static final String TAG = "FEBO Work with Date";

    private static final Logger mLogger = Logger.getLogger(TAG);
    public final String DIR = Environment.getExternalStorageDirectory().toString();
    public final String FEB_ONION_DIR = Environment.getExternalStorageDirectory().toString() + "/python/";

    public Febo_Data() {
        //mLogger = null;
    }


    public void writeData(DataSetting dataSetting) {
        try {
            File filename = new File(FEB_ONION_DIR, "onion.json");
            JSONObject object = new JSONObject();

            object.put("IpAddress", dataSetting.getConnect().getIpAddress());
            object.put("UserName", dataSetting.getConnect().getUserName());
            object.put("Password", dataSetting.getConnect().getPassword());
            object.put("Port", dataSetting.getConnect().getPort());
            object.put("ProjectDirHost", dataSetting.getConnect().getProjectDirHost());
            object.put("Protocol", dataSetting.getConnect().getProtocol());
            object.put("Python", dataSetting.getConnect().getPython());
            object.put("TextSize", dataSetting.getSetting().getTextSize());

            new FileOutputStream(filename).write(object.toString().getBytes());
        } catch (IOException ex) {
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public String toString() {

        File filename = new File(FEB_ONION_DIR, "onion.json");
        String JSON = "";
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(filename));
            String str;
            while ((str = br.readLine()) != null) {
                JSON = JSON + str;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSON;
    }

    private String ipAddress;
    private String userName;
    private String password;
    private String port;
    private Protocol protocol;
    private String projectDirHost;
    private Connect.Python python;

    public Connect connect() {

        try {
            JSONObject jsonRootObject = new JSONObject(toString());
            ipAddress = jsonRootObject.getString("IpAddress");
            userName = jsonRootObject.getString("UserName");
            password = jsonRootObject.getString("Password");
            port = jsonRootObject.getString("Port");
            protocol = Protocol.valueOf(jsonRootObject.getString("Protocol"));
            projectDirHost = jsonRootObject.getString("ProjectDirHost");
            python = Connect.Python.valueOf(jsonRootObject.getString("Python"));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Connect
                .newHost()
                .ipAddress(ipAddress)
                .setUserName(userName)
                .setPassword(password)
                .setPort(port)
                .setProtocol(protocol)
                .setProjectDirHost(projectDirHost)
                .setPython(python)
                .accept();
    }

    private int textSize;

    public Setting setting() {
        try {
            JSONObject jsonRootObject = new JSONObject(toString());
            textSize = Integer.parseInt(jsonRootObject.getString("TextSize"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Setting
                .newSet()
                .setTextSize(textSize)
                .accept();
    }

    public DataSetting readData() {
        return DataSetting
                .newSetting()
                .setConnect(connect())
                .setSetting(setting())
                .accept();
    }

    /**
     * Here we check file if is not directory we create directory
     */
    public void checkDirectory(String directory) {
        File theDir = new File(directory);
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
    }

    /**
     * Here we delete file
     */
    public void deleteFile(File file) {
        if (!file.exists())
            return;
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                deleteFile(f);
            }
        }
        file.delete();
    }

    /**
     * Here we delete directory
     */
    public void deleteDir(String directory) {
        File file = new File(directory);
        if (file.isFile()) {
            file.delete();
        } else {
            deleteFile(file);
        }
    }

    public boolean checkFile(String file) {
        return new File(file).isFile();
    }

    public String readFile(String FileName) {
        String code = "";
        File sdFile = new File(FileName);
        try {
            BufferedReader br = new BufferedReader(new FileReader(sdFile));
            String string;
            while ((string = br.readLine()) != null) {
                code = code + string + "\n";
            }

        } catch (IOException e) {
        }
        return code;
    }

    public void createFile(String fileName, String text, String dir) {
        File directory = new File(dir);
        File file = new File(directory, fileName);
        try {
            if (text.length() > 0) {
                new FileOutputStream(file)
                        .write(text.getBytes());
            } else {
            }
        } catch (IOException ex) {
        }
    }

    public String aboutFile(String FileName) {
        String about = "";
        File sdFile = new File(FileName);
        try {
            BufferedReader br = new BufferedReader(new FileReader(sdFile));
            String string;
            while ((string = br.readLine()) != null) {
                about = about + string;
            }
            Matcher m = Pattern.compile("\\#(([^\\#]+))\\##").matcher(about);
            if (m.find())
                return m.group(1);
        } catch (IOException e) {

        }
        return "There is no information in the code. Probably someone forgot to write it.";
    }

    public  void sendFile(Context context, ArrayList<Uri> uriList) {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
        intent.setType("*/*");
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
        context.startActivity(Intent.createChooser(intent, "Share Image:"));
    }

    public File[] arrayFile(String directory) {
        File listFile = new File(directory);
        return listFile.listFiles();
    }

    public String[] arrayDir(String directory) {
        File dir = new File(directory);
        return dir.list();
    }

    /**
     * Ebaute of and change file
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getTime(String directory) {
        BasicFileAttributes attr = null;
        File file = new File(directory);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Path path = file.toPath();
            try {
                attr = Files.readAttributes(path, BasicFileAttributes.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return attr.lastModifiedTime().toString().replaceAll("T|T1|T2|Z", "\n");
        }
        return "";
    }

    public boolean checkProject(String directory) {
        String[] ProgramName = directory.replace(FEB_ONION_DIR, "").split("/");
        return ProgramName[0].endsWith("_project");
    }

    public static void copyFilesFromAssets(Context context, String aFileName) {
        AssetManager assetManager = context.getAssets();

        InputStream lInputStream;
        OutputStream lOutputStream;
        try {
            lInputStream = assetManager.open(aFileName, AssetManager.ACCESS_BUFFER);

            String outDir = Common.getEngineRootDirectory(context);

            File outFile = new File(outDir, aFileName);

            lOutputStream = new FileOutputStream(outFile);
            copyFile(lInputStream, lOutputStream);
            lInputStream.close();
            lOutputStream.flush();
            lOutputStream.close();
        } catch (IOException e) {
            Log.e("tag", "Failed to copy asset file: " + aFileName, e);
        }

    }


    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public static void unzipFileFromAssets(Context mContext, String aSourceFile) {
        AssetManager assetManager = mContext.getAssets();

        InputStream lInputStream = null;
        try {
            lInputStream = assetManager.open(aSourceFile, AssetManager.ACCESS_BUFFER);
        } catch (IOException e) {
            e.printStackTrace();
        }


//        String lDestinationFile = mContext.getExternalFilesDir(null) + \"\\\\Python" + aSourceFile;
        String lDestinationFile = Common.getEngineRootDirectory(mContext);

        ZipInputStream zis;
        try {
            // ensure the destination directory exists
            if (!new File(lDestinationFile).mkdirs()) {
                mLogger.log(Level.INFO, "Unable to create directory '" + lDestinationFile + "'. It probably already exists");
            }

            // get the input stream of the zip file itself
            assert lInputStream != null;
            zis = new ZipInputStream(new BufferedInputStream(lInputStream));
            ZipEntry ze;
            byte[] buffer = new byte[8192];
            int count;

            // loop through all the zip entries in the zip
            while ((ze = zis.getNextEntry()) != null) {
                // if this entry is simply a directory, just create it and move on
                if (ze.isDirectory()) {
                    String dir = Common.ensureStringEndsWithForwardslash(lDestinationFile + File.separator + ze.getName());
                    File fmd = new File(dir);
                    if (!fmd.exists()) {
                        // directory doesn't exist yet, try to create it
                        if (!fmd.mkdirs()) {
                            mLogger.log(Level.WARNING, "Unable to create directory on device from zip: " + dir + ", DirExistsAlready=" + fmd.exists());
                        }
                    }
                    continue;
                }

                // if we get here, we're dealing with a straight file
                File destFile = new File(lDestinationFile, ze.getName());
                File destinationParent = destFile.getParentFile();

                // create the parent directory structure if needed for the file
                assert destinationParent != null;
                if (!destinationParent.isDirectory() && !destinationParent.mkdirs()) {
                    mLogger.log(Level.WARNING, "Unable to create directory " + destinationParent);
                }

                // write the file
                FileOutputStream lFileStream = new FileOutputStream(lDestinationFile + File.separator + ze.getName());
                while ((count = zis.read(buffer)) != -1) {
                    lFileStream.write(buffer, 0, count);
                }

                lFileStream.close();
                zis.closeEntry();
            }

            zis.close();

            mLogger.log(Level.INFO, aSourceFile + " unzipped successfully");
        } catch (Exception e) {
            mLogger.log(Level.SEVERE, "Exception while unzipping file:" + e.getMessage());
        }
    }
}
