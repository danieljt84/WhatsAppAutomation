package com.service;

import java.net.http.HttpClient;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.model.Brand;
import com.model.DataFile;
import com.model.WhatsappGroup;

public class ApiService {
	
	HttpClient httpClient;
	ObjectMapper mapper;
	
	public ApiService() {
		this.httpClient = HttpClient.newHttpClient();
		mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
	}
	
	
	
	public List<Brand> getBrands(){
		try {
			HttpResponse<JsonNode> jsonResponse = Unirest.get("https://painel4p.com:8084/whatsappgroup/brand")
					  .queryString("limit", 40)
					  .queryString("offset", 0)
					  .header("accept", "application/json")
					  .asJson();
			return Arrays.asList(mapper.readValue(jsonResponse.getBody().toString(), Brand[].class));
		} catch (UnirestException | JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<DataFile> getDataFileByBrand(Long idbrand){
		try {
			HttpResponse<JsonNode> jsonResponse = Unirest.get("https://painel4p.com:8084/datafile")
					  .queryString("idbrand", idbrand)
					  .queryString("date", LocalDate.now().toString())
					  .header("accept", "application/json")
					  .asJson();
			return Arrays.asList(mapper.readValue(jsonResponse.getBody().toString(), DataFile[].class));
		} catch (UnirestException | JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<WhatsappGroup> getWhatsappGroup(Long idbrand){
		try {
			HttpResponse<JsonNode> jsonResponse = Unirest.get("https://painel4p.com:8084/whatsappgroup")
					  .queryString("idbrand", idbrand)
					  .header("accept", "application/json")
					  .asJson();
			return Arrays.asList(mapper.readValue(jsonResponse.getBody().toString(), WhatsappGroup[].class));
		} catch (UnirestException | JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void  updateSendStatus(Long idbrand){
		try {
			HttpResponse<JsonNode> jsonResponse = Unirest.put("https://painel4p.com:8084/datafile/sendstatus/"+idbrand)
					  .header("accept", "application/json")
					  .asJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
