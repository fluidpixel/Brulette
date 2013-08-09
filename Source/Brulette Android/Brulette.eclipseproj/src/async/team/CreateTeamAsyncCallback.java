package async.team;

import org.json.JSONException;

import webservices.APIManager;

import business.Membership;
import business.User;

import com.android.brewlette.R;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import async.AsyncCallbackResult;

public class CreateTeamAsyncCallback extends AsyncTask<String, Void, Membership> {
	private Context context;

	public CreateTeamAsyncCallback(Context context) {
		this.context = context;
	}

	protected Membership doInBackground(String... params) {

		try {
			return APIManager.createTeam(User.auth_token, params[0], params[1], Long.valueOf(13), Long.valueOf(12));
				
		} catch (JSONException e) {
			Toast.makeText(context, "Brewlette has encountered a problem" , Toast.LENGTH_LONG).show();
			return null;
		}
	}

	protected void onProgressUpdate(Void... progress) {

	}
}
