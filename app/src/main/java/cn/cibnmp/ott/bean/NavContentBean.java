package cn.cibnmp.ott.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wanqi on 2017/8/15.
 */

public class NavContentBean implements Serializable {

    private List<NavigationInfoItemBean> indexContents;

    public List<NavigationInfoItemBean> getIndexContents() {
        return indexContents;
    }

    public void setIndexContents(List<NavigationInfoItemBean> indexContents) {
        this.indexContents = indexContents;
    }
}
