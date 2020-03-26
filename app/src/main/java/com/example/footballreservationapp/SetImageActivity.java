package com.example.footballreservationapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

//카메라, 갤러리 둘중 하나의 이미지 업로드 방식 선택시 수행 되는 액티비티
public class SetImageActivity extends Activity implements OnClickListener {

	static final int camera=2001;
	static final int gallery=2002;
	Button ClosepopBtn,camerapopBtn,gallerypopBtn;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setimage);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		int width = dm.widthPixels;
		int height = dm.heightPixels;

		getWindow().setLayout((int)(width * 0.7), (int)(height * 0.2));

		init();

	}

	private void init() {
		camerapopBtn=(Button)findViewById(R.id.camerapopBtn);
		gallerypopBtn=(Button)findViewById(R.id.gallerypopBtn);
		ClosepopBtn=(Button)findViewById(R.id.ClosepopBtn);

		gallerypopBtn.setOnClickListener(this);
		camerapopBtn.setOnClickListener(this);
		ClosepopBtn.setOnClickListener(this);

	}
	
	@Override
	public void onClick(View v) {
		Intent intent=new Intent();
		switch(v.getId()){
		case R.id.camerapopBtn:
			intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			startActivityForResult(intent, camera);
			break;
		case R.id.gallerypopBtn:
			
			intent.setAction(Intent.ACTION_GET_CONTENT);
			intent.setType("image/-");
			startActivityForResult(intent, gallery);
			break;

		case R.id.ClosepopBtn:
			setResult(RESULT_CANCELED, intent);
			Toast.makeText(getApplicationContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
			finish();
			break;
		default:
			break;
		}
		
	}
	@SuppressLint("NewApi")
	private Bitmap resize(Bitmap bm){
		
		Configuration config=getResources().getConfiguration();
		/*if(config.smallestScreenWidthDp>=800)
			bm = Bitmap.createScaledBitmap(bm, 400, 240, true);//이미지 크기로 인해 용량초과
		else */if(config.smallestScreenWidthDp>=600)
			bm = Bitmap.createScaledBitmap(bm, 300, 180, true);
		else if(config.smallestScreenWidthDp>=400)
			bm = Bitmap.createScaledBitmap(bm, 200, 120, true);
		else if(config.smallestScreenWidthDp>=360)
			bm = Bitmap.createScaledBitmap(bm, 180, 108, true);
		else 
			bm = Bitmap.createScaledBitmap(bm, 160, 96, true);
		
		return bm;
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Intent intent=new Intent();
		Bitmap bm;
		if(resultCode==RESULT_OK){
			switch(requestCode){
			case camera:
				/*
				try {
					
					bm= Images.Media.getBitmap(getContentResolver(), data.getData());

					bm=resize(bm);
					intent.putExtra("bitmap",bm);
				} catch (FileNotFoundException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
					
					e.printStackTrace();
				}*/
				bm=(Bitmap) data.getExtras().get("data");

				bm=resize(bm);
				intent.putExtra("bitmap",bm);
				setResult(RESULT_OK, intent);
				finish();
				break;
			case gallery:
				try {
					
					bm = Images.Media.getBitmap( getContentResolver(), data.getData());
                    //Bitmap bm2 = Images.Media.getBitmap( getContentResolver(), data.getData());

//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                    byte[] bytes = stream.toByteArray();
//                    intent.putExtra("BMP",bytes);

//                    File file = new File(Environment.getExternalStorageDirectory() + "imageBitmap" + ".png");
//                    FileOutputStream fOut = new FileOutputStream(file);
//
//                    bm2.compress(Bitmap.CompressFormat.PNG, 85, fOut);
//                    fOut.flush();
//                    fOut.close();
//                    intent.putExtra("filename", "imageBitmap");

					bm=resize(bm);
					intent.putExtra("bitmap",bm);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch(OutOfMemoryError e){
					Toast.makeText(getApplicationContext(), "이미지 용량이 너무 큽니다.", Toast.LENGTH_SHORT).show();
				}
				setResult(RESULT_OK, intent);
				finish();
				break;
				
			default:
				setResult(RESULT_CANCELED, intent);
				finish();
				break;
					
			}
		}else{
			setResult(RESULT_CANCELED, intent);
			finish();
		}
	}

    public Bitmap readImageWithSampling(String imagePath, int targetWidth, int targetHeight) {
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);

        int photoWidth  = bmOptions.outWidth;
        int photoHeight = bmOptions.outHeight;

        if (targetHeight <= 0) {
            targetHeight = (targetWidth * photoHeight) / photoWidth;
        }

        // Determine how much to scale down the image
        int scaleFactor = 1;
        if (photoWidth > targetWidth) {
            scaleFactor = Math.min(photoWidth / targetWidth, photoHeight / targetHeight);
        }

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeFile(imagePath, bmOptions);
    }

}
