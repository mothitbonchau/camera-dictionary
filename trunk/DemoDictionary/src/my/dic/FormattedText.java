package my.dic;

import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.util.Log;

public class FormattedText {

	public static CharSequence setStyle_NotDelete(CharSequence text,
			String tokenStart, String tokenEnd, CharacterStyle... cs) {
		// Start and end refer to the points where the span will apply
		int tokenLenStart = tokenStart.length();
		int start = text.toString().indexOf(tokenStart) + tokenLenStart;
		int end = text.toString().indexOf(tokenEnd, start);

		if (start > -1 && end > -1) {
			// Copy the spannable string to a mutable spannable string
			SpannableStringBuilder ssb = new SpannableStringBuilder(text);
			for (CharacterStyle c : cs)
				ssb.setSpan(c, start, end, 0);

			// Delete the tokens before and after the span
			// ssb.delete(end, end + tokenEnd.length());
			// ssb.delete(start - tokenLenStart, start);

			text = ssb;
		}
		return text;
	}

	public static CharSequence setStyle_KindOfWord(CharSequence text,
			String tokenStart, String tokenEnd, CharacterStyle... cs) {
		// Start refer to the points where the span will apply
		int tokenStartLen = tokenStart.length();
		int start = text.toString().indexOf(tokenStart);
		// int end = text.toString().indexOf(tokenEnd, start);
		if (start > -1) {

			while (start > -1 /* && end>-1 */) {

				// Copy the spannable string to a mutable spannable string
				SpannableStringBuilder ssb = new SpannableStringBuilder(text);
				for (CharacterStyle c : cs) {
					Log.i("df", String.valueOf(start));
					ssb.setSpan(c, start, start + tokenStartLen, 0);

				}

				start = text.toString().indexOf(tokenStart, start + 1);
				// end = text.toString().indexOf(tokenEnd, end+1);
				text = (CharSequence) ssb;
			}

			
		}
		return text;
	}

	public static CharSequence setStyle_Example(CharSequence text,
			String tokenStart, String tokenEnd, CharacterStyle... cs) {
		int tokenStartLen = tokenStart.length();
		int start = text.toString().indexOf(tokenStart);
		int end = text.toString().indexOf(tokenEnd, start);
		if (start > -1) {

			while (start > -1 && end > -1) {

				// Copy the spannable string to a mutable spannable string
				SpannableStringBuilder ssb = new SpannableStringBuilder(text);
				for (CharacterStyle c : cs) {
					Log.i("df", String.valueOf(start));
					ssb.setSpan(c, start + tokenStartLen, end, 0);
				}
				ssb.replace(start+1, start + 2, " ");

				start = text.toString().indexOf(tokenStart,
						start + tokenStartLen);
				end = text.toString().indexOf(tokenEnd, end + 1);
				text = (CharSequence) ssb;
			}

		}
		return text;
	}

	public static CharSequence ReplaceText(CharSequence text, String oldStr,
			String newStr) {
		int start = text.toString().indexOf(oldStr);
		SpannableStringBuilder ssb = new SpannableStringBuilder(text);
		ssb.replace(start, start + oldStr.length(), newStr);
		text = ssb;
		return text;
	}

}
