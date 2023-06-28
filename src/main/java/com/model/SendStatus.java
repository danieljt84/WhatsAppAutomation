package com.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(schema = "whatsapp")
public class SendStatus {
	@Id
	private Long id;
	@OneToOne
	@JoinColumn(name = "id")
    @MapsId
	private DataFile datafile;
	private Boolean sendPhoto;
	private Boolean sendDetail;
	
	public DataFile getDatafile() {
		return datafile;
	}
	public void setDatafile(DataFile datafile) {
		this.datafile = datafile;
	}
	public Boolean getSendPhoto() {
		return sendPhoto;
	}
	public void setSendPhoto(Boolean sendPhoto) {
		this.sendPhoto = sendPhoto;
	}
	public Boolean getSendDetail() {
		return sendDetail;
	}
	public void setSendDetail(Boolean sendDetail) {
		this.sendDetail = sendDetail;
	}
}
