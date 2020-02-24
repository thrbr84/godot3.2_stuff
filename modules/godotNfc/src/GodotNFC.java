package org.godotengine.godot;

import android.app.Activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.app.PendingIntent;

import android.nfc.NfcAdapter;

import android.nfc.NdefRecord;
import android.nfc.NdefMessage;
import android.os.Parcelable;

import java.io.UnsupportedEncodingException;

import com.godot.game.R;


public class GodotNFC extends Godot.SingletonBase {

    	static public Godot.SingletonBase initialize(Activity p_activity) {
        	return new GodotNFC(p_activity);
    	}

    	public GodotNFC(Activity p_activity) {
			activity = (Godot)p_activity;

        	//register class name and functions to bind
        	registerClass("GodotNFC", new String[]{"setCallbackObject","getStatus","enableNFC"});
    	}

	Godot activity;
	
	private int callbackObjectID = 0;
	private String status = "Unitialized";

	NfcAdapter nfcAdapter;


	public void setCallbackObject(int callbackID)
	{
		callbackObjectID = callbackID;
	}

	public String getStatus()
	{
		return status;
	}


	public void enableNFC()
	{
		activity.runOnUiThread(new Runnable() {
        public void run() 
		{
			nfcAdapter = NfcAdapter.getDefaultAdapter(activity);

			if (nfcAdapter == null) { status = "NFC Unavailable"; }

			if (nfcAdapter.isEnabled()) 
			{
				status = "NFC Enabled";

				Intent intent = new Intent(activity,activity.getClass());
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

				PendingIntent pendingIntent = PendingIntent.getActivity(activity,0,intent,0);
				//IntentFilter[] intentFilter = new IntentFilter[]{};

				nfcAdapter.enableForegroundDispatch(activity,pendingIntent,null,null);
			}
			
			else { status = "NFC Disabled"; }
						
			GodotLib.calldeferred(callbackObjectID, "_on_callback", new Object[]{status});
        }
    	});

	}


	protected void onMainResume() 
	{
		//For some reason, this gets also called (and onMainPause), when... an Intent happens...? ...why? Lets use this I guess, since it doesn't look like there is another way to get an Intent...
		Intent intent = activity.getCurrentIntent();
		String action = intent.getAction();
		
		if (intent.hasExtra(NfcAdapter.EXTRA_TAG))
		{ 
			Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			if (parcelables != null && parcelables.length > 0){
				readTextFromMessage((NdefMessage) parcelables[0]);
			}
		}

	}


	private void readTextFromMessage(NdefMessage ndefMessage)
	{
		NdefRecord[] ndefRecords = ndefMessage.getRecords();

		if (ndefRecords != null && ndefRecords.length > 0)
		{
			NdefRecord ndefRecord = ndefRecords[0];
			String tagContent = getTextFromNdefRecord(ndefRecord);

			GodotLib.calldeferred(callbackObjectID, "_on_tag_read", new Object[]{tagContent});
		}
	}

	private String getTextFromNdefRecord(NdefRecord ndefRecord)
	{
		String tagContent = null;

		try {
			byte[] payload = ndefRecord.getPayload();
			String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
			int languageSize = payload[0] & 0063;
			tagContent = new String(payload,languageSize+1,payload.length - languageSize - 1, textEncoding);
		} catch (UnsupportedEncodingException e) {
			return "UnsupportedEncodingException";
		}

		return tagContent;
	}

}