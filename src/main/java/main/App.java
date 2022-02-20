package main;

import controller.WhatsAppController;

public class App {

	public static void main(String[] args) {
		WhatsAppController whatsAppController = new WhatsAppController();
		try {
			whatsAppController.run();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}