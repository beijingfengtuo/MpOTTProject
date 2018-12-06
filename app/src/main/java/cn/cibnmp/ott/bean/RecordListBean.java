package cn.cibnmp.ott.bean;

import java.io.Serializable;
import java.util.List;

import cn.cibnmp.ott.ui.detail.bean.*;
import cn.cibnmp.ott.ui.detail.bean.RecordBean;

/**
 * 收藏列表bean
 * <p>
 * Created by cibn-lyc on 2018/1/16.
 */

public class RecordListBean extends ListBean implements Serializable {

    private String LocalFlag;
    private String TotalNum;
    private String Vid;
    private String StartIndex;
    private List<cn.cibnmp.ott.ui.detail.bean.RecordBean> VideoCollectList;

    public String getLocalFlag() {
        return LocalFlag;
    }

    public void setLocalFlag(String localFlag) {
        LocalFlag = localFlag;
    }

    public String getTotalNum() {
        return TotalNum;
    }

    public void setTotalNum(String totalNum) {
        TotalNum = totalNum;
    }

    public String getVid() {
        return Vid;
    }

    public void setVid(String vid) {
        Vid = vid;
    }

    public String getStartIndex() {
        return StartIndex;
    }

    public void setStartIndex(String startIndex) {
        StartIndex = startIndex;
    }

    public List<RecordBean> getVideoCollectList() {
        return VideoCollectList;
    }

    public void setVideoCollectList(List<RecordBean> videoCollectList) {
        VideoCollectList = videoCollectList;
    }
}
