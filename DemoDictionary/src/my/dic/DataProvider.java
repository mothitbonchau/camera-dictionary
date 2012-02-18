package my.dic;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.util.Log;

public class DataProvider {
	public static final String path_index = "mnt/sdcard/dict/anh_viet/index.txt";
	public static final String path_mean = "mnt/sdcard/dict/anh_viet/mean.txt";
	public static final String path_hash1 = "mnt/sdcard/dict/anh_viet/hash1.txt";
	public static final String path_hash2 = "mnt/sdcard/dict/anh_viet/hash2.txt";

	public static FileInputStream fin_index = null;
	public static FileInputStream fin_mean = null;
	public static FileInputStream fin_hash1 = null;
	public static FileInputStream fin_hash2 = null;

	/*
	 * public static BufferedReader in_index = null; public static
	 * BufferedReader in_mean = null; public static BufferedReader in_hash1 =
	 * null; public static BufferedReader in_hash2 = null;
	 */

	public static ArrayList<Word> arrHash1 = null;
	public static ArrayList<Word> arrFirst = null;

	public static void Initiation() {
		// TODO Auto-generated constructor stub
		try {
			fin_hash1 = new FileInputStream(path_hash1);
			fin_hash2 = new FileInputStream(path_hash2);
			fin_index = new FileInputStream(path_index);
			fin_mean = new FileInputStream(path_mean);
			/*
			 * in_hash1 = new BufferedReader(new InputStreamReader( new
			 * FileInputStream(path_hash1),"UTF-8")); in_hash2 = new
			 * BufferedReader(new InputStreamReader( new
			 * FileInputStream(path_hash2),"UTF-8")); in_index = new
			 * BufferedReader(new InputStreamReader( new
			 * FileInputStream(path_index),"UTF-8")); in_mean = new
			 * BufferedReader(new InputStreamReader( new
			 * FileInputStream(path_mean),"UTF-8"));
			 */
			arrHash1 = new ArrayList<Word>();
			arrHash1 = ListWordOfHash1();
			arrFirst = new ArrayList<Word>();
			arrFirst = LoadTwentyIndexFirst();

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static ArrayList<Word> ListWordOfHash1() {
		ArrayList<Word> arrW = new ArrayList<Word>();
		try {
			fin_hash1.getChannel().position(0);
			BufferedReader in_hash1 = new BufferedReader(new InputStreamReader(
					fin_hash1, "UTF-8"));
			/*
			 * in_hash1.mark(0); in_hash1.reset();
			 */

			String code = in_hash1.readLine();
			while (code != null) {
				Word word = new Word();

				String[] strW = code.split("_");
				word.set_char(strW[0]);
				word.set_pos(Integer.parseInt(strW[1]));
				word.set_length(Integer.parseInt(strW[2]));
				arrW.add(word);
				code = in_hash1.readLine();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return arrW;
	}

	public static ArrayList<Word> LoadTwentyIndexFirst() {
		ArrayList<Word> arrW = new ArrayList<Word>();
		try {
			fin_index.getChannel().position(0);
			BufferedReader in_index = new BufferedReader(new InputStreamReader(
					fin_index, "UTF-8"));
			// in_index.mark(0);
			// in_index.reset();

			for (int i = 0; i < 20; i++) {
				Word word = new Word();
				String code = in_index.readLine();
				String[] strW = code.split("_");
				word.set_char(strW[0]);
				word.set_pos(Integer.parseInt(strW[1]));
				word.set_length(Integer.parseInt(strW[2]));
				arrW.add(word);

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return arrW;
	}

	public static String LoookKup(int pos, int length) {
		String mean = "";
		try {
			fin_mean.getChannel().position(0);
			BufferedReader in_mean = new BufferedReader(new InputStreamReader(
					fin_mean, "UTF-8"));
			// in_mean.mark(0);
			// in_mean.reset();

			char[] cbuf = new char[1000];
			in_mean.skip(pos);
			in_mean.read(cbuf, 0, length);
			mean = new String(cbuf);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return mean;
	}

	public static ArrayList<Word> GetListWord(String key) {
		ArrayList<Word> arrW = new ArrayList<Word>();
		try {
			Word word_hash1 = SearchWordInList(key.substring(0, 1), arrHash1);

			fin_hash2.getChannel().position(0);
			BufferedReader in_hash2 = new BufferedReader(new InputStreamReader(
					fin_hash2, "UTF-8"));
			// in_hash2.mark(0);
			// in_hash2.reset();

			char[] cbuf = new char[word_hash1.get_length()];
			in_hash2.skip(word_hash1.get_pos());
			in_hash2.read(cbuf, 0, word_hash1.get_length());

			String temp = new String(cbuf, 0, word_hash1.get_length());
			String key2;
			if (key.length() == 1) {
				key2 = key;
			} else {
				key2 = key.substring(0, 2);
			}
			 ArrayList<Word> arr2= ConvertBlockBytesToListWord(key2, temp);
			
			Word w1 = arr2.get(0);
			Word w2 = arr2.get(1);
			int wlegth= w1.get_length() + w2.get_length();
			
			fin_index.getChannel().position(0);
			BufferedReader in_index = new BufferedReader(new InputStreamReader(
					fin_index, "UTF-8"));

			cbuf = new char[wlegth];
			in_index.skip(w1.get_pos());
			in_index.read(cbuf, 0,wlegth);
			String demo = new String(cbuf, 0, wlegth);
			arrW = ConvertBlockBytesToListWord(key, demo);
			
			// }
			// Word word_hash2 = null;

		} catch (Exception e) {
			// TODO: handle exception
		}
		return arrW;
	}

	public static Word SearchWordInList(String word, ArrayList<Word> arrW) {
		for (int i = 0; i < arrW.size(); i++) {
			if (word.compareToIgnoreCase(arrW.get(i).get_char()) == 0) {
				return arrW.get(i);
			}
		}
		return null;
	}

	public static ArrayList<Word> ConvertBlockBytesToListWord(String key,
			String buf) {
		ArrayList<Word> arrW = new ArrayList<Word>();
		try {
			int iStart = buf.indexOf(key);
			if (iStart != -1) {
				String strBlock = buf.substring(iStart, buf.length());
				String[] arrStr = strBlock.split("\r\n");
				for (int i = 0; i < arrStr.length; i++) {
					Word word = new Word();
					String[] strW = arrStr[i].split("_");
					word.set_char(strW[0]);
					word.set_pos(Integer.parseInt(strW[1]));
					word.set_length(Integer.parseInt(strW[2]));
					arrW.add(word);
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return arrW;
	}

	
}
