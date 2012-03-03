package my.camera.demo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.googlecode.tesseract.android.TessBaseAPI;



public class CameraDemo2Activity extends Activity implements SurfaceHolder.Callback {
	  /** Called when the activity is first created. */
		//create new camera object
		private Camera camera ;
		private SurfaceView surfaceView;
		private SurfaceHolder surface_holder;
		private static boolean isActive=false;
		Button buttonClick;
		EditText editText;
		private Context mContext = this;
		static final int FOTO_MODE = 0;
		
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        getWindow().setFormat(PixelFormat.TRANSLUCENT);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        setContentView(R.layout.main);
	        surfaceView =(SurfaceView)findViewById(R.id.surface_camera);
	        buttonClick=(Button)findViewById(R.id.btn_ok);
	        surface_holder =surfaceView.getHolder();
	        surface_holder.addCallback((Callback) this);
	        editText=(EditText)findViewById(R.id.textCamera);
	        surface_holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	        
	    	buttonClick.setOnClickListener(new OnClickListener()
	    	{
	    		public void onClick(View arg0) {
	    			
	    			 camera.autoFocus(myAutoFocusCallback);

	    			camera.takePicture(null, mPictureCallback, mPictureCallback);
					//AlertDialog.Builder alertbox = new AlertDialog.Builder(
							//CameraDemo2Activity.this);
					//alertbox.setMessage("DKJdshdsjdskdjskdsdjs");
	    			//editText.setText("Hello every one!");

	    		}
	    		
	    	} );
	    	
	    }
	    
	    AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback(){

	    	  @Override
	    	  public void onAutoFocus(boolean arg0, Camera arg1) {
	    	   // TODO Auto-generated method stub

	    	  }};
	    	  
		Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
			public void onPictureTaken(byte[] imageData, Camera c) {

				if (imageData != null) {

					Intent mIntent = new Intent();

					StoreByteImage(mContext, imageData, 50,
							"ImageName",editText);
					camera.startPreview();

					setResult(FOTO_MODE, mIntent);

				//	finish();

				}
			}
		};


		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub

			Camera.Parameters params=camera.getParameters();
			List<Size> sizes=params.getSupportedPreviewSizes();
			Size s=sizes.get(0);
			params.setPreviewSize(s.width,s.height);
			camera.setParameters(params);
			
			try
			{
				camera.setPreviewDisplay(holder);
			}catch(IOException ex)
			{
				ex.printStackTrace();
			}
			camera.startPreview();
			
			isActive=true;
			
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			camera=Camera.open();
			if(isActive)
			{
				camera.stopPreview();
			}
			
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			camera.stopPreview();
			// TODO Auto-generated method stub
			isActive=false;
			camera.release();
			
		}
		


		public static boolean StoreByteImage(Context mContext, byte[] imageData,
				int quality, String expName,EditText edit) {

			File extStore = Environment.getExternalStorageDirectory();
	      //  File sdImageMainDirectory = new File(extStore,"/Pictures");
			FileOutputStream fileOutputStream = null;
			try {

				BitmapFactory.Options options=new BitmapFactory.Options();
				options.inSampleSize = 5;
				
				Bitmap myImage = BitmapFactory.decodeByteArray(imageData, 0,
						imageData.length,options);

				
				fileOutputStream = new FileOutputStream(extStore.toString()+"/Pictures/"+expName+".jpg");
								
	  
				BufferedOutputStream bos = new BufferedOutputStream(
						fileOutputStream);

				myImage.compress(CompressFormat.JPEG, quality, bos);

				bos.flush();
				bos.close();
				
				//process image
				TessBaseAPI baseApi = new TessBaseAPI();
				String p=extStore.getAbsolutePath();
				// File file = new File(extStore,"Pictures/Test1.jpg");//line2 
				baseApi.init(p+"/Tesseract/", "eng");
				baseApi.setImage(myImage);
				String recognizedText = baseApi.getUTF8Text();
				baseApi.end();
				edit.setText(recognizedText);

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return true;
		}
}