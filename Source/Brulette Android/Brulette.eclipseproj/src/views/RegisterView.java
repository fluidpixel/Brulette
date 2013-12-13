package views;


import com.android.brewlette.R;
import com.urbanairship.push.PushManager;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import async.AsyncCallbackResult;
import async.RegisterAsyncCallback;
import async.notifier.CreateNotifierAsyncCallback;

public class RegisterView  extends Activity {
	private Button registerButton;
	private TextView nameTextView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_view);
		registerButton = (Button) findViewById(R.id.RegisterView_RegisterButton);
		registerButton.setOnClickListener(new RegisterOnClickListener());

	}


	private class RegisterOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			nameTextView = (TextView) findViewById(R.id.RegisterView_NameTextField);

			String stringName = nameTextView.getText().toString();


			if(stringName.equals("")){

				Toast.makeText(getApplicationContext(),  getResources().getString(R.string.blank_fields_message), Toast.LENGTH_LONG).show();
			}
			else {

				registerButton.setEnabled(false);
				//Call API for create user
				AsyncTask<String, Void, Integer> asyncTask = new RegisterAsyncCallback(RegisterView.this).execute(stringName);

				Integer result = AsyncCallbackResult.EXCEPTION;

				try {
					result = asyncTask.get();
				} catch (Exception e) {}

				if (result == AsyncCallbackResult.TRUE) {
					//Call APi for create notification for device user
					new CreateNotifierAsyncCallback().execute("android", PushManager.shared().getAPID());
					//Close keybord
					 InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                     imm.hideSoftInputFromWindow(nameTextView.getWindowToken(), 0);
					finish();

				} else {
					registerButton.setEnabled(true);
				}
			}

		}
	}


}

