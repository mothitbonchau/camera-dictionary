import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;

public class ManagerData {
	private static String path_data_xml = "D:\\Anh_Viet.xml";
	private static String path_data_root = "D:\\Data Dictionary\\av.txt";
	private static String path_file_word = "D:\\Data Dictionary\\word.txt";
	private static String path_file_mean = "D:\\Data Dictionary\\mean.txt";
	private static String path_file_index = "D:\\Data Dictionary\\index.txt";
	private static String path_file_hash1 = "D:\\Data Dictionary\\hash1.txt";
	private static String path_file_hash2 = "D:\\Data Dictionary\\hash2.txt";

	private ArrayList<Data> _arrData;

	public ArrayList<Data> get_arrData() {
		return _arrData;
	}

	public void set_arrData(ArrayList<Data> _arrData) {
		this._arrData = _arrData;
	}

	public void LoadDataFromFile() {
		try {

			_arrData = new ArrayList<Data>();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(path_data_root), "UTF-8"));
			Data dt = new Data();
			String mean = "";
			String code = in.readLine();
			System.out.println(code);
			if (!code.startsWith("@")) {
				code = code.substring(1, code.length());
				System.out.println(code);
			}
			while (code != null) {
				// u ly code
				if (code.startsWith("@")) {
					if (mean != "") {
						dt.set_mean(mean);
						mean = "";
						_arrData.add(dt);
					}
					dt = new Data();
					String key = code.substring(1, code.length());
					dt.set_key(key);

				} else {
					mean = mean + code + "\r\n";
				}

				code = in.readLine();
			}
			in.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void LoadDataFormFileXML() {
		try {

			File fXmlFile = new File(path_data_xml);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			// System.out.println("sfsdfsdf");
			doc.getDocumentElement().normalize();

			// System.out.println("Root element :" +
			// doc.getDocumentElement().getNodeName());
			NodeList nList = doc.getElementsByTagName("record");
			// System.out.println("-----------------------");
			_arrData = new ArrayList<Data>();
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Data dt = new Data();
					Element eElement = (Element) nNode;

					dt.set_key(getTagValue("word", eElement));
					dt.set_mean(getTagValue("meaning", eElement));

					_arrData.add(dt);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void SortABCDictionary() {
		int length = _arrData.size();
		System.out.println(length);
		for (int i = 0; i < length - 1; i++)
			for (int j = i + 1; j < length; j++) {

				if (_arrData.get(i).get_key()
						.compareToIgnoreCase(_arrData.get(j).get_key()) > 0) {
					// hoan doi 2 word
					Data tam = new Data();
					tam.set_key(_arrData.get(i).get_key());
					tam.set_mean(_arrData.get(i).get_mean());
					_arrData.set(i, _arrData.get(j));
					_arrData.set(j, tam);

				}
			}
	}

	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
				.getChildNodes();

		Node nValue = (Node) nlList.item(0);

		return nValue.getNodeValue();
	}

	public void CreateFile_Mean_Word_Index() { // tao 3 file mean, word va index

		try {
			File file_word = new File(path_file_word);
			File file_mean = new File(path_file_mean);
			File file_index = new File(path_file_index);
			if (!file_word.exists() || !file_mean.exists()) {

				file_word.createNewFile();
				file_mean.createNewFile();
				file_index.createNewFile();
				// System.out.println("tao file moi thanh cong");
			} else {
				System.out.println("file da ton tai");
			}

			OutputStream out_word = new FileOutputStream(file_word);
			OutputStream out_mean = new FileOutputStream(file_mean);
			OutputStream out_index = new FileOutputStream(file_index);
			// System.out.println(_arrData.get(0).get_mean());

			int pos = 0;
			for (int i = 0; i < _arrData.size(); i++) {
				String word = _arrData.get(i).get_key();
				String mean = _arrData.get(i).get_mean();

				int length = mean.getBytes().length;
				String s = word + "_" + pos + "_" + length + "\r\n";

				out_mean.write(mean.getBytes("UTF-8"));
				out_word.write((word + "\r\n").getBytes("UTF-8"));
				out_index.write(s.getBytes("UTF-8"));

				pos += length;

			}
			out_word.flush();
			out_word.close();
			out_mean.flush();
			out_mean.close();
			out_index.flush();
			out_index.close();
			System.out.println("ghi file thanh cong");
		} catch (Exception e) {
			System.out.println("loi vang ra ngoai");
		}
	}

	public void CreateFile_Hash2() {
		// doc file word roi viet ra bang bam Hash 2
		try {
			// doc tung dong file index roi ghi ra mang arrWord
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(path_file_index), "UTF-8"));
			ArrayList<String> arrWord = new ArrayList<>();
			String code = in.readLine();
			while (code != null) {
				arrWord.add(code);
				code = in.readLine();
			}

			// ghi file Hash2 ( bang bam co 2 tu dau)
			File file = new File(path_file_hash2);
			if (!file.exists()) {
				file.createNewFile();
			}
			String word_current = arrWord.get(0).substring(0, 2);
			OutputStream out = new FileOutputStream(file);
			int pos = 0;
			int length = 0;
			for (int i = 0; i < arrWord.size(); i++) {
				String hash = arrWord.get(i).substring(0, 2);
				if (hash.charAt(1) == '_') {
					hash = hash.substring(0, 1);
				}
				if (hash.compareToIgnoreCase(word_current) == 0) {
					// tu cu --> tang length
					length = length + arrWord.get(i).getBytes().length + 2;// +2
																			// la
																			// ky
																			// tu
																			// \r\n
				} else {
					// tu moi
					out.write((word_current + "_" + pos + "_" + length + "\r\n")
							.getBytes("UTF-8"));

					word_current = hash;
					pos += length;
					length = arrWord.get(i).getBytes().length + 2;
				}
			}
			// viet tu cuoi cung
			out.write((word_current + "_" + pos + "_" + length + "\r\n")
					.getBytes("UTF-8"));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void CreateFile_Hash1() {
		// doc file Hash2 roi viet ra bang bam Hash 1
		try {
			// doc tung dong file index roi ghi ra mang arrWord
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(path_file_hash2), "UTF-8"));
			ArrayList<String> arrWord = new ArrayList<>();
			String code = in.readLine();
			while (code != null) {
				arrWord.add(code);
				code = in.readLine();
			}

			// ghi file Hash1 ( bang bam co 1 ky tu dau)
			File file = new File(path_file_hash1);
			if (!file.exists()) {
				file.createNewFile();
			}
			String word_current = arrWord.get(0).substring(0, 1);
			OutputStream out = new FileOutputStream(file);
			int pos = 0;
			int length = 0;
			for (int i = 0; i < arrWord.size(); i++) {
				String hash = arrWord.get(i).substring(0, 1);

				if (hash.compareToIgnoreCase(word_current) == 0) {
					// tu cu --> tang length
					length = length + arrWord.get(i).getBytes().length + 2;// +2
																			// la
																			// ky
																			// tu
																			// \r\n
				} else {
					// tu moi
					out.write((word_current + "_" + pos + "_" + length + "\r\n")
							.getBytes("UTF-8"));

					word_current = hash;
					pos += length;
					length = arrWord.get(i).getBytes().length + 2;
				}
			}
			// viet tu cuoi cung
			out.write((word_current + "_" + pos + "_" + length + "\r\n")
					.getBytes("UTF-8"));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Word FindWordIn(String key, char[] block) { // tim tu khoa trong 1
														// block
		Word w = new Word();
		int iStart = 0;

		String StrBlock = new String(block);
		// iStart = StrBlock.
		iStart = StrBlock.indexOf(key);

		char[] buff = new char[50];
		int j = 0;
		// doc phan key cua tu
		while (block[iStart] != '_') {
			buff[j] = block[iStart];
			iStart++;
			j++;
		}
		System.out.println(buff);
		w.set_char(new String(buff));

		// doc so offset
		iStart++;
		j = 0;
		buff = new char[50];
		while (block[iStart] != '_') {
			buff[j] = block[iStart];
			iStart++;
			j++;
		}

		w.set_pos(Integer.parseInt(new String(buff, 0, j)));
		System.out.println(buff);
		// doc phan length
		iStart++;
		j = 0;
		buff = new char[50];
		while (block[iStart] != '\r') {
			buff[j] = block[iStart];
			iStart++;
			j++;
		}
		System.out.println(buff);
		w.set_length(Integer.parseInt(new String(buff, 0, j)));
		return w;
	}

	public String Lookup(String key) {
		String mean = "";
		try {
			// gd1
			// doc toan bo file hash1
			String firstchar = key.substring(0, 1);

			File file_hash1 = new File(path_file_hash1);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(path_file_hash1), "UTF-8"));

			long length_file = file_hash1.length();
			char[] cbuf = new char[(int) length_file];
			in.read(cbuf, 0, (int) length_file); // do dai byte can doc
			Word w_hash1 = FindWordIn(firstchar, cbuf);

			// gd2
			// dua vao key 1 ky tu dau tim word roi tim trong hash2
			RandomAccessFile raf = new RandomAccessFile(file_hash1, "rw");

			// BufferedReader in2 = new BufferedReader(new InputStreamReader(
			// new FileInputStream(path_file_hash2), "UTF-8"));

			byte[] cbuf1 = new byte[w_hash1.get_length()];
			raf.seek(w_hash1.get_pos());
			raf.read(cbuf1, 0, w_hash1.get_length());
			// in2.skip(w_hash1.get_pos());
			// in2.read(cbuf, 0, w_hash1.get_length());
			String secondChar = key.substring(0, 2);
			// System.out.println(cbuf);
			Word w_hash2 = FindWordIn(secondChar, cbuf);

			// gd3
			// dua vao tu khoa key va block tim word tim tu trong index

			BufferedReader in3 = new BufferedReader(new InputStreamReader(
					new FileInputStream(path_file_index), "UTF-8"));

			cbuf = new char[w_hash2.get_length()];
			in3.skip(w_hash2.get_pos());
			in3.read(cbuf, 0, w_hash2.get_length());
			Word w_hash3 = FindWordIn(key, cbuf);

			// gd4 xuat ra mean cua key
			BufferedReader in4 = new BufferedReader(new InputStreamReader(
					new FileInputStream(path_file_mean), "UTF-8"));

			cbuf = new char[w_hash3.get_length()];
			in4.skip(w_hash3.get_pos());
			in4.read(cbuf, 0, w_hash3.get_length());
			mean = new String(cbuf);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return mean;

	}
}
