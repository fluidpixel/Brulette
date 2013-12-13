package async.team;

import org.json.JSONException;

import webservices.APIManager;
import android.os.AsyncTask;
import async.AsyncCallbackResult;
import business.User;

public class DeleteMembershipAsyncCallback extends AsyncTask<Integer, Void, Integer> {
	@Override
	protected Integer doInBackground(Integer... params)  {
		

		try {
			APIManager.deleteMemberShip(User.auth_token, params[0]);
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
