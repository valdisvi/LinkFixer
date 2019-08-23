package org.xwiki4.test;

import org.junit.After;
import org.junit.Before;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class SeleniumWebDriverTest {
  private WebDriver driver;

  @Before
  public void setUp() {
	System.setProperty("webdriver.gecko.driver", "lib/geckodriver");
	DesiredCapabilities capabilities = DesiredCapabilities.firefox();
	capabilities.setCapability("marionette", true);
	driver = new FirefoxDriver();
  }
  @After
  public void tearDown() {
    driver.quit();
  }
  @Test
  public void test1() {
	  driver.get("http://localhost:8080/xwiki/bin/view/Main/");
	    driver.manage().window().setSize(new Dimension(630, 698));
	    driver.findElement(By.id("tmDrawerActivator")).click();
	    driver.findElement(By.id("tmLogin")).click();
	    driver.findElement(By.id("j_username")).sendKeys("valdis");
	    driver.findElement(By.id("j_password")).click();
	    driver.findElement(By.id("j_password")).sendKeys("Student007");
	    driver.findElement(By.cssSelector(".btn-primary")).click();
	    driver.findElement(By.cssSelector("#tmEdit .btn-label")).click();
	    driver.findElement(By.name("action_save")).click();
  }
}