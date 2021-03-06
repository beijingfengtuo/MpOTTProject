package cn.cibnhp.grand.wxapi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by cibn-lyc on 2018/1/10.
 */

public class UserHttpClient {
    /**
     * Get Request
     *
     * @return String
     * @throws Exception
     */
    public String doGet(String url) {
        try {
            URL localURL = new URL(url);
            URLConnection connection = localURL.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

            httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            BufferedReader reader = null;
            StringBuffer resultBuffer = new StringBuffer();
            String tempLine = null;

            if (httpURLConnection.getResponseCode() >= 300) {
                throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
            }

            try {
                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                reader = new BufferedReader(inputStreamReader);

                while ((tempLine = reader.readLine()) != null) {
                    resultBuffer.append(tempLine);
                }
            } finally {
                if (reader != null) {
                    reader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return resultBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
