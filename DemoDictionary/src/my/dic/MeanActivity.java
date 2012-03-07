package my.dic;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MeanActivity extends Activity implements OnInitListener {
	private static TextView tvKey = null;
	private static TextView tvMean = null;
	private static Button btOk = null;
	private static Button btSound = null;

	private String mKey = null;
	private String mMean = null;
	private int mPos = 0;
	private int mLength = 0;

	// text to speech
	private TextToSpeech tts;
	private int DATA_CHECK_CODE = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mean);

		LoadControls();

		Intent i = this.getIntent();

		// int wordId = i.getIntExtra("id", 0);
		mKey = i.getStringExtra("word");
		mPos = i.getIntExtra("pos", 0);
		mLength = i.getIntExtra("length", 0);

		LoadMean();

		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, DATA_CHECK_CODE);
	}

	public void LoadControls() {
		tvKey = (TextView) findViewById(R.id.textView1);
		tvMean = (TextView) findViewById(R.id.textView2);
		btOk = (Button) findViewById(R.id.button1);
		btSound = (Button) findViewById(R.id.button2);
		btOk.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				// i.putExtra("word", mKey);
				setResult(RESULT_OK, i);
				finish();
			}
		});

		btSound.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				// text to speech here
				// TTS tts=new TTS();
				Speak(mKey);
			}

		});
	}

	public void LoadMean() {

		tvKey.setText(mKey);
		mMean = DataProvider.LoookKup(mPos, mLength);
		Log.i("dfs",mMean);
		CharSequence text = FormatText(mMean);
		//tvMean.setText("đường phan văn trị\r\n hữu thọ");
		tvMean.setText(text);
		//tvMean.setText(Html.fromHtml(mMean).toString());
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
				tts = new TextToSpeech(this, this);
			else {
				// install data for TTS
				Intent install = new Intent();
				install.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(install);
			}
		}

	}

	// speak a word
	public void Speak(String text) {

		tts.speak(text, TextToSpeech.QUEUE_ADD, null);

	}

	public void onInit(int status) {
		// TODO Auto-generated method stub
		if (status == TextToSpeech.SUCCESS) {
			if (tts.isLanguageAvailable(Locale.ENGLISH) == TextToSpeech.LANG_AVAILABLE)
				tts.setLanguage(Locale.ENGLISH);
		} else if (status == TextToSpeech.ERROR) {
			Toast.makeText(this, "Error when using TTS", Toast.LENGTH_LONG)
					.show();
		}
	}

	// format text

	public static CharSequence FormatText(String text) {
		CharSequence rs = text.subSequence(0, text.length());
		// dinh dang phan phien am
		rs = FormattedText.setStyle_NotDelete(rs, "[", "]",
				new ForegroundColorSpan(Color.GREEN));
		// dinh dang phan loai tu
		//rs = FormattedText.setStyle_KindOfWord(rs, "*","\r\n",
		//		new ForegroundColorSpan(Color.RED));
		// cac vi du
		//rs = FormattedText.setStyle_Example(rs, "=$", ":", new StyleSpan(
		//		Typeface.ITALIC));
		/*
		  rs = FormattedText.setStyle_NotDelete(rs, "!$", ":", new
		 * ForegroundColorSpan(0xFF4444FF), new UnderlineSpan(), new
		 * ClickableSpan() {
		 * 
		 * @Override public void onClick(View widget) { // When the span is
		 * clicked, show some text on-screen. // //
		 * findViewById(R.id.clicked_text).setVisibility(View.VISIBLE); } });
		 */
		// rs = FormattedText.ReplaceText(text, "$", " ");
		return rs;
	}
}
