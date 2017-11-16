package com.baofeng.mj.vrplayer.ftp.ftp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;


import com.baofeng.mj.vrplayer.ftp.util.PreferenceUtils;
import  com.baofeng.mj.util.publicutil.NetworkUtil;
import com.baofeng.mj.vrplayer.util.StorageUtil;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


public class FtpServerService extends Service implements Runnable {
	private static final String TAG = FtpServerService.class.getSimpleName();

	// some key
	public static final String PORT_KEY = "portNum";
	public static final String USER_NAME_KEY = "username";
	public static final String PASSWORD_KEY = "password";
	public static final String CHOOSE_DIR_KEY = "chrootDir";
	public static final String STAY_AWAKE_KEY = "stayAwake";
	public static final String IS_NEED_PASSWORD_KEY = "isNeedPassword";
	public static final String PASSWORD_CAN_SEE = "passwordCanSee";

	// Service will (global) broadcast when server start/stop
	public static final String ACTION_STARTED = "com.way.ftp.FTPSERVER_STARTED";
	public static final String ACTION_STOPPED = "com.way.ftp.FTPSERVER_STOPPED";
	public static final String ACTION_FAILEDTOSTART = "com.way.ftp.FTPSERVER_FAILEDTOSTART";

	public static final String WAKE_LOCK_TAG = "wayFTP";

	protected static Thread mServerThread = null;
	protected boolean mShouldExit = false;

	protected ServerSocket mListenSocket;
	protected static WifiLock mWifiLock = null;

	// The server thread will check this often to look for incoming
	// connections. We are forced to use non-blocking accept() and polling
	// because we cannot wait forever in accept() if we want to be able
	// to receive an exit signal and cleanly exit.
	public static final int WAKE_INTERVAL_MS = 1000; // milliseconds

	protected static int mPort = Defaults.portNumber;
	protected static boolean isFullWake;

	private TcpListener mWifiListener = null;
	private final List<SessionThread> mSessionThreads = new ArrayList<SessionThread>();

	PowerManager.WakeLock mWakeLock;

	@Override
	public IBinder onBind(Intent intent) {
		// We don't implement this functionality, so ignore it
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "SwiFTP server created");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		mShouldExit = false;
		int attempts = 10;
		// The previous server thread may still be cleaning up, wait for it
		// to finish.
		while (mServerThread != null) {
			Log.w(TAG, "Won't start, server thread exists");
			if (attempts > 0) {
				attempts--;
				Util.sleepIgnoreInterupt(1000);
			} else {
				Log.w(TAG, "Server thread already exists");
				return;
			}
		}
		Log.d(TAG, "Creating server thread");
		mServerThread = new Thread(this);
		mServerThread.start();
	}

	public static boolean isRunning() {
		// return true if and only if a server Thread is running
		if (mServerThread == null) {
			Log.d(TAG, "Server is not running (null serverThread)");
			return false;
		}
		if (!mServerThread.isAlive()) {
			Log.d(TAG, "serverThread non-null but !isAlive()");
		} else {
			Log.d(TAG, "Server is alive");
		}
		return true;
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy() Stopping server");
		mShouldExit = true;
		if (mServerThread == null) {
			Log.w(TAG, "Stopping with null serverThread");
			return;
		} else {
			mServerThread.interrupt();
			try {
				mServerThread.join(10000); // wait 10 sec for server thread to
											// finish
			} catch (InterruptedException e) {
			}
			if (mServerThread.isAlive()) {
				Log.w(TAG, "Server thread failed to exit");
				// it may still exit eventually if we just leave the
				// shouldExit flag set
			} else {
				Log.d(TAG, "serverThread join()ed ok");
				mServerThread = null;
			}
		}
		try {
			if (mListenSocket != null) {
				Log.i(TAG, "Closing listenSocket");
				mListenSocket.close();
			}
		} catch (IOException e) {
		}

		if (mWifiLock != null) {
			mWifiLock.release();
			mWifiLock = null;
		}
		Log.d(TAG, "FTPServerService.onDestroy() finished");
	}

	private boolean loadSettings() {
		Log.d(TAG, "Loading settings");
		mPort = PreferenceUtils.getPrefInt(this, PORT_KEY, Defaults.portNumber);
		Log.d(TAG, "Using port " + mPort);

		isFullWake = PreferenceUtils.getPrefBoolean(this, STAY_AWAKE_KEY,
				Defaults.stayAwake);

		// The username, password, and chrootDir are just checked for sanity
		String username = PreferenceUtils.getPrefString(this, USER_NAME_KEY,
				Defaults.username);
		String password = PreferenceUtils.getPrefString(this, PASSWORD_KEY,
				Defaults.password);
		String chrootDir = PreferenceUtils.getPrefString(this, CHOOSE_DIR_KEY,
				StorageUtil.getDownloadDir());
		boolean isNeedPassword = PreferenceUtils.getPrefBoolean(this,
				IS_NEED_PASSWORD_KEY, true);
		Defaults.chrootDir = StorageUtil.getDownloadDir();
		validateBlock: {
			if (!isNeedPassword) {
				if (username == null || password == null) {
					Log.e(TAG, "Username or password is invalid");
					break validateBlock;
				}
				Defaults.username = username;
			}

//			File chrootDirAsFile = new File(chrootDir);
//			if (!chrootDirAsFile.isDirectory()) {
//				Log.e(TAG, "Chroot dir is invalid");
//				break validateBlock;
//			}
			// Globals.setChrootDir(chrootDirAsFile);
			// Globals.setUsername(username);
//			Defaults.chrootDir = StorageUtil.getDownloadDir(this);
			return true;
		}
		// We reach here if the settings were not sane
		return false;
	}

	// This opens a listening socket on all interfaces.
	void setupListener() throws IOException {
		mListenSocket = new ServerSocket();
		mListenSocket.setReuseAddress(true);
		mListenSocket.bind(new InetSocketAddress(mPort));
	}

	public void run() {
		// The UI will want to check the server status to update its
		// start/stop server button

		Log.d(TAG, "Server thread running");
		// set our members according to user preferences
		if (!loadSettings()) {
			// loadSettings returns false if settings are not sane
			cleanupAndStopService();
			sendBroadcast(new Intent(ACTION_FAILEDTOSTART));
			Toast.makeText(this, "ftp server fail to start", Toast.LENGTH_LONG)
					.show();
			return;
		}

		if (!NetworkUtil.isWIFIConnected(this)) {
			cleanupAndStopService();
			sendBroadcast(new Intent(ACTION_FAILEDTOSTART));
			Toast.makeText(this, "ftp server fail to start", Toast.LENGTH_LONG)
					.show();
			return;
		}

		// If configured to accept connections via wifi, then set up the
		// socket
		try {
			setupListener();
		} catch (IOException e) {
			Log.w(TAG, "Error opening port, check your network connection.");
			// serverAddress = null;
			cleanupAndStopService();
			return;
		}
		takeWifiLock();
		takeWakeLock();

		Log.i(TAG, "SwiFTP server ready");

		// A socket is open now, so the FTP server is started, notify rest of
		// world
//		((FtpServerApp) FtpServerApp.getInstance()).setupNotification(this);
		sendBroadcast(new Intent(ACTION_STARTED));

		while (!mShouldExit) {
			if (mWifiListener != null) {
				if (!mWifiListener.isAlive()) {
					Log.d(TAG, "Joining crashed wifiListener thread");
					try {
						mWifiListener.join();
					} catch (InterruptedException e) {
					}
					mWifiListener = null;
				}
			}
			if (mWifiListener == null) {
				// Either our wifi listener hasn't been created yet, or has
				// crashed,
				// so spawn it
				mWifiListener = new TcpListener(getApplicationContext(),mListenSocket, this);
				mWifiListener.start();
			}
		}
		try {
			// todo: think about using ServerSocket, and just closing
			// the main socket to send an exit signal
			Thread.sleep(WAKE_INTERVAL_MS);
		} catch (InterruptedException e) {
			Log.d(TAG, "Thread interrupted");
		}

		terminateAllSessions();

		if (mWifiListener != null) {
			mWifiListener.quit();
			mWifiListener = null;
		}
		mShouldExit = false; // we handled the exit flag, so reset it to
								// acknowledge
		Log.d(TAG, "Exiting cleanly, returning from run()");

		cleanupAndStopService();
	}

	private void terminateAllSessions() {
		Log.i(TAG, "Terminating " + mSessionThreads.size()
				+ " session thread(s)");
		synchronized (this) {
			for (SessionThread sessionThread : mSessionThreads) {
				if (sessionThread != null) {
					sessionThread.closeDataSocket();
					sessionThread.closeSocket();
				}
			}
		}
	}

	public void cleanupAndStopService() {
		// Call the Android Service shutdown function
		stopSelf();
		releaseWifiLock();
		releaseWakeLock();
//		((FtpServerApp) FtpServerApp.getInstance()).clearNotification(this);
		sendBroadcast(new Intent(ACTION_STOPPED));
	}

	private void takeWakeLock() {
		if (mWakeLock == null) {
			Log.d(TAG, "About to take wake lock");
			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			// Many (all?) devices seem to not properly honor a
			// PARTIAL_WAKE_LOCK,
			// which should prevent CPU throttling. This has been
			// well-complained-about on android-developers.
			// For these devices, we have a config option to force the phone
			// into a
			// full wake lock.
			if (isFullWake) {
				Log.d(TAG, "Need to take full wake lock");
				mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK,
						WAKE_LOCK_TAG);
			} else {
				mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
						WAKE_LOCK_TAG);
			}
			mWakeLock.setReferenceCounted(false);
		}
		Log.d(TAG, "Acquiring wake lock");
		mWakeLock.acquire();
	}

	private void releaseWakeLock() {
		Log.d(TAG, "Releasing wake lock");
		if (mWakeLock != null) {
			mWakeLock.release();
			mWakeLock = null;
			Log.d(TAG, "Finished releasing wake lock");
		} else {
			Log.e(TAG, "Couldn't release null wake lock");
		}
	}

	private void takeWifiLock() {
		Log.d(TAG, "Taking wifi lock");
		if (mWifiLock == null) {
			WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			mWifiLock = manager.createWifiLock("wayFTP");
			mWifiLock.setReferenceCounted(false);
		}
		mWifiLock.acquire();
	}

	private void releaseWifiLock() {
		Log.d(TAG, "Releasing wifi lock");
		if (mWifiLock != null) {
			mWifiLock.release();
			mWifiLock = null;
		}
	}

	public void errorShutdown() {
		Log.e(TAG, "Service errorShutdown() called");
		cleanupAndStopService();
	}

	/**
	 * Gets the local ip address
	 * 
	 * @return local ip adress or null if not found
	 */
//	public static InetAddress getLocalInetAddress() {
//		if (!isConnectedToLocalNetwork()) {
//			Log.e(TAG, "getLocalInetAddress called and no connection");
//			return null;
//		}
//		// @TODO: next if block could probably be removed
//		if (isConnectedUsingWifi()) {
//			Context context = BaseA.a;
//			WifiManager wm = (WifiManager) context
//					.getSystemService(Context.WIFI_SERVICE);
//			int ipAddress = wm.getConnectionInfo().getIpAddress();
//			if (ipAddress == 0)
//				return null;
//			return Util.intToInet(ipAddress);
//		}
//		// This next part should be able to get the local ip address, but in
//		// some case
//		// I'm receiving the routable address
//		try {
//			Enumeration<NetworkInterface> netinterfaces = NetworkInterface
//					.getNetworkInterfaces();
//			while (netinterfaces.hasMoreElements()) {
//				NetworkInterface netinterface = netinterfaces.nextElement();
//				Enumeration<InetAddress> adresses = netinterface
//						.getInetAddresses();
//				while (adresses.hasMoreElements()) {
//					InetAddress address = adresses.nextElement();
//					// this is the condition that sometimes gives problems
//					if (!address.isLoopbackAddress()
//							&& !address.isLinkLocalAddress())
//						return address;
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

	/**
	 * Checks to see if we are connected to a local network, for instance wifi
	 * or ethernet
	 *
	 * @return true if connected to a local network
	 */
//	public static boolean isConnectedToLocalNetwork() {
//		Context context = BaseA.a;
//		ConnectivityManager cm = (ConnectivityManager) context
//				.getSystemService(Context.CONNECTIVITY_SERVICE);
//		NetworkInfo ni = cm.getActiveNetworkInfo();
//		// @TODO: this is only defined starting in api level 13
//		final int TYPE_ETHERNET = 0x00000009;
//		return ni != null
//				&& ni.isConnected() == true
//				&& (ni.getType() & (ConnectivityManager.TYPE_WIFI | TYPE_ETHERNET)) != 0;
//	}

	/**
	 * Checks to see if we are connected using wifi
	 *
	 * @return true if connected using wifi
	 */
//	public static boolean isConnectedUsingWifi() {
//		Context context = BaseA.a;
//		ConnectivityManager cm = (ConnectivityManager) context
//				.getSystemService(Context.CONNECTIVITY_SERVICE);
//		NetworkInfo ni = cm.getActiveNetworkInfo();
//		return ni != null && ni.isConnected() == true
//				&& ni.getType() == ConnectivityManager.TYPE_WIFI;
//	}

	public static void writeMonitor(boolean incoming, String s) {
	}

	// public static void writeMonitor(boolean incoming, String s) {
	// if(incoming) {
	// s = "> " + s;
	// } else {
	// s = "< " + s;
	// }
	// sessionMonitor.add(s.trim());
	// int maxSize = Defaults.getSessionMonitorScrollBack();
	// while(sessionMonitor.size() > maxSize) {
	// sessionMonitor.remove(0);
	// }
	// updateClients();
	// }

	public static int getPort() {
		return mPort;
	}

	public static void setPort(int port) {
		FtpServerService.mPort = port;
	}

	/**
	 * The FTPServerService must know about all running session threads so they
	 * can be terminated on exit. Called when a new session is created.
	 */
	public void registerSessionThread(SessionThread newSession) {
		// Before adding the new session thread, clean up any finished session
		// threads that are present in the list.

		// Since we're not allowed to modify the list while iterating over
		// it, we construct a list in toBeRemoved of threads to remove
		// later from the sessionThreads list.
		synchronized (this) {
			List<SessionThread> toBeRemoved = new ArrayList<SessionThread>();
			for (SessionThread sessionThread : mSessionThreads) {
				if (!sessionThread.isAlive()) {
					Log.d(TAG, "Cleaning up finished session...");
					try {
						sessionThread.join();
						Log.d(TAG, "Thread joined");
						toBeRemoved.add(sessionThread);
						sessionThread.closeSocket(); // make sure socket closed
					} catch (InterruptedException e) {
						Log.d(TAG, "Interrupted while joining");
						// We will try again in the next loop iteration
					}
				}
			}
			for (SessionThread removeThread : toBeRemoved) {
				mSessionThreads.remove(removeThread);
			}

			// Cleanup is complete. Now actually add the new thread to the list.
			mSessionThreads.add(newSession);
		}
		Log.d(TAG, "Registered session thread");
	}

}
