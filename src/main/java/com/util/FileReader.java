package com.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import com.model.DataFile;
import com.model.Photo;

//Classe respons�vel por ler os arquivos
public class FileReader {
	private File[] matches;
	private List<File> files;
	private File dir;
	private final static String URL = "C:\\Users\\4P\\Downloads";

	public FileReader() {
		files = new ArrayList<File>();
		dir = new File(URL);
	}

    //Renomeio o arquivo com o nome do projeto passado como parametro
	//Usado no seleniumService

	public List<DataFile> getLojas(List<DataFile> datas) {
		List<DataFile> filter = datas;
		List<File> files = getMatches();
		
		//Retiro o a url do download das 'photos'
		//filtro por nome igual e insiro o caminho da foto no meu computador no 'Photo'
		datas.forEach(data -> data.getPhotos().clear());
		for (File file : files) {
			String loja = file.getName().split("!..!")[0];
			for (DataFile data : datas) {
				String nameShop = data.getShop().getName();
				if(nameShop.contains("/")) {
					nameShop = nameShop.replace("/", " ");
				}
				if (nameShop.contains(loja)) {
					Photo photo = new Photo();
					photo.setUrl(file.getAbsolutePath());
					data.getPhotos().add(photo);
				}
			}
		}
		return filter;
	}
	
	public List<File> getMatches() {
		matches = new File("C:\\Users\\Daniel\\Documents\\backup robo\\WhatsAppAutomationProfileOne\\src\\main\\resources\\fotos").listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".png");
			}
		});
		for (File f : matches) {
			files.add(f);
		}
		return files;
	}

	public void deleteFile() {
		for (File file : getMatches()) {
			file.delete();
		}
	}
}
