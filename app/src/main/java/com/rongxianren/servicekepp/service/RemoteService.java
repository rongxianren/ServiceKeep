package com.rongxianren.servicekepp.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by wty on 2016/11/10.
 */

public class RemoteService extends Service {

    MyBinder.Stub myBinder = null;
    MyConnection myConnection = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
        Toast.makeText(this, "本地服务解绑", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myBinder = new MyBinderImpl();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (null == myConnection) {
            myConnection = new MyConnection();
        }
        RemoteService.this.bindService(new Intent(RemoteService.this, LocalService.class), myConnection, Service.BIND_IMPORTANT);

        new Thread(new MyTask()).start();
        
        return super.onStartCommand(intent, flags, startId);
    }


    class MyBinderImpl extends MyBinder.Stub {
        @Override
        public String bindInfo() throws RemoteException {
            return RemoteService.class.getName();
        }
    }

    class MyConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            MyBinder.Stub binder = (MyBinder.Stub) iBinder;
            System.out.println("-----本地服务绑定成功----");
//            try {
//                System.out.println("*****远程服务绑定的服务名字是*****" + binder.bindInfo());
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            System.out.println("********本地服务被杀死了********");
            RemoteService.this.unbindService(myConnection);

            RemoteService.this.startService(new Intent(RemoteService.this, LocalService.class));
            RemoteService.this.bindService(new Intent(RemoteService.this, LocalService.class), myConnection, Service.BIND_IMPORTANT);
        }
    }

    class MyTask implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                    System.out.println("+++++++RemoteService+++++++");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

