package views.round;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import views.HomeView;
import views.brew.BrewsView;
import views.order.OrderExpandListAdapter;
import webservices.APIManager;

import business.BrewTags;
import business.RoundStatus;
import business.User;

import com.android.brewlette.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import async.AsyncCallbackResult;

public class RoundView extends Activity {
	private Button orderButton;
	private TextView startClock;
	private TextView finishClock;
	private TextView teamName;
	private List<BrewTags> brews;
	private ExpandableListView expandList;
	private OrderExpandListAdapter adapter;
	private RoundStatus status;
	private int roundId;
	private LinearLayout linlaHeaderProgress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.round_view);
		orderButton = (Button) findViewById(R.id.RoundView_OrderButton);
		startClock = (TextView) findViewById(R.id.RoundView_StartClock);
		finishClock = (TextView) findViewById(R.id.RoundView_FinishClock);
		teamName = (TextView) findViewById(R.id.RoundView_TeamNameTextView);
		expandList = (ExpandableListView) findViewById(R.id.RoundView_OrderList);
		expandList.setClickable(true);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			roundId = (int) extras.getLong("round_id");
			startClock.setText(extras.getString("created"));
			finishClock.setText(extras.getString("closes"));
			teamName.setText(extras.getString("team_name"));
			status = (RoundStatus) extras.get("status");
		}
		linlaHeaderProgress = (LinearLayout) findViewById(R.id.HeaderProgressBar);

		
		
		if(status == RoundStatus.open)
			orderButton.setOnClickListener(new OrderOnClickListener());
		else
			orderButton.setVisibility(View.INVISIBLE);
		
		brews = new ArrayList<BrewTags>();
		
		adapter = new OrderExpandListAdapter(RoundView.this, brews);	
		expandList.setAdapter(adapter);
		
		expandList.setOnChildClickListener(adapter);

		expandList.expandGroup(0);
		
		ActionBar bar = getActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		new RoundBrewAsyncCallback().execute(roundId);
	}
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.round_view, menu);
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
		case R.id.refreshOrders:
			new RoundBrewAsyncCallback().execute(roundId);
		}
		
		return false;
	}
	
	private class OrderOnClickListener implements android.view.View.OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(RoundView.this,BrewsView.class);
			intent.putExtra("round_id", roundId);
            startActivity(intent);
		}
		
	}
	
	private class RoundBrewAsyncCallback extends AsyncTask<Integer, Void, Integer>{
		@Override
		protected Integer doInBackground(Integer... params) {
			try {
				brews = APIManager.roundBrews(User.auth_token, params[0], -1, -1);
			} catch (JSONException e) {
				return AsyncCallbackResult.EXCEPTION;
			}
			return AsyncCallbackResult.TRUE;
		}
		
		@Override
		protected void onPreExecute() {  
		    linlaHeaderProgress.setVisibility(View.VISIBLE);
		}
		
		protected void onPostExecute(Integer result) {
			
			 // HIDE THE SPINNER AFTER LOADING FEEDS
		    linlaHeaderProgress.setVisibility(View.GONE);

			if (result == AsyncCallbackResult.TRUE) {
				
				adapter.setBrews(brews);
				
				adapter.notifyDataSetChanged();

			} else if (result == AsyncCallbackResult.FALSE) {

				

			} else if (result == AsyncCallbackResult.EXCEPTION) {

				

			}

		}
	}

}
