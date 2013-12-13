package async;

import org.json.JSONException;

import com.android.brewlette.R;

import views.HomeView;
import webservices.APIManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;



public class ConnectUserAsyncCallback extends AsyncTask<String, Void, Integer> {
	private Context context;




	public ConnectUserAsyncCallback(Context context) {
		this.context = context;
	}


	protected Integer doInBackground(String... params) {

		try {
			if(APIManager.login(params[0], params[1]))
				return AsyncCallbackResult.TRUE;
			else
				return AsyncCallbackResult.FALSE;
		} catch (JSONException e) {
			return AsyncCallbackResult.EXCEPTION;
		}
	}

	protected void onProgressUpdate(Void... progress) {

	}

	/**
	 * Checking for all possible internet providers
	 * **/
	public boolean isConnectingToInternet(){
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null)
		{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED)
					{
						return true;
					}

		}
		return false;
	}

	protected void onPostExecute(Integer result) {


		if (result == AsyncCallbackResult.TRUE) {
			Intent intent = new Intent(context, HomeView.class);
			context.startActivity(intent);


		} else if (result == AsyncCallbackResult.FALSE) {

			if(isConnectingToInternet())
				Toast.makeText(context,  context.getResources().getString(R.string.login_error_message), Toast.LENGTH_LONG).show();
			else
				Toast.makeText(context,  context.getResources().getString(R.string.no_network_error), Toast.LENGTH_LONG).show();


		} else if (result == AsyncCallbackResult.EXCEPTION) {

			Toast.makeText(context, context.getResources().getString(R.string.no_network_error), Toast.LENGTH_LONG).show();

		}

	}
}
