package async.notifier;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import webservices.APIManager;
import business.Notifier;
import business.User;
import android.os.AsyncTask;
import async.AsyncCallbackResult;

public class UserNotifiersAsyncCallback extends AsyncTask<String, Void, List<Notifier>> {
	
	@Override
	protected List<Notifier> doInBackground(String... params) {
		try {
			return APIManager.userNotifiers(User.auth_token, 2);
		} catch (JSONException e) {
			return new ArrayList<Notifier>();
		}
	
	}

}
