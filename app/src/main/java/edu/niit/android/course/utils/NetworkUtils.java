package edu.niit.android.course.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtils {
    public static String get(String urlPath) {
        HttpURLConnection connection = null;
        InputStream is = null;
        try {
            // 1. 将url字符串转为URL对象
            URL url = new URL(urlPath);
            // 2. 获得HttpURLConnection对象
            connection = (HttpURLConnection) url.openConnection();
            // 3. 设置连接的相关参数
            connection.setRequestMethod("GET"); // 默认为GET
            connection.setUseCaches(false); //不使用缓存
            connection.setConnectTimeout(10000); //设置超时时间
            connection.setReadTimeout(10000); //设置读取超时时间
            connection.setDoInput(true);

            // 4. 配置https的证书（可选）
            if ("https".equalsIgnoreCase(url.getProtocol())){
                ((HttpsURLConnection) connection).setSSLSocketFactory(HttpsUtil.getSSLSocketFactory());
            }

            // 5. 进行数据的读取，首先判断响应码是否为200
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                is = connection.getInputStream(); //获得输入流
                BufferedReader reader = new BufferedReader(new InputStreamReader(is)); //包装字节流为字符流
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                // 6. 返回结果
                return response.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String post(String urlPath, Map<String, String> params) {
        // 1. 处理post参数
        if (params == null || params.size() == 0) {
            return get(urlPath);
        }
        OutputStream os = null;
        InputStream is = null;
        HttpURLConnection connection = null;
        String body = getParamString(params);
        byte[] data = body.getBytes();

        try {
            URL url = new URL(urlPath); //获得URL对象
            connection = (HttpURLConnection) url.openConnection(); //获得HttpURLConnection对象
            connection.setRequestMethod("POST"); // 设置请求方法为post
            connection.setUseCaches(false); //不使用缓存
            connection.setConnectTimeout(10000); //设置超时时间
            connection.setReadTimeout(10000); //设置读取超时时间
            connection.setDoInput(true); //设置是否从httpUrlConnection读入，默认情况下是true;
            connection.setDoOutput(true); //设置为true后才能写入参数
            // 配置https的证书
            if ("https".equalsIgnoreCase(url.getProtocol())){
                ((HttpsURLConnection) connection).setSSLSocketFactory(HttpsUtil.getSSLSocketFactory());
            }
            // 写入参数
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            os = connection.getOutputStream();
            os.write(data);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //相应码是否为200
                is = connection.getInputStream();
                //获得输入流
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                //包装字节流为字符流
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    // 将键值对转为请求参数，格式为key=value&key=value
    private static String getParamString(Map<String, String> params) {
        StringBuffer result = new StringBuffer();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> param = iterator.next();
            String key = param.getKey();
            String value = param.getValue();
            result.append(key).append('=').append(value);
            if (iterator.hasNext()) {
                result.append('&');
            }
        }
        return result.toString();
    }
}
