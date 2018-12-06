package cn.cibnmp.ott.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.bean.HomeMenuItemBean;
import cn.cibnmp.ott.utils.img.ImageFetcher;

public class HomeTwoMoveGridViewAdapter extends BaseAdapter {
	private Context context;
	private List<HomeMenuItemBean> menuList;
	private int hidePosition = AdapterView.INVALID_POSITION;
	private int listSize;

	public HomeTwoMoveGridViewAdapter(Context context, List<HomeMenuItemBean> menuList) {
		this.context = context;
		this.menuList = menuList;
		dealData();
	}
	
	public void dealData() {
		if (menuList ==null || menuList.size()==0) {
			return ;
		}
		listSize = menuList.size();
		if (menuList.size()%3!=0) {
			for (int i=0;i<menuList.size()%3;i++) {
				menuList.add(new HomeMenuItemBean());
			}
		}
	}

	@Override
	public int getCount() {
		if (menuList ==null || menuList.size()==0) {
			return 0;
		}
		return menuList.size();
	}

	@Override
	public Object getItem(int position) {
		if (menuList ==null || menuList.size()==0) {
			return null;
		}
		return menuList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolderMove viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.move_gridview_item, parent, false);
			viewHolder = new ViewHolderMove();
			viewHolder.move_rl = (RelativeLayout) convertView.findViewById(R.id.move_rl);
			viewHolder.move_item_rl= (RelativeLayout) convertView.findViewById(R.id.move_item_rl);
			viewHolder.move_img = (ImageView) convertView.findViewById(R.id.move_img);
			viewHolder.move_text = (TextView) convertView.findViewById(R.id.move_text);
			viewHolder.move_new = (ImageView) convertView.findViewById(R.id.move_new);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolderMove) convertView.getTag();
		}
		
		if (position < listSize) {
			ImageFetcher.getInstance().loadImage(menuList.get(position).getPicFID(),viewHolder.move_img , R.mipmap.channels_defult_pic);
			viewHolder.move_text.setText(menuList.get(position).getName());
		} else {
			viewHolder.move_text.setText("");
			viewHolder.move_img.setBackgroundResource(0);
		}
		
//		if(menuList.get(position).isSetNew()){
//			viewHolder.move_new.setBackgroundResource(R.drawable.onew);
//		}else{
//			viewHolder.move_new.setBackgroundResource(R.color.transparent);
//		}
		// hide时隐藏View
		if (position != hidePosition) {
			viewHolder.move_item_rl.setVisibility(View.VISIBLE);
		} else {
			viewHolder.move_item_rl.setVisibility(View.INVISIBLE);
		}
		viewHolder.move_rl.setId(position);
		return viewHolder.move_rl;
	}

	public void hideView(int pos) {
		hidePosition = pos;
		notifyDataSetChanged();
	}

	public void showHideView() {
		hidePosition = AdapterView.INVALID_POSITION;
		notifyDataSetChanged();
	}

	public void removeView(int pos) {
		menuList.remove(pos);
		notifyDataSetChanged();
	}

	// 更新拖动时的gridView
	public void swapView(int draggedPos, int destPos) {
		// 从前向后拖动，其他item依次前移
		if (draggedPos < destPos) {
			menuList.add(destPos + 1, (HomeMenuItemBean) getItem(draggedPos));
			menuList.remove(draggedPos);
		}
		// 从后向前拖动，其他item依次后移
		else if (draggedPos > destPos) {
			menuList.add(destPos, (HomeMenuItemBean) getItem(draggedPos));
			menuList.remove(draggedPos + 1);
		}
		hidePosition = destPos;
		notifyDataSetChanged();
	}
	
	class ViewHolderMove{ 
		RelativeLayout move_rl;
		RelativeLayout move_item_rl;
		ImageView move_img;
		TextView move_text;
		ImageView move_new;
	}
}