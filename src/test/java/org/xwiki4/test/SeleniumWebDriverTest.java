package org.xwiki4.test;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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

		driver.get("http://10.0.0.40:8080/xwiki/bin/view/LinkFixer/GUI/");
	    driver.findElement(By.xpath("//form[@id='selectorForm']/dl/dd/div/div")).click();
	    {
	      WebElement element = driver.findElement(By.xpath("//body[@id='body']/div[2]/div/div[2]"));
	      Actions builder = new Actions(driver);
	      builder.moveToElement(element).clickAndHold().perform();
	    }
	    {
	      WebElement element = driver.findElement(By.cssSelector("#selectorForm > dl:nth-child(2) > dt"));
	      Actions builder = new Actions(driver);
	      builder.moveToElement(element).release().perform();
	    }
	    driver.findElement(By.id("body")).click();
	    driver.findElement(By.cssSelector(".not-full")).click();
	    {
	      WebElement element = driver.findElement(By.xpath("//body[@id='body']/div[3]/div/div/span"));
	      Actions builder = new Actions(driver);
	      builder.moveToElement(element).clickAndHold().perform();
	    }
	    {
	      WebElement element = driver.findElement(By.id("xwikicontent"));
	      Actions builder = new Actions(driver);
	      builder.moveToElement(element).release().perform();
	    }
	    driver.findElement(By.id("body")).click();
	    driver.findElement(By.id("numIgnoreURLs")).sendKeys(Keys.BACK_SPACE);
	    driver.findElement(By.id("numIgnoreURLs")).sendKeys("1");
	    driver.findElement(By.id("numIgnoreURLs")).click();
	    driver.findElement(By.id("body")).click();
	    driver.findElement(By.id("ignoreURL0")).click();
	    driver.findElement(By.id("ignoreURL0")).sendKeys("XWikiGuest");
	    driver.findElement(By.id("numOfRecursions")).clear();
	    driver.findElement(By.id("numOfRecursions")).sendKeys("3");
	    driver.findElement(By.id("numOfRecursions")).click();
	    driver.findElement(By.id("numberOfThreads")).clear();
	    driver.findElement(By.id("numberOfThreads")).click();
	    driver.findElement(By.id("numberOfThreads")).sendKeys("5");
	    driver.findElement(By.id("resultLocation")).sendKeys("/home/student/badlinks.html");
	    driver.findElement(By.id("domainToFix")).click();
	    driver.findElement(By.id("domainToFix")).sendKeys("http://10.0.0.40:8080/xwiki/bin/view/TestPageLinkChecker/SimplePage/");
	    driver.findElement(By.id("ajaxButton")).click();
	    driver.findElement(By.id("abortButton")).click();
	    driver.findElement(By.id("username")).click();
	    driver.findElement(By.id("username")).sendKeys("Admin");
	    driver.findElement(By.id("password")).click();
	    driver.findElement(By.id("password")).sendKeys("admin");
	    driver.findElement(By.id("resultLocationSeparate")).sendKeys("/home/student/badlinks.html");
	    driver.findElement(By.id("linkFixerButton")).click();
	    driver.findElement(By.id("resetProcedure")).click();
	  }
}
