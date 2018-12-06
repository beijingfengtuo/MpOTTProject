package cn.cibnmp.ott.ui.search;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.cibnhp.grand.R;

/**
 * 所有tab标签的adapter
 * @author chaoshaui
 *
 */
public class SearchRecordAdapter extends BaseAdapter {
	private Context context;
	private List<RecordContent> RecordList;
	
	public SearchRecordAdapter(Context context, List<RecordContent> RecordList){
		this.context = context;
		this.RecordList = RecordList;
	}

	// 清楚历史操作
	public void setData(List<RecordContent> RecordList){
		this.RecordList = RecordList;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		if (RecordList==null ||RecordList.size() == 0) {
			return 0;
		}
		return RecordList.size();
	}

	@Override
	public Object getItem(int position) {
		if (RecordList==null ||RecordList.size() == 0) {
			return null;
		}
		return RecordList.get(position).keyword;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = View.inflate(context, R.layout.all_tab_item, null);
		TextView tab = (TextView) convertView.findViewById(R.id.tv_tab);
		tab.setText(RecordList.get(position).getKeyWord());
		return convertView;
	}
}