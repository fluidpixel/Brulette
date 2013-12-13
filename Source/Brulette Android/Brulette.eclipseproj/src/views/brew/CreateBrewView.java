package views.brew;

import java.util.ArrayList;
import java.util.List;

import views.HomeView;

import business.Brew;
import business.BrewCategory;
import business.BrewTags;


import com.android.brewlette.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import async.brew.CreateBrewAsyncCallback;

public class CreateBrewView extends Activity {
	private ListView  listView;
	private ImageView brewImage;
	private List<String> tags;
	private CreateBrewAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_brew_view);
		listView = (ListView)findViewById(R.id.CreateBrewView_CreateBrewsListView);
		brewImage = (ImageView) findViewById(R.id.CreateBrewView_BrewImageView);

		listView.setOnItemClickListener(new OnCategoryClickListener());

		//Check if we are in the round case
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			if(extras.getInt("round_id") != 0)
				SavePreferences("round_id", extras.getInt("round_id"));
		}

		ActionBar bar = getActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void onResume(){
		super.onResume();
		Bundle extras = getIntent().getExtras();
		String brew,sweeter,smoother,apparatus,method;
		if (extras != null) {
			brew= extras.getString("brew");
			sweeter= extras.getString("sweeter");
			smoother= extras.getString("smoother");
			apparatus= extras.getString("apparatus");
			method= extras.getString("method");
			if(brew != null && !brew.equals("")){
				SavePreferences("brew", brew.toUpperCase());
			}else if (sweeter != null && !sweeter.equals("")){
				SavePreferences("sweeter", sweeter.toUpperCase());
			}else if (smoother != null && !smoother.equals("")){
				SavePreferences("smoother", smoother.toUpperCase());
			}else if (apparatus != null && !apparatus.equals("")){
				SavePreferences("apparatus", apparatus.toUpperCase());
			}else if (method != null && !method.equals("")){
				SavePreferences("method", method.toUpperCase());
			}
		}

		constructTags();
		selectBrewImage(tags.get(0));
		adapter = new CreateBrewAdapter(this, tags);

		listView.setAdapter(adapter);

		ActionBar bar = getActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_brew, menu);
		return true;
	}

	private void constructTags(){
		tags= new ArrayList<String>();
		SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
		tags.add(sharedPreferences.getString("brew", "BREW"));
		tags.add(sharedPreferences.getString("sweeter", "SWEETER"));
		tags.add(sharedPreferences.getString("smoother", "SMOOTHER"));
		tags.add(sharedPreferences.getString("apparatus", "APPARATUS"));
		tags.add(sharedPreferences.getString("method", "METHOD"));

	}

	private void selectBrewImage(String brew){
		String br = brew.toLowerCase();
		if(br.equals("coffee")){
			this.brewImage.setImageResource(R.drawable.cup_coffee);
		}else if (br.equals("tea")){
			this.brewImage.setImageResource(R.drawable.cup_tea);
		}
	}

	private void SavePreferences(String key, String value) {
		SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	private void SavePreferences(String key, int value) {
		SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	private void clearAllPreferences() {
		SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.clear();
		editor.commit();
	}

	private class OnCategoryClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent;
			switch(position){
			case 0:
				intent = new Intent(CreateBrewView.this, SelectBrewView.class);
				startActivity(intent);
				break;
			case 1:
				intent = new Intent(CreateBrewView.this, SelectSweeterView.class);
				startActivity(intent);
				break;
			case 2:
				intent = new Intent(CreateBrewView.this, SelectSmootherView.class);
				startActivity(intent);
				break;
			case 3:
				intent = new Intent(CreateBrewView.this, SelectApparatusView.class);
				startActivity(intent);
				break;
			case 4:
				intent = new Intent(CreateBrewView.this, SelectMethodView.class);
				startActivity(intent);
				break;
			}


		}

	}
	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);            
		builder.setTitle("Quit")
		.setMessage(this.getResources().getString(R.string.quit_page_message))
		.setPositiveButton("yes", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				clearAllPreferences();
				finish();
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
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case android.R.id.home:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);            
			builder.setTitle("Quit")
			.setMessage(this.getResources().getString(R.string.quit_page_message))
			.setPositiveButton("yes", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog,
						int which) {

					clearAllPreferences();
					Intent intent = new Intent(CreateBrewView.this, HomeView.class);            
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
					startActivity(intent);
				}
			}).setNegativeButton("no",  new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}

			}).create().show();

			return true;   
		case R.id.done:
			if(!tags.get(0).equals("BREW")){
				intent = new Intent(CreateBrewView.this,BrewsView.class);

				List<Brew> brews = new ArrayList<Brew>();
				brews.add(new Brew(tags.get(0), BrewCategory.drink));
				if(!tags.get(1).equals("SWEETER"))
					brews.add(new Brew(tags.get(1), BrewCategory.sweet));
				if(!tags.get(2).equals("SMOOTHER"))
					brews.add(new Brew(tags.get(2), BrewCategory.smooth));
				if(!tags.get(3).equals("APPARATUS"))
					brews.add(new Brew(tags.get(3), BrewCategory.apparatus));
				if(!tags.get(4).equals("METHOD"))
					brews.add(new Brew(tags.get(4), BrewCategory.method));

				AsyncTask<List<Brew>, Void, BrewTags> asyncTask = new CreateBrewAsyncCallback().execute(brews);
				BrewTags result =null;

				try {
					result = asyncTask.get();
				} catch (Exception e) {	}

				if (result != null) {
					BrewsView.addBrew(result);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
					int roundId =sharedPreferences.getInt("round_id", -1);
					intent.putExtra("round_id", roundId);
					startActivity(intent);
					clearAllPreferences();
					Toast.makeText(getApplicationContext(),  "Brew added !", Toast.LENGTH_LONG).show();
					finish();
				}
			}
			else{
				Toast.makeText(getApplicationContext(),  "Please choose your brew...", Toast.LENGTH_LONG).show();
			}
			return true;
		}

		return false;
	}

}
