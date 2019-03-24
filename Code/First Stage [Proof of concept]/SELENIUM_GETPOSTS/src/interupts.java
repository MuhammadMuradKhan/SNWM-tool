import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class interupts {
	public static String[][] attributes = null;

	public static void readstate() {
		try {
			int lines = 0;
			FileInputStream fstream = new FileInputStream("config.txt");

			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String line;
			while ((line = br.readLine()) != null) {
				lines++;
			}
			br.close();
			line = "a";
			fstream = new FileInputStream("config.txt");
			br = new BufferedReader(new InputStreamReader(fstream));
			attributes = new String[lines][2];
			int i = 0;
			while ((line = br.readLine()) != null) {
				String[] temp = line.split("=");
				attributes[i][0] = temp[0];
				attributes[i][1] = temp[1];
				i++;
			}
			br.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("tete");
		}

	}

	public static boolean pause() {
		try {

			FileInputStream fstream = new FileInputStream("config.txt");

			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains("pause=0")) {
					return false;
				}
				if (line.contains("pause=1")) {
					return true;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public static boolean end() {
		try {

			FileInputStream fstream = new FileInputStream("config.txt");

			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains("end")) {
					if (line.contains("0")) {
						return false;
					}
					if (line.contains("1")) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public static void makepause() {
		readstate();
		if (attributes[0][1].contains("0")) {
			attributes[0][1] = "1";
		}
		writestate();
	}

	public static void makeend() {
		readstate();
		System.out.println(1);
		if (attributes[1][1].contains("0")) {
			attributes[1][1] = "1";
		}
		writestate();
	}

	public static void writestate() {
		PrintWriter writer;
		try {
			writer = new PrintWriter("config.txt", "UTF-8");
			writer.println(attributes[0][0] + "=" + attributes[0][1]);
			writer.println(attributes[1][0] + "=" + attributes[1][1]);
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void reset() {
		readstate();
		PrintWriter writer;
		try {
			writer = new PrintWriter("config.txt", "UTF-8");
			writer.println(attributes[0][0] + "=0");
			writer.println(attributes[1][0] + "=0");
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}