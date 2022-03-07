package com.main;

import com.model.DataFile;
import com.model.Shop;
import com.repository.DataFileRepository;

public class Teste {

	public static void main(String[] args) {
		DataFileRepository dataFileRepository = new DataFileRepository();
		DataFile dataFile = new DataFile();
	    dataFile.setId(275);
	    Shop shop = new Shop();
	    shop.setId(1223l);
	    dataFile.setShop(shop);
		dataFileRepository.checkLastSentDetails(dataFile, 2);
	}
}
