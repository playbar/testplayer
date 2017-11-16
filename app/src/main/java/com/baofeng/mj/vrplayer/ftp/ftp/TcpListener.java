/*
Copyright 2009 David Revell

This file is part of SwiFTP.

SwiFTP is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

SwiFTP is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with SwiFTP.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.baofeng.mj.vrplayer.ftp.ftp;

import android.content.Context;
import android.util.Log;

import java.net.ServerSocket;
import java.net.Socket;

public class TcpListener extends Thread {
    private static final String TAG = TcpListener.class.getSimpleName();

    ServerSocket listenSocket;
    FtpServerService ftpServerService;
    private Context mContext;

    public TcpListener(Context mContext,ServerSocket listenSocket, FtpServerService ftpServerService) {
        this.listenSocket = listenSocket;
        this.ftpServerService = ftpServerService;
        this.mContext = mContext;
    }

    public void quit() {
        try {
            listenSocket.close(); // if the TcpListener thread is blocked on accept,
                                  // closing the socket will raise an exception
        } catch (Exception e) {
            Log.d(TAG, "Exception closing TcpListener listenSocket");
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket clientSocket = listenSocket.accept();
                Log.i(TAG, "New connection, spawned thread");
                SessionThread newSession = new SessionThread(mContext,clientSocket,
                        new NormalDataSocketFactory(mContext), SessionThread.Source.LOCAL);
                newSession.start();
                ftpServerService.registerSessionThread(newSession);
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception in TcpListener");
        }
    }
}
