package com.jiarui.znxj.utils;

import android.content.Context;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * 网络请求类
 *
 * @author Only You
 * @version 1.0
 * @date 2016年1月6日
 */
public class HttpUtil {

    /**
     * 访问数据路径（http://znxj.0791jr.com/Xunjian/Web/?）
     *
     * @param context
     * @return
     */
    public static String BASE_URL(Context context) {
        //访问数据IP
        String BASE_IP_URL = (String) PreferencesUtil.get(context, "BASE_IP_URL", "znxj.0791jr.com");
        return "http://" + BASE_IP_URL + "/Xunjian/Web/?";
    }

    /**
     * 加载路径（http://znxj.0791jr.com/imagine/cache/?）
     *
     * @param context
     * @return
     */
    public static String LOAD_URL(Context context) {
        //访问数据IP
        String BASE_IP_URL = (String) PreferencesUtil.get(context, "BASE_IP_URL", "znxj.0791jr.com");
        return "http://" + BASE_IP_URL + "/imagine/cache/?";
    }

    /**
     * 上传图片（http://"+BASE_IP_URL+"/Imagine/upload.php）
     *
     * @param context
     * @return
     */
    public static String UPLOAD_IMG(Context context) {
        //访问数据IP
        String BASE_IP_URL = (String) PreferencesUtil.get(context, "BASE_IP_URL", "znxj.0791jr.com");
        return "http://" + BASE_IP_URL + "/Imagine/upload.php";
    }

    /**
     * 上传文件（http://znxj.0791jr.com/Imagine/attachment.php）
     *
     * @param context
     * @return
     */
    public static String UPLOAD_FILE(Context context) {
        //访问数据IP
        String BASE_IP_URL = (String) PreferencesUtil.get(context, "BASE_IP_URL", "znxj.0791jr.com");
        return "http://" + BASE_IP_URL + "/Imagine/attachment.php";
    }

    public static final String TAG = HttpUtil.class.getSimpleName();

    private static final int TIMEOUT_VALUE = 20000;

    /**
     * 执行post请求
     *
     * @param url
     * @param params
     * @return
     */
    public static String executePost(String url, List<NameValuePair> params) {
        StringBuffer sb = new StringBuffer();
        String ret = "";
        try {
            HttpPost httpPost = new HttpPost(url);
            HttpEntity httpEntity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            httpPost.setEntity(httpEntity);
            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT_VALUE);
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, TIMEOUT_VALUE);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = httpResponse.getEntity();
                InputStream is = entity.getContent();
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                int len = 0;
                char[] buf = new char[1024];
                while ((len = isr.read(buf)) != -1) {
                    sb.append(new String(buf, 0, len));
                }
                is.close();
                isr.close();
                ret = sb.toString();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "ret = " + ret);
        return ret;
    }

    /**
     * 执行get请求
     *
     * @param url
     * @return
     */
    public static String executeGet(String url) {
        StringBuffer sb = new StringBuffer();
        String ret = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT_VALUE);
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, TIMEOUT_VALUE);
            HttpGet get = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(get);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = httpResponse.getEntity();
                InputStream is = entity.getContent();
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                int len = 0;
                char[] buf = new char[1024];
                while ((len = isr.read(buf)) != -1) {
                    sb.append(new String(buf, 0, len));
                }
                is.close();
                isr.close();
                ret = sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i(TAG, "url = " + url + "---- ret = " + ret);
        return ret;
    }

    /**
     * 执行get请求
     *
     * @param url
     * @param retryCount 重复请求的次数
     * @return
     */
    public static String executeGet(String url, int retryCount) {
        String ret = null;
        for (int i = 0; i < retryCount; i++) {
            ret = executeGet(url);
            if (ret != null) {
                break;
            }
        }
        return ret;
    }
}
