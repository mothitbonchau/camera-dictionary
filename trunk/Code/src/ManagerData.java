import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;



public class ManagerData {
	private static String path_data_root ="D:\\Data Dictionary\\Anh-Viet.txt";
	private static String path_file_word = "D:\\Data Dictionary\\word.txt";
	private static String path_file_mean = "D:\\Data Dictionary\\mean.txt";
	private static String path_file_index="D:\\Data Dictionary\\index.txt";
	private static String path_file_hash1= "D:\\Data Dictionary\\hash1.txt";
	private static String path_file_hash2= "D:\\Data Dictionary\\hash2.txt";
	
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

	public void CreateFile_Mean_Word_Index() { //tao 3 file mean, word va index
		// Writing a file...

		try {
			File file_word = new File(path_file_word);
			File file_mean = new File(path_file_mean);
			File file_index = new File(path_file_index);
			if (!file_word.exists() || !file_mean.exists() ) {

				file_word.createNewFile();
				file_mean.createNewFile();
				file_index.createNewFile();
				//System.out.println("tao file moi thanh cong");
			} else {
				System.out.println("file da ton tai");
			}

			OutputStream out_word = new FileOutputStream(file_word);
			OutputStream out_mean = new FileOutputStream(file_mean);
			OutputStream out_index = new FileOutputStream(file_index);

			int pos = 0;
			for (int i = 0; i < _arrData.size(); i++) {
				String word = _arrData.get(i).get_key();
				String mean = _arrData.get(i).get_mean();
				
				int length = mean.getBytes().length;
				String s = word + "_" + pos + "_" + length + "\r\n";

				
				out_mean.write(mean.getBytes());
				out_word.write((word+"\r\n").getBytes());
				out_index.write(s.getBytes());
				
				pos += length;
				// ow.write(_arrData.get(i).get_mean().getBytes());
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

	public void CreateFile_Hash1() {
		//File file = new File("D:\\Data Dictionary\\Hash1.txt");

	}

	public void CreateFileHash2() {

		// doc file word roi viet ra bang bam Hash 2
		try {

			// doc file
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream("D:\\Data Dictionary\\word.txt"),
					"UTF-8"));
			ArrayList<String> arrWord = new ArrayList<>();
			String code = in.readLine();
			while (code != null) {
				arrWord.add(code);
				code = in.readLine();
			}

			// ghi file

			File file = new File("D:\\Data Dictionary\\Hash2.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			String word_current = arrWord.get(0).substring(0, 2);
			OutputStream out = new FileOutputStream(file);
			int pos = 0;
			int length = 0;
			for (int i = 0; i < arrWord.size(); i++) {
				String hash = arrWord.get(i).substring(0, 2);

				if (hash.equals(word_current)) {
					// tu cu --> tang length
					length = length + arrWord.get(i).getBytes().length;
				} else {
					// tu moi

					out.write((hash + "_" + pos + "_" + length + "\r\n")
							.getBytes());
					word_current = hash;


				}
				pos = arrWord.get(i).getBytes().length;
			}
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void TraTu() {
		try {

			
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream("D:\\Data Dictionary\\mean.txt"),
					"UTF-8"));
			char[] cbuf = new char[1000];
			in.read(cbuf, 0, 69);
			System.out.println(cbuf);
			System.out.println("\r\n".getBytes().length);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}
