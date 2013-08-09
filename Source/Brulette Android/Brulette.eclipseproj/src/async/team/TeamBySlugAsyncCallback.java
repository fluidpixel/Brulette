package async.team;

import org.json.JSONException;

import business.Team;

import webservices.APIManager;
import android.os.AsyncTask;

public class TeamBySlugAsyncCallback extends AsyncTask<String, Void, Team> {
	@Override
	protected Team doInBackground(String... params) {
		try{
			return APIManager.teamBySlug(params[0]);
		} catch (JSONException e) {
			return null;
		}
	}
}
