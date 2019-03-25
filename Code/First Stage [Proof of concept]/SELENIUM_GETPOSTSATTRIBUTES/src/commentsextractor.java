import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class commentsextractor {
	public static Elements getCommentsFromMainDiv(Document doc) {
		try {
			Elements commentsHTML = doc.select("div[id*=\"\"]");
			String[] divIdNames = new String[commentsHTML.size()];
			Elements commentslist = new Elements();
			for (int k = 0; k < divIdNames.length; k++) {
				String id = commentsHTML.get(k).id();
				if (isnumber(id)) {
					commentslist.add(commentsHTML.get(k));
				}
				System.out.println(id);
			}
			return commentslist;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Element getPreviousPageFromMainDiv(Document doc) {
		try {
			Elements commentsHTML = doc.select("div[id*=\"\"]");
			String[] divIdNames = new String[commentsHTML.size()];
			Element prevLink = null;
			for (int k = 0; k < divIdNames.length; k++) {
				String id = commentsHTML.get(k).id();
				if (id.contains("see_prev_")) {
					prevLink = commentsHTML.get(k);
				}
				System.out.println(id);
			}
			return prevLink;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Element getNextPageFromMainDiv(Document doc) {
		try {
			Elements commentsHTML = doc.select("div[id*=\"\"]");
			String[] divIdNames = new String[commentsHTML.size()];
			Element nextLink = null;
			for (int k = 0; k < divIdNames.length; k++) {
				String id = commentsHTML.get(k).id();
				if (id.contains("see_next_")) {
					nextLink = commentsHTML.get(k);
				}
				System.out.println(id);
			}
			return nextLink;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String[] getCommentsValuesFromHTML(Element ele, String postid) {
		try {
			String[] inp = new String[4];

			Elements tel = ele.select("div");

			// USERID
			Elements elink = tel.get(1).select("h3");
			String link = elink.get(0).select("a").toString();
			// System.out.println(tel.get(1).toString());
			String[] tempx = null;
			if (link.contains("profile.php")) {
				tempx = link.split("id=");
				tempx = tempx[1].split("&amp");
			} else {
				tempx = link.split("href=\"/");
				// System.out.println(link);
				tempx = tempx[1].split("\\?rc=");
			}

			String userid = tempx[0];
			// COMMENT
			String comment = tel.get(2).text();
			// SNLP score
			inp[0] = postid;
			inp[1] = userid;
			inp[2] = comment;
			inp[3] = "";// NLP.findSentiment(comment) + "";
			return inp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static boolean isnumber(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
}
