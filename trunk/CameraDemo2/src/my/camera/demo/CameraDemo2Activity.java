package my.camera.demo;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
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
import android.view.WindowManager.LayoutParams;
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
		private boolean isFocuse;
		Button buttonClick;
		EditText editText;
		private Context mContext = this;
		static final int FOTO_MODE = 0;
		
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	    	isFocuse=false;
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
	      //  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	    //    addContentView(draw, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	    	buttonClick.setOnClickListener(new OnClickListener()
	    	{
	    		public void onClick(View arg0) {
	    			

	    			camera.autoFocus(myAutoFocusCallback);	    		
	    			try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			//camera.takePicture(null, mPictureCallback, mPictureCallback);
	    			camera.setPreviewCallback(previewCallback);
	    			
					//AlertDialog.Builder alertbox = new AlertDialog.Builder(
							//CameraDemo2Activity.this);
					//alertbox.setMessage("DKJdshdsjdskdjskdsdjs");
	    			//editText.setText("Hello every one!");

	    		}
	    		
	    	} );
	    	
	    }
	    
	Camera.Size getBestPreviewSize(int width, int height,
			Camera.Parameters parameters) {
		Camera.Size result = null;

		for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
			if (size.width <= width && size.height <= height) {
				if (result == null) {
					result = size;
				} else {
					int resultArea = result.width * result.height;
					int newArea = size.width * size.height;

					if (newArea > resultArea) {
						result = size;
					}
				}
			}
		}

		return (result);
	}
	    
	//camara preview callback
	 Camera.PreviewCallback previewCallback = new Camera.PreviewCallback()  
	    { 
	            public void onPreviewFrame(byte[] data, Camera camera)  
	            { 
	            	
	            	storeByteImage(data,camera);
	                    
	                    
	            } 

	    };
	    
	    void storeByteImage(byte[]data,Camera camera)
	    {
	    	if(camera!=null)
        	{
                try 
                { 
                
                	int w=camera.getParameters().getPreviewSize().width;
                	int h=camera.getParameters().getPreviewSize().height;
                	Bitmap bitmap=null;
                	YuvImage yuv=new YuvImage(data,ImageFormat.NV21,w,h,null);
                     //   BitmapFactory.Options opts = new BitmapFactory.Options(); 
                      //  Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);//,opts); 
                	File extStore = Environment.getExternalStorageDirectory();
                	   ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                	//FileOutputStream output=new FileOutputStream(extStore.toString()+"/Pictures/"+"EXP.jpg"); 
                	yuv.compressToJpeg(new Rect(0,0,w,h), 100, outputStream);
                	bitmap= BitmapFactory.decodeByteArray( outputStream.toByteArray(), 0, outputStream.size());
                	
                	//output.close();
                	if(bitmap!=null)
                	{
                		FileOutputStream output=new FileOutputStream(extStore.toString()+"/Pictures/"+"FileThuNghiem.jpg"); 
                		  bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
                		  output.flush();
                		  output.close();
                		  File file = new File(extStore,"Pictures/FileThuNghiem.jpg");//line2 
          				TessBaseAPI baseApi = new TessBaseAPI();
        				String p=extStore.getAbsolutePath();
        				// File file = new File(extStore,"Pictures/Test1.jpg");//line2 
        				baseApi.init(p+"/Tesseract/", "eng");
        				baseApi.setImage(file);
        				String recognizedText = baseApi.getUTF8Text();
        				baseApi.end();
        				//editText=(EditText)findViewById(R.id.textCamera);
        				editText.setText(recognizedText);
                	}
                } 
                catch(Exception e) 
                {
                	
                	editText.setText("Error");
                } 
                camera.setPreviewCallback(null);
        	}
	    	
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
			List<Size> sl = params.getSupportedPictureSizes();
			//Size s=sizes.get(0);
/*			List<String> focusModes = params.getSupportedFocusModes();
			if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO))
			{
				params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
			}*/
			Size size=getBestPreviewSize(width,height,params);
			if(size!=null)
				params.setPreviewSize(size.width,size.height);

			
			//if(sl.size()>2)
				//params.setPictureSize(sl.get(sl.size()-1).width, sl.get(sl.size()-1).height);
			
			//editText.setText(sizes.size());
			camera.setParameters(params);
			

			camera.startPreview();
			
			isActive=true;
			
			
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			// Canvas c = holder.lockCanvas(null);
			//Canvas c = holder.lockCanvas(new Rect(5,3,44,45));
            // onDraw(c);
			
            // holder.unlockCanvasAndPost(c);
			camera=Camera.open();
			try
			{
				
				camera.setPreviewDisplay(holder);
			}catch(IOException ex)
			{
				ex.printStackTrace();
			}
/*			if(isActive)
			{
				camera.stopPreview();
			}*/
			
		}


		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			//camera.setPreviewCallback(null);
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

				//myImage=Bitmap.createBitmap(myImage, 0, 0, w, h);
			//	myImage=myImage.copy(Bitmap.Config.ARGB_8888, true);
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