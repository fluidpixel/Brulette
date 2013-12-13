package views.brew;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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

public class SelectBrewView extends Activity {
	private ImageView addButton;
	private ImageView coffeeButton;
	private ImageView teaButton;
	private AutoCompleteTextView tagAutoComplet;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_brew_view);
		addButton = (ImageView) findViewById(R.id.SelectBrewView_AddImageView);
		
		tagAutoComplet = (AutoCompleteTextView)findViewById(R.id.SelectBrewView_TagTextView);
		tagAutoComplet.setThreshold(1);
		
		new brewTagsAutoCompletAsyncCallback().execute(BrewCategory.drink);
		
		
		
		addButton.setOnClickListener(new AddOnClickListener(""));
		
		coffeeButton = (ImageView) findViewById(R.id.SelectBrewView_CoffeeImageView);
		coffeeButton.setOnClickListener(new AddOnClickListener("coffee"));
		
		teaButton = (ImageView) findViewById(R.id.SelectBrewView_TeaImageView);
		teaButton.setOnClickListener(new AddOnClickListener("tea"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_brew_view, menu);
		return true;
	}
	
	 private class AddOnClickListener implements android.view.View.OnClickListener {

		 private String brewSelected;
		 private AddOnClickListener(String brew){
			 brewSelected = brew; 
		 }
			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(tagAutoComplet.getWindowToken(), 0);
				 Intent intent = new Intent(SelectBrewView.this, CreateBrewView.class);
				 //Case that the tag has been enter
				 if(brewSelected.equals(""))
					 brewSelected = tagAutoComplet.getText().toString();
				 intent.putExtra("brew", brewSelected);
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
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(SelectBrewView.this, android.R.layout.simple_dropdown_item_1line, result);
				tagAutoComplet.setAdapter(adapter);
			}

		}

}
