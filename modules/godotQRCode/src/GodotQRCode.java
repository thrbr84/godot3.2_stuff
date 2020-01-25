// Author: Thiago Bruno (thiago.bruno@birdy.studio)
// https://github.com/thiagobruno/godot3.2_compilarmodulo

package org.godotengine.godot;

import android.util.Log;
import java.io.*;
import java.util.Date;
import java.util.Hashtable;
import android.util.Base64;
import java.text.SimpleDateFormat;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.graphics.Bitmap; 
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import androidx.core.content.FileProvider;
import android.graphics.Matrix;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.MultiFormatReader; //found via import at compile time, however was found at run time 
import com.google.zxing.NotFoundException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.RGBLuminanceSource;//found via import at compile time, however was found at run time 
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class GodotQRCode extends Godot.SingletonBase {
	private static final String TAG = "godot";
	private String aplicationId = "com.thiagobruno.godotqrcode";
	private static Activity activity;
	private Integer cameraCallbackId = 0;
	private static File cameraPathFile;

	static final int REQUEST_IMAGE_CAPTURE = 1;
	private Bitmap mImageBitmap;
	private String currentPhotoPath;
	private int degreeRotation = 0;
	private int codeSize = 200;
	private String cleanImage;

	private static final int WHITE = 0xFFFFFFFF;
	private static final int BLACK = 0xFF000000;

	public void scanCode()
	{
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
		    // Create the File where the photo should go
		    File photoFile = null;
		    try {
			photoFile = createImageFile();
			
		    } catch (IOException ex) {
			// Error occurred while creating the File
			Log.d(TAG, "Ocorreu um erro ao criar o arquivo de foto. Verifique as permissões!");
		    }
		    // Continue only if the File was successfully created
		    if (photoFile != null) {
			Uri photoURI = FileProvider.getUriForFile(activity, aplicationId, photoFile);
			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
			activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		    }
		}

	}



	static Bitmap encodeAsBitmap(String contents,
		               BarcodeFormat format,
		               int desiredWidth,
		               int desiredHeight) throws WriterException {
		Hashtable<EncodeHintType,Object> hints = null;
		String encoding = guessAppropriateEncoding(contents);
		if (encoding != null) {
			hints = new Hashtable<EncodeHintType,Object>(2);
			hints.put(EncodeHintType.CHARACTER_SET, encoding);
		}
		MultiFormatWriter writer = new MultiFormatWriter();    
		BitMatrix result = writer.encode(contents, format, desiredWidth, desiredHeight, hints);
		int width = result.getWidth();
		int height = result.getHeight();
		int[] pixels = new int[width * height];
		// All are 0, or black, by default
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	private static String guessAppropriateEncoding(CharSequence contents) {
		// Very crude at the moment
		for (int i = 0; i < contents.length(); i++) {
			if (contents.charAt(i) > 0xFF) {
				return "UTF-8";
			}
		}
		return null;
	}

	public void generateCode(String code) 
	{
		// this is a small sample use of the QRCodeEncoder class from zxing
		try {
			// generate a 150x150 QR code
			if (code != null) 
			{
			    	Bitmap bm = encodeAsBitmap(code, BarcodeFormat.QR_CODE, codeSize, codeSize);

				if(bm != null) {
					//image_view.setImageBitmap(bm);

					Bitmap imageBitmap = scaleImageKeepAspectRatio(bm);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);
					byte[] imageBytes = baos.toByteArray();
					String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
					cleanImage = imageString.replace("data:image/png;base64,", "").replace("data:image/jpeg;base64,","");

					GodotLib.calldeferred(cameraCallbackId, "_on_godotQRCodeGenerated_success", new Object[] { code, cleanImage });
				}
			}

		} catch (WriterException e) { 
			//eek 
		}
	}

	public Bitmap scaleImageKeepAspectRatio(Bitmap scaledGalleryBitmap){

		Matrix matrix = new Matrix();
		matrix.postRotate(degreeRotation);
		int imageSize = 500;
		int imageWidth = scaledGalleryBitmap.getWidth();
		int imageHeight = scaledGalleryBitmap.getHeight();
		int newHeight = (imageHeight * imageSize)/imageWidth;
		Bitmap scaledBitmap = Bitmap.createScaledBitmap(scaledGalleryBitmap, imageSize, newHeight, false);
		Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
		return rotatedBitmap;
	}

	private File createImageFile() throws IOException {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "PNG_" + timeStamp + "_";
		File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(
		    imageFileName,  /* prefix */
		    ".png",         /* suffix */
		    storageDir      /* directory */
		);
		currentPhotoPath = image.getAbsolutePath();
		return image;
	}

	static public Godot.SingletonBase initialize (Activity p_activity) {
		return new GodotQRCode(p_activity);
	}

	public void setCallbackId(int _cameraCallbackId) {
		this.cameraCallbackId = _cameraCallbackId;
	}

	public void setCodeSize(int _codeSize) {
		this.codeSize = _codeSize;
	}

	public void setImageRotated(int _rotatedDegree) {
		this.degreeRotation = _rotatedDegree;
	}

	public GodotQRCode(Activity p_activity) {
		registerClass("GodotQRCode", new String[]
		{
			"scanCode",
			"generateCode",
			"setCodeSize",
			"setImageRotated",
			"setCallbackId"
		});
		activity = p_activity;
	}

	public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
	{
	    ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
	    image.compress(compressFormat, quality, byteArrayOS);
	    return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
	}

	public static String scanQRImage(Bitmap bMap) {
	    String contents = null;

	    int[] intArray = new int[bMap.getWidth()*bMap.getHeight()];
	    //copy pixel data from the Bitmap into the 'intArray' array
	    bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());

	    LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
	    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

	    Reader reader = new MultiFormatReader();
	    try {
		Result result = reader.decode(bitmap);
		contents = result.getText();
	    }
	    catch (Exception e) {
		Log.e("QrTest", "Error decoding barcode", e);
	    }
	    return contents;
	}

	protected void onMainActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == activity.RESULT_OK) {

			// 
			Bitmap imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);

			// Scan code
			String qrcode = scanQRImage(imageBitmap);


			// Return image
			imageBitmap = scaleImageKeepAspectRatio(imageBitmap);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);
			byte[] imageBytes = baos.toByteArray();
			String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
			cleanImage = imageString.replace("data:image/png;base64,", "").replace("data:image/jpeg;base64,","");

			GodotLib.calldeferred(cameraCallbackId, "_on_godotQRCodeScanned_success", new Object[] { qrcode, cleanImage });
		}
	}

}
