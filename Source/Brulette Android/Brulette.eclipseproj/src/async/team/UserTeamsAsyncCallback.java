package async.team;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import webservices.APIManager;
import business.Team;
import business.User;
import android.os.AsyncTask;

public class UserTeamsAsyncCallback extends AsyncTask<String, Void, List<Team> > {
	
	@Override
	protected List<Team> doInBackground(String... params) {
		try{
			return APIManager.userTeams(User.auth_token, 50, 1);
		} catch (JSONException e) {
			return new ArrayList<Team>();
		}
	}

}
