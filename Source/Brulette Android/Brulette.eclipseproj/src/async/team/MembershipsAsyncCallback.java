package async.team;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import webservices.APIManager;
import android.os.AsyncTask;
import business.Membership;
import business.User;

public class MembershipsAsyncCallback extends AsyncTask<String, Void, List<Membership> >{
	@Override
	protected List<Membership> doInBackground(String... params) {
		try {
			return APIManager.memberships(User.auth_token);

		} catch (JSONException e) {
			return new ArrayList<Membership>();
		}
	}
}
