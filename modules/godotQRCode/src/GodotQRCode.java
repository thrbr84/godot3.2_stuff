// Author: Thiago Bruno (message@thiagobruno.eti.br)
// https://github.com/thiagobruno/godot3.2_compilarmodulo

package org.godotengine.godot;

import android.util.Log;
import java.io.*;
import java.util.Date;
import android.app.Activity;
import android.content.Intent;
import java.util.Hashtable;
import android.util.Base64;
import android.graphics.Matrix;
import android.graphics.Bitmap; 
import androidx.core.content.FileProvider;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.EncodeHintType;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;


public class GodotQRCode extends Godot.SingletonBase {
	private static final String TAG = "godot";
	private String aplicationId = "com.thiagobruno.godotqrcode";
	private static Activity activity;
	private Integer cameraCallbackId = 0;

	static final int REQUEST_IMAGE_CAPTURE = 1;
	private int degreeRotation = 0;
	private int codeSize = 200;
	private String title = "Scan QR Code";

	private static final int WHITE = 0xFFFFFFFF;
	private static final int BLACK = 0xFF000000;

	public void scanCode()
	{
		/* Create a Zxing IntentIntegrator and start the QR code scan */
		IntentIntegrator integrator = new IntentIntegrator(activity);
		integrator.setRequestCode(REQUEST_IMAGE_CAPTURE);
		integrator.setOrientationLocked(false);
		integrator.initiateScan();
		integrator.setPrompt(title);
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
		try {
			if (code != null) 
			{
			    Bitmap bm = encodeAsBitmap(code, BarcodeFormat.QR_CODE, codeSize, codeSize);

				if(bm != null) {
					Bitmap imageBitmap = scaleImageKeepAspectRatio(bm);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);
					byte[] imageBytes = baos.toByteArray();
					String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
					String cleanImage = imageString.replace("data:image/png;base64,", "").replace("data:image/jpeg;base64,","");

					GodotLib.calldeferred(cameraCallbackId, "_on_godotQRCodeGenerated_success", new Object[] { code, cleanImage });
				}
			}

		} catch (WriterException e) { 
			GodotLib.calldeferred(cameraCallbackId, "_on_godotQRCodeGenerated_error", new Object[] { "Error to generate the QR Code" });
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

	static public Godot.SingletonBase initialize (Activity p_activity) {
		return new GodotQRCode(p_activity);
	}

	public void setCallbackId(int _cameraCallbackId) {
		this.cameraCallbackId = _cameraCallbackId;
	}

	public void setCodeSize(int _codeSize) {
		this.codeSize = _codeSize;
	}

	public void setTitle(String _title) {
		this.title = _title;
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
			"setTitle",
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

	protected void onMainActivityResult(int requestCode, int resultCode, Intent data) {

		if(requestCode != REQUEST_IMAGE_CAPTURE) {
			super.onMainActivityResult(requestCode, resultCode, data);
			return;
		}

		if(IntentIntegrator.parseActivityResult(resultCode, data) != null) {
			String qrcode = IntentIntegrator.parseActivityResult(resultCode, data).getContents();
			if(qrcode == null) {
				GodotLib.calldeferred(cameraCallbackId, "_on_godotQRCodeScanned_error", new Object[] { "Cancelled", "" });
			}
			else
			{
				GodotLib.calldeferred(cameraCallbackId, "_on_godotQRCodeScanned_success", new Object[] { qrcode, "" });
			}

		} else {
			super.onMainActivityResult(requestCode, resultCode, data);
		}
	}
}
