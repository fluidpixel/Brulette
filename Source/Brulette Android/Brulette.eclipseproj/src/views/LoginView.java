package views;

import business.User;

import com.android.brewlette.R;
import com.urbanairship.push.PushManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import async.AsyncCallbackResult;
import async.ConnectUserAsyncCallback;

public class LoginView extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_view);
		
	}
	
	 protected void onResume(){
		 super.onResume();
		 
		//When the user was not disconnect and the app is restarted
			if(User.id != -1){
				HomeView.clearRounds();
				Intent intent = new Intent(LoginView.this, HomeView.class);
				startActivity(intent);
				finish();
			}else{
			
				AsyncTask<String, Void, Integer> asyncTask = new ConnectUserAsyncCallback(LoginView.this).execute(PushManager.shared().getAPID()+"@fpstudios.com", PushManager.shared().getAPID());
				Integer result = AsyncCallbackResult.EXCEPTION;
		
				try {
					result = asyncTask.get();
				} catch (Exception e) {}
		
				if (result != AsyncCallbackResult.TRUE) {
					//User is not register
					Intent intent = new Intent(LoginView.this, RegisterView.class);
					startActivity(intent);
					finish();
				}
			}
		 
	 }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_view, menu);
		return true;
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.register:
			Intent intent = new Intent(LoginView.this,RegisterView.class);
            startActivity(intent);
			return true;
		}
		return false;
	}

}
