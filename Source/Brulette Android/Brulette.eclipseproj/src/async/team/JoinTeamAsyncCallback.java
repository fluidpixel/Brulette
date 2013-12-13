package async.team;

import org.json.JSONException;

import webservices.APIManager;
import android.os.AsyncTask;
import business.Membership;
import business.User;

public class JoinTeamAsyncCallback extends AsyncTask<String, Void, Membership> {
	@Override
	protected Membership doInBackground(String... params) {
		

		try {
			
			return APIManager.joinTeam(User.auth_token, Integer.valueOf(params[0]), params[1]);
				
		} catch (JSONException e) {
			return null;
		}
	}

}
