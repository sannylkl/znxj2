package com.jiarui.znxj.utils;

/**
 * @Title: FileHelper.java
 * @Package com.tes.textsd
 * @Description: TODO(用一句话描述该文件做什么)
 * @author Alex.Z
 * @date 2013-2-26 下午5:45:40
 * @version V1.0
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class FileHelper {
    private Context context;
    /**
     * SD卡是否存在
     **/
    private boolean hasSD = false;
    /**
     * SD卡的路径
     **/
    private String SDPATH;
    /**
     * 当前程序包的路径
     **/
    private String FILESPATH;

    public FileHelper(Context context) {
        this.context = context;
        hasSD = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        SDPATH = Environment.getExternalStorageDirectory().getPath();
        FILESPATH = this.context.getFilesDir().getPath();
    }

    /**
     * 在SD卡上创建文件
     *
     * @throws IOException
     */
    public File createSDFile(String fileName) throws IOException {
        File file = new File(SDPATH + "//" + fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    /**
     * 删除SD卡上的文件
     *
     * @param fileName
     */
    public boolean deleteSDFile(String fileName) {
        File file = new File(SDPATH + "//" + fileName);
        if (file == null || !file.exists() || file.isDirectory())
            return false;
        return file.delete();
    }

    /**
     * 写入内容到SD卡中的txt文本中
     * str为内容
     */
    public void writeSDFile(String str, String fileName) {
        try {
            FileWriter fw = new FileWriter(SDPATH + "//" + fileName);
            File f = new File(SDPATH + "//" + fileName);
            fw.write(str);
            FileOutputStream os = new FileOutputStream(f);
            DataOutputStream out = new DataOutputStream(os);
            out.writeShort(2);
            out.writeUTF("");
            System.out.println(out);
            fw.flush();
            fw.close();
            System.out.println(fw);
        } catch (Exception e) {
        }
    }

    /**
     * 读取SD卡中文本文件
     *
     * @param fileName
     * @return
     */
    public String readSDFile(String fileName) {
        StringBuffer sb = new StringBuffer();
        File file = new File(SDPATH + "//" + fileName);
        try {
            FileInputStream fis = new FileInputStream(file);
            int c;
            while ((c = fis.read()) != -1) {
                sb.append((char) c);
            }
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    Map<Integer, String> map = new HashMap<Integer, String>();
    /**
     * 读取指定目录下的所有TXT文件的文件内容 + 分行读取
     *
     * @param file 格式 UTF-8 或者GBK
     * @return  文本内容
     */
    public Map<Integer, String>  getFileContent(File file) {
        if (file.isDirectory() ) {  //检查此路径名的文件是否是一个目录(文件夹)
            Log.e("zeng", "The File doesn't not exist "
                    +file.getName().toString()+file.getPath().toString());
        } else {
            if (file.getName().endsWith(".txt")) {//文件格式为txt文件
                Log.e("getName", ""+file.getName());
                map.clear();
                try {
                    InputStream instream = new FileInputStream(file);
                    if (instream != null) {
                        InputStreamReader inputreader
                                =new InputStreamReader(instream, "GBK");
                        BufferedReader buffreader = new BufferedReader(inputreader);
                        String line="";
                        int i=0;
                        //分行读取
                        while (( line = buffreader.readLine()) != null) {
                            Log.e("line"+i,""+line);
                            map.put(i++,line);
                        }
                        instream.close();       //关闭输入流
                    }
                }
                catch (FileNotFoundException e) {
                    Log.e("TestFile", "The File doesn't not exist.");
                }
                catch (IOException e)  {
                    Log.e("TestFile", e.getMessage());
                }
            }
        }
        return map ;
    }

    public String getFILESPATH() {
        return FILESPATH;
    }

    public String getSDPATH() {
        return SDPATH;
    }

    public boolean hasSD() {
        return hasSD;
    }

    /**
     * 读取指定app里的Raw目录下的所有TXT文件的文件内容 + 分行读取
     *
     * @param inputStream  inputStream  格式 UTF-8 或者GBK
     * @return  文本内容
     */
    public static String getRawString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}

