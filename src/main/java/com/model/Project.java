package com.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "project", schema = "operation")
public class Project {
	@Id
	private Long id;
	private String name;
	private String nameApi;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNameApi() {
		return nameApi;
	}
	public void setNameApi(String nameApi) {
		this.nameApi = nameApi;
	}
	
	
	
}
