package views.profile;

import java.util.List;


import com.android.brewlette.R;

import business.Notifier;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import async.notifier.UpdateNotifierAsyncCallback;

public class ProfileAdapter extends BaseAdapter {

	private List<Notifier> notifiers;
	private LayoutInflater inflater;

	public ProfileAdapter(Context context, List<Notifier> notifiers) {


		this.inflater = LayoutInflater.from(context);
		this.notifiers = notifiers;

	}

	@Override
	public int getCount() {
		return notifiers.size();
	}

	@Override
	public Object getItem(int position) {
		return notifiers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return notifiers.get(position).getId();
	}

	public String getProvider(int position){
		return notifiers.get(position).getProvider();
	}

	public String getIdentifier(int position){
		return notifiers.get(position).getIdentifier();
	}

	public boolean isActive(int position){
		return notifiers.get(position).isActive();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if(convertView == null) {

			holder = new ViewHolder();

			convertView = inflater.inflate(R.layout.notifier_row, null);

			holder.provider = (TextView)convertView.findViewById(R.id.NotifierRow_ProviderTextView);
			holder.identifier = (TextView)convertView.findViewById(R.id.NotifierRow_IdentifierTextView);
			holder.active = (CheckBox)convertView.findViewById(R.id.NotifierRow_ActiveCheckBox);

			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();

		}

		holder.provider.setText(getProvider(position));
		holder.identifier.setText(getIdentifier(position));
		/**
		*    Ensure no other setOnCheckedChangeListener is attached before you manually
		*    change its state.
		*/
		holder.active.setOnCheckedChangeListener(null);
		
		holder.active.setChecked(isActive(position));
		
		holder.active.setOnCheckedChangeListener(new ActiveOnCheckedChangeListener(position));
		return convertView;
	}

	private class ViewHolder {

		TextView provider;
		TextView identifier;
		CheckBox active;

	}
	
	private class ActiveOnCheckedChangeListener implements OnCheckedChangeListener
	{
		private int position;
		private ActiveOnCheckedChangeListener(int position){
			this.position = position;
		}
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	    {
	        	new UpdateNotifierAsyncCallback().execute(String.valueOf(getItemId(position)),getProvider(position), getIdentifier(position), String.valueOf(isChecked));


	    }
	}



}
