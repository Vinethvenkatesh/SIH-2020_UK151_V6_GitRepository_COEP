package com.example.atsproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyReceiver extends BroadcastReceiver {
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SmsBroadcastReceiver";
    String msg,phone ="";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    public void onReceive(Context context, Intent intent) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("location").child("sms");

        Log.i(TAG, "Intent Received : " + intent.getAction() );
        if (intent.getAction() == SMS_RECEIVED){

            Bundle dataBundle = intent.getExtras();
            if(dataBundle != null){
                Object[] mypdu = (Object[])dataBundle.get("pdus");
                final SmsMessage[] message = new SmsMessage[mypdu.length];
                for (int i=0; i<mypdu.length; i++){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        String format = dataBundle.getString("format");
                        message[i] = SmsMessage.createFromPdu((byte[]) mypdu[i],format);
                    }
                    else{
                        message[i] = SmsMessage.createFromPdu((byte[]) mypdu[i]);
                    }

                    msg = message[i].getMessageBody();
                    phone = message[i].getOriginatingAddress();
                }
                if (phone.equals("+918825579177")){

                    databaseReference.setValue(msg);

                }
            }
        }
    }
}
