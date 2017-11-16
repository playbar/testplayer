package com.baofeng.mj.vrplayer.ftp.ftp;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;


import com.baofeng.mj.vrplayer.util.StorageUtil;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;


/**
 * This media rescanner runs in the background. The rescan might not happen
 * immediately.
 * 
 * 
 */
public enum MediaUpdater {
	INSTANCE;

	private final static String TAG = MediaUpdater.class.getSimpleName();

	// the systembroadcast to remount the media is only done after a little
	// while (5s)
//	private static Timer sTimer = new Timer();

	public static void notifyFileCreated(Context mContext,String path) {
		if (Defaults.do_mediascanner_notify) {
			MediaScannerConnection.scanFile(mContext, new String[] { path },
					null, new MediaScannerConnection.OnScanCompletedListener() {
						@Override
						public void onScanCompleted(String path, Uri uri) {
						}
					});
		}
	}

	public static void notifyFileDeleted(final Context mContext,final String path) {
		// The media mounted broadcast is very taxing on the system, so we only
		// do this
		// if for 5 seconds there was no same request, otherwise we wait again.
		if (Defaults.do_mediascanner_notify) {
			Log.d(TAG, "Notifying others about deleted file: " + path);
			// the systembroadcast might have been requested already, cancel if
			// so
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
				final Uri contentUri = Uri.fromFile(Environment.getExternalStorageDirectory());
				scanIntent.setData(contentUri);
				mContext.sendBroadcast(scanIntent);
			} else {
				final Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory()));
				mContext.sendBroadcast(intent);
			}
//			sTimer.cancel();
//			// that timer is of no value any more, create a new one
//			sTimer = new Timer();
//			// and in 5s let it send the broadcast, might never hapen if before
//			// that time it gets canceled by this code path
//			sTimer.schedule(new TimerTask() {
//				@Override
//				public void run() {
//					Log.d(TAG, "Sending ACTION_MEDIA_MOUNTED broadcast");
//
//
////					mContext.sendBroadcast(new Intent(
////							Intent.ACTION_MEDIA_MOUNTED,
////							Uri.parse("file://"
////									+ Environment.getExternalStorageDirectory())));
//				}
//			}, 5000);
		}
	}

}
