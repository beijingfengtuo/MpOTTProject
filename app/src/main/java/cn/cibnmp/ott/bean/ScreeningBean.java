package cn.cibnmp.ott.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 筛选菜单选项（全部、最新、地区分类列表数据）
 *
 * @Description 描述：使用页面：筛选页面
 * @author zhangxiaoyang create at 2018/1/16
 */
public class ScreeningBean implements Serializable {

    /**
     * code : 1000
     * msg : 返回成功
     * data : [{"id":37,"name":"地区","code":"area","seq":0,"subjectId":997,"pname":"好戏连台","epgId":1031,"content":[{"subjectId":1003,"name":"北京","slogan":"","subjectType":3,"action":"open_normal_list_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","imgUrl":"","bgImgUrl":"","pysx":"","pyqp":"","isNameShow":0,"priceType":0,"vipType":0,"isSearchShow":0,"isScreenShow":0,"seq":1,"epgId":1031,"psubjectId":997,"pname":"好戏连台","isSearchSubject":0,"screenKey":"997_area_北京","isDefaultShow":1,"isDefaultFocus":0,"isMustShow":0,"isSolidShow":0,"imgDirection":0,"columnType":0},{"subjectId":1005,"name":"天津","slogan":"","subjectType":3,"action":"open_normal_list_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","imgUrl":"","bgImgUrl":"","pysx":"","pyqp":"","isNameShow":0,"priceType":0,"vipType":0,"isSearchShow":0,"isScreenShow":0,"seq":2,"epgId":1031,"psubjectId":997,"pname":"好戏连台","isSearchSubject":0,"screenKey":"997_area_天津","isDefaultShow":1,"isDefaultFocus":0,"isMustShow":0,"isSolidShow":0,"imgDirection":0,"columnType":0},{"subjectId":1004,"name":"上海","slogan":"","subjectType":3,"action":"open_normal_list_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","imgUrl":"","bgImgUrl":"","pysx":"","pyqp":"","isNameShow":0,"priceType":0,"vipType":0,"isSearchShow":0,"isScreenShow":0,"seq":3,"epgId":1031,"psubjectId":997,"pname":"好戏连台","isSearchSubject":0,"screenKey":"997_area_上海","isDefaultShow":1,"isDefaultFocus":0,"isMustShow":0,"isSolidShow":0,"imgDirection":0,"columnType":0},{"subjectId":1022,"name":"张家口东路二人台","slogan":"","subjectType":3,"action":"open_normal_list_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","imgUrl":"","bgImgUrl":"","pysx":"","pyqp":"","isNameShow":0,"priceType":0,"vipType":0,"isSearchShow":0,"isScreenShow":0,"seq":7,"epgId":1031,"psubjectId":997,"pname":"好戏连台","isSearchSubject":0,"screenKey":"997_area_张家口东路二人台","isDefaultShow":1,"isDefaultFocus":0,"isMustShow":0,"isSolidShow":0,"imgDirection":0,"columnType":0},{"subjectId":1024,"name":"武安平调","slogan":"","subjectType":3,"action":"open_normal_list_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","imgUrl":"","bgImgUrl":"","pysx":"","pyqp":"","isNameShow":0,"priceType":0,"vipType":0,"isSearchShow":0,"isScreenShow":0,"seq":9,"epgId":1031,"psubjectId":997,"pname":"好戏连台","isSearchSubject":0,"screenKey":"997_area_武安平调","isDefaultShow":1,"isDefaultFocus":0,"isMustShow":0,"isSolidShow":0,"imgDirection":0,"columnType":0}]},{"id":39,"name":"类型","code":"category","seq":0,"subjectId":997,"pname":"好戏连台","epgId":1031,"content":[{"subjectId":1014,"name":"京剧","slogan":"","subjectType":3,"action":"open_normal_list_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","imgUrl":"","bgImgUrl":"","pysx":"","pyqp":"","isNameShow":0,"priceType":0,"vipType":0,"isSearchShow":0,"isScreenShow":0,"seq":1,"epgId":1031,"psubjectId":997,"pname":"好戏连台","isSearchSubject":0,"screenKey":"997_category_京剧","isDefaultShow":1,"isDefaultFocus":0,"isMustShow":0,"isSolidShow":0,"imgDirection":0,"columnType":0},{"subjectId":1015,"name":"越剧","slogan":"","subjectType":3,"action":"open_normal_list_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","imgUrl":"","bgImgUrl":"","pysx":"","pyqp":"","isNameShow":0,"priceType":0,"vipType":0,"isSearchShow":0,"isScreenShow":0,"seq":2,"epgId":1031,"psubjectId":997,"pname":"好戏连台","isSearchSubject":0,"screenKey":"997_category_越剧","isDefaultShow":1,"isDefaultFocus":0,"isMustShow":0,"isSolidShow":0,"imgDirection":0,"columnType":0},{"subjectId":1016,"name":"黄梅戏","slogan":"","subjectType":3,"action":"open_normal_list_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","imgUrl":"","bgImgUrl":"","pysx":"","pyqp":"","isNameShow":0,"priceType":0,"vipType":0,"isSearchShow":0,"isScreenShow":0,"seq":3,"epgId":1031,"psubjectId":997,"pname":"好戏连台","isSearchSubject":0,"screenKey":"997_category_黄梅戏","isDefaultShow":1,"isDefaultFocus":0,"isMustShow":0,"isSolidShow":0,"imgDirection":0,"columnType":0},{"subjectId":1017,"name":"评剧","slogan":"","subjectType":3,"action":"open_normal_list_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","imgUrl":"","bgImgUrl":"","pysx":"","pyqp":"","isNameShow":0,"priceType":0,"vipType":0,"isSearchShow":0,"isScreenShow":0,"seq":4,"epgId":1031,"psubjectId":997,"pname":"好戏连台","isSearchSubject":0,"screenKey":"997_category_评剧","isDefaultShow":1,"isDefaultFocus":0,"isMustShow":0,"isSolidShow":0,"imgDirection":0,"columnType":0},{"subjectId":1018,"name":"河北梆子","slogan":"","subjectType":3,"action":"open_normal_list_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","imgUrl":"","bgImgUrl":"","pysx":"","pyqp":"","isNameShow":0,"priceType":0,"vipType":0,"isSearchShow":0,"isScreenShow":0,"seq":5,"epgId":1031,"psubjectId":997,"pname":"好戏连台","isSearchSubject":0,"screenKey":"997_category_河北梆子","isDefaultShow":1,"isDefaultFocus":0,"isMustShow":0,"isSolidShow":0,"imgDirection":0,"columnType":0},{"subjectId":1021,"name":"保定老调","slogan":"","subjectType":3,"action":"open_normal_list_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","imgUrl":"","bgImgUrl":"","pysx":"","pyqp":"","isNameShow":0,"priceType":0,"vipType":0,"isSearchShow":0,"isScreenShow":0,"seq":6,"epgId":1031,"psubjectId":997,"pname":"好戏连台","isSearchSubject":0,"screenKey":"997_category_保定老调","isDefaultShow":1,"isDefaultFocus":0,"isMustShow":0,"isSolidShow":0,"imgDirection":0,"columnType":0},{"subjectId":1023,"name":"哈哈腔","slogan":"","subjectType":3,"action":"open_normal_list_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","imgUrl":"","bgImgUrl":"","pysx":"","pyqp":"","isNameShow":0,"priceType":0,"vipType":0,"isSearchShow":0,"isScreenShow":0,"seq":8,"epgId":1031,"psubjectId":997,"pname":"好戏连台","isSearchSubject":0,"screenKey":"997_category_哈哈腔","isDefaultShow":1,"isDefaultFocus":0,"isMustShow":0,"isSolidShow":0,"imgDirection":0,"columnType":0},{"subjectId":1026,"name":"唐剧","slogan":"","subjectType":3,"action":"open_normal_list_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","imgUrl":"","bgImgUrl":"","pysx":"","pyqp":"","isNameShow":0,"priceType":0,"vipType":0,"isSearchShow":0,"isScreenShow":0,"seq":10,"epgId":1031,"psubjectId":997,"pname":"好戏连台","isSearchSubject":0,"screenKey":"997_category_唐剧","isDefaultShow":1,"isDefaultFocus":0,"isMustShow":0,"isSolidShow":0,"imgDirection":0,"columnType":0},{"subjectId":1027,"name":"剧院","slogan":"","subjectType":3,"action":"open_normal_list_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","imgUrl":"","bgImgUrl":"","pysx":"","pyqp":"","isNameShow":0,"priceType":0,"vipType":0,"isSearchShow":0,"isScreenShow":0,"seq":11,"epgId":1031,"psubjectId":997,"pname":"好戏连台","isSearchSubject":0,"screenKey":"997_category_剧院","isDefaultShow":1,"isDefaultFocus":0,"isMustShow":0,"isSolidShow":0,"imgDirection":0,"columnType":0}]},{"id":40,"name":"年份","code":"year","seq":0,"subjectId":997,"pname":"好戏连台","epgId":1031,"content":[{"subjectId":1020,"name":"2018","slogan":"","subjectType":3,"action":"open_normal_list_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","imgUrl":"","bgImgUrl":"","pysx":"","pyqp":"","isNameShow":0,"priceType":0,"vipType":0,"isSearchShow":0,"isScreenShow":0,"seq":1,"epgId":1031,"psubjectId":997,"pname":"好戏连台","isSearchSubject":0,"screenKey":"997_year_2018","isDefaultShow":1,"isDefaultFocus":0,"isMustShow":0,"isSolidShow":0,"imgDirection":0,"columnType":0},{"subjectId":1019,"name":"2017","slogan":"","subjectType":3,"action":"open_normal_list_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","imgUrl":"","bgImgUrl":"","pysx":"","pyqp":"","isNameShow":0,"priceType":0,"vipType":0,"isSearchShow":0,"isScreenShow":0,"seq":2,"epgId":1031,"psubjectId":997,"pname":"好戏连台","isSearchSubject":0,"screenKey":"997_year_2017","isDefaultShow":1,"isDefaultFocus":0,"isMustShow":0,"isSolidShow":0,"imgDirection":0,"columnType":0},{"subjectId":1028,"name":"2016","slogan":"","subjectType":3,"action":"open_normal_list_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","imgUrl":"","bgImgUrl":"","pysx":"","pyqp":"","isNameShow":0,"priceType":0,"vipType":0,"isSearchShow":0,"isScreenShow":0,"seq":3,"epgId":1031,"psubjectId":997,"pname":"好戏连台","isSearchSubject":0,"screenKey":"997_year_2016","isDefaultShow":1,"isDefaultFocus":0,"isMustShow":0,"isSolidShow":0,"imgDirection":0,"columnType":0}]}]
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

    /**
     * 筛选菜单选项 - 菜单分类列表数据（全部、最新、地区分类）
     */
    public static class DataBean implements Serializable{
        /**
         * id : 37
         * name : 地区
         * code : area
         * seq : 0
         * subjectId : 997
         * pname : 好戏连台
         * epgId : 1031
         * content : [{"subjectId":1003,"name":"北京","slogan":"","subjectType":3,"action":"open_normal_list_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","imgUrl":"","bgImgUrl":"","pysx":"","pyqp":"","isNameShow":0,"priceType":0,"vipType":0,"isSearchShow":0,"isScreenShow":0,"seq":1,"epgId":1031,"psubjectId":997,"pname":"好戏连台","isSearchSubject":0,"screenKey":"997_area_北京","isDefaultShow":1,"isDefaultFocus":0,"isMustShow":0,"isSolidShow":0,"imgDirection":0,"columnType":0},{"subjectId":1005,"name":"天津","slogan":"","subjectType":3,"action":"open_normal_list_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","imgUrl":"","bgImgUrl":"","pysx":"","pyqp":"","isNameShow":0,"priceType":0,"vipType":0,"isSearchShow":0,"isScreenShow":0,"seq":2,"epgId":1031,"psubjectId":997,"pname":"好戏连台","isSearchSubject":0,"screenKey":"997_area_天津","isDefaultShow":1,"isDefaultFocus":0,"isMustShow":0,"isSolidShow":0,"imgDirection":0,"columnType":0},{"subjectId":1004,"name":"上海","slogan":"","subjectType":3,"action":"open_normal_list_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","imgUrl":"","bgImgUrl":"","pysx":"","pyqp":"","isNameShow":0,"priceType":0,"vipType":0,"isSearchShow":0,"isScreenShow":0,"seq":3,"epgId":1031,"psubjectId":997,"pname":"好戏连台","isSearchSubject":0,"screenKey":"997_area_上海","isDefaultShow":1,"isDefaultFocus":0,"isMustShow":0,"isSolidShow":0,"imgDirection":0,"columnType":0},{"subjectId":1022,"name":"张家口东路二人台","slogan":"","subjectType":3,"action":"open_normal_list_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","imgUrl":"","bgImgUrl":"","pysx":"","pyqp":"","isNameShow":0,"priceType":0,"vipType":0,"isSearchShow":0,"isScreenShow":0,"seq":7,"epgId":1031,"psubjectId":997,"pname":"好戏连台","isSearchSubject":0,"screenKey":"997_area_张家口东路二人台","isDefaultShow":1,"isDefaultFocus":0,"isMustShow":0,"isSolidShow":0,"imgDirection":0,"columnType":0},{"subjectId":1024,"name":"武安平调","slogan":"","subjectType":3,"action":"open_normal_list_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","imgUrl":"","bgImgUrl":"","pysx":"","pyqp":"","isNameShow":0,"priceType":0,"vipType":0,"isSearchShow":0,"isScreenShow":0,"seq":9,"epgId":1031,"psubjectId":997,"pname":"好戏连台","isSearchSubject":0,"screenKey":"997_area_武安平调","isDefaultShow":1,"isDefaultFocus":0,"isMustShow":0,"isSolidShow":0,"imgDirection":0,"columnType":0}]
         */

        private int id;
        private String name;
        private String code;
        private int seq;
        private int subjectId;
        private String pname;
        private int epgId;
        private List<ContentBean> content;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getSeq() {
            return seq;
        }

        public void setSeq(int seq) {
            this.seq = seq;
        }

        public int getSubjectId() {
            return subjectId;
        }

        public void setSubjectId(int subjectId) {
            this.subjectId = subjectId;
        }

        public String getPname() {
            return pname;
        }

        public void setPname(String pname) {
            this.pname = pname;
        }

        public int getEpgId() {
            return epgId;
        }

        public void setEpgId(int epgId) {
            this.epgId = epgId;
        }

        public List<ContentBean> getContent() {
            return content;
        }

        public void setContent(List<ContentBean> content) {
            this.content = content;
        }

        /**
         * 筛选菜单选项 - 菜单分类列表中的数据集合数据（全部、最新、地区分类列表中的数据）
         */
        public static class ContentBean implements Serializable{
            /**
             * subjectId : 1003
             * name : 北京
             * slogan :
             * subjectType : 3
             * action : open_normal_list_page
             * actionUrl :
             * actionParams : {'p1':'','p2':'','p3':''}
             * imgUrl :
             * bgImgUrl :
             * pysx :
             * pyqp :
             * isNameShow : 0
             * priceType : 0
             * vipType : 0
             * isSearchShow : 0
             * isScreenShow : 0
             * seq : 1
             * epgId : 1031
             * psubjectId : 997
             * pname : 好戏连台
             * isSearchSubject : 0
             * screenKey : 997_area_北京
             * isDefaultShow : 1
             * isDefaultFocus : 0
             * isMustShow : 0
             * isSolidShow : 0
             * imgDirection : 0
             * columnType : 0
             */
            private int subjectId;
            private String name;
            private String slogan;
            private int subjectType;
            private String action;
            private String actionUrl;
            private String actionParams;
            private String imgUrl;
            private String bgImgUrl;
            private String pysx;
            private String pyqp;
            private int isNameShow;
            private int priceType;
            private int vipType;
            private int isSearchShow;
            private int isScreenShow;
            private int seq;
            private int epgId;
            private int psubjectId;
            private String pname;
            private int isSearchSubject;
            private String screenKey;
            private int isDefaultShow;
            private int isDefaultFocus;
            private int isMustShow;
            private int isSolidShow;
            private int imgDirection;
            private int columnType;

            //TODO zxy 筛选页面 筛选项是否选中
            private boolean ischecked;

            public boolean ischecked() {
                return ischecked;
            }

            public void setIschecked(boolean ischecked) {
                this.ischecked = ischecked;
            }

            public ContentBean() {

            }

            /**
             * TODO 构造方法
             *
             * @param epgId
             * @param subjectId
             * @param name
             * @param screenKey 关键词
             */
            public ContentBean (int epgId, int subjectId, String name, String screenKey) {
                this.epgId = epgId;
                this.subjectId = subjectId;
                this.name = name;
                this.screenKey = screenKey;
            }

            public int getSubjectId() {
                return subjectId;
            }

            public void setSubjectId(int subjectId) {
                this.subjectId = subjectId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSlogan() {
                return slogan;
            }

            public void setSlogan(String slogan) {
                this.slogan = slogan;
            }

            public int getSubjectType() {
                return subjectType;
            }

            public void setSubjectType(int subjectType) {
                this.subjectType = subjectType;
            }

            public String getAction() {
                return action;
            }

            public void setAction(String action) {
                this.action = action;
            }

            public String getActionUrl() {
                return actionUrl;
            }

            public void setActionUrl(String actionUrl) {
                this.actionUrl = actionUrl;
            }

            public String getActionParams() {
                return actionParams;
            }

            public void setActionParams(String actionParams) {
                this.actionParams = actionParams;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public String getBgImgUrl() {
                return bgImgUrl;
            }

            public void setBgImgUrl(String bgImgUrl) {
                this.bgImgUrl = bgImgUrl;
            }

            public String getPysx() {
                return pysx;
            }

            public void setPysx(String pysx) {
                this.pysx = pysx;
            }

            public String getPyqp() {
                return pyqp;
            }

            public void setPyqp(String pyqp) {
                this.pyqp = pyqp;
            }

            public int getIsNameShow() {
                return isNameShow;
            }

            public void setIsNameShow(int isNameShow) {
                this.isNameShow = isNameShow;
            }

            public int getPriceType() {
                return priceType;
            }

            public void setPriceType(int priceType) {
                this.priceType = priceType;
            }

            public int getVipType() {
                return vipType;
            }

            public void setVipType(int vipType) {
                this.vipType = vipType;
            }

            public int getIsSearchShow() {
                return isSearchShow;
            }

            public void setIsSearchShow(int isSearchShow) {
                this.isSearchShow = isSearchShow;
            }

            public int getIsScreenShow() {
                return isScreenShow;
            }

            public void setIsScreenShow(int isScreenShow) {
                this.isScreenShow = isScreenShow;
            }

            public int getSeq() {
                return seq;
            }

            public void setSeq(int seq) {
                this.seq = seq;
            }

            public int getEpgId() {
                return epgId;
            }

            public void setEpgId(int epgId) {
                this.epgId = epgId;
            }

            public int getPsubjectId() {
                return psubjectId;
            }

            public void setPsubjectId(int psubjectId) {
                this.psubjectId = psubjectId;
            }

            public String getPname() {
                return pname;
            }

            public void setPname(String pname) {
                this.pname = pname;
            }

            public int getIsSearchSubject() {
                return isSearchSubject;
            }

            public void setIsSearchSubject(int isSearchSubject) {
                this.isSearchSubject = isSearchSubject;
            }

            public String getScreenKey() {
                return screenKey;
            }

            public void setScreenKey(String screenKey) {
                this.screenKey = screenKey;
            }

            public int getIsDefaultShow() {
                return isDefaultShow;
            }

            public void setIsDefaultShow(int isDefaultShow) {
                this.isDefaultShow = isDefaultShow;
            }

            public int getIsDefaultFocus() {
                return isDefaultFocus;
            }

            public void setIsDefaultFocus(int isDefaultFocus) {
                this.isDefaultFocus = isDefaultFocus;
            }

            public int getIsMustShow() {
                return isMustShow;
            }

            public void setIsMustShow(int isMustShow) {
                this.isMustShow = isMustShow;
            }

            public int getIsSolidShow() {
                return isSolidShow;
            }

            public void setIsSolidShow(int isSolidShow) {
                this.isSolidShow = isSolidShow;
            }

            public int getImgDirection() {
                return imgDirection;
            }

            public void setImgDirection(int imgDirection) {
                this.imgDirection = imgDirection;
            }

            public int getColumnType() {
                return columnType;
            }

            public void setColumnType(int columnType) {
                this.columnType = columnType;
            }
        }
    }
}
