package views.brew;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import views.HomeView;
import webservices.APIManager;
import business.BrewTags;
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
import android.content.DialogInterface.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import async.AsyncCallbackResult;
import async.round.JoinRoundAsyncCallback;

public class BrewsView extends Activity {

	private ListView  listView;
	private static List<BrewTags> brews = new ArrayList<BrewTags>();
	private BrewAdapter adapter;
	private int roundId;
	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.brews_view);
		listView = (ListView)findViewById(R.id.BrewsView_BrewsListView);
		Bundle extras = getIntent().getExtras();

		//Check if we are in a round case
		//
		if (extras != null) {
			roundId = extras.getInt("round_id");
		}else{

			roundId = -1;
		}
		if(brews.size() == 0)
			refreshBrews();
		
		listView.setOnItemLongClickListener(new OnBrewLongClickListener());
		listView.setOnItemClickListener(new OnBrewClickListener(roundId));
		
		ActionBar bar = getActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void onResume(){
		super.onResume();
		adapter = new BrewAdapter(BrewsView.this, brews);
		listView.setAdapter(adapter);
	}

	private void refreshBrews(){
		new UserBrewsAsyncCallback().execute();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.brews_view, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		
		Intent intent;
		switch (item.getItemId()) {
		//Menu add Brew
		case R.id.addBrew:
			intent = new Intent(BrewsView.this,CreateBrewView.class);
			if(roundId != -1)
				intent.putExtra("round_id", roundId);
			startActivity(intent);
			return true;
		case R.id.refreshBrews:
			refreshBrews();
			return true;
		case android.R.id.home:            
	         intent = new Intent(this, HomeView.class);            
	         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
	         startActivity(intent);            
	         return true;  
		}
		return false;
	}

	private class OnBrewClickListener implements OnItemClickListener {
		private int roundId;
		private OnBrewClickListener(int roundId){
			this.roundId = roundId;
		}
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if(roundId == -1) {
				dialog = new Dialog(BrewsView.this);
				dialog.setContentView(R.layout.popu_brew_tags);
				TextView tags = (TextView) dialog.findViewById(R.id.PopupBrewTags);
				String content = new String();
				for(String tag : ((BrewTags)adapter.getItem(position)).getTags()){
					content += "- " + tag +"\n";
				}
				tags.setText(content);
				dialog.setTitle(adapter.getName(position));
				dialog.show();
			}else{
				AlertDialog.Builder builder = new AlertDialog.Builder(BrewsView.this);            
				builder.setTitle("Order this brew")
				.setMessage("Do you want order this brew :" + adapter.getName(position))
				.setPositiveButton("yes", new OnYesClickListener(id))
				.setNegativeButton("no",  new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}

				})
				.create()
				.show();
			}

		}



		private class OnYesClickListener implements OnClickListener {
			private long id;

			private OnYesClickListener(long id){
				this.id = id;
			}
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//Make the order call
				new JoinRoundAsyncCallback().execute(String.valueOf(roundId), String.valueOf(id));
				finish();

			}
		}



	}



	public class OnBrewLongClickListener implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int position, long id) {
			AlertDialog.Builder builder = new AlertDialog.Builder(BrewsView.this);            
			builder.setTitle("Delete this brew")
			.setMessage("Do you want delete this brew :" + adapter.getName(position))
			.setPositiveButton("yes", new OnDeleteYesClickListener(id))
			.setNegativeButton("no",  new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}

			})
			.create()
			.show();

			return true;
		}

		private class OnDeleteYesClickListener implements OnClickListener {
			private long id;

			private OnDeleteYesClickListener(long id){
				this.id = id;
			}
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//Make the delete call


			}
		}

	}

	public class UserBrewsAsyncCallback extends AsyncTask<String, Void, Integer >{
		private List<BrewTags> currentBrews;
		private ProgressDialog progressDialog;

		private UserBrewsAsyncCallback(){
			super();
			progressDialog = new ProgressDialog(BrewsView.this);
		}

		@Override
		protected void onPreExecute() {
			progressDialog.setMessage("Loading...");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
		}
		@Override
		protected Integer doInBackground(String... params)  {
			try {

				currentBrews = APIManager.userBrews(User.auth_token, 10, 1);
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
				if(currentBrews.size() != brews.size()){
					brews = new ArrayList<BrewTags>(currentBrews);
					adapter = new BrewAdapter(BrewsView.this, brews);
					listView.setAdapter(adapter);
				}
			}else {
				Toast.makeText(getApplicationContext(),  "Brewlette has encountered a problem" , Toast.LENGTH_LONG).show();
			}
		}
	}

	public static void clearBrews(){
		brews.clear();
	}
	
	public static void addBrew(BrewTags brew){
		brews.add(brew);
	}
}
