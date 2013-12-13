package views.brew;

import com.android.brewlette.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class BrewView extends Activity {
	private int id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.brew_view);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			id = extras.getInt("id");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.brew_view, menu);
		return true;
	}

}
