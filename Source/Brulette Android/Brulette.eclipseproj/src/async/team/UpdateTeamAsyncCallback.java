package async.team;

import org.json.JSONException;

import webservices.APIManager;
import android.os.AsyncTask;
import async.AsyncCallbackResult;
import business.User;



public class UpdateTeamAsyncCallback extends AsyncTask<String, Void, Integer> {
	
	@Override
	protected Integer doInBackground(String... params) {
		

		try {
			APIManager.updateTeam(User.auth_token, 2, "fpstudios", "", Long.valueOf(-1), Long.valueOf(-1));
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
