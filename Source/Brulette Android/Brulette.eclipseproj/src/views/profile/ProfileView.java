package views.profile;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;

import views.HomeView;
import webservices.APIManager;
import business.Notifier;
import business.User;
import com.android.brewlette.R;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import async.AsyncCallbackResult;

public class ProfileView extends Activity {
	private static List<Notifier> notifiers = new ArrayList<Notifier>();
	private ProfileAdapter adapter;
	private ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_view);
		listView = (ListView)findViewById(R.id.ProfileView_NotifierListView);
		if(notifiers.size() == 0)
			refreshNotifiers();
		else{
			adapter = new ProfileAdapter(ProfileView.this, notifiers);
			listView.setAdapter(adapter);
		}
		
		ActionBar bar = getActionBar();
		bar.setDisplayHomeAsUpEnabled(true);

	}

	private void refreshNotifiers(){
		new UserNotifiersAsyncCallback().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile_view, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		
		case R.id.refreshProfile:
			refreshNotifiers();
			break;
		case android.R.id.home:            
	         Intent intent = new Intent(this, HomeView.class);            
	         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
	         startActivity(intent);            
	         break;  
		}
		return true;
	}

	private class UserNotifiersAsyncCallback extends AsyncTask<String, Void, Integer> {
		private List<Notifier> currentNotifiers;
		private ProgressDialog progressDialog;

		private UserNotifiersAsyncCallback(){
			super();
			progressDialog = new ProgressDialog(ProfileView.this);
		}

		@Override
		protected void onPreExecute() {
			progressDialog.setMessage("Loading...");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
				currentNotifiers = new ArrayList<Notifier>();
				currentNotifiers.addAll(APIManager.userNotifiers(User.auth_token, 2));
				return AsyncCallbackResult.TRUE;
			} catch (JSONException e) {
				return AsyncCallbackResult.EXCEPTION;
			}

		}

		@Override
		protected void onPostExecute(Integer result) {
			if (progressDialog.isShowing())
				progressDialog.dismiss();

			if (result == AsyncCallbackResult.TRUE) {
				if(currentNotifiers.size() != notifiers.size()){

					notifiers = new ArrayList<Notifier>(currentNotifiers);

					adapter = new ProfileAdapter(ProfileView.this, notifiers);
					listView.setAdapter(adapter);

				}
			}else {
				Toast.makeText(getApplicationContext(),  "Brewlette has encountered a problem" , Toast.LENGTH_LONG).show();
			}
		}

	}

}
