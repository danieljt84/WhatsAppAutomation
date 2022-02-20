package util;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;



public abstract class DriverFactory {
	
	public WebDriver driver;
	public JavascriptExecutor js;
	
	public DriverFactory() {
		System.setProperty("webdriver.edge.driver", "C:\\Users\\daniel\\Desktop\\driver\\msedgedriver.exe");
		this.driver = new _ChromeDriver();
		this.driver.manage().window();
		js = (JavascriptExecutor) driver;  
 	}

}
