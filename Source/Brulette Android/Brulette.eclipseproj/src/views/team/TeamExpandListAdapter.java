package views.team;

import java.util.ArrayList;
import java.util.List;

import com.android.brewlette.R;

import business.Membership;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class TeamExpandListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private List<Membership> teams;
	private List<Integer> teamsId;

	public TeamExpandListAdapter(Context context, List<Membership> teams){
		this.context = context;
		this.teams = teams;
		teamsId = new ArrayList<Integer>();
	}

	public List<Integer> getSelectedTeams(){
		return teamsId;
	}


	@Override
	public Object getChild(int groupPosition, int childPosition) {

		return teams.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return teams.get(childPosition).getId();
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.expandlist_child_item, null);
		}
		TextView tv = (TextView) convertView.findViewById(R.id.tvChild);
		tv.setText(teams.get(childPosition).getTeam_name());
		CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkChild);
		checkBox.setOnCheckedChangeListener(null);


		checkBox.setOnCheckedChangeListener(new TeamOnCheckedChangeListener(childPosition));

		// TODO Auto-generated method stub
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return teams.size();
	}

	@Override
	public Object getGroup(int groupPosition) {

		return teams;
	}

	@Override
	public int getGroupCount() {

		return 1;
	}

	@Override
	public long getGroupId(int groupPosition) {

		return 1;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			convertView = inf.inflate(R.layout.expandlist_group_item, null);
		}
		TextView tv = (TextView) convertView.findViewById(R.id.tvGroup);

		tv.setText("Teams");
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	private class TeamOnCheckedChangeListener implements OnCheckedChangeListener
	{
		private int position;
		private TeamOnCheckedChangeListener(int position){
			this.position = position;
		}
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		{
			int team = teams.get(position).getTeam_id();
			if(isChecked){
				if(!teamsId.contains(team))
					teamsId.add(team);
			}else {
				teamsId.remove(Integer.valueOf(team));
			}
		}
	}

}
