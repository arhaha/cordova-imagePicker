/**
 * An Image Picker Plugin for Cordova/PhoneGap.
 */
package com.synconset;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public class ImagePicker extends CordovaPlugin {
	public static String TAG = "ImagePicker";

	private CallbackContext callbackContext;
	private JSONObject params;

	public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext)
			throws JSONException {
		this.callbackContext = callbackContext;
		this.params = args.getJSONObject(0);
		if (action.equals("getPictures")) {
			Intent intent = new Intent(cordova.getActivity(), MultiImageChooserActivity.class);
			int max = 20;
			int desiredWidth = 0;
			int desiredHeight = 0;
			int quality = 100;
			if (this.params.has("maximumImagesCount")) {
				max = this.params.getInt("maximumImagesCount");
			}
			if (this.params.has("width")) {
				desiredWidth = this.params.getInt("width");
			}
			if (this.params.has("height")) {
				desiredWidth = this.params.getInt("height");
			}
			if (this.params.has("quality")) {
				quality = this.params.getInt("quality");
			}

			if (this.params.has("compress") && this.params.getBoolean("compress")) {
				int maxWidthOrHeight = 2048;
				int compressQuality = 90;
				int maxImageByteSize = 5 * 1024 * 1024; // 5mb
				int minNeedcompressByteSize = 512 * 1024; // 512kb
				boolean autoCrop = false;
				if (this.params.has("maxWidthOrHeight")) {
					maxWidthOrHeight = this.params.getInt("maxWidthOrHeight");
				}
				if (this.params.has("compressQuality")) {
					compressQuality = this.params.getInt("compressQuality");
				}
				if (this.params.has("maxImageByteSize")) {
					maxImageByteSize = this.params.getInt("maxImageByteSize");
				}
				if (this.params.has("minNeedcompressByteSize")) {
					minNeedcompressByteSize = this.params.getInt("minNeedcompressByteSize");
				}
				if (this.params.has("autoCrop")) {
					autoCrop = this.params.getBoolean("autoCrop");
				}
				intent.putExtra("COMPRESS", 1);
				intent.putExtra("MAXWIDTHORHEIGHT", maxWidthOrHeight);
				intent.putExtra("COMPRESSQUALITY", compressQuality);
				intent.putExtra("MAXIMAGEBYTESIZE", maxImageByteSize);
				intent.putExtra("MINNEEDCOMPRESSBYTESIZE", minNeedcompressByteSize);
				intent.putExtra("AUTOCROP", autoCrop == true ? 1 : 0);
			} else {
				intent.putExtra("COMPRESS", 0);
			}
			intent.putExtra("MAX_IMAGES", max);
			intent.putExtra("WIDTH", desiredWidth);
			intent.putExtra("HEIGHT", desiredHeight);
			intent.putExtra("QUALITY", quality);
			if (this.cordova != null) {
				this.cordova.startActivityForResult((CordovaPlugin) this, intent, 0);
			}
		}
		return true;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && data != null) {
			ArrayList<String> fileNames = data.getStringArrayListExtra("MULTIPLEFILENAMES");
			JSONArray res = new JSONArray(fileNames);
			this.callbackContext.success(res);
		} else if (resultCode == Activity.RESULT_CANCELED && data != null) {
			String error = data.getStringExtra("ERRORMESSAGE");
			this.callbackContext.error(error);
		} else if (resultCode == Activity.RESULT_CANCELED) {
			JSONArray res = new JSONArray();
			this.callbackContext.success(res);
		} else {
			this.callbackContext.error("No images selected");
		}
	}

	public void onRestoreStateForActivityResult(Bundle state, CallbackContext callbackContext) {
		this.callbackContext = callbackContext;
	}
}