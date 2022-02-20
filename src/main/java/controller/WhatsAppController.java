package controller;

import java.util.ArrayList;
import java.util.List;

import model.Brand;
import model.DataFile;
import model.Detail_Product;
import model.Photo;
import model.Shop;
import model.WhatsappGroup;
import repository.BrandRepository;
import repository.DataFileRepository;
import repository.WhatsappGroupRepository;
import service.WhatsappService;
import util.FileReader;
import util.ImageURL;

public class WhatsAppController {

	DataFileRepository dataFileRepository;
	WhatsappGroupRepository groupRepository;
	BrandRepository brandRepository;
	List<DataFile> datas;
	WhatsappService whatsapp;
	FileReader reader;
	ImageURL image;
	List<Brand> brands;
	int cont = 1;

	public WhatsAppController() {
		whatsapp = new WhatsappService();
		brandRepository = new BrandRepository();
		dataFileRepository = new DataFileRepository();
		groupRepository = new WhatsappGroupRepository();
		brands = brandRepository.findAll();
	}

	// Funcao principal, que controla a execu��o da automac�o
	// Para cada 'Brand' � executado a rotina de envio
	public void run() throws InterruptedException {
		int cont = 1;
		while (cont > 0) {
			for (Brand brand : brands) {
				datas = dataFileRepository.findByBrand(brand.getId());
				routine(brand);
			}
			// Ap�s finalizar, espera 30 segundos para realizar a rotina novamente
			Thread.sleep(300000);
		}
	}

	private void routine(Brand brand) {
		// baixando as imagens
		image = new ImageURL();
		reader = new FileReader();
		List<WhatsappGroup> groups = groupRepository.findAll(brand.getId());

		// Usando a logica que temos que enviar em um grupo de cada vez
		// meu loop seguir� os grupos
		for (WhatsappGroup groupName : groups) {
			WhatsappGroup group = groupRepository.findShopsByGroup(groupName);
			List<DataFile> filter = filterDataFile(group, datas);
			if (filter.size() != 0) {
				for (DataFile data : filter) {
					for (Photo photo : data.getPhotos()) {
						try {
							image.viewImage(data.getShop().getName(), photo.getUrl());
						} catch (Exception e) {
							System.out.println(e);
							continue;
						}
					}
				}
				// envio de mensagem no selenium
				// usando o model datafile para parsear as informações
				List<DataFile> files = reader.getLojas(filter);

				// envio para o grupo
				whatsapp.findContact("Eu vivo");
				for (DataFile file : files) {
					int cont = 0;
					try {
						String nameShop = file.getShop().getName();
						String number = file.getPromoter().getNumber();
						String namePromoter = file.getPromoter().getName();
						List<Detail_Product> details_Products = file.getDetail_Products();
						if (number == null) {
							number = "!";
						}
						for (Photo photo : file.getPhotos()) {
							if (whatsapp.sendImage(photo.getUrl()))
								cont++;
						}
						if (cont > 0) {
							whatsapp.sendInfo(file);
							// dataFileRepository.updateStatusDatas(file);
						}
					} catch (Exception e) {
						continue;
					}
				}
				// excluindo as fotos enviadas
				reader.deleteFile();
			}
		}
	}

	//Capturo os 'DataFiles' da 'Brand' e fa�o um filtro para determinado grupo de whatsapp
	//Retorno os dados filtrados
	static List<DataFile> filterDataFile(WhatsappGroup group, List<DataFile> datas) {
		List<DataFile> filterShop = new ArrayList<DataFile>();
		for (Shop shop : group.getShops()) {
			for (DataFile data : datas) {
				try {
					if (shop.getId().equals(data.getShop().getId())) {
						DataFile datafile = new DataFile(data);
						filterShop.add(datafile);
					}
				} catch (Exception e) {
					continue;
				}
			}
		}
		return filterShop;
	}
}
