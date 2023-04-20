package com.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;



public class _ChromeDriver extends FirefoxDriver {
	
	public _ChromeDriver(FirefoxOptions options) {
		super(options);
	}
	
	
	@Override
	public WebElement findElement(By by) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return super.findElement(by);
	}
	
	
}
