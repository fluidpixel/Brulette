package async.brew;

import org.json.JSONException;

import webservices.APIManager;
import business.User;
import android.os.AsyncTask;
import async.AsyncCallbackResult;

public class BrewTagsAsyncCallback extends AsyncTask<String, Void, Integer> {
	@Override
	protected Integer doInBackground(String... params) {
		try {
			APIManager.brewTags(User.auth_token, null, 40, -1);
		} catch (JSONException e) {
			
			return AsyncCallbackResult.EXCEPTION;
		}
		return AsyncCallbackResult.TRUE;
	}
	
	protected void onPostExecute(Integer result) {

		if (result == AsyncCallbackResult.TRUE) {



		} else if (result == AsyncCallbackResult.FALSE) {

			

		} else if (result == AsyncCallbackResult.EXCEPTION) {

			

		}

	}
}
