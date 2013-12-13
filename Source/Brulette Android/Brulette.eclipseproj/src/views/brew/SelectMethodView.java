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
import android.widget.TextView;

public class SelectMethodView extends Activity {
	private ImageView addButton;
	private AutoCompleteTextView tagAutoComplet;
	private TextView stirredTextView;
	private TextView slowTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_method_view);
		tagAutoComplet = (AutoCompleteTextView)findViewById(R.id.SelectMethodView_TagTextView);
		addButton = (ImageView) findViewById(R.id.SelectMethodView_AddImageView);
		addButton.setOnClickListener(new AddOnClickListener(""));
		
		stirredTextView = (TextView)findViewById(R.id.SelectMethodView_StirredTextView);
		stirredTextView.setOnClickListener(new AddOnClickListener("stirred"));
		
		slowTextView = (TextView)findViewById(R.id.SelectMethodView_SlowTextView);
		slowTextView.setOnClickListener(new AddOnClickListener("slow brew"));
		tagAutoComplet.setThreshold(1);
		
		new brewTagsAutoCompletAsyncCallback().execute(BrewCategory.method);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_method_view, menu);
		return true;
	}

	private class AddOnClickListener implements android.view.View.OnClickListener {

		private String methodSelected;
		private AddOnClickListener(String brew){
			methodSelected = brew; 
		}
		@Override
		public void onClick(View v) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(tagAutoComplet.getWindowToken(), 0);
			Intent intent = new Intent(SelectMethodView.this, CreateBrewView.class);
			//Case that the tag has been enter
			if(methodSelected.equals(""))
				methodSelected = tagAutoComplet.getText().toString();
			intent.putExtra("method", methodSelected);
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
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(SelectMethodView.this, android.R.layout.simple_dropdown_item_1line, result);
			tagAutoComplet.setAdapter(adapter);
		}

	}

}
