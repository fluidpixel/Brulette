package views;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import org.json.JSONException;
import views.brew.BrewsView;
import views.profile.ProfileView;
import views.round.RoundAdapter;
import views.round.RoundView;
import views.round.StartRoundView;
import views.team.TeamsView;
import webservices.APIManager;
import business.Round;
import business.RoundStatus;
import business.User;
import com.android.brewlette.R;
import com.urbanairship.push.PushManager;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import async.AsyncCallbackResult;
import async.ConnectUserAsyncCallback;
import async.LogoutAsyncCallback;


public class HomeView extends Activity {

	private static List<Round> rounds = new ArrayList<Round>();

	private Dialog dialog;
	private Button loginButton;
	private ListView listView;
	private RoundAdapter adapter;
	private Comparator<Round> myComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_view);
		Bundle extras = getIntent().getExtras();

		if (extras != null)
			Toast.makeText(getApplicationContext(),  extras.getString("push_message"), Toast.LENGTH_LONG).show();


		listView = (ListView)findViewById(R.id.HomeView_RoundListView);
		listView.setOnItemClickListener(new OnRoundClickListener());
		myComparator = new Comparator<Round>() {
			public int compare(Round obj1,Round obj2) {
				return obj1.getCreated().compareTo(obj2.getCreated());
			}
		};

		ActionBar bar = getActionBar();
		bar.setHomeButtonEnabled(true);
	}

	@Override
	public void onResume(){
		super.onResume();
		if(User.id == -1){
			rounds.clear();
			AsyncTask<String, Void, Integer> asyncTask = new ConnectUserAsyncCallback(HomeView.this).execute(PushManager.shared().getAPID()+"@fpstudios.com", PushManager.shared().getAPID());
			Integer result = AsyncCallbackResult.EXCEPTION;
	
			try {
				result = asyncTask.get();
			} catch (Exception e) {}
	
			if (result != AsyncCallbackResult.TRUE) {
				//User is not register
				Intent intent = new Intent(HomeView.this, RegisterView.class);
				startActivity(intent);
				finish();
			}
		}
		else {
			new RoundsAsyncCallback().execute();
			//Refresh adapter
			Collections.sort(rounds,Collections.reverseOrder(myComparator));
			adapter = new RoundAdapter(HomeView.this, new ArrayList<Round>(rounds));
			listView.setAdapter(adapter);
		}
	}


	private void showLoginDialog(){
		dialog = new Dialog(HomeView.this);
		dialog.setContentView(R.layout.popup_login_view);
		dialog.setTitle("Login");
		loginButton = (Button) dialog.findViewById(R.id.PopupLoginView_LoginButton);
		loginButton.setOnClickListener(new LoginOnClickListener());
		dialog.setCanceledOnTouchOutside(false);

		//Lock the backPressed
		dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey (DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK && 
						event.getAction() == KeyEvent.ACTION_UP && 
						!event.isCanceled()) {
					return true;
				}
				return false;

			}
		});
		dialog.show();
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);            
		builder.setTitle("Quit")
		.setMessage(this.getResources().getString(R.string.quit_message))
		.setPositiveButton("yes", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				User.id = -1;

				//Clear all the user data
				//
				HomeView.clearRounds();
				BrewsView.clearBrews();
				TeamsView.clearTeams();
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				new LogoutAsyncCallback(HomeView.this).execute(User.auth_token);

				finish();
				moveTaskToBack(true);
			}

		})
		.setNegativeButton("no",  new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}

		})
		.create()
		.show();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_view, menu);
		return true;
	}

	//Home Menu
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case android.R.id.home:
			clearRounds();
			new RoundsAsyncCallback().execute();
			return true;       

		case R.id.team:
			intent = new Intent(HomeView.this,TeamsView.class);
			startActivity(intent);
			return true;
		case R.id.brew:
			intent = new Intent(HomeView.this,BrewsView.class);
			startActivity(intent);
			return true;
		case R.id.profile:
			intent = new Intent(HomeView.this,ProfileView.class);
			startActivity(intent);
			return true;
		case R.id.round:
			intent = new Intent(HomeView.this,StartRoundView.class);
			startActivity(intent);
			return true;

		}

		return false;
	}


	private class LoginOnClickListener implements android.view.View.OnClickListener {
		private TextView loginTextView;
		private TextView passwordTextView;
		@Override
		public void onClick(View v) {
			loginTextView = (TextView) dialog.findViewById(R.id.PopupLoginView_LoginTextField);
			passwordTextView = (TextView) dialog.findViewById(R.id.PopupLoginView_PasswordTextField);
			String stringLogin = loginTextView.getText().toString();
			String stringPassword = passwordTextView.getText().toString();

			if(stringLogin.equals("") ||  stringPassword.equals("")){

				Toast.makeText(getApplicationContext(),  getResources().getString(R.string.blank_fields_message), Toast.LENGTH_LONG).show();

			} else {

				/* Empêche de multiples créations de threads */
				loginButton.setEnabled(false);
				AsyncTask<String, Void, Integer> asyncTask = new LoginAsyncCallback().execute(stringLogin, stringPassword);
				Integer result = AsyncCallbackResult.EXCEPTION;

				try {
					result = asyncTask.get();
				} catch (Exception e) {}

				if (result == AsyncCallbackResult.TRUE) {
					new RoundsAsyncCallback().execute();
				} else {
					loginButton.setEnabled(true);
				}
			}

		}

	}

	private class OnRoundClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			SimpleDateFormat formatDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss", Locale.ENGLISH);
			Intent intent = new Intent(HomeView.this, RoundView.class);
			intent.putExtra("team_id", adapter.getTeamId(position));	
			intent.putExtra("round_id", id);
			intent.putExtra("created", formatDate.format(adapter.getCreated(position)));
			intent.putExtra("closes", formatDate.format(adapter.getCloses(position)));
			intent.putExtra("team_name", adapter.getTeamName(adapter.getTeamId(position)));
			intent.putExtra("status", adapter.getStatus(position));

			startActivity(intent);


		}
	}

	//Call API for obtain user's teams round 
	private class RoundsAsyncCallback extends AsyncTask<String, Void, Integer > {
		private ProgressDialog progressDialog;
		private List<Round> currentRounds;

		private RoundsAsyncCallback(){
			super();
			progressDialog = new ProgressDialog(HomeView.this);
			progressDialog.setCanceledOnTouchOutside(false);
		}

		@Override
		protected void onPreExecute() {
			if(rounds.size()==0){
				progressDialog.setMessage("Loading...");
				progressDialog.show();
			}
		}
		@Override
		protected Integer doInBackground(String... params)  {
			try{
				currentRounds = new ArrayList<Round>();
				currentRounds.addAll(APIManager.rounds(User.auth_token, RoundStatus.open, 100, -1));
				currentRounds.addAll(APIManager.rounds(User.auth_token, RoundStatus.closed, 100, -1));
				currentRounds.addAll(APIManager.rounds(User.auth_token, RoundStatus.finished, 100, -1));
				if(TeamsView.teams.size() == 0){
					TeamsView.teams = APIManager.memberships(User.auth_token);
				}
				return AsyncCallbackResult.TRUE;
			} catch (JSONException e) {
				return AsyncCallbackResult.EXCEPTION;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (progressDialog.isShowing())
				try{
					progressDialog.dismiss();
				}catch(Exception e){}


			if (result == AsyncCallbackResult.TRUE) {
				if(currentRounds.size() != rounds.size() || !currentRounds.containsAll(rounds)){
					rounds = new ArrayList<Round>(currentRounds);
					//Refresh adapter
					Collections.sort(rounds,Collections.reverseOrder(myComparator));
					adapter = new RoundAdapter(HomeView.this, new ArrayList<Round>(rounds));
					listView.setAdapter(adapter);
				}
			}else {
				Toast.makeText(getApplicationContext(),  "Brewlette has encountered a problem" , Toast.LENGTH_LONG).show();
				rounds.clear();
				//showLoginDialog();
			}


		}

	}

	public static void clearRounds(){
		rounds.clear();
	}
	
	private class LoginAsyncCallback extends AsyncTask<String, Void, Integer> {


		protected Integer doInBackground(String... params) {

			try {
				//if(APIManager.login(params[0], params[1]))
				if(APIManager.login("nolan@fpstudios.com", "password"))
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
			ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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

				dialog.dismiss();

			} else if (result == AsyncCallbackResult.FALSE) {

				if(isConnectingToInternet())
					Toast.makeText(HomeView.this,  getResources().getString(R.string.login_error_message), Toast.LENGTH_LONG).show();
				else
					Toast.makeText(HomeView.this,  getResources().getString(R.string.no_network_error), Toast.LENGTH_LONG).show();


			} else if (result == AsyncCallbackResult.EXCEPTION) {

				Toast.makeText(HomeView.this, getResources().getString(R.string.no_network_error), Toast.LENGTH_LONG).show();

			}

		}
	}

}
