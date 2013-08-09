package views.order;

import java.util.List;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;
import business.BrewTags;

import com.android.brewlette.R;

public class OrderExpandListAdapter extends BaseExpandableListAdapter implements OnChildClickListener {

	private Context context;
	private List<BrewTags> brews;
	private Dialog dialog;

	public OrderExpandListAdapter(Context context, List<BrewTags> brews){
		this.context = context;
		this.brews = brews;
		
	}
	public void setBrews(List<BrewTags> brews){
		this.brews = brews;
	}
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.order_expandlist_child_item, null);
		}
		TextView tv = (TextView) convertView.findViewById(R.id.Order_tvChild);
		tv.setText(brews.get(childPosition).getName());
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return brews.size();
	}

	@Override
	public Object getGroup(int groupPosition) {

		return null;
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

		tv.setText("Orders (" + brews.size() + ")");
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}


	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return brews.get(childPosition);
	}


	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return brews.get(childPosition).getUserId();
	}
	@Override
	public boolean onChildClick(ExpandableListView arg0, View arg1, int groupPosition,
			int childPosition, long id) {

		dialog = new Dialog(context);
		dialog.setContentView(R.layout.popu_brew_tags);
		TextView tags = (TextView) dialog.findViewById(R.id.PopupBrewTags);
		String content = new String();
		for(String tag : brews.get(childPosition).getTags()){
			content += "- " + tag +"\n";
		}
		tags.setText(content);
		String name = brews.get(childPosition).getName();
		dialog.setTitle(name.substring(0, name.indexOf("|")));
		dialog.show();
		return true;
	}
	
}
