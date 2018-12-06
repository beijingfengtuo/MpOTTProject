package cn.cibnmp.ott.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yangwenwu on 18/1/25.
 */

public class WelcomeBean implements Serializable{

    /**
     * code : 200
     * msg : success
     * data : [{"mediatype":"image","fid":"7e56182ac8fcd7faa117643473bd1dbe","url":"http://114.247.94.87:8080/view?fid=7e56182ac8fcd7faa117643473bd1dbe","href":"","duration":5}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

    @Override
    public String toString() {
        return "WelcomeBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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

    public static class DataBean {
        /**
         * mediatype : image
         * fid : 7e56182ac8fcd7faa117643473bd1dbe
         * url : http://114.247.94.87:8080/view?fid=7e56182ac8fcd7faa117643473bd1dbe
         * href :
         * duration : 5
         */

        private String mediatype;
        private String fid;
        private String url;
        private String href;
        private int duration;

        @Override
        public String toString() {
            return "DataBean{" +
                    "mediatype='" + mediatype + '\'' +
                    ", fid='" + fid + '\'' +
                    ", url='" + url + '\'' +
                    ", href='" + href + '\'' +
                    ", duration=" + duration +
                    '}';
        }

        public String getMediatype() {
            return mediatype;
        }

        public void setMediatype(String mediatype) {
            this.mediatype = mediatype;
        }

        public String getFid() {
            return fid;
        }

        public void setFid(String fid) {
            this.fid = fid;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }
    }
}
