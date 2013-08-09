package async.round;

import org.json.JSONException;

import webservices.APIManager;
import android.os.AsyncTask;
import async.AsyncCallbackResult;
import business.User;

public class RateRoundAsyncCallback extends AsyncTask<String, Void, Integer>{
	@Override
	protected Integer doInBackground(String... params) {
		

		try {
			APIManager.rateRound(User.auth_token, 37, 5, "Very well");
			return AsyncCallbackResult.TRUE;
				
		} catch (JSONException e) {
			return AsyncCallbackResult.EXCEPTION;
		}
	}


	protected void onPostExecute(Integer result) {

		if (result == AsyncCallbackResult.TRUE) {
			//Change view


		} else if (result == AsyncCallbackResult.FALSE) {



		} else if (result == AsyncCallbackResult.EXCEPTION) {


		}
	}
}
