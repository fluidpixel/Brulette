package async.round;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import webservices.APIManager;
import android.os.AsyncTask;
import async.AsyncCallbackResult;
import business.Round;
import business.RoundStatus;
import business.User;

public class RoundsAsyncCallback extends AsyncTask<String, Void, List<Round> > {
	@Override
	protected List<Round> doInBackground(String... params)  {
		try{
			return APIManager.rounds(User.auth_token, RoundStatus.valueOf(params[0]), 100, -1);
		} catch (JSONException e) {
			return new ArrayList<Round>();
		}
	}
	
}
