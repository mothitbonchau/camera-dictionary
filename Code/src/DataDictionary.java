import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class DataDictionary {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ManagerData mnd = new ManagerData();
		// mnd.LoadDataFormFileXML();
		
		 mnd.LoadDataFromFile(); //Qua trinh chuan bi doc du lieu
		 mnd.SortABCDictionary();
		 mnd.CreateFile_Mean_Word_Index(); //tao cac file mean - word - index
		mnd.CreateFile_Hash2(); // tao bang bam 2 ky tu dau
		mnd.CreateFile_Hash1(); // tao bang bam 1 ky tu dau
		 System.out.println("Da hoan thanh");
		
		  //String mean = mnd.Lookup("apple"); // thu nghiem tra tu
		
		 //System.out.println(mean);
		 /*try{ File file = new File("D:\\nhap.txt");
		 if (!file.exists()) { file.createNewFile(); }
		  
		  OutputStream out = new FileOutputStream(file);
		  out.write(mean.getBytes("UTF-8")); } catch (Exception e) { // TODO:
		  handle exception }*/
		 

	}

}
