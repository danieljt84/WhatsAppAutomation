package com.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;

@Entity
@SecondaryTable(name = "status_whatsapp", pkJoinColumns = 
{ @PrimaryKeyJoinColumn(name = "id") })
public class DataFile {
	
	@Id@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@ManyToOne
	private Shop shop;
	@OneToMany(cascade=CascadeType.ALL,fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name="datafile_photos",
    joinColumns={@JoinColumn(name="datafile_id",
     referencedColumnName="id")},
    inverseJoinColumns={@JoinColumn(name="photos_id",
      referencedColumnName="id")})
	private List<Photo> photos;
	@OneToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	private List<DetailProduct> detailProducts; 
	@ManyToOne
	private Brand brand;
	private LocalDate data;
	@ManyToOne
	private Promoter promoter;
	private Project project;
	@Column(table = "status_whatsapp", name = "sendPhoto")
	private Boolean sendPhoto;
	@Column(table = "status_whatsapp", name = "sendDetail")
	private Boolean sendDetail;
	
	public DataFile() {
		photos = new ArrayList<Photo>();
		this.data = LocalDate.now();
	}
	public DataFile(DataFile datafile) {
		super();
		this.id = datafile.getId();
		this.shop =  datafile.getShop();
		this.photos =  datafile.getPhotos();
		this.brand =  datafile.getBrand();
		this.promoter =  datafile.getPromoter();
		this.data =  datafile.getData();
		this.detailProducts = datafile.getDetail_Products();
	}
	public boolean isSendPhoto() {
		return sendPhoto;
	}
	public void setSendPhoto(boolean sendPhoto) {
		this.sendPhoto = sendPhoto;
	}
	public boolean isSendDetail() {
		return sendDetail;
	}
	public void setSendDetail(boolean sendDetail) {
		this.sendDetail = sendDetail;
	}
	public List<DetailProduct> getDetail_Products() {
		return detailProducts;
	}
	public void setDetail_Products(List<DetailProduct> detail_Products) {
		this.detailProducts = detail_Products;
	}

	public List<Photo> getPhotos() {
		return photos;
	}

	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	//Outro set que recebe uma string 
	public void setProject(String project) {
	   switch(project) {
	   case "Compartilhado Fixo":
		   this.project = Project.FIXO_RJ;
		   break;
	   case "Compartilhado RJ":
		   this.project = Project.COMPARTILHADO_RJ;
		   break;
	   case "Compartilhado SP":
		   this.project = Project.COMPARTILHADO_SP;
		   break;
	   case "Compartilhado BA":
		   this.project = Project.COMPARTILHADO_BA;
		   break;
	   case "Compartilhado ES":
		   this.project = Project.COMPARTILHADO_ES;
		   break;
	   }
	}
	public Promoter getPromoter() {
		return promoter;
	}

	public void setPromoter(Promoter promoter) {
		this.promoter = promoter;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}
	
	//Como o Photos está em cascade, coloquei a logica de inserção nessa função
	public void createPhoto(String link,String section) {
		Photo photo = new Photo();
		photo.setUrl(link);
		photo.setSection(section);
		this.photos.add(photo);
	}
	
	//Verifica se o link tem "nomedia", que indica foto invalida
	public boolean checkNullFiles() {
		boolean retorno = false;
		for(Photo photo: this.getPhotos()) {
			if(photo.getUrl().contains("nomedia")) {
				retorno = true;
				break;
			}
		}
		return retorno;
	}
}
