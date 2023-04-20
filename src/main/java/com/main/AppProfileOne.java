package com.main;

import com.controller.WhatsAppController;

public class AppProfileOne {

	public static void main(String[] args) {
		WhatsAppController whatsAppController = new WhatsAppController();
		try {
			whatsAppController.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}