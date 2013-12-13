package com.example.uebungjson.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class PersonRepo {

	static InputStream is = null;
	static String json = "";

	private static String getJSONFromURL (String url){

		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntitiy = httpResponse.getEntity();
			is = httpEntitiy.getContent();
		} catch (UnsupportedEncodingException e){
			e.printStackTrace();
		} catch (ClientProtocolException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e){
			Log.e("Buffer error", "Error converting" + e.toString());
		}
		return json;
	}

	private static String makeHttpRequest(String url, String method, List<NameValuePair> params){
		try {
			if (method == "POST"){
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(params));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
			} else if (method == "GET"){
				DefaultHttpClient httpClient = new DefaultHttpClient();
				String paramString = URLEncodedUtils.format(params, "utf-8");
				url += "?" + paramString;
				HttpGet httpGet = new HttpGet(url);

				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
			}
		} catch (UnsupportedEncodingException e){
			e.printStackTrace();
		} catch (ClientProtocolException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null){
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e){
			Log.e("Buffer error", "Error pasring" + e.toString());
		}
		return json;
	}

    public static List<Person> getAllPerson(String url){
        String json = makeHttpRequest(url, "GET", new ArrayList<NameValuePair>());
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<List<Person>>(){}.getType());
    }

}