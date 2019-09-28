package com.magnocorrea.crawler_java_selenium_chrome.sandbox;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * A simple main to be an great sandbox! :)
 * @author magno@magnocorrea.com
 *
 */
public class Main {

	public static void main(String[] args) {
		System.out.println("Hello Wold!!!");
		// Init chromedriver
		String chromeDriverPath = ApplicationProperties.INSTANCE.get("chromeDriverPath") ;  
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);  
		ChromeOptions options = new ChromeOptions();  
		options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200","--ignore-certificate-errors");  
		WebDriver driver = new ChromeDriver(options);  

		driver.get("https://imobiliariaprates.com.br/aluguel/tipo/cidadebairro/");
		whait();
		whait();

		closeModalPopUp(driver);
		whait();
		readElements(driver);
		
		/*
		 * goToNextPage(driver); whait(); readElements(driver);
		 */		
		driver.quit();
	}

	private static void goToNextPage(WebDriver driver) {
		List<WebElement> elements = driver.findElements(By.xpath("//ul[@class='pagination']/li/a"));
		boolean findedActive = false;
		for (WebElement element : elements) {
			if("active".equals(getParent(element).getAttribute("class"))) {
				findedActive = true;
			}
			else{
				if(findedActive) {
					element.click();
					return;
				}
			}
		}
	}
	
	private static void whait() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void closeModalPopUp(WebDriver driver) {
		WebElement element = driver.findElement(By.xpath("//div[@class='modal-exit']"));
		element.click();
		
	}

	private static void readElementsPage(WebDriver driver) {
		String in = driver.getPageSource();
		System.out.println(in);
	}
	private static void readElements(WebDriver driver) {
//		List<WebElement> elements = driver.findElements(By.xpath("//div[@class='col-xs-12 grid-imovel']/div/a"));
		List<WebElement> elements = driver.findElements(By.xpath("//div[@class='limited-block-info']"));

		for (WebElement element : elements) {
			List<WebElement> tmps = element.findElements(By.xpath("./a"));
			String url = "";
			String titulo = "";
			for (WebElement tmp : tmps) {
				url = tmp.getAttribute("href");
				titulo = tmp.findElement(By.xpath("..//h3")).getText();
			}
			String strAmenidades = "";
			List<WebElement> amenidades = element.findElements(By.xpath("./div[@class='property-amenities']"));
			for (WebElement tmp : amenidades) {
				strAmenidades += tmp.getText();
			}
			strAmenidades = strAmenidades.replace('\n', '\t');
			/*
			 * tmp = tmp.findElement(By.xpath("./h3")); String titulo = tmp.getText();
			 */
			System.out.println(titulo + "\t" + strAmenidades + "\t" + url);
		}
	}
	
	private static WebElement getParent(WebElement childElement) {
		WebElement parentElement = childElement.findElement(By.xpath("./.."));
		return parentElement;
	}
}
