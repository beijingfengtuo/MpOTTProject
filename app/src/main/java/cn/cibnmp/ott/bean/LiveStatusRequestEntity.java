package cn.cibnmp.ott.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 转播页面 - 请求节目直播状态参数类
 *
 * @Description 描述：
 * @author zhangxiaoyang create at 2018/2/1
 */
public class LiveStatusRequestEntity implements Serializable {

    /**
     * epgId : 111
     * liveParam : [{"contentId":"111","childId":"111"},{"contentId":"222","childId":"222"}]
     */

    private String epgId;
    private List<LiveParamBean> liveParam;

    public String getEpgId() {
        return epgId;
    }

    public void setEpgId(String epgId) {
        this.epgId = epgId;
    }

    public List<LiveParamBean> getLiveParam() {
        return liveParam;
    }

    public void setLiveParam(List<LiveParamBean> liveParam) {
        this.liveParam = liveParam;
    }

    public static class LiveParamBean implements Serializable {
        /**
         * contentId : 111
         * childId : 111
         */

        private String contentId;
        private String childId;

        public LiveParamBean() {

        }

        public LiveParamBean(String contentId, String childId) {
            this.contentId = contentId;
            this.childId = childId;
        }

        public String getContentId() {
            return contentId;
        }

        public void setContentId(String contentId) {
            this.contentId = contentId;
        }

        public String getChildId() {
            return childId;
        }

        public void setChildId(String childId) {
            this.childId = childId;
        }
    }
}
