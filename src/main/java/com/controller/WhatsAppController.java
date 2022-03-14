package com.controller;

import java.util.ArrayList;
import java.util.List;

import com.model.Brand;
import com.model.DataFile;
import com.model.Photo;
import com.model.Shop;
import com.model.WhatsappGroup;
import com.repository.BrandRepository;
import com.repository.DataFileRepository;
import com.repository.WhatsappGroupRepository;
import com.service.WhatsappService;
import com.util.FileReader;
import com.util.ImageURL;

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
		whatsapp.openWhatsapp();
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
					file.setDetail_Products(dataFileRepository.checkLastSentDetails(file, 2));
					try {
						for (Photo photo : file.getPhotos()) {
							if (whatsapp.sendImage(photo.getUrl()))
								cont++;
						}
						if (cont > 0) {
							whatsapp.sendInfo(file);
							dataFileRepository.updateStatus(file);
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
