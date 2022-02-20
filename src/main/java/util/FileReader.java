package util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import model.DataFile;
import model.Photo;

//Classe respons�vel por ler os arquivos
public class FileReader {
	private File[] matches;
	private List<File> files;
	private File dir;

	public FileReader() {
		String url = "C:\\Users\\daniel\\Desktop\\foto";
		files = new ArrayList<File>();
		dir = new File(url);
	}

	public static File[] getFileLink() {
		String url = "C:\\Users\\Daniel\\Downloads";
		File dir = new File(url);

		File[] matches = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.contains("Links");
			}
		});
		return matches;
	}
	
	public static File[] getFilePhoto() {
		String url = "C:\\Users\\Daniel\\Downloads";
		File dir = new File(url);

		File[] matches = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.contains(" photo_4p");
			}
		});
		return matches;
	}

	public File[] getFilePesquisa() {
		String url = "C:\\Users\\Daniel\\Downloads";
		File dir = new File(url);

		File[] matches = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.contains("Pesquisa");
			}
		});
		return matches;
	}
    //Renomeio o arquivo com o nome do projeto passado como parametro
	//Usado no seleniumService
	public static void renameFile(String project) {
		File matche = getFileLink()[0];
		File newFile = new File("C:\\Users\\Daniel\\Downloads\\" + project + " photo_4p.xlsx");
		
		try {
			Files.move(matche.toPath(), newFile.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<DataFile> getLojas(List<DataFile> datas) {
		List<DataFile> filter = datas;
		List<File> files = getMatches();

		//Retiro o a url do download das 'photos'
		//filtro por nome igual e insiro o caminho da foto no meu computador no 'Photo'
		filter.forEach(data -> data.getPhotos().clear());
		for (File file : files) {
			String loja = file.getName().split("!..!")[0];
			for (DataFile data : filter) {
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
		matches = dir.listFiles(new FilenameFilter() {
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
