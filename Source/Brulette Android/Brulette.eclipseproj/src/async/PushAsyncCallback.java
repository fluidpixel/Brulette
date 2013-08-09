package async;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.urbanairship.push.PushManager;



import urbanairship.Android;
import urbanairship.Push;
import urbanairship.UrbanAirshipClient;

import android.os.AsyncTask;
import android.util.Log;

public class PushAsyncCallback  extends AsyncTask<String, Void, Integer> {

	@Override
	protected Integer doInBackground(String... params) {
		

		Push push = new Push();
		push.setAliases(Arrays.asList("39901"));
		List<String> apids = new ArrayList<String>();
		apids.add("64016e2c-df2c-44e8-b6e7-73e23e67ba1e");
		push.setApids(apids);
		// For Android
		Android android = new Android();
		android.setAlert("Push By Rest");
		push.setAndroid(android);

		// For iOS
		// APS aps = new APS();
		// aps.setBadge(1);
		// aps.setAlert("hi there");
		// aps.setSound("default");
		// aps.setData("additinoal data");
		// push.setAps(aps);

		UrbanAirshipClient uac = new UrbanAirshipClient("Z7BLLaPeTCaW3m4KIDT1OQ",
				"8NSrSufBQNWhEz_-TYAAbg");
		
		uac.sendPushNotifications(push);
		//Broadcast
		//uac.sendPushBroadcastNotifications(push);
		Log.i("REST PUSH", PushManager.shared().getAPID());



		return AsyncCallbackResult.TRUE;
	}

}
