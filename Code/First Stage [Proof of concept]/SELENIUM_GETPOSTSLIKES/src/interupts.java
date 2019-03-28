import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class interupts {
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
				if (line.contains("end=0")) {
					return false;
				}
				if (line.contains("end=1")) {
					return true;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
}
