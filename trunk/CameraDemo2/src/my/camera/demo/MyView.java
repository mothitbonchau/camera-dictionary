package my.camera.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyView extends View {

	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

	private float initX, initY, radius;
	private boolean drawing = false;

	public MyView(Context context) {
		super(context);
		init();
		// TODO Auto-generated constructor stub
	}

	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		//paint.setStyle(Paint.Style.);
	//	paint.setStrokeWidth(3);
		paint.setColor(Color.RED);
		paint.setAlpha(30);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
				MeasureSpec.getSize(heightMeasureSpec));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
	//	if (drawing) {
		
			canvas.drawCircle(initX, initY, radius, paint);
	//	}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		int action = event.getAction();
		if (action == MotionEvent.ACTION_MOVE) {
			float x = event.getX();
			float y = event.getY();

			radius = (float) Math.sqrt(Math.pow(x - initX, 2)
					+ Math.pow(y - initY, 2));

		} else if (action == MotionEvent.ACTION_DOWN) {
			initX = event.getX();
			initY = event.getY();
			radius = 1;
			drawing = true;
		} else if (action == MotionEvent.ACTION_UP) {
			drawing = false;
		}
		invalidate();
		return true;

	}

}
