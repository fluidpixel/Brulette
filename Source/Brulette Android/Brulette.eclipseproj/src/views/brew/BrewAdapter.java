package views.brew;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import business.BrewTags;

import com.android.brewlette.R;

public class BrewAdapter extends BaseAdapter {

	List<BrewTags> brews;

	LayoutInflater  inflater;

	public BrewAdapter(Context context, List<BrewTags> brews) {


		inflater = LayoutInflater.from(context);
		this.brews = brews;

	}
	@Override
	public int getCount() {
		return brews.size();
	}

	@Override
	public Object getItem(int position) {
		return brews.get(position);
	}

	@Override
	public long getItemId(int position) {
		return brews.get(position).getId();
	}
	
	public String getName(int position){
		return brews.get(position).getName();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if(convertView == null) {

			holder = new ViewHolder();

			convertView = inflater.inflate(R.layout.brew_row, null);

			holder.teamName = (TextView)convertView.findViewById(R.id.BrewRow_BrewNameTextView);

			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();

		}

		holder.teamName.setText(getName(position));
		return convertView;
	}

	private class ViewHolder {

		TextView teamName;

	}


}
