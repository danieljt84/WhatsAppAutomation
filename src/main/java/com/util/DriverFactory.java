package com.util;


import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;


public abstract class DriverFactory {
	
	public WebDriver driver;
	public JavascriptExecutor js;
	
	
	
	public DriverFactory() {
		System.setProperty("webdriver.gecko.driver", "C:\\Users\\Daniel\\Downloads\\geckodriver-v0.33.0-win32\\geckodriver.exe");
		ProfilesIni listProfiles = new ProfilesIni();
		FirefoxProfile profile = listProfiles.getProfile("trabalho");
		FirefoxOptions options = new FirefoxOptions();
		options.addArguments("--headless");
		options.setProfile(profile);
		options.setBinary("C:\\Program Files\\Mozilla Firefox\\firefox.exe");
		this.driver = new FirefoxDriver(options);
		this.driver.manage().window();
		this.js = (JavascriptExecutor) driver;   
 	}
	
	public WebDriver getDriver() {
		return driver;
	}
	public JavascriptExecutor getJs() {
		return js;
	}

}
