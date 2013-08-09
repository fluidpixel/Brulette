package views.team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONException;


import views.HomeView;
import webservices.APIManager;


import business.Membership;
import business.Team;
import business.User;

import com.android.brewlette.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import async.AsyncCallbackResult;
import async.team.CreateTeamAsyncCallback;
import async.team.DeleteMembershipAsyncCallback;
import async.team.TeamBySlugAsyncCallback;

public class TeamsView extends Activity {
	private ListView  listView;
	public static List<Membership> teams = new ArrayList<Membership>();
	private TeamAdapter adapter;
	private Button dialButton;
	private  Dialog dialog;
	Comparator<Membership> myComparator;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teams_view);
		listView = (ListView)findViewById(R.id.TeamsView_TeamsListView);
		myComparator = new Comparator<Membership>() {
			public int compare(Membership obj1,Membership obj2) {
				return obj1.getTeam_name().compareToIgnoreCase(obj2.getTeam_name());
			}
		};
		if(teams.size() == 0)
			refreshTeams();
		Collections.sort(teams, myComparator);
		adapter = new TeamAdapter(TeamsView.this, teams);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnTeamClickListener());
		listView.setOnItemLongClickListener(new OnTeamLongClickListener());
		ActionBar bar = getActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		
	}

	@Override
	public void onResume(){
		super.onResume();
		Collections.sort(teams, myComparator);
        adapter.notifyDataSetChanged();
		
	}
	
	private void refreshTeams(){
		new MembershipsAsyncCallback().execute();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.teams_view, menu);
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
		case R.id.addTeam:
			dialog = new Dialog(TeamsView.this);
			dialog.setContentView(R.layout.create_team_view);
			dialog.setTitle("ADD TEAM");
			dialButton = (Button) dialog.findViewById(R.id.CreateTeamView_CreateButton);
			dialButton.setOnClickListener(new CreateOnClickListener());
			dialog.show();
			break;
		case R.id.searchTeam:
			showSearchDialog();
			break;
		case R.id.refreshTeams:
			refreshTeams();
			break;
		}
		return true;
	}

	private void showSearchDialog(){
		dialog = new Dialog(TeamsView.this);
		dialog.setContentView(R.layout.search_team_view);
		dialog.setTitle("Search team");
		dialButton = (Button) dialog.findViewById(R.id.SearchTeamView_SearchButton);
		dialButton.setOnClickListener(new SearchOnClickListener());
		dialog.show();
	}

	//For device with search button
	public boolean onSearchRequested (){
		showSearchDialog();
		return true;
	}


	private class OnTeamClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(TeamsView.this, TeamView.class);
			intent.putExtra("team_name", adapter.getTeamName(position));
			intent.putExtra("team_id", adapter.getTeamId(position));	
			intent.putExtra("membership_id", id);
			startActivity(intent);

		}
	}

	private class CreateOnClickListener implements OnClickListener {
		private TextView teamName;
		private TextView teamPassword;
		@Override
		public void onClick(View v) {
			teamName = (TextView) dialog.findViewById(R.id.CreateTeamView_NameTextField);
			teamPassword = (TextView) dialog.findViewById(R.id.CreateTeamView_PasswordTextField);
			String stringTeam = teamName.getText().toString();
			String stringPassword = teamPassword.getText().toString();

			if(stringTeam.equals("")){

				Toast.makeText(getApplicationContext(),  getResources().getString(R.string.blank_fields_message), Toast.LENGTH_LONG).show();

			} else if (stringTeam.length() < 5) {
				Toast.makeText(getApplicationContext(),  getResources().getString(R.string.short_team), Toast.LENGTH_LONG).show();
			}
			else {
				//Call webservice
				dialButton.setEnabled(false);
				AsyncTask<String, Void, Membership> asyncTask = new CreateTeamAsyncCallback(TeamsView.this).execute(stringTeam, stringPassword);
				Membership result = null;
				dialog.dismiss();
				try {
					result = asyncTask.get();
				} catch (Exception e) {}

				if (result != null) {
					Toast.makeText(getApplicationContext(),  "Team " + stringTeam + " created !" , Toast.LENGTH_LONG).show();
					teams.add(result);
					Collections.sort(teams, myComparator);
					adapter = new TeamAdapter(TeamsView.this, teams);
					listView.setAdapter(adapter);
				} else {
					dialButton.setEnabled(true);
				}
			}

		}

	}

	private class SearchOnClickListener implements OnClickListener {
		private TextView teamSlug;
		@Override
		public void onClick(View v) {
			teamSlug = (TextView) dialog.findViewById(R.id.SearchTeamView_SlugTextView);
			String stringSlug = teamSlug.getText().toString();

			if(stringSlug.equals("")){

				Toast.makeText(getApplicationContext(),  getResources().getString(R.string.blank_fields_message), Toast.LENGTH_LONG).show();

			} 
			else {
				//Call webservice
				dialButton.setEnabled(false);
				AsyncTask<String, Void, Team> asyncTask = new TeamBySlugAsyncCallback().execute(stringSlug);
				Team result = null;

				try {
					result = asyncTask.get();
				} catch (Exception e) {	}

				if (result != null) {
					Intent intent = new Intent(TeamsView.this, TeamView.class);
					intent.putExtra("team_name", result.getName());
					intent.putExtra("team_id", result.getId());
					startActivity(intent);
					dialog.dismiss();
				} else {
					Toast msg = Toast.makeText(getApplicationContext(),  "No result..." , Toast.LENGTH_LONG);
					msg.setGravity(Gravity.TOP, 0, 50);
					msg.show();
					dialButton.setEnabled(true);
				}
			}

		}

	}

	public class OnTeamLongClickListener implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int position, long id) {
			AlertDialog.Builder builder = new AlertDialog.Builder(TeamsView.this);            
			builder.setTitle("Leave this team")
			.setMessage("Do you want leave this team :" + adapter.getTeamName(position))
			.setPositiveButton("yes", new OnLeaveYesClickListener(position, id))
			.setNegativeButton("no",  new android.content.DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}

			})
			.create()
			.show();

			return true;
		}

		private class OnLeaveYesClickListener implements android.content.DialogInterface.OnClickListener {
			private long id;
			private int position;

			private OnLeaveYesClickListener(int position, long id){
				this.id = id;
				this.position = position;
			}
			@Override
			public void onClick(DialogInterface dialog, int which) {
				new DeleteMembershipAsyncCallback().execute((int)id);
				teams.remove(position);
				adapter = new TeamAdapter(TeamsView.this, teams);
				listView.setAdapter(adapter);
			}
		}

	}

	private class MembershipsAsyncCallback extends AsyncTask<String, Void, Integer >{

		private List<Membership> currentTeams;
		private ProgressDialog progressDialog;

		private MembershipsAsyncCallback(){
			super();
			progressDialog = new ProgressDialog(TeamsView.this);
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
				currentTeams = APIManager.memberships(User.auth_token);
				return AsyncCallbackResult.TRUE;

			} catch (JSONException e) {
				return AsyncCallbackResult.FALSE;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (progressDialog.isShowing())
				progressDialog.dismiss();

			if (result == AsyncCallbackResult.TRUE) {
				if(currentTeams.size() != teams.size()){
					
					teams = new ArrayList<Membership>(currentTeams);
					Collections.sort(teams, myComparator);
					adapter = new TeamAdapter(TeamsView.this, teams);
					listView.setAdapter(adapter);
					
				}
			}else {
				Toast.makeText(getApplicationContext(),  "Brewlette has encountered a problem" , Toast.LENGTH_LONG).show();
			}
		}
	}
	
	public static void clearTeams(){
		teams.clear();
	}
	
	public static void removeTeam(int idMembership){
		for(Membership m : teams){
			if(m.getId() == idMembership){
				teams.remove(m);
				break;
			}
		}
	}

}
