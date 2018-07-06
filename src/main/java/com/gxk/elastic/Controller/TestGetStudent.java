/**
 * 
 */
package com.gxk.elastic.Controller;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

/**
 * @author gexiaokang
 *
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/teststudent")
public class TestGetStudent {
	
	private String Host = "127.0.0.1";
	
	private int port = 9200;
	
	@ResponseBody
	@RequestMapping("/test")
	private JSONObject addIndex() throws IOException {
		
		JSONObject json = new JSONObject();
		json.put("test", "success");
		return json;
	}
	
	
	@ResponseBody
	@RequestMapping("/checkEs")
	private String checkEs() throws Exception {
		RestClient restClient = RestClient.builder(
	            new HttpHost(Host, port, "http")).build();
		// （1） 执行一个基本的方法，验证es集群是否搭建成功
		Response response = restClient.performRequest("GET", "/", Collections.singletonMap("pretty", "true"));
        String result = EntityUtils.toString(response.getEntity());
        restClient.close();
        return result;

	}
	
	
	@ResponseBody
	@RequestMapping("/checkIndex")
	private String checkIndex() throws Exception {
		RestClient restClient = RestClient.builder(
				new HttpHost(Host, port, "http")).build();
		// （2）验证es的某个索引是否存在
		Response response2 = restClient.performRequest("HEAD","/student_test_index_new",Collections.<String, String>emptyMap());
		String result = response2.getStatusLine().getReasonPhrase();
		restClient.close();
		return result;
	}
	
	
	/**
	 * @throws Exception 
	 * 
	 */
	@ResponseBody
	@RequestMapping("/getIndexInfo")
	private String getIndexInfo() throws Exception {
		RestClient restClient = RestClient.builder(
				new HttpHost(Host, port, "http")).build();
		//获取集群状态
		Response response = restClient.performRequest("GET", "/");
		String result = EntityUtils.toString(response.getEntity());
		restClient.close();
		return result;
	}
	
	
	@ResponseBody
	@RequestMapping("/getIndexInfo2")
	private String getIndexInfo2() throws Exception {
		RestClient restClient = RestClient.builder(
				new HttpHost(Host, port, "http")).build();
		Map<String, String> params = Collections.singletonMap("pretty", "true");
		Response response = restClient.performRequest("GET", "/", params);
		String result = EntityUtils.toString(response.getEntity());
		restClient.close();
		return result;
	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	@ResponseBody
	@RequestMapping("/putinfo")
	private String putinfo() throws Exception {
		RestClient restClient = RestClient.builder(
				new HttpHost(Host, port, "http")).build();
		//存储数据
		Map<String, String> params = Collections.emptyMap();
		String jsonString = "{" +
		            "\"name\":\"javatest\"," +
		            "\"desc\":\"hello i am java test put\"," +
		            "\"sex\":0," +
		            "\"age\":88," +
		            "\"height\":888," +
		            "\"postDate\":\"2018-08-08\"," +
		            "\"@timestamp\":"+System.currentTimeMillis()+"" +
		        "}";
		HttpEntity entity = new NStringEntity(jsonString, ContentType.APPLICATION_JSON);
		Response response = restClient.performRequest("PUT", "/student_test_index_new/user_new/8", params, entity);
		String result = response.getStatusLine().getReasonPhrase();
		restClient.close();
		return result;
	}
	
	
	/**
	 * @throws Exception 
	 * 
	 */
	@ResponseBody
	@RequestMapping("/getAll")
	private JSONObject getAll() throws Exception {
		RestClient restClient = RestClient.builder(
				new HttpHost(Host, port, "http")).build();
		//查询数据
		Map<String, String> params = Collections.singletonMap("pretty", "true");
		String jsonString = "{\"query\": {\"match\":{\"name\":\"gxk\"}},"
				+ "\"highlight\": {\"fields\": {\"name\": {}}}"
				+",\"sort\": [ {\"_id\": { \"order\": \"desc\"   }  }  ]"
				+ "}";
		HttpEntity entity = new NStringEntity(jsonString, ContentType.APPLICATION_JSON);
		Response response = restClient.performRequest("POST", "/student_test_index_new/user_new/_search", params, entity);
		String result = EntityUtils.toString(response.getEntity());
		restClient.close();
		return JSONObject.parseObject(result);
	}
	
	
	@ResponseBody
	@RequestMapping("/getById")
	private JSONObject getById() throws Exception {
		RestClient restClient = RestClient.builder(
	            new HttpHost(Host, port, "http")).build();
		Response response = restClient.performRequest("GET", "/student_test_index_new/user_new/8", Collections.<String, String>emptyMap());
        String result = EntityUtils.toString(response.getEntity());
        restClient.close();
        return JSONObject.parseObject(result);
	}
	
	
}
