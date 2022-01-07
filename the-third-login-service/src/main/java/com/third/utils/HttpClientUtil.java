package com.third.utils;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * @author guoyiguang
 * @description $
 * @date 2022/1/6$
 */
public class HttpClientUtil {

    public static String a(String  url) throws IOException {

        // mo ni qing qiu
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionTimeToLive(6000, TimeUnit.MILLISECONDS).build();
        HttpPost httpPost = new HttpPost(url.trim());
        httpPost.setHeader(new BasicHeader("Content-type", "application/x-www-form-urlencoded"));
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("email", "123456789@163.com"));
        list.add(new BasicNameValuePair("password", "123456"));
        httpPost.setEntity(new UrlEncodedFormEntity(list, "utf-8"));
        HttpResponse response = httpClient.execute(httpPost);


        Header[] headers = response.getHeaders("Set-Cookie");
        HashMap<String, String> cookies = new HashMap<String, String>(2);
        for (Header header : headers) {
            if (header.getValue().contains("_yapi_token")) {
                String token = header.getValue()
                        .substring(header.getValue().indexOf("=") + 1, header.getValue().indexOf(';'));
                cookies.put("_yapi_token", token);
            } else if (header.getValue().contains("_yapi_uid")) {
                String uid = header.getValue()
                        .substring(header.getValue().indexOf("=") + 1, header.getValue().indexOf(';'));
                cookies.put("_yapi_uid", uid);
            }
        }


        BasicCookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpClient1 = HttpClients.custom()
                .setConnectionTimeToLive(6000, TimeUnit.MILLISECONDS).build();
        httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BEST_MATCH);
        BasicClientCookie tokenCookie = new BasicClientCookie("_yapi_token",
                cookies.get("_yapi_token"));
        tokenCookie.setPath("/");
        tokenCookie.setExpiryDate(new Date(System.currentTimeMillis() + 60 * 60 * 1000));
        BasicClientCookie uidCookies = new BasicClientCookie("_yapi_uid", cookies.get("_yapi_uid"));
        uidCookies.setExpiryDate(new Date(System.currentTimeMillis() + 60 * 60 * 1000));
        uidCookies.setPath("/");
        cookieStore.addCookie(tokenCookie);
        cookieStore.addCookie(uidCookies);
        HttpGet httpGet = new HttpGet("1");
        //
        httpGet.addHeader("Cookie", cookieStore.toString());
        CloseableHttpResponse response1 = httpClient1.execute(httpGet);
        if (response.getStatusLine().getStatusCode() == HTTP_OK) {
            return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8.displayName());
        } else {
            return String.valueOf(response.getStatusLine().getStatusCode());
        }

    }


}
