package cn.cibnmp.ott.ui.categoryList;
import cn.cibnmp.ott.ui.home.HomeThreeOtherGridAdapter;

/**
 * 玩票页面的播放器处理点击监听回调
 *
 * @Description 描述：
 * @author zhangxiaoyang create at 2018/1/15
 */
public interface HomePlayOnClickListener {
    /**
     * 点击监听回调
     *
     * @param viewHolder
     * @param itemData
     */
    public void getPlayOnClickListener(HomeThreeOtherGridAdapter.VideoViewHolder viewHolder, int type, Object itemData);
}
