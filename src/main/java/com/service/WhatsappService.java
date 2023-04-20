package com.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.model.DataFile;
import com.model.DetailProduct;
import com.model.Promoter;
import com.util.DriverFactory;
import com.util.Status;

import javassist.bytecode.stackmap.BasicBlock.Catch;
import net.bytebuddy.implementation.bytecode.Throw;

public class WhatsappService extends DriverFactory {

	WebElement element;
	DriverFactory driverFactory;

	public void openWhatsapp() {
		driver.get("https://web.whatsapp.com/");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean findContact(String contact) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
		element = wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div/div[4]/div/div[1]/div/div/div[2]/div/div[1]")));
		try {
			clearPOfContact();
		}catch (Exception e) {
			// TODO: handle exception
		}
		sendTextInElement(contact,element);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		element.sendKeys(Keys.ENTER);
		return checkNameContact(contact);
	}

	public boolean sendImage(String file) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
		try {
			WebElement element = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(@title, 'Anexar')]")));
			driver.findElement(By.xpath("//*[contains(@title, 'Anexar')]"));
			js.executeScript("var element = arguments[0];" + "element.click();", element);
			element = driver.findElement(By.cssSelector("input[type='file']"));
			element.sendKeys(file);
			Thread.sleep(2000);
			element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/div[3]/div[2]/span/div/span/div/div/div[2]/div/div[2]/div[2]/div/div")));
 			element.click();
			Thread.sleep(2000);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("ERRO DE ENVIO DE FOTO: " + LocalDate.now());
		}

	}

	// Envio as informacaos dos promotores que fizeram o envio das fotos e relat�rio
	// Se retorno igual a verdadeiro, as informa��es foram enviadas completas
	// Se retorno igual a falso, foi enviado apenas as fotos
	public Status sendInfo(DataFile datafile) {
		Status status;
		try {
			// checkLastImageSend();
			Thread.sleep(2000);
			element = driver
					.findElement(By.xpath("/html/body/div[1]/div/div/div[5]/div/footer/div[1]/div/span[2]/div/div[2]/div[1]/div/div[1]"));
			sendText("*" + datafile.getShop().getName() + "*");
			element.sendKeys(Keys.CONTROL, Keys.ENTER);
			sendNumberAndName(Optional.ofNullable(datafile.getPromoter()), element);
			status = (checkDetail(datafile, element) ? Status.COMPLETE : Status.ONLY_PHOTOS);
			Thread.sleep(2000);
			element.sendKeys(Keys.ENTER);
			return status;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void sendNumberAndName(Optional<Promoter> promoter, WebElement element) {
		if (promoter.isEmpty() != true) {
			sendText(promoter.get().getName());
		}
		element.sendKeys(Keys.CONTROL, Keys.ENTER);
	}

	public boolean checkNameContact(String contact) {
		String name;
		try {
			name = driver.findElement(By.xpath("/html/body/div[1]/div/div/div[5]/div/header/div[2]/div[1]/div/span"))
					.getText();
		} catch (Exception e) {
			name = " ";
		}
		if (!name.toLowerCase().equals(contact.toLowerCase())) {
			System.out.println("NOME NÃO ENCONTRADO: " + contact);
			return false;
		}
		return true;
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
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			element.sendKeys(Keys.CONTROL, Keys.ENTER);
			for (DetailProduct detail_Product : datafile.getDetailProducts()) {
				if (detail_Product.getRuptura().equals("NÃO")) {
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
					if (detail_Product.getRuptura().contains("S� PDV")) {
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
		} catch (Exception e) {
			return _return;
		}
		return _return;
	}
	
	public void clearPOfContact() {
		var element = driver
				.findElement(By.xpath("/html/body/div[1]/div/div/div[4]/div/div[1]/div/div/span/button"));	
		element.click();
	}

	private void sendText(String text) {
		element = driver.findElement(By
				.xpath("/html/body/div[1]/div/div/div[5]/div/footer/div[1]/div/span[2]/div/div[2]/div[1]/div/div[1]"));
		var char_text = text.toCharArray();
		for (char _char : char_text) {
			element.sendKeys(String.valueOf(_char));
		}
	}
	
	public void sendTextInElement(String text, WebElement element) {
	    var char_text = text.toCharArray();
	    for(char _char : char_text) {
	    	element.sendKeys(String.valueOf(_char));
	    }
	}
}
