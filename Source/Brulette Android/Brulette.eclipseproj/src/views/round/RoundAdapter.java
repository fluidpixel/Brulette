package views.round;

import java.util.Date;
import java.util.List;

import views.team.TeamsView;

import com.android.brewlette.R;

import business.Membership;
import business.Round;
import business.RoundStatus;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RoundAdapter extends BaseAdapter {

	private List<Round> rounds;
	private LayoutInflater inflater;

	public RoundAdapter(Context context, List<Round> rounds){
		this.inflater = LayoutInflater.from(context);
		this.rounds = rounds;
	}

	@Override
	public int getCount() {
		return rounds.size();
	}

	@Override
	public Object getItem(int position) {
		return rounds.get(position);
	}

	@Override
	public long getItemId(int position) {
		return rounds.get(position).getId();
	}
	
	public Date getCreated(int position){
		return rounds.get(position).getCreated();
	}
	
	public Date getCloses(int position){
		return rounds.get(position).getCloses();
	}
	
	public int getTeamId(int position) {
		return rounds.get(position).getIdTeam();
	}

	public RoundStatus getStatus(int position){
		return rounds.get(position).getStatus();
	}
	
	public Round getRound(int position){
		return rounds.get(position);
	}

	public int getImageDrawable(int position){
		switch(rounds.get(position).getStatus()){
		
		case open:
			return R.drawable.green;
		case closed:
			return R.drawable.red;

		default:
			return R.drawable.red;

		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if(convertView == null) {

			holder = new ViewHolder();

			convertView = inflater.inflate(R.layout.round_row, null);

			holder.teamName = (TextView)convertView.findViewById(R.id.RoundRowView_TeamTextView);
			holder.statusImageView = (ImageView)convertView.findViewById(R.id.RoundRowView_StatusImageView);

			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();

		}
		/*Typeface type = Typeface.createFromAsset(context.getAssets(),"comic.ttf"); 
		holder.teamName.setTypeface(type);*/
		holder.teamName.setText(getTeamName(getTeamId(position)));
		holder.statusImageView.setImageResource(getImageDrawable(position));

		return convertView;
	}

	private class ViewHolder {

		TextView teamName;
		ImageView statusImageView;

	}
	
	public String getTeamName(int teamId){
		for(Membership m : TeamsView.teams){
			if(m.getTeam_id() == teamId)
				return m.getTeam_name();
		}
		
		return "Team " + teamId;
	}

}
