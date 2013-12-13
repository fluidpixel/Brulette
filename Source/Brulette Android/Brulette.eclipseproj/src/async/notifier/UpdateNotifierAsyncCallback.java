package async.notifier;

import org.json.JSONException;

import webservices.APIManager;
import android.os.AsyncTask;
import async.AsyncCallbackResult;
import business.User;

public class UpdateNotifierAsyncCallback extends AsyncTask<String, Void, Integer> {
	@Override
	protected Integer doInBackground(String... params) {
		try {
			APIManager.updateNotifier(User.auth_token, Integer.valueOf(params[0]), params[1], params[2], Boolean.valueOf(params[3]));
		} catch (JSONException e) {
			return AsyncCallbackResult.EXCEPTION;
		}
		return AsyncCallbackResult.TRUE;
	}
}
