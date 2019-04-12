import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
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
	static LinkedList<String[]> commentsdata = new LinkedList<String[]>();
	static List<List> comments = new ArrayList<>();
	static int attemptslimit = 1000;
	static String pagetitle = "";
	static String[][] pids;
	static String[][] retpids;
	static int hasnextcount = 0;

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
				if (pids[i][0].contains("\"")) {
					pids[i][0] = pids[i][0].replace("\"", "");
				}
				String[] rett = gettitleofpids(pids[i][0]);

				writecsv.writegeneric1DS("comments_" + pids[i][0] + ".csv", commentsdata);
			}
		}
	}

	public static int hasnextfun() {
		try {
			WebElement hasNext = null;
			hasNext = driver.findElement(By.linkText("View previous comments�"));
			return hasnextcount = 1;
		} catch (Exception e) {
			e.printStackTrace();
			return hasnextcount = 0;
		}
	}

	public static List<WebElement> analyzecomments(String inp) {
		List<WebElement> ret = null;
		int ok = 0;
		try {
			if (ok == 0) {
				WebElement element = driver.findElement(By.cssSelector("div[id='ufi_" + inp + "']"));
				List<WebElement> elementsub = element.findElements(
						By.xpath("/html/body/div/div/div[2]/div/div[1]/div/div/div[3]/div[2]/div/div/div[3]"));
				System.out.println(elementsub.size());
				ret = elementsub.get(0).findElements(By.cssSelector("div[id]"));
				System.out.println(ret.size());
				if (ret.size() >= 1) {
					ok = 1;
				} else {
					Exception e = null;
					throw e;
				}
			}
		} catch (Exception e) {
		}
		try {
			if (ok == 0) {
				WebElement element = driver.findElement(By.cssSelector("div[id='ufi_" + inp + "']"));
				List<WebElement> elementsub = element
						.findElements(By.xpath("/html/body/div/div/div[2]/div/div[1]/div[2]/div/div[4]"));
				System.out.println(elementsub.size() + elementsub.get(0).getText());
				ret = elementsub.get(0).findElements(By.cssSelector("div[id]"));
				System.out.println(ret.size());
				if (ret.size() >= 1) {
					ok = 1;
				} else {
					Exception e = null;
					throw e;
				}
			}

		} catch (Exception e) {
		}
		try {
			if (ok == 0) {
				WebElement element = driver.findElement(By.cssSelector("div[id='ufi_" + inp + "']"));
				List<WebElement> elementsub = element
						.findElements(By.xpath("/html/body/div/div/div[2]/div/div[1]/div[2]/div/div[5]"));
				System.out.println(elementsub.size() + elementsub.get(0).getText());
				ret = elementsub.get(0).findElements(By.cssSelector("div[id]"));
				System.out.println(ret.size() + ret.get(0).getText());
				if (ret.size() >= 1) {
					ok = 1;
				} else {
					Exception e = null;
					throw e;
				}
			}

		} catch (Exception e) {
		}
		try {
			if (ok == 0) {
				WebElement element = driver.findElement(By.cssSelector("div[id='ufi_" + inp + "']"));
				List<WebElement> elementsub = element
						.findElements(By.xpath("/html/body/div/div/div[2]/div/div[1]/div/div/div[3]/div[2]/div/div/div[4]"));
				System.out.println(elementsub.size() + elementsub.get(0).getText());
				ret = elementsub.get(0).findElements(By.cssSelector("div[id]"));
				System.out.println(ret.size() + ret.get(0).getText());
				if (ret.size() >= 1) {
					ok = 1;
				} else {
					Exception e = null;
					throw e;
				}
			}

		} catch (Exception e) {
		}		

		return ret;
	}

	public static String[] gettitleofpids(String inp) {
		/*
		 * try { driver.get("https://m.facebook.com/" + inp); JavascriptExecutor js =
		 * (JavascriptExecutor) driver; //
		 * js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
		 * WebElement element = driver.findElement(By.className("bj")); int content =
		 * element.getAttribute("innerHTML").length(); if (content > 10) { return
		 * Jsoup.parse(element.getAttribute("innerHTML")).text(); } } catch (Exception
		 * e) { } return "EMPTY";
		 */
		int localhncheck = 0;
		try {

			localhncheck = hasnextcount;
			if (inp.contains("_")) {
				inp = inp.split("_")[1];
			}
			driver.get("https://m.facebook.com/" + inp);
			TimeUnit.SECONDS.sleep(5);
			WebElement hasNext = null;
			List<WebElement> commentslist = null;
			localhncheck = hasnextfun();
			if (localhncheck == 0) {
				commentslist = analyzecomments(inp);
			}
			if (localhncheck == 1) {
				commentslist = analyzecomments(inp);
			}

			while (hasnextcount == 1 || commentslist.get(0).getAttribute("innerHTML").length() > 20) {
				List<WebElement> cids = commentslist;
				int i = 0;
				for (i = 0; i < cids.size(); i++) {
					String[] temp = new String[4];
					temp[0] = temp[1] = temp[2] = temp[3] = "empty";
					try {
						WebElement d = cids.get(i);
						temp[0] = d.getAttribute("id");
						temp[1] = d.findElement(By.cssSelector("h3")).findElement(By.cssSelector("a"))
								.getAttribute("href").toString();
						temp[1] = temp[1].split("\\?")[0];
						temp[2] = d.findElement(By.cssSelector("h3")).findElement(By.cssSelector("a")).getText();
						temp[3] = d.findElement(By.cssSelector("div")).findElement(By.cssSelector("div")).getText();
						temp[3] = temp[3].replaceAll(",", "_").replaceAll("\n", ". ");
					} catch (Exception ees) {
					}
					commentsdata.add(temp);
				}
				if (localhncheck == 1) {
					driver.findElement(By.linkText("View previous comments�")).click();
					TimeUnit.SECONDS.sleep(5);
				} else {
					commentslist.clear();
					break;
				}
				localhncheck = hasnextfun();
				if (localhncheck == 0) {
					commentslist = analyzecomments(inp);
				}
				if (localhncheck == 1) {
					commentslist = analyzecomments(inp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] ret = new String[4];
		return ret;
	}

	public static void loadpostids() {
		pids = loadcsv.loadfile("fdg_comments2.csv");
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
		CharSequence username = "muhammadmurad@gcuf.edu.pk";
		CharSequence password = "fbAlpha@782129";
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
