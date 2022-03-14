package com.service;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.model.DataFile;
import com.model.DetailProduct;
import com.util.DriverFactory;
import com.util.Status;

public class WhatsappService extends DriverFactory {

	WebElement element;

	public void openWhatsapp() {
		driver.get("https://web.whatsapp.com/");
		try {
			Thread.sleep(40000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void findContact(String contact) {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		element = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='side']/div[1]/div/label/div/div[2]")));
		element.clear();
		element.sendKeys(contact);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		element.sendKeys(Keys.ENTER);
		checkNameContact(contact);
	}

	public boolean sendImage(String file) {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		try {
			driver.findElement(By.xpath("//*[contains(@title, 'Anexar')]"));
			js.executeScript("var element = arguments[0];" + "element.click();",
					driver.findElement(By.xpath("//*[contains(@title, 'Anexar')]")));
			element = driver.findElement(By.cssSelector("input[type='file']"));
			element.sendKeys(file);
			Thread.sleep(1000);
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.className("_165_h")));
			js.executeScript("var element = arguments[0];" + "element.click();",
					element);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
    //Envio as informacaos dos promotores que fizeram o envio das fotos e relatï¿½rio
	//Se retorno igual a verdadeiro, as informações foram enviadas completas
	//Se retorno igual a falso, foi enviado apenas as fotos
	public Status sendInfo(DataFile datafile) {
		Status status;
		try {
			// checkLastImageSend();
			Thread.sleep(2000);
			element = driver
					.findElement(By.xpath("//*[@id=\'main\']/footer/div[1]/div/span[2]/div/div[2]/div[1]/div/div[2]"));
			element.sendKeys("*" + datafile.getShop() + "*");
			element.sendKeys(Keys.CONTROL, Keys.ENTER);
			sendNumberAndName(datafile.getPromoter().getNumber(), datafile.getPromoter().getName(), element);
			status = (checkDetail(datafile, element) ? Status.COMPLETE : Status.ONLY_PHOTOS);
			Thread.sleep(2000);
			element.sendKeys(Keys.ENTER);
			return status;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void sendNumberAndName(String number, String name, WebElement element) {
		element.sendKeys(name);
		element.sendKeys(Keys.CONTROL, Keys.ENTER);
		element.sendKeys(" @" + number);
	}

	public void checkNameContact(String contact) {
		String name = driver.findElement(By.className("ggj6brxn")).getText();
		if (name.toLowerCase().contains(contact.toLowerCase()))
			findContact(contact);
	}
    
	public void checkLastImageSend() throws Exception {
		boolean _return = false;
		do {
			List<WebElement> elements = driver.findElements(By.className("_2wUmf"));
			WebElement element = elements.get(elements.size() - 1).findElement(By.xpath("./span"));
			if (element != null) {
				_return = true;
			}
		} while (_return == false);
	}

	// Verifico se tem alguma pesquisa relacionado a loja e se esta na data certa
	public boolean checkDetail(DataFile datafile, WebElement element) {
		boolean _return = false;
		if (datafile.getDetail_Products() != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			element.sendKeys(Keys.CONTROL, Keys.ENTER);
			for (DetailProduct detail_Product : datafile.getDetail_Products()) {
				if (detail_Product.getRuptura().contains("NÃƒO")) {
					element.sendKeys(Keys.CONTROL, Keys.ENTER);
					element.sendKeys(detail_Product.getProduct().getName());
					element.sendKeys(Keys.CONTROL, Keys.ENTER);
					element.sendKeys("VALOR: " + detail_Product.getPrice().toString());
					element.sendKeys(Keys.CONTROL, Keys.ENTER);
					element.sendKeys("VALIDADE: " + detail_Product.getValidity().format(formatter));
					element.sendKeys(Keys.CONTROL, Keys.ENTER);
					element.sendKeys("ESTOQUE: " + String.valueOf(detail_Product.getStock()));
					element.sendKeys(Keys.CONTROL, Keys.ENTER);
					element.sendKeys(Keys.CONTROL, Keys.ENTER);
				} else {
					if (detail_Product.getRuptura().contains("SÃ“ PDV")) {
						element.sendKeys(Keys.CONTROL, Keys.ENTER);
						element.sendKeys(detail_Product.getProduct().getName());
						element.sendKeys(Keys.CONTROL, Keys.ENTER);
						element.sendKeys("VALOR: " + detail_Product.getPrice().toString());
						element.sendKeys(Keys.CONTROL, Keys.ENTER);
						element.sendKeys("VALIDADE: " + detail_Product.getValidity().format(formatter));
						element.sendKeys(Keys.CONTROL, Keys.ENTER);
						element.sendKeys("ESTOQUE: " + String.valueOf(detail_Product.getStock()));
						element.sendKeys(Keys.CONTROL, Keys.ENTER);
						element.sendKeys("RUPTURA: " + String.valueOf(detail_Product.getRuptura()));
						element.sendKeys(Keys.CONTROL, Keys.ENTER);
						element.sendKeys(Keys.CONTROL, Keys.ENTER);
					} else {
						element.sendKeys(Keys.CONTROL, Keys.ENTER);
						element.sendKeys(detail_Product.getProduct().getName());
						element.sendKeys(Keys.CONTROL, Keys.ENTER);
						element.sendKeys("RUPTURA: " + String.valueOf(detail_Product.getRuptura()));
						element.sendKeys(Keys.CONTROL, Keys.ENTER);
						element.sendKeys(Keys.CONTROL, Keys.ENTER);
					}
				}
			}
			_return = true;
		}
		return _return;
	}
}
