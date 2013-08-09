package views.brew;


import java.util.List;

import com.android.brewlette.R;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CreateBrewAdapter extends BaseAdapter {

	private static final String[] categories = {"1.brew", "2.sweeter", "3.smoother", "4.appartus", "5.method"};
	private List<String> tags;
	Context context;

	public CreateBrewAdapter(Context context, List<String> tags) {

		this.context = context;
		this.tags = tags;

	}

	@Override
	public int getCount() {
		return categories.length;
	}

	@Override
	public Object getItem(int position) {
		return categories[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if(convertView == null) {

			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.create_brew_row, null);

			holder.category = (TextView)convertView.findViewById(R.id.CreateBrewRow_CategorieTextView);
			holder.tag = (TextView)convertView.findViewById(R.id.CreateBrewRow_TagTextView);

			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();

		}

		holder.category.setText(categories[position]);

		holder.tag.setText(getFormatedTag(tags.get(position), position));
		return convertView;
	}

	private SpannableString getFormatedTag(String tag, int position){
		SpannableString spanString = new SpannableString(tag);
		switch(position){
		case 0:
			if(!tag.equals("BREW")){
				spanString = new SpannableString(tag);
				spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
				spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
				spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);
			}
			break;
		case 1:
			if(!tag.equals("SWEETER")){
				spanString = new SpannableString("with " +tag.toLowerCase());
				spanString.setSpan(new UnderlineSpan(), 5, spanString.length(), 0);
				spanString.setSpan(new StyleSpan(Typeface.BOLD), 5, spanString.length(), 0);
				spanString.setSpan(new StyleSpan(Typeface.ITALIC), 5, spanString.length(), 0);
			}
			break;
		default :
			if(!tag.equals("SMOOTHER") && !tag.equals("APPARATUS") && !tag.equals("METHOD") ){
				spanString = new SpannableString(tag.toLowerCase());
				spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
				spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
				spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);
			}
			break;
		}

		//Check if the tag has been modified
		/*if(!tag.equals("BREW") && !tag.equals("SWEETER") && !tag.equals("SMOOTHER") && !tag.equals("APPARATUS") && !tag.equals("METHOD") ){
			spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
			spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
			spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);
		}*/
		return spanString;
	}

	private class ViewHolder {

		TextView category;
		TextView tag;

	}

}
