package async;



import org.json.JSONException;

import com.android.brewlette.R;

import webservices.APIManager;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class LogoutAsyncCallback extends AsyncTask<String, Void, Integer> {
	private Context context;
	
	public LogoutAsyncCallback(Context context) {
		this.context = context;
	}

	@Override
	protected Integer doInBackground(String... params) {
		try {
			APIManager.logout(params[0]);
		} catch (JSONException e) {
			return AsyncCallbackResult.EXCEPTION;
		}
			return AsyncCallbackResult.TRUE;
	}
	
	protected void onPostExecute(Integer result) {

		if (result == AsyncCallbackResult.TRUE) {
			


		} else if (result == AsyncCallbackResult.FALSE) {

			Toast.makeText(context,  context.getResources().getString(R.string.login_error_message), Toast.LENGTH_LONG).show();

		} else if (result == AsyncCallbackResult.EXCEPTION) {

			Toast.makeText(context, context.getResources().getString(R.string.no_network_error), Toast.LENGTH_LONG).show();

		}

	}

}
