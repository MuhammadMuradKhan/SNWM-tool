import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class loadcsv {
	public String name = "";
	public static int rows = 0;
	public static int cols = 0;
	public static String[] label = null;
	public static String[][] ret = null;

	public loadcsv() {
		System.out.println("object created");
	}

	public static String[][] loadfile(String filename) {

		BufferedReader br = null;

		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					filename)));

			String line;

			line = br.readLine();
			setrows(line);

			int count = 0;

			while ((line = br.readLine()) != null) {

				count++;

			}

			cols = count;

			ret = new String[cols][rows];

			br.close();

			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					filename)));

			line = "start";
			count = 0;
			while ((line) != null) {
				line = br.readLine();
				System.out.println(count + ":" + line);
				
				if (line != null) {

					String[] temp = line.split(",");
					try {
						for (int i = 0; i < rows; i++) {
							ret[count][i] = temp[i];
						}
					} catch (Exception e) {
						//e.printStackTrace();
						continue;
					}
				}
				count++;
			}

			System.out.println("load complete");

			return ret;

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	private static void setrows(String firstrow) {
		String[] temp = firstrow.split(",");
		rows = temp.length;

		label = new String[rows];

		for (int i = 0; i < rows; i++) {
			label[i] = temp[i];
		}
	}
}
