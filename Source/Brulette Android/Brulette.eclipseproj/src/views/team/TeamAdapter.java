package views.team;

import java.util.List;

import com.android.brewlette.R;

import business.Membership;
import business.Team;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TeamAdapter extends BaseAdapter {

	List<Membership> teams;

	LayoutInflater inflater;

	public TeamAdapter(Context context, List<Membership> teams) {

	
	this.inflater = LayoutInflater.from(context);
	this.teams = teams;

	}
	@Override
	public int getCount() {
		return teams.size();
	}
	
	
	public String getTeamName(int position){
		return teams.get(position).getTeam_name();
	}
	
	public int getTeamId(int position){
		return teams.get(position).getTeam_id();
	}

	@Override
	public Object getItem(int position) {
		return teams.get(position);
	}

	@Override
	public long getItemId(int position) {
		return teams.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if(convertView == null) {

		holder = new ViewHolder();
		
		convertView = inflater.inflate(R.layout.team_row, null);

		holder.teamName = (TextView)convertView.findViewById(R.id.TeamRow_TeamNameTextView);

		convertView.setTag(holder);

		} else {

		holder = (ViewHolder) convertView.getTag();

		}

		holder.teamName.setText(getTeamName(position));
		//convertView.setOnClickListener(new OneTeamViewOnClickListener(context, teams.get(position).getSlug()));
		return convertView;
	}
	
	private class ViewHolder {

		TextView teamName;

		}

}
