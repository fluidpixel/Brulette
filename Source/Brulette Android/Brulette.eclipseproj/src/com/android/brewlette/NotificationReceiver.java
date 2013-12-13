package com.android.brewlette;

import views.HomeView;

import com.urbanairship.UAirship;
import com.urbanairship.push.PushManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent intent) {
		Log.i("PUSH", "Received intent: " + intent.toString());
		String action = intent.getAction();

		if (action.equals(PushManager.ACTION_PUSH_RECEIVED)) {

		} else if (action.equals(PushManager.ACTION_NOTIFICATION_OPENED)) {
			Log.i("PUSH", "OPEN");
			Log.i("PUSH MESSAGE", intent.getStringExtra(PushManager.EXTRA_ALERT));
			//Open the round view
			 Intent launch = new Intent(Intent.ACTION_MAIN);
			    launch.setClass(UAirship.shared().getApplicationContext(),
			            HomeView.class);

			    launch.putExtra("push_message",
			            intent.getStringExtra(PushManager.EXTRA_ALERT));
			    launch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			    UAirship.shared().getApplicationContext().startActivity(launch);
		}

	}
}
