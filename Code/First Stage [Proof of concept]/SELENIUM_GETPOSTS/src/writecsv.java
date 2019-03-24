
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class writecsv {

	public static void write(String filename, String[][] array, long starttime, long endtime) {
		try {
			BufferedWriter br = new BufferedWriter(new FileWriter(filename));
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < array.length; i++) {
				String[] ary = array[i];
				for (String element : ary) {
					sb.append(element);
					sb.append(",");
				}
				sb.append("\n");
			}
			sb.append(starttime + "," + endtime + "," + ((starttime - endtime) / 1000000000) + ",,,,\n");
			br.write(sb.toString());
			br.close();
			NumberFormat formatter = new DecimalFormat("#0.00000");
			System.out.println("Execution time is " + formatter.format((endtime - starttime) / 1000d) + " seconds");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writegeneric(String filename, String[][] array) {
		try {
			BufferedWriter br = new BufferedWriter(new FileWriter(filename));
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < array.length; i++) {
				String[] ary = array[i];
				for (String element : ary) {
					sb.append(element);
					sb.append(",");
				}
				sb.append("\n");
			}
			br.write(sb.toString());
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writegeneric(String filename, LinkedList<String[][]> arraylist) {
		try {
			BufferedWriter br = new BufferedWriter(new FileWriter(filename));
			StringBuilder sb = new StringBuilder();

			while (!arraylist.isEmpty()) {
				String[][] ary = arraylist.removeFirst();
				for (String[] elemen : ary) {
					for (String element : elemen)
						sb.append(element);
					sb.append(", ");
				}
				sb.append("\n");
			}
			br.write(sb.toString());
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writegenericsingle(String filename, LinkedList<String> arraylist) {
		try {
			BufferedWriter br = new BufferedWriter(new FileWriter(filename));
			StringBuilder sb = new StringBuilder();

			while (!arraylist.isEmpty()) {
				String ary = arraylist.removeFirst();
				sb.append(ary);
				sb.append(", ");
				sb.append("\n");
			}
			br.write(sb.toString());
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * public static void writegeneric(String filename, LinkedList<List> llist) {
	 * try {
	 * 
	 * Object[] llistobj = llist.toArray(); ArrayList<String[]> tmpobj =
	 * (ArrayList<String[]>) llistobj[0]; String[] tmp = new String[tmpobj.size()];
	 * tmp = (String[]) tmpobj.toArray(tmp); String[][] array = new
	 * String[llistobj.length][tmp.length];
	 * 
	 * for(int i=0;i<llistobj.length;i++){ tmpobj = (ArrayList<String[]>)
	 * llistobj[i]; tmp = new String[tmpobj.size()]; tmp = (String[])
	 * tmpobj.toArray(tmp); for (int j=0;j<tmp.length;j++){ array[i][j]=tmp[j]; } }
	 * 
	 * BufferedWriter br = new BufferedWriter(new FileWriter(filename));
	 * StringBuilder sb = new StringBuilder();
	 * 
	 * for (int i = 0; i < array.length; i++) { String[] ary = array[i]; for (String
	 * element : ary) { sb.append(element); sb.append(","); } sb.append("\n"); }
	 * br.write(sb.toString()); br.close(); } catch (Exception e) {
	 * e.printStackTrace(); } }
	 */

}
