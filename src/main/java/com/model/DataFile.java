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
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;


@Entity
@Table(name = "datafile",schema = "report")
public class DataFile {
	
	@Id@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@ManyToOne
	private Shop shop;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name="datafile_photos",
    joinColumns={@JoinColumn(name="datafile_id",
     referencedColumnName="id")},
    inverseJoinColumns={@JoinColumn(name="photos_id",
      referencedColumnName="id")},schema = "report")
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Photo> photos;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name="datafile_detail_products",
    joinColumns={@JoinColumn(name="data_file_id",
     referencedColumnName="id")},
    inverseJoinColumns={@JoinColumn(name="detail_products_id",
      referencedColumnName="id")},schema = "report")
	private List<DetailProduct> detailProducts; 
	@ManyToOne
	private Brand brand;
	private LocalDate data;
	@ManyToOne
	private Promoter promoter;
	@ManyToOne
	private Project project;
	
	public DataFile() {
		photos = new ArrayList<Photo>();
		detailProducts = new ArrayList<DetailProduct>();
	}
	
	

	public DataFile( Shop shop, List<Photo> photos, Brand brand,
			LocalDate data, Promoter promoter, Project project) {
		super();
		this.shop = shop;
		this.photos = photos;
		this.brand = brand;
		this.data = data;
		this.promoter = promoter;
		this.project = project;
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

	public List<DetailProduct> getDetailProducts() {
		return detailProducts;
	}

	public void setDetailProducts(List<DetailProduct> detailProducts) {
		this.detailProducts = detailProducts;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}
	
	//Como o Photos estao em cascade, coloquei a logica de insercao nessa funcao
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

