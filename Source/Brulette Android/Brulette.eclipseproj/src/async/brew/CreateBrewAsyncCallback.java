package async.brew;

import java.util.List;

import org.json.JSONException;

import webservices.APIManager;
import business.BrewTags;
import business.User;
import business.Brew;
import android.os.AsyncTask;

public class CreateBrewAsyncCallback extends AsyncTask<List<Brew> , Void, BrewTags> {
	
	@Override
	protected BrewTags doInBackground(List<Brew>... params) {
		try {
			return APIManager.createBrew(User.auth_token, params[0]);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

}
