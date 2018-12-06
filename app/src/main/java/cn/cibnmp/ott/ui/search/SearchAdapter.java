package cn.cibnmp.ott.ui.search;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.DragAndDropPermissions;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.utils.DisplayUtils;
import cn.cibnmp.ott.utils.Utils;
import cn.cibnmp.ott.utils.img.ImageFetcher;

public class SearchAdapter extends BaseAdapter {
    private List<ContentBean> SearchList;
    private Context context;
    private String keyword;

    public SearchAdapter(Context context, List<ContentBean> SearchList, String keyword) {
        this.SearchList = SearchList;
        this.context = context;
        this.keyword = keyword;
    }

    @Override
    public int getCount() {
        if (SearchList == null || SearchList.size() == 0) {
            return 0;
        }
        return SearchList.size();
    }

    @Override
    public Object getItem(int position) {
        if (SearchList == null || SearchList.size() == 0) {
            return null;
        }
        return SearchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.search_item, null);
            viewHolder.iv = (ImageView) convertView.findViewById(R.id.iv);
            viewHolder.tvVname = (TextView) convertView.findViewById(R.id.tv_vname);
            viewHolder.year = (TextView) convertView.findViewById(R.id.year);
            viewHolder.dirName = (TextView) convertView.findViewById(R.id.tv_dirname);
            viewHolder.starName = (TextView) convertView.findViewById(R.id.starname);
            viewHolder.type = (TextView) convertView.findViewById(R.id.tv_type);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //显示图片
        if(SearchList.get(position).getImg()== null){
            ImageFetcher.getInstance().loodingImage(SearchList.get(position).getImgh(), viewHolder.iv, R.color.home_bg_color);
        }else {
            ImageFetcher.getInstance().loodingImage(SearchList.get(position).getImg(), viewHolder.iv, R.color.home_bg_color);
        }
        viewHolder.tvVname.setText(Html.fromHtml(DisplayUtils.changeTextColor(SearchList.get(position).getDisplayName(), keyword)));
        viewHolder.year.setText(SearchList.get(position).getYear());

        //TODO 节目显示内容修改
//        viewHolder.type.setText(SearchList.get(position).organ);
//        viewHolder.starName.setText(Utils.replaceStr(SearchList.get(position).star));
//        viewHolder.dirName.setText(Utils.replaceStr(SearchList.get(position).screenwriter));

        return convertView;
    }

    class ViewHolder {
        ImageView iv;
        TextView tvVname;
        TextView year;
        TextView dirName;
        TextView starName;
        TextView type;
    }

}
