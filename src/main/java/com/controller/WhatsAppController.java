package com.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.model.Brand;
import com.model.DataFile;
import com.model.DetailProduct;
import com.model.Photo;
import com.model.Shop;
import com.model.WhatsappGroup;
import com.repository.BrandRepository;
import com.repository.DataFileRepository;
import com.repository.WhatsappGroupRepository;
import com.service.WhatsappService;
import com.util.FileReader;
import com.util.ImageURL;
import com.util.Status;

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
	public void run() throws Exception {
		int cont = 1; 
		whatsapp.openWhatsapp();
		while (cont > 0) {
			try {
				for (Brand brand : brands) {
					datas = dataFileRepository.findByBrand(brand.getId());
					routine(brand);
				}
				// Ap�s finalizar, espera 30 segundos para realizar a rotina novamente
				Thread.sleep(300000);
			}catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	private void routine(Brand brand) throws InterruptedException {
		// baixando as imagens
		image = new ImageURL();
		reader = new FileReader();
		List<WhatsappGroup> groups = groupRepository.findAll(brand.getId());

		// Usando a logica que temos que enviar em um grupo de cada vez
		// meu loop seguir� os grupos
		for (WhatsappGroup group : groups) {
			
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
				if(!whatsapp.findContact(group.getName())) continue;
				int cont = 0;
				for (DataFile file : files) {
					// Verifica se � necessario enviar o Detail
					if (group.isSendDetail()) {
						List<DetailProduct> details = dataFileRepository.checkLastSentDetails(file,
								group.getDaysToSend());
						file.setDetailProducts(details);
					}
					try {
						for (Photo photo : file.getPhotos()) {
							if (whatsapp.sendImage(photo.getUrl()))
								cont++;
						}
						if (cont > 0) {
							Status status = whatsapp.sendInfo(file);
							dataFileRepository.updateStatus(file, status);
						}
					} catch (Exception e) {
						// Caso o site do whatsapp trave, ir� lancar uma exception e com isso reinicio o
						// proceso de envio
						e.printStackTrace();
						Thread.sleep(10000);
						whatsapp.openWhatsapp();
						whatsapp.findContact(group.getName());
						continue;
					}
				}
				System.out.println(group.getName()+": ENVIADO");
				// excluindo as fotos enviadas
				reader.deleteFile();
			}
		}
	}

	// Capturo os 'DataFiles' da 'Brand' e fa�o um filtro para determinado grupo
	// de whatsapp
	// Retorno os dados filtrados
	static List<DataFile> filterDataFile(WhatsappGroup group, List<DataFile> datas) {
		List<DataFile> filterShop = new ArrayList<DataFile>();
		try {
			for (Shop shop : group.getShops()) {
				for (DataFile data : datas) {
					try {
						if (shop.getId().equals(data.getShop().getId())) {
							filterShop.add(new DataFile(data.getShop(),data.getPhotos(),data.getBrand(),data.getData(),data.getPromoter(),data.getProject()));
						}
					} catch (Exception e) {
						continue;
					}
				}
			}
		} catch (Exception e) {
		}
		return filterShop;
	}
}
