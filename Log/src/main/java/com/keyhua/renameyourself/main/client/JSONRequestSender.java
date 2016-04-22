package com.keyhua.renameyourself.main.client;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.keyhua.protocol.json.JSONException;
import com.keyhua.protocol.json.JSONObject;

public class JSONRequestSender {

	private String requestURL = null;

	public JSONRequestSender(String requestURL) {

		this.requestURL = requestURL;
		System.out.println("Request URL is set to:" + requestURL);

	}

	public JSONObject send(JSONObject request) {

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(requestURL);
		// 根据JSON对象生成HTTP POST请求的内容
		try {
			System.out.println("Request to be sent: " + request.toString());
			StringEntity entity = new StringEntity(request.toString(), "UTF-8");
			httpPost.setHeader("Content-type", "text/plain; charset=utf-8");
			httpPost.setEntity(entity);
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
			return null;
		}

		JSONObject json = null;
		try {

			HttpResponse response = httpClient.execute(httpPost);

			int status = response.getStatusLine().getStatusCode();
			if (status != HttpStatus.SC_OK) {
				System.out.println("Server response non OK status: " + status
						+ ", msg: "
						+ response.getStatusLine().getReasonPhrase());
				return null;
			}

			// 从返回中读取JSON格式的字符串
			HttpEntity content = response.getEntity();
			char msg[] = new char[1024];
			StringBuilder sb = new StringBuilder();
			InputStreamReader reader = new InputStreamReader(
					content.getContent(), "UTF-8");
			int ret = 0;
			while (-1 != (ret = reader.read(msg))) {
				sb.append(msg, 0, ret);
			}

			System.out.println("Response message received: " + sb.toString());

			// 解析JSON字符串并生成返回的JSON对象
			json = new JSONObject(sb.toString());

		} catch (ClientProtocolException e) {
			System.out.println(e.getMessage());
			return null;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return null;
		} catch (JSONException e) {
			System.out.println("Response JSON message format error.");
			System.out.println(e.getMessage());
			return null;
		}catch (Exception e) {
			System.out.println("Response JSON message format error.");
			System.out.println(e.getMessage());
			return null;
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return json;

	}

}
