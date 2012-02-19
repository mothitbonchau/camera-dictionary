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



	public static ArrayList<Word> arrHash1 = null;
	public static ArrayList<Word> arrFirst = null;

	public static void Initiation() {
		// TODO Auto-generated constructor stub
		try {
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
			BufferedReader in_hash1 = new BufferedReader(new InputStreamReader(
					new FileInputStream(path_hash1), "UTF-8"));

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
			BufferedReader in_index = new BufferedReader(new InputStreamReader(
					new FileInputStream(path_index), "UTF-8"));

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

			BufferedReader in_mean = new BufferedReader(new InputStreamReader(
					new FileInputStream(path_mean), "UTF-8"));

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
			
			int index1 = BinarySearch(arrHash1, key.substring(0, 1));

			if (index1 == -1)
				return arrW;

			Word w1 = arrHash1.get(index1);

			BufferedReader in_hash2 = new BufferedReader(new InputStreamReader(
					new FileInputStream(path_hash2), "UTF-8"));
			char[] cbuf = new char[w1.get_length()];
			in_hash2.skip(w1.get_pos());
			in_hash2.read(cbuf, 0, w1.get_length());

			String temp = new String(cbuf, 0, w1.get_length());
			String key2;
			if (key.length() == 1) {
				key2 = key;
			} else {
				key2 = key.substring(0, 2);
			}

			ArrayList<Word> arr2 = ConvertBlockBytesToListWord(temp);
			int index2 = BinarySearch(arr2, key2);
			

			if (index2 == -1)
				return arrW;

			Word w21 = arr2.get(index2);
			Word w22 = arr2.get(index2 + 1);
			int wlegth = w21.get_length() + w22.get_length();

			BufferedReader in_index = new BufferedReader(new InputStreamReader(
					new FileInputStream(path_index), "UTF-8"));

			cbuf = new char[wlegth];
			in_index.skip(w21.get_pos());
			in_index.read(cbuf, 0, wlegth);
			String demo = new String(cbuf, 0, wlegth);
			ArrayList<Word> arr3 = ConvertBlockBytesToListWord( demo);
			int index3 = BinarySearchLess(arr3, key);
			// Log.i("THONGBAO", String.valueOf(index3));

			for(int i =index3 ;i <arr3.size(); i++){
				arrW.add(arr3.get(i));
			}

			 
		} catch (Exception e) {
			// TODO: handle exception
		}
		return arrW;
	}

	public static int BinarySearch(ArrayList<Word> arrW, String x) { // tim kiem chinh xac
		int n = arrW.size();
		int l = 0, r = n - 1;
		while (l <= r) {
			int m = (l + r) / 2;
			int compare = arrW.get(m).get_char().compareToIgnoreCase(x);
			if (compare == 0) {
				return m;
			} else if (compare > 0)
				r = m - 1;
			else
				l = m + 1;
		}
		return -1;
	}
	public static int BinarySearchLess(ArrayList<Word> arrW, String x) { //tim kiem gan dung
		int n = arrW.size();
		int l = 0, r = n - 1;
		int m=-1;
		while (l <= r) {
			 m = (l + r) / 2;
			int compare = arrW.get(m).get_char().compareToIgnoreCase(x);
			if (compare == 0) {
				return m;
			} else if (compare > 0)
				r = m - 1;
			else
				l = m + 1;
		}
		if(arrW.get(m).get_char().startsWith(x)) return m;
		if(arrW.get(m+1).get_char().startsWith(x)) return m+1;
		return -1;
	}

/*	public static Word SearchWordInList(String word, ArrayList<Word> arrW) {
		for (int i = 0; i < arrW.size(); i++) {
			if (word.compareToIgnoreCase(arrW.get(i).get_char()) == 0) {
				return arrW.get(i);
			}
		}
		return null;
	}*/

	public static ArrayList<Word> ConvertBlockBytesToListWord(
			String buf) {
		ArrayList<Word> arrW = new ArrayList<Word>();
		try {
			
			String[] arrStr = buf.split("\r\n");
			for (int i = 0; i < arrStr.length; i++) {
				Word word = new Word();
				String[] strW = arrStr[i].split("_");
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

}
