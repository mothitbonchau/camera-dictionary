package my.dic;

import java.util.ArrayList;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class DemoDictionaryActivity extends Activity {

	private static EditText etKey = null;
	private static ListView lvListKey = null;
	private ArrayAdapter<String> mAdapter = null;
	private ArrayList<Word> mListWord = null;

	public DataProvider mDataProvider = null;

	private Handler mHandler;
	private Runnable mUpdateTimeTask;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		LoadControl();
		// LoadData();

		DataProvider.Initiation(); // khoi tao cac bang bam cap 1
		LoadListWhenTextNull();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			/*
			 * TODO what if the dictionary was changed by history viewing????
			 */
			/*
			 * String word = data.getStringExtra("word"); //etKey = (EditText)
			 * findViewById(id.txtKeyWord); etKey.setText(word);
			 * etKey.setSelection(0, word.length());
			 */
		}
	}

	/*
	 * @Override protected void onPause() { // TODO Auto-generated method stub
	 * super.onDestroy(); }
	 */

	public void LoadControl() {
		etKey = (EditText) findViewById(R.id.editText1);
		lvListKey = (ListView) findViewById(R.id.listView1);
		mAdapter = new ArrayAdapter<String>(getApplicationContext(),
				R.layout.list_item);
		// khoi tao danh sach ban dau truoc

		mHandler = new Handler();

		mUpdateTimeTask = new Runnable() {
			public void run() {
				String textKey = etKey.getText().toString();
				if (textKey.length() > 0) {
					etKey.setEnabled(false);
					ShowWordList(textKey);
					etKey.setEnabled(true);
				} else {
					LoadListWhenTextNull();
				}
				etKey.requestFocus();

			}
		};

		etKey.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				mHandler.removeCallbacks(mUpdateTimeTask);
				mHandler.postDelayed(mUpdateTimeTask, 500);
			}
		});
		lvListKey.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			// @Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {

				Intent i = new Intent(v.getContext(), MeanActivity.class); //
				i.putExtra("id", position);
				i.putExtra("word", mAdapter.getItem(position));
				i.putExtra("pos", mListWord.get(position).get_pos());
				i.putExtra("length", mListWord.get(position).get_length());
				startActivityForResult(i, 0);

				// ShowResult(mListWord.get(position));
				etKey.selectAll();
			}
		});

	}

	public void ShowResult(Word w) {
		String result = DataProvider.LoookKup(w.get_pos(), w.get_length());
		Toast msg = Toast.makeText(this, result, Toast.LENGTH_LONG);
		msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
				msg.getYOffset() / 2);
		msg.show();

	}

	/*
	 * public void LoadData(){ try{ DataProvider.in_index = new
	 * BufferedReader(new InputStreamReader( new FileInputStream( path_index),
	 * "UTF-8")); DataProvider.in_mean = new BufferedReader(new
	 * InputStreamReader( new FileInputStream( path_mean), "UTF-8"));
	 * DataProvider.in_hash1 = new BufferedReader(new InputStreamReader( new
	 * FileInputStream( path_hash1), "UTF-8")); DataProvider.in_hash2 = new
	 * BufferedReader(new InputStreamReader( new FileInputStream( path_hash2),
	 * "UTF-8")); } catch (Exception e) { // TODO: handle exception }
	 */
	public void LoadListWhenTextNull() {
		mListWord = DataProvider.arrFirst;
		UpdateListView(mListWord);
	}

	public void UpdateListView(ArrayList<Word> arrW) {
		mAdapter.clear();
		for (int i = 0; i < arrW.size(); i++) {
			mAdapter.add(arrW.get(i).get_char());
		}
		lvListKey.setAdapter(mAdapter);
	}

	public void ShowWordList(String textKey) {

		if (DataProvider.arrHash1.size() > 0) {
			mListWord = DataProvider.GetListWord(textKey);
			UpdateListView(mListWord);
		}
	}
}