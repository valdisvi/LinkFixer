package org.xwiki4.test;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.concurrent.TimeUnit;

public class SeleniumWebDriverTest {

	private WebDriver driver;

	@Before
	public void setUp() {
		System.setProperty("webdriver.gecko.driver", "lib/geckodriver");
		DesiredCapabilities capabilities = DesiredCapabilities.firefox();
		capabilities.setCapability("marionette", true);
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
	}

	@After
	public void tearDown() {
		driver.quit();
	}

	@Test
	public void linkFixerGUI() {
		driver.get("http://localhost:8080/xwiki/bin/view/LinkFixer/GUI/");
		driver.findElement(By.xpath("//div[@id=\'xwikicontent\']/p[3]/div/div")).click();
		{
			WebElement element = driver.findElement(By.xpath("//body[@id=\'body\']/div[2]/div/div[2]/span"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).clickAndHold().perform();
		}
		{
			WebElement element = driver.findElement(By.id("outputFile-selectized"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).release().perform();
		}
		driver.findElement(By.cssSelector(".not-full")).click();
		{
			WebElement element = driver.findElement(By.xpath("//body[@id='body']/div[3]/div/div"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).clickAndHold().perform();
		}
		{
			WebElement element = driver.findElement(By.cssSelector("p:nth-child(4)"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).release().perform();
		}
		driver.findElement(By.id("body")).click();
		driver.findElement(By.id("numOfRecursions")).clear();
		driver.findElement(By.id("numOfRecursions")).click();
		driver.findElement(By.id("numOfRecursions")).sendKeys("1");
		driver.findElement(By.id("numberOfThreads")).clear();
		driver.findElement(By.id("numberOfThreads")).click();
		driver.findElement(By.id("numberOfThreads")).sendKeys("5");
		driver.findElement(By.id("resultLocation")).click();
		driver.findElement(By.id("resultLocation")).sendKeys("/home/student/linkchecker-out.html");
		driver.findElement(By.id("domainToFix")).click();
		driver.findElement(By.id("domainToFix")).sendKeys("http://localhost:8080/xwiki/bin/view/Test1/BadLinks/");
		driver.findElement(By.id("ajaxButton")).click();
		driver.findElement(By.id("abortButton")).click();
		driver.findElement(By.id("linkFixerButton")).click();
	}
}
