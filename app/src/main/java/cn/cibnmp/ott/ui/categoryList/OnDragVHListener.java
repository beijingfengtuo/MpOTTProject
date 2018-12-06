package cn.cibnmp.ott.ui.categoryList;

/**
 * ViewHolder 被选中 以及 拖拽释放 触发监听器
 * Created by yangwenwu on 17/12/29.
 */
public interface OnDragVHListener {
    /**
     * Item被选中时触发
     */
    void onItemSelected();


    /**
     * Item在拖拽结束/滑动结束后触发
     */
    void onItemFinish();
}
