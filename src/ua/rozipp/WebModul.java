package ua.rozipp;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

public class WebModul {
	private WebDriver driver;
	private JavascriptExecutor js;

	public WebDriver getDriver(){
		return driver;
	}
	
	public WebModul() {
		logginSite();
	}

	public void delete() {
		if (driver != null) driver.quit();
	}

	public void logginSite() {
		// System.setProperty("webdriver.chrome.driver", "/WebDriver/chromedriver.exe");
		System.setProperty("webdriver.gecko.driver", "./WebDriver/geckodriver.exe");
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		this.js = (JavascriptExecutor) driver;

		driver.get("http://kiptivska.gromada.org.ua/");
		driver.findElement(By.cssSelector(".grid-25 > .open-popup > u")).click();
		{
			WebElement element = driver.findElement(By.cssSelector(".grid-25 > .open-popup > u"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		driver.findElement(By.id("login")).sendKeys("kiptivska");
		driver.findElement(By.id("password")).sendKeys("cce9437d");
		driver.findElement(By.name("pAction")).click();
	}

	public void writeElement(String doc_title, String number, String from_date, String doc_convocation, String doc_session, String doc_name, String doc_file) throws WebDriverException {
		if (driver == null) throw new WebDriverException("Не открыто окно браузера");
		driver.get("https://gromada.org.ua/gromada/kiptivska/docs/");
		driver.findElement(By.partialLinkText("Додати документ")).click();

		driver.findElement(By.id("doc_title")).sendKeys(doc_title);
		driver.findElement(By.id("number")).sendKeys(number);
		{
			WebElement el = driver.findElement(By.id("from_date"));
			el.clear();
			el.sendKeys(from_date);
		}
		driver.findElement(By.id("add_doc_convocation")).click();
		driver.findElement(By.id("add_doc_convocation")).findElement(By.xpath("//option[. = '" + doc_convocation + "']")).click();
		driver.findElement(By.id("add_doc_session")).click();
		driver.findElement(By.id("add_doc_session")).findElement(By.xpath("//option[. = '" + doc_session + "']")).click();
		
//		driver.findElement(By.id("doc_session")).click();
//		driver.findElement(By.id("doc_session")).findElement(By.xpath("//option[. = '" + doc_session + "']")).click();

		driver.findElement(By.name("doc_name[]")).sendKeys(doc_name);

		js.executeScript("b=document.getElementsByName('doc_file[]')[0];" //
				+ "b.style.position=\"relative\";" //
				+ "b.style.opacity=100;");
		driver.findElement(By.name("doc_file[]")).sendKeys(doc_file);
		driver.findElement(By.name("pAction")).click();
	}

	public void editRishennya(String nomerRishennya, String doc_file) throws WebDriverException {
		if (driver == null) throw new WebDriverException("Не открыто окно браузера");
		driver.findElement(By.linkText("Редагувати")).click();

		String doc_name = driver.findElement(By.id("doc_title")).getAttribute("value");
		System.out.println(doc_name);

		driver.findElement(By.name("doc_name[]")).sendKeys(doc_name);

		js.executeScript("b=document.getElementsByName('doc_file[]')[0];" //
				+ "b.style.position=\"relative\";" //
				+ "b.style.opacity=100;");
		driver.findElement(By.name("doc_file[]")).sendKeys(doc_file);

		driver.findElement(By.name("pAction")).click();
	}

	public void writeElementAndRishennya(String doc_title, String number, String from_date, String doc_convocation, String doc_session, String doc_name, String doc_file, String doc_file2) throws WebDriverException {
		if (driver == null) throw new WebDriverException("Не открыто окно браузера");
		driver.get("https://gromada.org.ua/gromada/kiptivska/docs/");
		driver.findElement(By.partialLinkText("Додати документ")).click();

		driver.findElement(By.id("doc_title")).sendKeys(doc_title);
		driver.findElement(By.id("number")).sendKeys(number);
		{
			WebElement el = driver.findElement(By.id("from_date"));
			el.clear();
			el.sendKeys(from_date);
		}
		driver.findElement(By.id("add_doc_convocation")).click();
		driver.findElement(By.id("add_doc_convocation")).findElement(By.xpath("//option[. = '" + doc_convocation + "']")).click();
		driver.findElement(By.id("add_doc_session")).click();
		driver.findElement(By.id("add_doc_session")).findElement(By.xpath("//option[. = '" + doc_session + "']")).click();

		js.executeScript("b=document.getElementsByName('doc_file[]')[0];" //
				+ "b.style.position=\"relative\";" //
				+ "b.style.opacity=100;");
		driver.findElements(By.name("doc_file[]")).get(0).sendKeys(doc_file);
		driver.findElements(By.name("doc_name[]")).get(0).sendKeys(doc_name);

		driver.findElement(By.className("add_new_file")).click();
		js.executeScript("b=document.getElementsByName('doc_file[]')[1];" //
				+ "b.style.position=\"relative\";" //
				+ "b.style.opacity=100;");
		driver.findElements(By.name("doc_file[]")).get(1).sendKeys(doc_file2);
		driver.findElements(By.name("doc_name[]")).get(1).sendKeys(doc_title);

		driver.findElement(By.name("pAction")).click();
	}

	public String openFindRishennya(String nomerRishennya) throws WebDriverException {
		if (driver == null) throw new WebDriverException("Не открыто окно браузера");
		driver.get("https://gromada.org.ua/gromada/kiptivska/docs/");
		driver.findElement(By.name("number")).click();
		driver.findElement(By.name("number")).sendKeys(nomerRishennya);
		driver.findElement(By.name("filter")).click();
		String title;
		{
			WebElement element = driver.findElement(By.className("one_doc"));
			title = element.findElement(By.tagName("a")).getText();
			element.click();
		}

		{
			WebElement element = driver.findElement(By.className("file_ol"));
			List<WebElement> eles = element.findElements(By.tagName("a"));
			if (eles.size() == 0) return null;
			if (eles.size() == 1) return title + "\t" + "";
			return title + "\t" + eles.get(1).getAttribute("href");
		}
	}

	public String deleteFindRishennya(String nomerRishennya) throws WebDriverException {
		if (driver == null) throw new WebDriverException("Не открыто окно браузера");
		driver.get("https://gromada.org.ua/gromada/kiptivska/docs/");
		driver.findElement(By.name("number")).click();
		driver.findElement(By.name("number")).sendKeys(nomerRishennya);
		driver.findElement(By.name("filter")).click();
		String title;
		{
			WebElement element = driver.findElement(By.className("one_doc"));
			title = element.findElement(By.tagName("a")).getText();
			element.click();
		}
		
		driver.findElement(By.linkText("Видалити документ")).click();
		driver.findElement(By.className("btn btn-yellow")).click();
		driver.findElement(By.className("mfp-close")).click();
		return title;
	}
}
