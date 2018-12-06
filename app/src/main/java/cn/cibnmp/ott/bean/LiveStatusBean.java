package cn.cibnmp.ott.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 转播页面获取节目直播状态
 *
 * @Description 描述：
 * @author zhangxiaoyang create at 2018/2/5
 */
public class LiveStatusBean implements Serializable {

    /**
     * code : 1000
     * data : [{"endDate":1527601247000,"liveId":"40","liveName":"越剧《西园记》","sid":"58","startDate":1496056526000,"status":1},{"endDate":1525529400000,"liveId":"37","liveName":"越剧《孔雀东南飞》","sid":"46","startDate":1494846900000,"status":1}]
     * msg : 返回成功
     */

    private String code;
    private String msg;
    private List<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LiveStatusBean{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public static class DataBean implements Serializable{
        /**
         * endDate : 1527601247000
         * liveId : 40
         * liveName : 越剧《西园记》
         * sid : 58
         * startDate : 1496056526000
         * status : 1
         */

        private long endDate;
        private String liveId;
        private String liveName;
        private String sid;
        private long startDate;
        private int status;

        public long getEndDate() {
            return endDate;
        }

        public void setEndDate(long endDate) {
            this.endDate = endDate;
        }

        public String getLiveId() {
            return liveId;
        }

        public void setLiveId(String liveId) {
            this.liveId = liveId;
        }

        public String getLiveName() {
            return liveName;
        }

        public void setLiveName(String liveName) {
            this.liveName = liveName;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public long getStartDate() {
            return startDate;
        }

        public void setStartDate(long startDate) {
            this.startDate = startDate;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "endDate=" + endDate +
                    ", liveId='" + liveId + '\'' +
                    ", liveName='" + liveName + '\'' +
                    ", sid='" + sid + '\'' +
                    ", startDate=" + startDate +
                    ", status=" + status +
                    '}';
        }
    }
}
