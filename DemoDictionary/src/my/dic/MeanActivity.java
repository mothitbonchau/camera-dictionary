package my.dic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MeanActivity extends Activity {
	private static TextView tvKey = null;
	private static TextView tvMean = null;
	private static Button btOk = null;
	
	private String mKey =null;
	private String mMean = null;
	private int mPos = 0;
	private int mLength =0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mean);
		
		LoadControls();

		Intent i = this.getIntent();

		//int wordId = i.getIntExtra("id", 0);
		mKey = i.getStringExtra("word");
		mPos = i.getIntExtra("pos", 0);
		mLength = i.getIntExtra("length", 0);
		
		LoadMean();
	}
	
	public void LoadControls(){
		tvKey = (TextView) findViewById(R.id.textView1);
		tvMean = (TextView) findViewById(R.id.textView2);
		btOk = (Button) findViewById(R.id.button1);
		
		btOk.setOnClickListener( new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent();				
				i.putExtra("word", mKey);
				setResult(RESULT_OK, i);
				finish();
			}
		});
	}
	public void LoadMean()
	{
		tvKey.setText(mKey);
	tvMean.setText(DataProvider.LoookKup(mPos, mLength));
	}
	
}
