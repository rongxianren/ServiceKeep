package com.rongxianren.servicekepp.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by wty on 2016/11/10.
 */

public class LocalService extends Service {


    MyConnection myConnection = null;
    MyBinder.Stub myBinder = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
        Toast.makeText(this, "远程服务解绑", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myBinder = new BinderImpl();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null == myConnection) {
            myConnection = new MyConnection();
        }
        LocalService.this.bindService(new Intent(LocalService.this, RemoteService.class), myConnection, Service.BIND_IMPORTANT);

        new Thread(new MyTask()).start();
        return super.onStartCommand(intent, flags, startId);
    }


    class BinderImpl extends MyBinder.Stub {
        @Override
        public String bindInfo() throws RemoteException {
            return LocalService.this.getClass().getName();
        }
    }


    class MyConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            System.out.println("----远程服务绑定成功-----");
//            MyBinder.Stub binder = (MyBinder.Stub) iBinder;
//            try {
//                System.out.println("---------被绑定的服务名字----------" + binder.bindInfo());
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            System.out.println("********远程服务被杀死了********");

            LocalService.this.unbindService(myConnection);

            LocalService.this.startService(new Intent(LocalService.this, RemoteService.class));
            LocalService.this.bindService(new Intent(LocalService.this, RemoteService.class), myConnection, Service.BIND_IMPORTANT);
        }
    }

    class MyTask implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                    System.out.println("+++++++LocalService+++++++");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
