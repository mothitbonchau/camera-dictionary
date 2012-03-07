package my.camera.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

class DrawOnTop extends View {
	 
	 
	 
    public DrawOnTop(Context context) {

            super(context);

            // TODO Auto-generated constructor stub

    }



    @Override

    protected void onDraw(Canvas canvas) {

            // TODO Auto-generated method stub

           

            Paint paint = new Paint();

            paint.setStyle(Paint.Style.FILL);

            paint.setColor(Color.BLUE);

            canvas.drawText("Test Text", 15, 15, paint);

           

            super.onDraw(canvas);

    }

   

}
