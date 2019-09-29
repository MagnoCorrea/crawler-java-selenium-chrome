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

		int acc = 0;
		
		boolean continueCrawling = true;
		while (continueCrawling) {
			readElements(driver);
			goToNextPage(driver); whait();
			acc++;
		}
		System.out.println(acc + " pages processed.");
		driver.quit();
	}

	private static boolean goToNextPage(WebDriver driver) {
		List<WebElement> elements = driver.findElements(By.xpath("//ul[@class='pagination']/li/a"));
		boolean findedActive = false;
		for (WebElement element : elements) {
			if("active".equals(getParent(element).getAttribute("class"))) {
				findedActive = true;
			}
			else{
				if(findedActive) {
					element.click();
					return true;
				}
			}
		}
		return false; // it's the lest element. Sorry, the crawling is over. :)
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
			Unit unit = new Unit();
			for (WebElement tmp : tmps) {
				unit.url = tmp.getAttribute("href");
				unit.title = tmp.findElement(By.xpath("..//h3")).getText();
			}
			List<WebElement> amenities = element.findElements(By.xpath("./div[@class='property-amenities']"));
			for (WebElement tmp : amenities) {
				String strAmenity = tmp.getText().replace('\n', '\t');
				unit.amenities.add(strAmenity);
			}

			WebElement costs = element.findElement(By.xpath(".//div[@id='valores-grid']/div[@class='div-valores']"));
			String status = costs.findElement(By.xpath("..//span[@class='thumb-status']")).getText();
			unit.costs.add(status);

			String itemPrice = "";
			try {
				itemPrice = costs.findElement(By.xpath("..//span[@class='thumb-price']")).getText();
			} catch (Exception e) {
				itemPrice = "no price";
			}
			unit.costs.add(itemPrice);
			
			List<WebElement> otherCosts = costs.findElements(By.xpath("./..//div[@class='div-cond-iptu']/span"));
			for (WebElement tmp : otherCosts) {
				String cost = tmp.getText();
				unit.costs.add(cost);
			}
			
			System.out.println(unit);
		}
	}
	
	private static WebElement getParent(WebElement childElement) {
		WebElement parentElement = childElement.findElement(By.xpath("./.."));
		return parentElement;
	}
}
