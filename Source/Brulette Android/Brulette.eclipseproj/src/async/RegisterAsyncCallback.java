package async;

import org.json.JSONException;

import com.android.brewlette.R;
import com.urbanairship.push.PushManager;

import views.HomeView;
import webservices.APIManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

public class RegisterAsyncCallback  extends AsyncTask<String, Void, Integer> {
	private Context context;
	private ProgressDialog progressDialog;

	public RegisterAsyncCallback(Context context) {
		this.context = context;
	}
	
	@Override
	protected void onPreExecute() {
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage("Loading...");
		progressDialog.show();
	}

	@Override
	protected Integer doInBackground(String... params) {
		try {
			APIManager.register(params[0], PushManager.shared().getAPID()+"@fpstudios.com", PushManager.shared().getAPID(), PushManager.shared().getAPID(), Long.valueOf(12), Long.valueOf(12));
		} catch (JSONException e) {
			return AsyncCallbackResult.EXCEPTION;
		}
		return AsyncCallbackResult.TRUE;
	}

	protected void onPostExecute(Integer result) {
		
		if (progressDialog.isShowing())
			progressDialog.dismiss();

		if (result == AsyncCallbackResult.TRUE) {
			Intent intent = new Intent(context, HomeView.class);
			context.startActivity(intent);


		} else if (result == AsyncCallbackResult.FALSE) {

			Toast.makeText(context,  context.getResources().getString(R.string.login_error_message), Toast.LENGTH_LONG).show();

		} else if (result == AsyncCallbackResult.EXCEPTION) {

			Toast.makeText(context, context.getResources().getString(R.string.no_network_error), Toast.LENGTH_LONG).show();

		}

	}



}
