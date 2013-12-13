package views.team;


import views.HomeView;
import views.util.NumberPicker;


import business.Membership;

import com.android.brewlette.R;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import async.AsyncCallbackResult;
import async.round.StartRoundAsyncCallback;
import async.team.DeleteMembershipAsyncCallback;
import async.team.JoinTeamAsyncCallback;

public class TeamView extends Activity {
	private String teamName;
	private int membershipId;
	private int teamId;
	private TextView nameTextView;
	private Button startButton;
	private Button joinTeamButton;
	private Button joinRoundButton;
	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.team_view);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			teamName = extras.getString("team_name");
			membershipId = (int) extras.getLong("membership_id");
			teamId = extras.getInt("team_id");
		}
		nameTextView = (TextView) findViewById(R.id.TeamView_NameTextView);
		nameTextView.setText(teamName);

		startButton = (Button) findViewById(R.id.TeamView_StartRoundButton);
		joinTeamButton = (Button) findViewById(R.id.TeamView_JoinTeamButton);
		joinTeamButton.setOnClickListener(new JoinTeamOnClickListener());

		//Check if user is a member of the team
		if(membershipId==0){
			joinTeamButton.setVisibility(View.VISIBLE);
			startButton.setVisibility(View.INVISIBLE);
		}
		joinRoundButton = (Button) findViewById(R.id.TeamView_JoinRoundButton);
		
		ActionBar bar = getActionBar();
		bar.setDisplayHomeAsUpEnabled(true);

		// add button listener
		startButton.setOnClickListener(new StartRoundOnClickListener());
		//Check if a round has been start

	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.team_view, menu);
		if(membershipId==0)
			menu.getItem(0).setVisible(false);
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
	         
		case R.id.leaveTeam:
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which){
					case DialogInterface.BUTTON_POSITIVE:
						new DeleteMembershipAsyncCallback().execute(membershipId);
						TeamsView.removeTeam(membershipId);
						finish();
						break;

					case DialogInterface.BUTTON_NEGATIVE:
						break;
					}
				}
			};

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Do you want leave this team ?").setPositiveButton("Yes", dialogClickListener)
			.setNegativeButton("No", dialogClickListener).show();
			return true;
		}

		return false;
	}

	private class JoinTeamOnClickListener implements OnClickListener {
		private TextView password;
		private Toast msg;
		@Override
		public void onClick(View arg0) {


			AsyncTask<String, Void, Membership> asyncTask = new JoinTeamAsyncCallback().execute(String.valueOf(teamId),"");
			Membership result = null;

			try {
				result = asyncTask.get();
			} catch (Exception e) {	}

			if (result != null) {
				msg = Toast.makeText(getApplicationContext(),  "Team joined" , Toast.LENGTH_LONG);
				msg.setGravity(Gravity.TOP, 0, 60);
				msg.show();
				TeamsView.teams.add(result);
				finish();

			}else{

				dialog = new Dialog(TeamView.this);
				dialog.setContentView(R.layout.join_team_view);
				dialog.setTitle("Team Password");

				// set the custom dialog components - text, image and button

				Button dialogButton = (Button) dialog.findViewById(R.id.JoinTeamView_JoinButton);
				password = (TextView) dialog.findViewById(R.id.JoinTeamView_PasswordTextField);
				dialogButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						AsyncTask<String, Void, Membership> asyncTask = new JoinTeamAsyncCallback().execute(String.valueOf(teamId),password.getText().toString());
						Membership result = null;

						try {
							result = asyncTask.get();
						} catch (Exception e) {	}

						if (result != null) {
							msg = Toast.makeText(getApplicationContext(),  "Team joined" , Toast.LENGTH_LONG);
							dialog.dismiss();
							TeamsView.teams.add(result);
							finish();

						}
						else{
							msg = Toast.makeText(getApplicationContext(),  "Wrong password" , Toast.LENGTH_LONG);
						}

						msg.setGravity(Gravity.TOP, 0, 60);
						msg.show();
					}
				});

				dialog.show();
			}

		}

	}

	private class StartRoundOnClickListener implements OnClickListener {
		private NumberPicker picker;
		@Override
		public void onClick(View v) {
			// custom dialog
			dialog = new Dialog(TeamView.this);
			dialog.setContentView(R.layout.popup_start_round_view);
			dialog.setTitle("Round Time (minute)");

			// set the custom dialog components - text, image and button

			Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
			picker = (NumberPicker) dialog.findViewById(R.id.StartRoundView_TimeNumberPicker);
			// if button is clicked, close the custom dialog
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast msg;
					if(picker.getCurrent() > 0){
						AsyncTask<Integer, Void, Integer> asyncTask = new StartRoundAsyncCallback().execute(teamId, picker.getCurrent());
						Integer result = AsyncCallbackResult.EXCEPTION;

						try {
							result = asyncTask.get();
						} catch (Exception e) {	}

						if (result == AsyncCallbackResult.TRUE) {
							msg = Toast.makeText(getApplicationContext(),  "Round started" , Toast.LENGTH_LONG);
							dialog.dismiss();
							finish();
						}
						else{
							msg = Toast.makeText(getApplicationContext(),  "Round not started" , Toast.LENGTH_LONG);
						}
					}
					else{
						msg = Toast.makeText(getApplicationContext(),  "Please choose the round time" , Toast.LENGTH_LONG);
					}
					msg.setGravity(Gravity.TOP, 0, 60);
					msg.show();
				}
			});

			dialog.show();
		}

	}
}
