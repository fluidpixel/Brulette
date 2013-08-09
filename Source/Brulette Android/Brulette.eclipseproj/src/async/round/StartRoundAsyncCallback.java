package async.round;

import org.json.JSONException;

import webservices.APIManager;
import android.os.AsyncTask;
import async.AsyncCallbackResult;
import business.User;

public class StartRoundAsyncCallback extends AsyncTask<Integer, Void, Integer> {
	@Override
	protected Integer doInBackground(Integer... params) {
		try{
			APIManager.startRound(User.auth_token, params[0], true, params[1]*60);
			return AsyncCallbackResult.TRUE;
		} catch (JSONException e) {
			return AsyncCallbackResult.EXCEPTION;
		}
	}
	
}
