package async.round;

import org.json.JSONException;

import webservices.APIManager;
import android.os.AsyncTask;
import async.AsyncCallbackResult;
import business.User;

public class JoinRoundAsyncCallback extends AsyncTask<String, Void, Integer> {
	@Override
	protected Integer doInBackground(String... params) {
		

		try {
			APIManager.joinRound(User.auth_token, Integer.valueOf(params[0]), Integer.valueOf(params[1]));
			return AsyncCallbackResult.TRUE;
				
		} catch (JSONException e) {
			return AsyncCallbackResult.EXCEPTION;
		}
	}
}
