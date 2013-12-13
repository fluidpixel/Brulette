package views.round;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.json.JSONException;

import views.HomeView;
import views.team.TeamExpandListAdapter;
import views.team.TeamsView;
import views.util.NumberPicker;
import webservices.APIManager;
import com.android.brewlette.R;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;
import async.AsyncCallbackResult;
import business.Membership;
import business.User;


public class StartRoundView extends Activity {
	private ExpandableListView expandList;
	private List<Membership> teams;
	private TeamExpandListAdapter adapter;
	private Button startButton;
	private NumberPicker picker;
	Comparator<Membership> myComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_round_view);

		expandList = (ExpandableListView) findViewById(R.id.StartRoundView_TeamExpandableListView);
		startButton = (Button) findViewById(R.id.StartRoundView_StartButton);
		startButton.setOnClickListener(new StartRoundOnClickListener());
		picker = (NumberPicker)findViewById(R.id.StartRoundView_TimeNumberPicker);
		myComparator = new Comparator<Membership>() {
		    public int compare(Membership obj1,Membership obj2) {
		        return obj1.getTeam_name().compareToIgnoreCase(obj2.getTeam_name());
		    }
		 };
		getTeams();
		expandList.expandGroup(0);
		ActionBar bar = getActionBar();
		bar.setDisplayHomeAsUpEnabled(true);

	}

	private void getTeams(){
		
		teams = new ArrayList<Membership>(TeamsView.teams);

		Collections.sort(teams, myComparator);
		adapter = new TeamExpandListAdapter(StartRoundView.this, teams);	
		expandList.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.start_round, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:            
	         Intent intent = new Intent(this, HomeView.class);            
	         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
	         startActivity(intent);            
	         return true;        
		}
		
		return false;
	}

	private class StartRoundOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Toast msg = null;
			Log.i("TEAMS ROUND", adapter.getSelectedTeams().toString());
			if(adapter.getSelectedTeams().size() > 0){
				if(picker.getCurrent() > 0){
					new StartMultRoundAsyncCallback().execute();
				}else{
					msg = Toast.makeText(getApplicationContext(),  "Please choose the round time" , Toast.LENGTH_LONG);
				}
			}
			else {
				msg = Toast.makeText(getApplicationContext(),  "Please choose a team" , Toast.LENGTH_LONG);
			}
			if(msg != null){
				msg.setGravity(Gravity.TOP, 0, 60);
				msg.show();
			}

		}

	}

	public class StartMultRoundAsyncCallback extends AsyncTask<Integer, Void, Integer> {
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(StartRoundView.this);
			progressDialog.setMessage("Loading...");
			progressDialog.show();
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			try{
				for(int i=0; i<adapter.getSelectedTeams().size(); i++){
					APIManager.startRound(User.auth_token, adapter.getSelectedTeams().get(i), true, picker.getCurrent()*60);
				}

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
				Toast.makeText(StartRoundView.this,  "Round started" , Toast.LENGTH_LONG).show();
				finish();
			}else {
				Toast.makeText(getApplicationContext(),  "Round has encountered a problem" , Toast.LENGTH_LONG).show();
			}
		}

	}

}
