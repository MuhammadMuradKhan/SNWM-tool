import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.json.*;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class main {
	static WebDriver driver = null;
	static int projectid = 2;
	static String[] pages = null;
	static int postlimit = 0;
	static List<WebElement> postselements = new ArrayList<>();
	static List<String[]> postsdata = new ArrayList<>();
	static List<List> comments = new ArrayList<>();
	static int attemptslimit = 1000;
	static String pagetitle = "";
	static String[][] pids;
	static String[][] retpids;

	public static void main(String args[]) {
		loadpostids();
		initializedriver();
		loginToFacebook();
		downloadfrompostids();
		writecsv.write("postsWithTitle.csv", retpids, 0, 0);
	}

	public static void downloadfrompostids() {
		int i = 0;
		String title = "";
		for (i = 0; i < pids.length; i++) {
			System.out.println(">>>>>>>" + i + "<<<<<<<<<<");
			if (pids[i][0].length() > 4) {
				String[] rett = gettitleofpids(pids[i][0]);
				retpids[i][0] = pids[i][0];
				retpids[i][1] = rett[0].replaceAll(",", "_").replaceAll("\n", ". ");
				retpids[i][2] = rett[1];
				retpids[i][3] = rett[2];
				retpids[i][4] = rett[3];
			}
		}
	}

	public static String[] gettitleofpids(String inp) {
		String comments = "";
		String shares = "";
		String likes = "";
		String message = "";

		/*
		 * try { driver.get("https://m.facebook.com/" + inp); JavascriptExecutor js =
		 * (JavascriptExecutor) driver; //
		 * js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
		 * WebElement element = driver.findElement(By.className("bj")); int content =
		 * element.getAttribute("innerHTML").length(); if (content > 10) { return
		 * Jsoup.parse(element.getAttribute("innerHTML")).text(); } } catch (Exception
		 * e) { } return "EMPTY";
		 */

		try {
			driver.get("https://facebook.com/" + inp);
			System.out.println(driver.getCurrentUrl());
			TimeUnit.SECONDS.sleep(5);
			Actions action = new Actions(driver);
			action.sendKeys(Keys.ESCAPE).perform();
			JavascriptExecutor js = (JavascriptExecutor) driver;
			// js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
			WebElement element = driver.findElement(By.id("pagelet_timeline_main_column"));
			List<WebElement> elements = driver.findElements(By.cssSelector("div[class='_4-u2 _4-u8']"));
			int i = 0;
			for (i = 0; i < elements.size(); i++) {
				String temp = elements.get(i).getAttribute("innerHTML").toString();
				if (temp.contains(inp)) {
					WebElement et1 = elements.get(i).findElement(By.cssSelector("form[class='commentable_item']"));
					WebElement et2 = elements.get(i)
							.findElement(By.cssSelector("a[data-comment-prelude-ref='action_link_bling']"));
					// Document doc = Jsoup.parse(temp);
					Document doc = Jsoup.parse(et2.getAttribute("innerHTML").toString());
					comments = doc.text();
					doc = Jsoup.parse(temp);
					shares = doc.text().split(comments)[1];
					shares = shares.split("shares")[0];
					shares = shares.trim();
					comments = comments.split(" ")[0];
					List<WebElement> et3 = elements.get(i)
							.findElements(By.cssSelector("div[class='_5pbx userContent _3576']"));
					List<WebElement> et4 = elements.get(i)
							.findElements(By.cssSelector("div[class='_3t53 _4ar- _ipn']"));
					doc = Jsoup.parse(et3.get(0).getAttribute("innerHTML").toString());
					message = doc.text();
					List<WebElement> et5 = elements.get(i)
							.findElements(By.cssSelector("div[class='_3t53 _4ar- _ipn']"));
					et5 = et5.get(0).findElements(By.cssSelector("a"));
					doc = Jsoup.parse(et5.get(0).getAttribute("innerHTML").toString());
					likes = doc.text();
					writedata(doc, "comment_check2.htm");
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] ret = new String[4];
		ret[0] = message;
		ret[1] = likes;
		ret[2] = comments;
		ret[3] = shares;
		return ret;
	}

	public static void loadpostids() {
		pids = loadcsv.loadfile("posts.csv");
		retpids = new String[pids.length][5];
	}

	public static void downloadDataFrom(String url) {
		fillposts(url);
		System.out.println("################# POSTS COMPLETE #################");

		// fillcomments();
	}

	public static void sleepprogram(int ms) {
		try {
			// do something
			Thread.sleep(ms);
			// do something after waking up
		} catch (InterruptedException e) {
			// We lose some up to 300 ms of sleep each time this
			// happens... This can be tuned by making more iterations
			// of lesser duration. Or adding 150 ms back to a 'sleep
			// pool' etc. There are many ways to approximate 3 seconds.
			e.printStackTrace();
		}
	}

	public static void fillposts(String url) {
		driver.get(url);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
		WebElement element = driver.findElement(By.id("pagelet_timeline_main_column"));
		int content = element.getAttribute("innerHTML").length();
		sleepprogram(5000);
		int new_content = 0;
		int attempts = 0;
		int totalattempts = 0;
		try {
			while (attempts < attemptslimit && !interupts.end()) {
				while (interupts.pause()) {
					System.out.println("## PAUSE ##");
					sleepprogram(1000);
				}
				js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
				sleepprogram(1000);
				element = driver.findElement(By.id("pagelet_timeline_main_column"));
				new_content = element.getAttribute("innerHTML").length();
				if (new_content == content) {
					attempts++;
					totalattempts++;
				} else {
					System.out.println("Attempt: RESET");
					attempts = 0;
					totalattempts++;
					content = new_content;
				}
				System.out.println("Attempt: [" + attempts + "] ## Total Attempts:[" + totalattempts + "]");
			}
		} catch (Exception e) {
		}
		Document doc = Jsoup.parse(element.getAttribute("innerHTML"));
		writedata(doc, "posts-" + pagetitle + ".html");
	}

	public static void loginToFacebook() {
		driver.get("https://m.facebook.com");
		String title = driver.getTitle();
		System.out.println(title);
		CharSequence username = "username@hostingcompany.com";
		CharSequence password = "password";
		driver.findElement(By.id("m_login_email")).sendKeys(username);
		driver.findElement(
				By.xpath("/html/body/div/div/div[2]/div/table/tbody/tr/td/div[2]/div[2]/form/ul/li[2]/div/input"))
				.sendKeys(password);
		driver.findElement(
				By.xpath("/html/body/div/div/div[2]/div/table/tbody/tr/td/div[2]/div[2]/form/ul/li[3]/input")).click();

	}

	public static String[] getPagesUsingDriver(int pid) {
		driver.get("http://localhost/fbapp/page-select.php?pid=" + pid);
		String inp = driver.getPageSource();
		inp = inp.replaceAll("<html><head></head><body>", "");
		inp = inp.replaceAll("</body></html>", "");
		inp = inp.replaceAll("\\[", "");
		inp = inp.replaceAll("\\]", "");
		String[] temp = inp.split("\\},\\{");
		String[] ret = new String[temp.length];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = temp[i].replaceAll("\\{", "");
			temp[i] = temp[i].replaceAll("\\}", "");
			temp[i] = temp[i].replaceAll("\\\\", "");
			temp[i] = "{" + temp[i] + "}";
			JSONObject obj = new JSONObject(temp[i]);
			ret[i] = obj.getString("pageid");
			ret[i] = ret[i] + "::" + obj.getString("pageTitle");
			ret[i] = ret[i] + "::" + obj.getString("pageURL");
		}
		return ret;
	}

	public static void initializedriver() {
		FirefoxOptions options = new FirefoxOptions();
		options.addPreference("permissions.default.image", 2);
		options.addPreference("media.autoplay.enabled", false);
		System.setProperty("webdriver.gecko.driver", "driver.exe");
		driver = new FirefoxDriver(options);
	}

	public static void writedata(Document doc, String filename) {
		Writer writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"));
			writer.write(doc.toString());
		} catch (IOException ex) {
			// Report
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {/* ignore */
			}
		}

	}

}
