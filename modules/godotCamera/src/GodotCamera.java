// Author: Thiago Bruno (thiago.bruno@birdy.studio)
// https://github.com/thiagobruno/godot3.2_compilarmodulo

package org.godotengine.godot;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;

import org.godotengine.godot.Godot;
import org.godotengine.godot.GodotLib;

import android.hardware.Camera;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import android.widget.FrameLayout;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import android.util.Base64;
import java.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.graphics.Bitmap; 
import android.graphics.Rect;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import androidx.core.content.FileProvider;
//import android.support.v4.content.FileProvider;
import android.graphics.Matrix;

public class GodotCamera extends Godot.SingletonBase {
	private static final String TAG = "godot";
	private String aplicationId = "com.thiagobruno.godotcamera";
	private static Activity activity;
	private Integer cameraCallbackId = 0;
	private static File cameraPathFile;

	static final int REQUEST_IMAGE_CAPTURE = 1;
	private Bitmap mImageBitmap;
	private String currentPhotoPath;
	private int degreeRotation = 0;
	private int imageSize = 500;
	private String cleanImage;

	private FrameLayout layout = null; // Store the layout
	private FrameLayout.LayoutParams adParams = null; // Store the layout params

	private Context mContext;
	private ViewGroup mRoot;

	private static final int ERROR_CAMERA_NONE = 0;
	private static final int ERROR_CAMERA_FATAL = 1;
	private static final int ERROR_CAMERA_OUT_OF_MEMORY = 2;
	private static final int ERROR_CAMERA_MINIMUM_NUMBER_OF_FACE_NOT_DETECTED = 3;

	private GodotCameraView mGodotCameraView;


	public void openCamera()
	{
		Log.d(TAG, "taking...");
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
			Log.d(TAG, "right!");
			Uri photoURI = FileProvider.getUriForFile(activity, aplicationId, photoFile);
			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
			activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		    }
		}

	}

	public Bitmap scaleImageKeepAspectRatio(Bitmap scaledGalleryBitmap){

		Matrix matrix = new Matrix();
		matrix.postRotate(degreeRotation);

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
		return new GodotCamera(p_activity);
	}

	public boolean setCameraCallbackId(int _cameraCallbackId,
		final boolean cameraFacingBack, 
		final String parameters, 
		final int x, 
		final int y, 
		final int w, 
		final int h, 
		final boolean visibility) 
	{
		this.cameraCallbackId = _cameraCallbackId;


		activity.runOnUiThread(new Runnable() {
		@Override
		public void run() {

			GodotCameraView godotCameraView;
			try {
				Log.d(TAG, "Camera inicializada...");
			    godotCameraView = GodotCameraView.initializeView(activity, 
				ParameterSerializer.unSerialize(parameters),
				cameraFacingBack ? Camera.CameraInfo.CAMERA_FACING_BACK : Camera.CameraInfo.CAMERA_FACING_FRONT,
				new Rect(x, y, x + w, y + h), visibility);
			} catch (Exception e) {
			    Log.e(TAG, "failed to create camera preview: " + e.getMessage());
			    return;
			}

			mGodotCameraView = godotCameraView;
			layout.addView(mGodotCameraView);

			GodotLib.calldeferred(cameraCallbackId, "_set_camera_features_", new Object[]{
				ParameterSerializer.serialize(mGodotCameraView.getCameraFeatures())
		        });
		    }
		});

		return true;


	}

	@Override
	public View onMainCreateView(Activity activity) {
	   layout = new FrameLayout(activity);
	   return layout;
	}

	public void setImageSize(int _imageSize) {
		this.imageSize = _imageSize;
	}

	public void setImageRotated(int _rotatedDegree) {
		this.degreeRotation = _rotatedDegree;
	}

	public GodotCamera(Activity p_activity) {
		registerClass("GodotCamera", new String[]
		{
			"initializeView",
			"resizeView",
			"destroyView",
			"setViewVisibility",
			"setViewParameterInt",
			"setViewParameterBool",
			"setViewParameterString",
			"setPreviewCameraFacing",
			"takePicture",
			"refreshCameraPreview",

			"openCamera",
			"setImageSize",
			"setImageRotated",
			"setCameraCallbackId"
		});
		activity = p_activity;
	}

	public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
	{
	    ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
	    image.compress(compressFormat, quality, byteArrayOS);
	    return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
	}


    public boolean sanityCheck(int instanceId) {
	    if (cameraCallbackId == null) {
            Log.d(TAG, "sanityCheck: view not instantiated");
            return false;
        } else if (cameraCallbackId != instanceId) {
            Log.d(TAG, "sanityCheck: methods should only be called by instance script");
            return false;
        } else {
	        return true;
        }
    }



    public void destroyView(int instanceId) {
	    if (!sanityCheck(instanceId))
	        return;
		Log.d(TAG, "Java destruindo...");

	    activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _destroyView();
            }
        });
    }

    private void _destroyView() {
	    if (mGodotCameraView != null) {
	        layout.removeView(mGodotCameraView);
	        cameraCallbackId = null;
        }
    }

    private void setViewVisibility(int instanceId, final boolean is_visible) {
	    if (!sanityCheck(instanceId))
	        return;

	    if (mGodotCameraView != null)
	        activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mGodotCameraView.setVisibility(is_visible ? View.VISIBLE : View.INVISIBLE);
                }
            });
    }

    private void setViewParameterInt(int instanceId, String parameterKey, int parameterValue) {
	    _setViewParameter(instanceId, parameterKey, parameterValue);
    }

    private void setViewParameterBool(int instanceId, String parameterKey, boolean parameterValue) {
        _setViewParameter(instanceId, parameterKey, parameterValue);
    }

    private void setViewParameterString(int instanceId, String parameterKey, String parameterValue) {
        _setViewParameter(instanceId, parameterKey, parameterValue);
    }

    private void _setViewParameter(int instanceId, final String parameterKey, final Object parameterValue) {
        if (!sanityCheck(instanceId))
            return;

	    if (mGodotCameraView != null)
	        activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mGodotCameraView.setViewCameraParameterValue(parameterKey, parameterValue);
                }
            });
    }


    private void setPreviewCameraFacing(final int instanceId, final boolean isBackFacing) {
	    if (!sanityCheck(instanceId))
	        return;

	    if (mGodotCameraView != null)
	        activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final  HashMap<String, Object> cameraFeatures = mGodotCameraView.setViewCameraFacing(isBackFacing);
                    if (cameraFeatures != null)
                        GodotLib.calldeferred(instanceId, "_set_camera_features_", new Object[] {
                                ParameterSerializer.serialize(cameraFeatures)
                        });
                }
            });
    }

    private void refreshCameraPreview(final int instanceId) {
	    if (!sanityCheck(instanceId))
	        return;

	    if (mGodotCameraView != null) {
	        activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mGodotCameraView.refreshPreview();
                }
            });
        }
    }

    private void takePicture(final int instanceId, final int minimumNumberOfFace) {
	    if (!sanityCheck(instanceId))
	        return;

	    if (mGodotCameraView != null) {
	        activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mGodotCameraView.takePicture(new GodotCameraView.TakePictureCallback() {

                        @Override
                        public boolean onPicturePreTake(List<Rect> detectedFaces) {
                            if (detectedFaces.size() < minimumNumberOfFace) {
                                GodotLib.calldeferred(instanceId, "_on_picture_taken_", new Object[] {
                                        ERROR_CAMERA_MINIMUM_NUMBER_OF_FACE_NOT_DETECTED, "", ""
                                });
                                return false;
                            }
                            return true;
                        }

                        @Override
                        public void onPictureTaken(Bitmap bitmap, List<Rect> detectedFaces) {

                            final HashMap<String, Object> detectedFacesExtra = new HashMap<>();
                            int faceCount = 0;
                            for (Rect rect : detectedFaces) {
                                faceCount++;
                                detectedFacesExtra.put("face" + faceCount, rect);
                            }

                            final ByteArrayOutputStream out = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                            try {
                                GodotLib.calldeferred(instanceId, "_on_picture_taken_", new Object[]{
                                        ERROR_CAMERA_NONE, out.toByteArray(), ParameterSerializer.serialize(detectedFacesExtra)
                                });
                            } catch (Exception e) {
                                Log.e(TAG, "onPictureTaken: Error -> " + e.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            GodotLib.calldeferred(instanceId, "_on_picture_taken_", new Object[] {
                                    e instanceof OutOfMemoryError ? ERROR_CAMERA_OUT_OF_MEMORY : ERROR_CAMERA_FATAL, "", ""
                            });
                        }
                    });
                }
            });
        }
    }



	protected void onMainActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == activity.RESULT_OK) {
			Log.d(TAG, "sending to godot...");
			Log.d(TAG, currentPhotoPath);
			Bitmap imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);
			imageBitmap = scaleImageKeepAspectRatio(imageBitmap);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);
			byte[] imageBytes = baos.toByteArray();
			String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
			cleanImage = imageString.replace("data:image/png;base64,", "").replace("data:image/jpeg;base64,","");

			GodotLib.calldeferred(cameraCallbackId, "_on_GodotCamera_success", new Object[] { cleanImage });
		}
	}

/*
	@Override
	protected void onMainPause() {
	    if (mGodotCameraView != null)
		mGodotCameraView.onActivityPause();
	}

	@Override
	protected void onMainResume() {
	if (mGodotCameraView != null)
	    mGodotCameraView.onActivityResume();
	}

	@Override
	protected void onMainDestroy() {
		_destroyView();
	}
*/


}
