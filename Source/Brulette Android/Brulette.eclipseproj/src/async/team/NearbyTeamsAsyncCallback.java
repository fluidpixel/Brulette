package async.team;

import org.json.JSONException;


import business.User;


import webservices.APIManager;

import android.os.AsyncTask;
import async.AsyncCallbackResult;


public class NearbyTeamsAsyncCallback extends AsyncTask<String, Void, Integer> {

	@Override
	protected Integer doInBackground(String... params) {
		try{
			APIManager.nearbyTeams(User.auth_token, Long.valueOf(-1), "");
			return AsyncCallbackResult.TRUE;
		} catch (JSONException e) {
			return AsyncCallbackResult.EXCEPTION;
		}
	}
	
	protected void onPostExecute(Integer result) {

		if (result == AsyncCallbackResult.TRUE) {



		} else if (result == AsyncCallbackResult.FALSE) {

			

		} else if (result == AsyncCallbackResult.EXCEPTION) {

			

		}

	}

}
