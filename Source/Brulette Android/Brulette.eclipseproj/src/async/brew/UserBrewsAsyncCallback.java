package async.brew;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import webservices.APIManager;
import android.os.AsyncTask;
import business.BrewTags;
import business.User;

public class UserBrewsAsyncCallback extends AsyncTask<String, Void, List<BrewTags> >{
	@Override
	protected List<BrewTags> doInBackground(String... params)  {
		try {
			return APIManager.userBrews(User.auth_token, 10, 1);
		} catch (JSONException e) {
			
			return new ArrayList<BrewTags>();
		}
		
	}
}
