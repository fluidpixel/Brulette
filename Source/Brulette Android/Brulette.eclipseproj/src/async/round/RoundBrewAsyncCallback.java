package async.round;

import org.json.JSONException;

import webservices.APIManager;
import android.os.AsyncTask;
import async.AsyncCallbackResult;
import business.User;

public class RoundBrewAsyncCallback extends AsyncTask<String, Void, Integer>{
	@Override
	protected Integer doInBackground(String... params) {
		try {
			APIManager.roundBrews(User.auth_token, 37, -1, -1);
		} catch (JSONException e) {
			return AsyncCallbackResult.EXCEPTION;
		}
		return AsyncCallbackResult.TRUE;
	}
}
