import java.io.File;




public class DataDictionary {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ManagerData mnd = new ManagerData();
		//mnd.LoadDataFromFile();  //Qua trinh chuan bi doc du lieu 
		//mnd.CreateFile_Mean_Word_Index(); //tao cac file mean  - word - index
		//mnd.CreateFile_Hash2(); //tao bang bam 2 ky tu dau
		//mnd.CreateFile_Hash1(); // tao bang bam 1 ky tu dau
		String mean = mnd.Lookup("tree"); // thu nghiem tra tu
		System.out.println(mean);
	}

}
