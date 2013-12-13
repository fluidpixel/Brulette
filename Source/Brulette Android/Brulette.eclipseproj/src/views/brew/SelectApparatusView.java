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

public class SelectApparatusView extends Activity {
	private ImageView addButton;
	private AutoCompleteTextView tagAutoComplet;
	private ImageView small;
	private ImageView medium;
	private ImageView large;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_apparatus_view);
		tagAutoComplet = (AutoCompleteTextView)findViewById(R.id.SelectApparatusView_TagTextView);
		addButton = (ImageView) findViewById(R.id.SelectApparatusView_AddImageView);
		addButton.setOnClickListener(new AddOnClickListener(""));
		
		small = (ImageView) findViewById(R.id.SelectApparatusView_SmallImageView);
		small.setOnClickListener(new AddOnClickListener("small cup"));
		medium = (ImageView) findViewById(R.id.SelectApparatusView_MediumImageView);
		medium.setOnClickListener(new AddOnClickListener("medium cup"));
		large = (ImageView) findViewById(R.id.SelectApparatusView_LargeImageView);
		large.setOnClickListener(new AddOnClickListener("large cup"));
		
		tagAutoComplet.setThreshold(1);

		new brewTagsAutoCompletAsyncCallback().execute(BrewCategory.apparatus);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_apparatus_view, menu);
		return true;
	}

	private class AddOnClickListener implements android.view.View.OnClickListener {

		private String apparatusSelected;
		private AddOnClickListener(String brew){
			apparatusSelected = brew; 
		}
		@Override
		public void onClick(View v) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(tagAutoComplet.getWindowToken(), 0);
			Intent intent = new Intent(SelectApparatusView.this, CreateBrewView.class);
			//Case that the tag has been enter
			if(apparatusSelected.equals(""))
				apparatusSelected = tagAutoComplet.getText().toString();
			intent.putExtra("apparatus", apparatusSelected);
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
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(SelectApparatusView.this, android.R.layout.simple_dropdown_item_1line, result);
			tagAutoComplet.setAdapter(adapter);
		}

	}

}
