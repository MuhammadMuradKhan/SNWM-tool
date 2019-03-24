import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
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
	static int postlimit = 10;
	static List<WebElement> postselements = new ArrayList<>();
	static List<String[]> postsdata = new ArrayList<>();
	static List<List> comments = new ArrayList<>();
	static int attemptslimit = 1000;
	static String pagetitle = "";
	static int limit_month = 00;
	static int limit_year = 00;
	static int limit_day = 00;
	static LinkedList<String> postsids = new LinkedList<String>();

	public static void main(String args[]) {
		// NLP.init();
		interupts.reset();
		initializedriver();
		loginToFacebook();
		pagetitle = "imdb";
		String pageurl = "https://facebook.com/" + pagetitle;
		postlimit = 100;
		limit_year = 2019;
		limit_month = 2;
		limit_day = 1;
		downloadDataFrom(pageurl);
		displaydata("posts-" + pagetitle + ".html");
	}

	public static void downloadDataFrom(String url) {
		fillposts(url);
		System.out.println("################# POSTS CRAWLING COMPLETE #################");
		

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
		String scontent = element.getAttribute("innerHTML").toString();
		int content = scontent.length();
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
				scontent = element.getAttribute("innerHTML").toString();
				new_content = scontent.length();

				if (new_content == content) {
					attempts++;
					totalattempts++;
				} else {
					System.out.println("Attempt: RESET");
					attempts = 0;
					totalattempts++;
					content = new_content;
				}
				fillpostsid(scontent);
				System.out.println("Attempt: [" + attempts + "] ## Total Attempts:[" + totalattempts + "]");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Document doc = Jsoup.parse(scontent);
		writedata(doc, "posts-" + pagetitle + ".html");
		writecsv.writegenericsingle("posts.csv", postsids);
	}

	public static void fillpostsid(String inp) {
		String[] inps = inp.split("feed_subtitle");
		int inpslimit = inps.length;
		for (int i = 1; i < inpslimit; i++) {
			try {
				if (inps[i].length() > 100) {
					String post = inps[i].split(";")[1];
					// System.out.println(post);
					if (!postsids.contains(post)) {
						postsids.add(post);
					}
				}
			} catch (Exception e) {
			}
		}
		System.out.println("Posts identified:" + postsids.size());
		if (postlimit <= postsids.size()) {
			interupts.makeend();
		}
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

	public static void displaydata(String filename) {

		String line = null;
		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(filename);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				if (line.contains("feed_subtitle")) {
					System.out.println(line);
				}
			}

			// Always close files.
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + filename + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + filename + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}
	}

}
