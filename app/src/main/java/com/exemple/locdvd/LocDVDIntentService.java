package com.exemple.locdvd;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;



public class LocDVDIntentService extends IntentService{


    public LocDVDIntentService()  {
        super("LocDVDIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("Service", "Le service va commencer le traitement");

      //  Messenger messager = null;
        int waitDuration = intent.getIntExtra("waitDuration", 1000);

      //  messager = (Messenger)intent.getParcelableExtra("messager");

        try {
            Log.d("Service", String.format("Le service va attendre %d millisecondes" ,waitDuration));
            Thread.sleep(waitDuration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("Service", "Le service a terminé le traitement");
      /*  if(messager!=null) {
            Message message = Message.obtain();
            Bundle replyData = new Bundle();
            replyData.putString("reply","Le service est terminé");
            message.setData(replyData);
            try {
                messager.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        */
        Intent resultIntent = new Intent("LocDVD.ServiceEnded");
        resultIntent.putExtra("replyMessage","Le service a envoyé un broadcast");
        sendBroadcast(resultIntent);

    }
}
