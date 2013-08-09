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

public class SelectSmootherView extends Activity {
	private ImageView addButton;
	private AutoCompleteTextView tagAutoComplet;
	private ImageView milk;
	private ImageView lemon;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_smoother_view);
		tagAutoComplet = (AutoCompleteTextView)findViewById(R.id.SelectSmootherView_TagTextView);
		addButton = (ImageView) findViewById(R.id.SelectSmootherView_AddImageView);
		addButton.setOnClickListener(new AddOnClickListener(""));

		milk = (ImageView)findViewById(R.id.SelectSmootherView_MilkImageView);
		milk.setOnClickListener(new AddOnClickListener("milk"));
		lemon = (ImageView)findViewById(R.id.SelectSmootherView_LimeImageView);
		lemon.setOnClickListener(new AddOnClickListener("lemon"));

		tagAutoComplet.setThreshold(1);

		new brewTagsAutoCompletAsyncCallback().execute(BrewCategory.smooth);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_smoother_view, menu);
		return true;
	}

	private class AddOnClickListener implements android.view.View.OnClickListener {

		private String smootherSelected;
		private AddOnClickListener(String brew){
			smootherSelected = brew; 
		}
		@Override
		public void onClick(View v) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(tagAutoComplet.getWindowToken(), 0);
			Intent intent = new Intent(SelectSmootherView.this, CreateBrewView.class);
			//Case that the tag has been enter
			if(smootherSelected.equals(""))
				smootherSelected = tagAutoComplet.getText().toString();
			intent.putExtra("smoother", smootherSelected);
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
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(SelectSmootherView.this, android.R.layout.simple_dropdown_item_1line, result);
			tagAutoComplet.setAdapter(adapter);
		}

	}

}
