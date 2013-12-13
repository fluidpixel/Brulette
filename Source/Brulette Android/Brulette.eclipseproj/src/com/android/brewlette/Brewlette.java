package com.android.brewlette;

import com.urbanairship.AirshipConfigOptions;
import com.urbanairship.UAirship;
import com.urbanairship.push.PushManager;

import android.app.Application;

public class Brewlette extends Application {
	
	 @Override
	   public void onCreate(){
		 	// Configure your application
			//
			// This can be done in code as illustrated here,
			// or you can add these settings to a properties file
			// called airshipconfig.properties
			// and place it in your "assets" folder
			AirshipConfigOptions options = AirshipConfigOptions.loadDefaultOptions(this);
			options.developmentAppKey = "Z3b1QjTVTVemPTUM5HVOyg";
			options.developmentAppSecret = "awKyoz89Q86MusU7dZ0x0w";
			options.productionAppKey = "Z3b1QjTVTVemPTUM5HVOyg";
			options.productionAppSecret = "awKyoz89Q86MusU7dZ0x0w";
			options.gcmSender = "1044228667332";
			options.inProduction = false; //determines which app key to use
			UAirship.takeOff(this, options);
			PushManager.enablePush();
			PushManager.shared().setIntentReceiver(NotificationReceiver.class);
	   }

}
