package async.team;

import org.json.JSONException;

import webservices.APIManager;
import android.os.AsyncTask;
import async.AsyncCallbackResult;
import business.User;

public class DeleteTeamAsyncCallback extends AsyncTask<String, Void, Integer> {
	@Override
	protected Integer doInBackground(String... params) {
		try {
			APIManager.deleteTeam(User.auth_token, 2);
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
