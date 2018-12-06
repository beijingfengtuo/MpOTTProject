package cn.cibnmp.ott.ui.search;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.utils.DisplayUtils;

/**
 * 所有tab标签的adapter
 * @author geshuaipeng
 *
 */
public class PromptListAdapter extends BaseAdapter {
	private Context context;
	private List<ContentBean> list;
	private String editKeyword;
	
	public PromptListAdapter(Context context, List<ContentBean> list, String editKeyword){
		this.context = context;
		this.list = list;
		this.editKeyword = editKeyword;
	}
	
	public void setData(List<ContentBean> list){
		this.list = list;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return list==null?0:list.size();
	}

	@Override
	public Object getItem(int position) {

		return list==null?null:list .get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = View.inflate(context, R.layout.prompt_item, null);
		TextView tab = (TextView) convertView.findViewById(R.id.tv_tab);
		tab.setText(Html.fromHtml(DisplayUtils.changeTextColor(list.get(position).getDisplayName(), editKeyword)));
		return convertView;
	}
}