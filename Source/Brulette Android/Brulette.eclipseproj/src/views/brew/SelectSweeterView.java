package views.brew;


import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;

import webservices.APIManager;

import business.BrewCategory;
import business.User;

import com.android.brewlette.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

public class SelectSweeterView extends Activity {
	private ImageView addButton;
	private ImageView sugar1;
	private ImageView sugar2;
	private ImageView honey;
	private AutoCompleteTextView tagAutoComplet;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_sweeter_view);
		tagAutoComplet = (AutoCompleteTextView)findViewById(R.id.SelectSweeterView_TagTextView);
		addButton = (ImageView) findViewById(R.id.SelectSweeterView_AddImageView);
		addButton.setOnClickListener(new AddOnClickListener(""));

		sugar1 = (ImageView)findViewById(R.id.SelectSweeterView_Sugar1ImageView);
		sugar1.setOnClickListener(new AddOnClickListener("sweetener"));
		sugar2 = (ImageView)findViewById(R.id.SelectSweeterView_Sugar2ImageView);
		sugar2.setOnClickListener(new AddOnClickListener("sugar"));
		honey = (ImageView)findViewById(R.id.SelectSweeterView_HoneyImageView);
		honey.setOnClickListener(new AddOnClickListener("honey"));

		tagAutoComplet.setThreshold(1);

		new brewTagsAutoCompletAsyncCallback().execute(BrewCategory.sweet);
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_sweeter_view, menu);
		return true;
	}


	private class AddOnClickListener implements android.view.View.OnClickListener {

		private String sweeterSelected;
		private AddOnClickListener(String brew){
			sweeterSelected = brew; 
		}
		@Override
		public void onClick(View v) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(tagAutoComplet.getWindowToken(), 0);
			Intent intent = new Intent(SelectSweeterView.this, CreateBrewView.class);
			//Case that the tag has been enter
			if(sweeterSelected.equals(""))
				sweeterSelected = tagAutoComplet.getText().toString();
			intent.putExtra("sweeter", sweeterSelected);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}

	}

	private class brewTagsAutoCompletAsyncCallback extends AsyncTask<BrewCategory, Void, List<String> > {

		@Override
		protected List<String> doInBackground(BrewCategory... params) {

			try {
				return APIManager.brewTagsAutoComplet(User.auth_token,params[0]);
			} catch (JSONException e) {
				return new ArrayList<String>();
			}
		}

		protected void onPostExecute(List<String> result) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(SelectSweeterView.this, android.R.layout.simple_dropdown_item_1line, result);
			tagAutoComplet.setAdapter(adapter);
		}

	}
}
