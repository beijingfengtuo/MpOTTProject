package cn.cibnmp.ott.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 筛选搜索数据
 *
 * @Description 描述：使用页面：筛选页面
 * @author zhangxiaoyang create at 2018/1/16
 */
public class ScreeningDataBean implements Serializable {

    /**
     * code : 1000
     * msg : 返回成功
     * data : {"subjectId":999,"name":"好戏连台栏目","epgId":1031,"layout":{"layoutId":null,"name":null,"layoutType":null,"layoutJson":null,"description":null,"state":null},"blocks":[],"content":[],"newblocks":[],"newcontent":[],"listcontent":{"total":12,"pageSize":0,"pageNum":100,"content":[{"displayName":"跟随绍百下乡去（三）","slogan":"东方大剧院跟随绍百下乡演出的步伐，为大家带来别开生面的幕后直播。我们将独家采访吴凤花、吴素英、陈飞、张琳等绍百当家台柱，听她们讲述下乡经历与趣事；看他们在后台的点点滴滴以及如何化妆扮戏和演出等。也将带来难得一见的民间戏曲仪式\u201c扫台\u201d。跟随我们的镜头一起观看独家直播吧！","scrollMarquee":"","playTime":"2018-01-08 12:19:43","img":"http://114.247.94.68:8181/view/e3b05f7e7b2f0ec156771b7b59013659-260-346","imgh":"","viewtypecode":null,"action":"open_normal_detail_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"269459","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2018","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":""},{"displayName":"王志萍、黄慧《甄嬛》（下本）选段","slogan":"王志萍、黄慧上越《甄嬛》（下本）选段","scrollMarquee":"","playTime":"2018-01-02 17:06:35","img":"","imgh":"http://114.247.94.68:8181/view/683f072a0c6809e5dd1b0dceff9b0e25-454-256","viewtypecode":null,"action":"open_normal_detail_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"196838","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2018","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":"0"},{"displayName":"京剧大戏《十年之约之龙凤呈祥》","slogan":"京剧《龙凤呈祥》是出自《三国演义》中的重要选段，讲述的是孙权因刘备占据荆州不还，与周瑜设美人计，假称孙权之妹孙尚香许婚刘备，以换荆州的故事。在京剧界被誉之为\u201c吉祥戏\u201d，是人们在逢年过节搭台唱戏必点的合家欢式的经典传统剧目。角色众多展示每行当精华；三国多表男人戏，唯此剧彰显巾帼英气；化戾气为祥和的智慧胸襟符合中国人的观念；三国戏偏好杀伐争斗，难得这样暖心之作；无怪乎各院团年节都要尽遣主力\"龙凤呈祥\"。","scrollMarquee":"","playTime":"2018-01-02 16:04:18","img":"","imgh":"http://114.247.94.68:8181/view/2a11f4fef1a58230efc70504d5a73e3b-454-256","viewtypecode":null,"action":"open_normal_detail_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"194580","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2017","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":"0"},{"displayName":"章海灵《韩非子》选段","slogan":"","scrollMarquee":"","playTime":"2018-01-02 16:04:31","img":"","imgh":"http://114.247.94.68:8181/view/3a58d6e37500812fc08b4581f5d36139-454-256","viewtypecode":null,"action":"open_normal_detail_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"196845","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2017","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":"0"},{"displayName":"《三看御妹》不得不看的越剧欢喜姻缘","slogan":"","scrollMarquee":"","playTime":"2018-01-02 17:06:04","img":"","imgh":"http://114.247.94.68:8181/view/557f688ca59569e266154fd4b52debe2-454-256","viewtypecode":null,"action":"open_normal_detail_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"197796","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2017","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":""},{"displayName":"越剧《三看御妹》","slogan":"","scrollMarquee":"","playTime":"2018-01-02 17:06:08","img":"","imgh":"http://114.247.94.68:8181/view/f53ae7cd323eb67614e20ad663c0cf2d-454-256","viewtypecode":null,"action":"open_normal_detail_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"197693","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2017","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":"0"},{"displayName":"王清《甄嬛》选段","slogan":"","scrollMarquee":"","playTime":"2018-01-02 17:06:32","img":"","imgh":"http://114.247.94.68:8181/view/ce402abb150dd61d77a2a7eabe02943b-454-256","viewtypecode":null,"action":"open_normal_detail_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"196843","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2017","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":"0"},{"displayName":"吴凤花：我愿为越剧放弃容颜","slogan":"","scrollMarquee":"","playTime":"2018-01-02 16:09:17","img":"","imgh":"http://114.247.94.68:8181/view/12b338a061ff1b20e494fc70f6a46605-454-256","viewtypecode":null,"action":"open_normal_detail_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"197793","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2017","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":"0"},{"displayName":"吴素英：舞台上的俏佳人，生活中的女汉子","slogan":"","scrollMarquee":"","playTime":"2018-01-02 16:09:31","img":"","imgh":"http://114.247.94.68:8181/view/73e65d09a7d8176d5cfea9440f4662e1-454-256","viewtypecode":null,"action":"open_normal_detail_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"197794","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2017","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":"0"},{"displayName":"《迎春∙反串\u2014\u2014越剧经典折子戏》宣传片","slogan":"","scrollMarquee":"","playTime":"2018-01-02 17:06:12","img":"","imgh":"http://114.247.94.68:8181/view/6fe2819d4a4299b5819b546952d04c1b-454-256","viewtypecode":null,"action":"open_normal_detail_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"196833","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2017","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":"0"},{"displayName":"钱惠丽《甄嬛》选段","slogan":"","scrollMarquee":"","playTime":"2018-01-02 17:06:15","img":"","imgh":"http://114.247.94.68:8181/view/fa91d3925eb4d55397c6c5c618ac8eea-454-256","viewtypecode":null,"action":"open_normal_detail_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"196842","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2017","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":""},{"displayName":"枕上无梦","slogan":"万历二十六年，汤显祖向吏部告了长假，回到家乡临川，潜心写戏。\r\n远离仕途，与诗文戏曲为伴，这一生到此便再无波澜。\r\n当真甘心于此？\r\n为何始终烦恼萦怀？为何仍感寂寞气闷？\r\n不再信『紫钗』的浪漫，却放不下『还魂』的执着。\r\n『南柯』一梦依旧彷徨，『邯郸』之梦又去向何方？\r\n戏中人梦里梦外，谁能救赎汤显祖？\r\n这半生该何去何从？","scrollMarquee":"","playTime":"2018-01-02 17:07:13","img":"http://114.247.94.68:8181/view/14796825f37ca101cc4cc2f17a38d0b2-260-346","imgh":"http://114.247.94.68:8181/view/0af9e3ea595d5c2865af53fbc65c9247-454-256","viewtypecode":null,"action":"open_normal_list_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"192565","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2016","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":"7200"}]}}
     */

    private String code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ScreeningDataBean{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public static class DataBean implements Serializable{
        /**
         * subjectId : 999
         * name : 好戏连台栏目
         * epgId : 1031
         * layout : {"layoutId":null,"name":null,"layoutType":null,"layoutJson":null,"description":null,"state":null}
         * blocks : []
         * content : []
         * newblocks : []
         * newcontent : []
         * listcontent : {"total":12,"pageSize":0,"pageNum":100,"content":[{"displayName":"跟随绍百下乡去（三）","slogan":"东方大剧院跟随绍百下乡演出的步伐，为大家带来别开生面的幕后直播。我们将独家采访吴凤花、吴素英、陈飞、张琳等绍百当家台柱，听她们讲述下乡经历与趣事；看他们在后台的点点滴滴以及如何化妆扮戏和演出等。也将带来难得一见的民间戏曲仪式\u201c扫台\u201d。跟随我们的镜头一起观看独家直播吧！","scrollMarquee":"","playTime":"2018-01-08 12:19:43","img":"http://114.247.94.68:8181/view/e3b05f7e7b2f0ec156771b7b59013659-260-346","imgh":"","viewtypecode":null,"action":"open_normal_detail_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"269459","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2018","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":""},{"displayName":"王志萍、黄慧《甄嬛》（下本）选段","slogan":"王志萍、黄慧上越《甄嬛》（下本）选段","scrollMarquee":"","playTime":"2018-01-02 17:06:35","img":"","imgh":"http://114.247.94.68:8181/view/683f072a0c6809e5dd1b0dceff9b0e25-454-256","viewtypecode":null,"action":"open_normal_detail_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"196838","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2018","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":"0"},{"displayName":"京剧大戏《十年之约之龙凤呈祥》","slogan":"京剧《龙凤呈祥》是出自《三国演义》中的重要选段，讲述的是孙权因刘备占据荆州不还，与周瑜设美人计，假称孙权之妹孙尚香许婚刘备，以换荆州的故事。在京剧界被誉之为\u201c吉祥戏\u201d，是人们在逢年过节搭台唱戏必点的合家欢式的经典传统剧目。角色众多展示每行当精华；三国多表男人戏，唯此剧彰显巾帼英气；化戾气为祥和的智慧胸襟符合中国人的观念；三国戏偏好杀伐争斗，难得这样暖心之作；无怪乎各院团年节都要尽遣主力\"龙凤呈祥\"。","scrollMarquee":"","playTime":"2018-01-02 16:04:18","img":"","imgh":"http://114.247.94.68:8181/view/2a11f4fef1a58230efc70504d5a73e3b-454-256","viewtypecode":null,"action":"open_normal_detail_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"194580","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2017","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":"0"},{"displayName":"章海灵《韩非子》选段","slogan":"","scrollMarquee":"","playTime":"2018-01-02 16:04:31","img":"","imgh":"http://114.247.94.68:8181/view/3a58d6e37500812fc08b4581f5d36139-454-256","viewtypecode":null,"action":"open_normal_detail_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"196845","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2017","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":"0"},{"displayName":"《三看御妹》不得不看的越剧欢喜姻缘","slogan":"","scrollMarquee":"","playTime":"2018-01-02 17:06:04","img":"","imgh":"http://114.247.94.68:8181/view/557f688ca59569e266154fd4b52debe2-454-256","viewtypecode":null,"action":"open_normal_detail_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"197796","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2017","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":""},{"displayName":"越剧《三看御妹》","slogan":"","scrollMarquee":"","playTime":"2018-01-02 17:06:08","img":"","imgh":"http://114.247.94.68:8181/view/f53ae7cd323eb67614e20ad663c0cf2d-454-256","viewtypecode":null,"action":"open_normal_detail_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"197693","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2017","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":"0"},{"displayName":"王清《甄嬛》选段","slogan":"","scrollMarquee":"","playTime":"2018-01-02 17:06:32","img":"","imgh":"http://114.247.94.68:8181/view/ce402abb150dd61d77a2a7eabe02943b-454-256","viewtypecode":null,"action":"open_normal_detail_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"196843","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2017","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":"0"},{"displayName":"吴凤花：我愿为越剧放弃容颜","slogan":"","scrollMarquee":"","playTime":"2018-01-02 16:09:17","img":"","imgh":"http://114.247.94.68:8181/view/12b338a061ff1b20e494fc70f6a46605-454-256","viewtypecode":null,"action":"open_normal_detail_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"197793","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2017","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":"0"},{"displayName":"吴素英：舞台上的俏佳人，生活中的女汉子","slogan":"","scrollMarquee":"","playTime":"2018-01-02 16:09:31","img":"","imgh":"http://114.247.94.68:8181/view/73e65d09a7d8176d5cfea9440f4662e1-454-256","viewtypecode":null,"action":"open_normal_detail_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"197794","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2017","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":"0"},{"displayName":"《迎春∙反串\u2014\u2014越剧经典折子戏》宣传片","slogan":"","scrollMarquee":"","playTime":"2018-01-02 17:06:12","img":"","imgh":"http://114.247.94.68:8181/view/6fe2819d4a4299b5819b546952d04c1b-454-256","viewtypecode":null,"action":"open_normal_detail_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"196833","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2017","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":"0"},{"displayName":"钱惠丽《甄嬛》选段","slogan":"","scrollMarquee":"","playTime":"2018-01-02 17:06:15","img":"","imgh":"http://114.247.94.68:8181/view/fa91d3925eb4d55397c6c5c618ac8eea-454-256","viewtypecode":null,"action":"open_normal_detail_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"196842","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2017","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":""},{"displayName":"枕上无梦","slogan":"万历二十六年，汤显祖向吏部告了长假，回到家乡临川，潜心写戏。\r\n远离仕途，与诗文戏曲为伴，这一生到此便再无波澜。\r\n当真甘心于此？\r\n为何始终烦恼萦怀？为何仍感寂寞气闷？\r\n不再信『紫钗』的浪漫，却放不下『还魂』的执着。\r\n『南柯』一梦依旧彷徨，『邯郸』之梦又去向何方？\r\n戏中人梦里梦外，谁能救赎汤显祖？\r\n这半生该何去何从？","scrollMarquee":"","playTime":"2018-01-02 17:07:13","img":"http://114.247.94.68:8181/view/14796825f37ca101cc4cc2f17a38d0b2-260-346","imgh":"http://114.247.94.68:8181/view/0af9e3ea595d5c2865af53fbc65c9247-454-256","viewtypecode":null,"action":"open_normal_list_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"192565","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2016","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":"7200"}]}
         * keywords:997_year_2018
         */

        private int subjectId;
        private String name;
        private int epgId;
        private LayoutBean layout;
        private ListcontentBean listcontent;
        private List<?> blocks;
        private List<?> content;
        private List<?> newblocks;
        private List<?> newcontent;
        private String keywords;

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

        public int getEpgId() {
            return epgId;
        }

        public void setEpgId(int epgId) {
            this.epgId = epgId;
        }

        public LayoutBean getLayout() {
            return layout;
        }

        public void setLayout(LayoutBean layout) {
            this.layout = layout;
        }

        public ListcontentBean getListcontent() {
            return listcontent;
        }

        public void setListcontent(ListcontentBean listcontent) {
            this.listcontent = listcontent;
        }

        public List<?> getBlocks() {
            return blocks;
        }

        public void setBlocks(List<?> blocks) {
            this.blocks = blocks;
        }

        public List<?> getContent() {
            return content;
        }

        public void setContent(List<?> content) {
            this.content = content;
        }

        public List<?> getNewblocks() {
            return newblocks;
        }

        public void setNewblocks(List<?> newblocks) {
            this.newblocks = newblocks;
        }

        public List<?> getNewcontent() {
            return newcontent;
        }

        public void setNewcontent(List<?> newcontent) {
            this.newcontent = newcontent;
        }

        public String getKeywords() {
            return keywords;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "subjectId=" + subjectId +
                    ", name='" + name + '\'' +
                    ", epgId=" + epgId +
                    ", layout=" + layout +
                    ", listcontent=" + listcontent +
                    ", blocks=" + blocks +
                    ", content=" + content +
                    ", newblocks=" + newblocks +
                    ", newcontent=" + newcontent +
                    ", keywords='" + keywords + '\'' +
                    '}';
        }

        public static class LayoutBean implements Serializable{
            /**
             * layoutId : null
             * name : null
             * layoutType : null
             * layoutJson : null
             * description : null
             * state : null
             */

            private Object layoutId;
            private Object name;
            private Object layoutType;
            private Object layoutJson;
            private Object description;
            private Object state;

            public Object getLayoutId() {
                return layoutId;
            }

            public void setLayoutId(Object layoutId) {
                this.layoutId = layoutId;
            }

            public Object getName() {
                return name;
            }

            public void setName(Object name) {
                this.name = name;
            }

            public Object getLayoutType() {
                return layoutType;
            }

            public void setLayoutType(Object layoutType) {
                this.layoutType = layoutType;
            }

            public Object getLayoutJson() {
                return layoutJson;
            }

            public void setLayoutJson(Object layoutJson) {
                this.layoutJson = layoutJson;
            }

            public Object getDescription() {
                return description;
            }

            public void setDescription(Object description) {
                this.description = description;
            }

            public Object getState() {
                return state;
            }

            public void setState(Object state) {
                this.state = state;
            }

            @Override
            public String toString() {
                return "LayoutBean{" +
                        "layoutId=" + layoutId +
                        ", name=" + name +
                        ", layoutType=" + layoutType +
                        ", layoutJson=" + layoutJson +
                        ", description=" + description +
                        ", state=" + state +
                        '}';
            }
        }

        public static class ListcontentBean implements Serializable{
            /**
             * total : 12
             * pageSize : 0
             * pageNum : 100
             * content : [{"displayName":"跟随绍百下乡去（三）","slogan":"东方大剧院跟随绍百下乡演出的步伐，为大家带来别开生面的幕后直播。我们将独家采访吴凤花、吴素英、陈飞、张琳等绍百当家台柱，听她们讲述下乡经历与趣事；看他们在后台的点点滴滴以及如何化妆扮戏和演出等。也将带来难得一见的民间戏曲仪式\u201c扫台\u201d。跟随我们的镜头一起观看独家直播吧！","scrollMarquee":"","playTime":"2018-01-08 12:19:43","img":"http://114.247.94.68:8181/view/e3b05f7e7b2f0ec156771b7b59013659-260-346","imgh":"","viewtypecode":null,"action":"open_normal_detail_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"269459","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2018","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":""},{"displayName":"王志萍、黄慧《甄嬛》（下本）选段","slogan":"王志萍、黄慧上越《甄嬛》（下本）选段","scrollMarquee":"","playTime":"2018-01-02 17:06:35","img":"","imgh":"http://114.247.94.68:8181/view/683f072a0c6809e5dd1b0dceff9b0e25-454-256","viewtypecode":null,"action":"open_normal_detail_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"196838","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2018","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":"0"},{"displayName":"京剧大戏《十年之约之龙凤呈祥》","slogan":"京剧《龙凤呈祥》是出自《三国演义》中的重要选段，讲述的是孙权因刘备占据荆州不还，与周瑜设美人计，假称孙权之妹孙尚香许婚刘备，以换荆州的故事。在京剧界被誉之为\u201c吉祥戏\u201d，是人们在逢年过节搭台唱戏必点的合家欢式的经典传统剧目。角色众多展示每行当精华；三国多表男人戏，唯此剧彰显巾帼英气；化戾气为祥和的智慧胸襟符合中国人的观念；三国戏偏好杀伐争斗，难得这样暖心之作；无怪乎各院团年节都要尽遣主力\"龙凤呈祥\"。","scrollMarquee":"","playTime":"2018-01-02 16:04:18","img":"","imgh":"http://114.247.94.68:8181/view/2a11f4fef1a58230efc70504d5a73e3b-454-256","viewtypecode":null,"action":"open_normal_detail_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"194580","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2017","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":"0"},{"displayName":"章海灵《韩非子》选段","slogan":"","scrollMarquee":"","playTime":"2018-01-02 16:04:31","img":"","imgh":"http://114.247.94.68:8181/view/3a58d6e37500812fc08b4581f5d36139-454-256","viewtypecode":null,"action":"open_normal_detail_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"196845","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2017","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":"0"},{"displayName":"《三看御妹》不得不看的越剧欢喜姻缘","slogan":"","scrollMarquee":"","playTime":"2018-01-02 17:06:04","img":"","imgh":"http://114.247.94.68:8181/view/557f688ca59569e266154fd4b52debe2-454-256","viewtypecode":null,"action":"open_normal_detail_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"197796","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2017","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":""},{"displayName":"越剧《三看御妹》","slogan":"","scrollMarquee":"","playTime":"2018-01-02 17:06:08","img":"","imgh":"http://114.247.94.68:8181/view/f53ae7cd323eb67614e20ad663c0cf2d-454-256","viewtypecode":null,"action":"open_normal_detail_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"197693","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2017","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":"0"},{"displayName":"王清《甄嬛》选段","slogan":"","scrollMarquee":"","playTime":"2018-01-02 17:06:32","img":"","imgh":"http://114.247.94.68:8181/view/ce402abb150dd61d77a2a7eabe02943b-454-256","viewtypecode":null,"action":"open_normal_detail_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"196843","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2017","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":"0"},{"displayName":"吴凤花：我愿为越剧放弃容颜","slogan":"","scrollMarquee":"","playTime":"2018-01-02 16:09:17","img":"","imgh":"http://114.247.94.68:8181/view/12b338a061ff1b20e494fc70f6a46605-454-256","viewtypecode":null,"action":"open_normal_detail_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"197793","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2017","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":"0"},{"displayName":"吴素英：舞台上的俏佳人，生活中的女汉子","slogan":"","scrollMarquee":"","playTime":"2018-01-02 16:09:31","img":"","imgh":"http://114.247.94.68:8181/view/73e65d09a7d8176d5cfea9440f4662e1-454-256","viewtypecode":null,"action":"open_normal_detail_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"197794","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2017","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":"0"},{"displayName":"《迎春∙反串\u2014\u2014越剧经典折子戏》宣传片","slogan":"","scrollMarquee":"","playTime":"2018-01-02 17:06:12","img":"","imgh":"http://114.247.94.68:8181/view/6fe2819d4a4299b5819b546952d04c1b-454-256","viewtypecode":null,"action":"open_normal_detail_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"196833","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2017","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":"0"},{"displayName":"钱惠丽《甄嬛》选段","slogan":"","scrollMarquee":"","playTime":"2018-01-02 17:06:15","img":"","imgh":"http://114.247.94.68:8181/view/fa91d3925eb4d55397c6c5c618ac8eea-454-256","viewtypecode":null,"action":"open_normal_detail_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"196842","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2017","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":""},{"displayName":"枕上无梦","slogan":"万历二十六年，汤显祖向吏部告了长假，回到家乡临川，潜心写戏。\r\n远离仕途，与诗文戏曲为伴，这一生到此便再无波澜。\r\n当真甘心于此？\r\n为何始终烦恼萦怀？为何仍感寂寞气闷？\r\n不再信『紫钗』的浪漫，却放不下『还魂』的执着。\r\n『南柯』一梦依旧彷徨，『邯郸』之梦又去向何方？\r\n戏中人梦里梦外，谁能救赎汤显祖？\r\n这半生该何去何从？","scrollMarquee":"","playTime":"2018-01-02 17:07:13","img":"http://114.247.94.68:8181/view/14796825f37ca101cc4cc2f17a38d0b2-260-346","imgh":"http://114.247.94.68:8181/view/0af9e3ea595d5c2865af53fbc65c9247-454-256","viewtypecode":null,"action":"open_normal_list_page","actionUrl":"","actionParams":"{'p1':'','p2':'','p3':''}","contentType":"video","contentId":"192565","epgId":1031,"vipType":0,"topLeftCorner":"","bottomLeftCorner":"","topRightCorner":"","bottomRightCorner":"","year":"2016","score":"0","content":null,"pkgName":"","clsName":"","sid":"0","duration":"7200"}]
             */

            private int total;
            private int pageSize;
            private int pageNum;
            private List<ContentBean> content;

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public int getPageSize() {
                return pageSize;
            }

            public void setPageSize(int pageSize) {
                this.pageSize = pageSize;
            }

            public int getPageNum() {
                return pageNum;
            }

            public void setPageNum(int pageNum) {
                this.pageNum = pageNum;
            }

            public List<ContentBean> getContent() {
                return content;
            }

            public void setContent(List<ContentBean> content) {
                this.content = content;
            }

            @Override
            public String toString() {
                return "ListcontentBean{" +
                        "total=" + total +
                        ", pageSize=" + pageSize +
                        ", pageNum=" + pageNum +
                        ", content=" + content +
                        '}';
            }

            public static class ContentBean implements Serializable{
                /**
                 * displayName : 跟随绍百下乡去（三）
                 * slogan : 东方大剧院跟随绍百下乡演出的步伐，为大家带来别开生面的幕后直播。我们将独家采访吴凤花、吴素英、陈飞、张琳等绍百当家台柱，听她们讲述下乡经历与趣事；看他们在后台的点点滴滴以及如何化妆扮戏和演出等。也将带来难得一见的民间戏曲仪式“扫台”。跟随我们的镜头一起观看独家直播吧！
                 * scrollMarquee :
                 * playTime : 2018-01-08 12:19:43
                 * img : http://114.247.94.68:8181/view/e3b05f7e7b2f0ec156771b7b59013659-260-346
                 * imgh :
                 * viewtypecode : null
                 * action : open_normal_detail_page
                 * actionUrl :
                 * actionParams : {'p1':'','p2':'','p3':''}
                 * contentType : video
                 * contentId : 269459
                 * epgId : 1031
                 * vipType : 0
                 * topLeftCorner :
                 * bottomLeftCorner :
                 * topRightCorner :
                 * bottomRightCorner :
                 * year : 2018
                 * score : 0
                 * content : null
                 * pkgName :
                 * clsName :
                 * sid : 0
                 * duration :
                 *
                 * "endDate": 1527601247000,
                 * "startDate": 1496056526000,
                 * "status": 1
                 *
                 *
                 */

                private String displayName;
                private String slogan;
                private String scrollMarquee;
                private String playTime;
                private String img;
                private String imgh;
                private Object viewtypecode;
                private String action;
                private String actionUrl;
                private String actionParams;
                private String contentType;
                private String contentId;
                private int epgId;
                private int vipType;
                private String topLeftCorner;
                private String bottomLeftCorner;
                private String topRightCorner;
                private String bottomRightCorner;
                private String year;
                private String score;
                private Object content;
                private String pkgName;
                private String clsName;
                private String sid;
                private String duration;

                //直播状态
                private long startDate; //直播开始时间
                private long endDate; //直播结束时间
                private int status = 5; //直播状态（0未开始、1直播中、2已结束、3回看 / 5表示点播节目）

                public String getDisplayName() {
                    return displayName;
                }

                public void setDisplayName(String displayName) {
                    this.displayName = displayName;
                }

                public String getSlogan() {
                    return slogan;
                }

                public void setSlogan(String slogan) {
                    this.slogan = slogan;
                }

                public String getScrollMarquee() {
                    return scrollMarquee;
                }

                public void setScrollMarquee(String scrollMarquee) {
                    this.scrollMarquee = scrollMarquee;
                }

                public String getPlayTime() {
                    return playTime;
                }

                public void setPlayTime(String playTime) {
                    this.playTime = playTime;
                }

                public String getImg() {
                    return img;
                }

                public void setImg(String img) {
                    this.img = img;
                }

                public String getImgh() {
                    return imgh;
                }

                public void setImgh(String imgh) {
                    this.imgh = imgh;
                }

                public Object getViewtypecode() {
                    return viewtypecode;
                }

                public void setViewtypecode(Object viewtypecode) {
                    this.viewtypecode = viewtypecode;
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

                public String getContentType() {
                    return contentType;
                }

                public void setContentType(String contentType) {
                    this.contentType = contentType;
                }

                public String getContentId() {
                    return contentId;
                }

                public void setContentId(String contentId) {
                    this.contentId = contentId;
                }

                public int getEpgId() {
                    return epgId;
                }

                public void setEpgId(int epgId) {
                    this.epgId = epgId;
                }

                public int getVipType() {
                    return vipType;
                }

                public void setVipType(int vipType) {
                    this.vipType = vipType;
                }

                public String getTopLeftCorner() {
                    return topLeftCorner;
                }

                public void setTopLeftCorner(String topLeftCorner) {
                    this.topLeftCorner = topLeftCorner;
                }

                public String getBottomLeftCorner() {
                    return bottomLeftCorner;
                }

                public void setBottomLeftCorner(String bottomLeftCorner) {
                    this.bottomLeftCorner = bottomLeftCorner;
                }

                public String getTopRightCorner() {
                    return topRightCorner;
                }

                public void setTopRightCorner(String topRightCorner) {
                    this.topRightCorner = topRightCorner;
                }

                public String getBottomRightCorner() {
                    return bottomRightCorner;
                }

                public void setBottomRightCorner(String bottomRightCorner) {
                    this.bottomRightCorner = bottomRightCorner;
                }

                public String getYear() {
                    return year;
                }

                public void setYear(String year) {
                    this.year = year;
                }

                public String getScore() {
                    return score;
                }

                public void setScore(String score) {
                    this.score = score;
                }

                public Object getContent() {
                    return content;
                }

                public void setContent(Object content) {
                    this.content = content;
                }

                public String getPkgName() {
                    return pkgName;
                }

                public void setPkgName(String pkgName) {
                    this.pkgName = pkgName;
                }

                public String getClsName() {
                    return clsName;
                }

                public void setClsName(String clsName) {
                    this.clsName = clsName;
                }

                public String getSid() {
                    return sid;
                }

                public void setSid(String sid) {
                    this.sid = sid;
                }

                public String getDuration() {
                    return duration;
                }

                public void setDuration(String duration) {
                    this.duration = duration;
                }

                public long getStartDate() {
                    return startDate;
                }

                public void setStartDate(long startDate) {
                    this.startDate = startDate;
                }

                public long getEndDate() {
                    return endDate;
                }

                public void setEndDate(long endDate) {
                    this.endDate = endDate;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                @Override
                public String toString() {
                    return "ContentBean{" +
                            "displayName='" + displayName + '\'' +
                            ", slogan='" + slogan + '\'' +
                            ", scrollMarquee='" + scrollMarquee + '\'' +
                            ", playTime='" + playTime + '\'' +
                            ", img='" + img + '\'' +
                            ", imgh='" + imgh + '\'' +
                            ", viewtypecode=" + viewtypecode +
                            ", action='" + action + '\'' +
                            ", actionUrl='" + actionUrl + '\'' +
                            ", actionParams='" + actionParams + '\'' +
                            ", contentType='" + contentType + '\'' +
                            ", contentId='" + contentId + '\'' +
                            ", epgId=" + epgId +
                            ", vipType=" + vipType +
                            ", topLeftCorner='" + topLeftCorner + '\'' +
                            ", bottomLeftCorner='" + bottomLeftCorner + '\'' +
                            ", topRightCorner='" + topRightCorner + '\'' +
                            ", bottomRightCorner='" + bottomRightCorner + '\'' +
                            ", year='" + year + '\'' +
                            ", score='" + score + '\'' +
                            ", content=" + content +
                            ", pkgName='" + pkgName + '\'' +
                            ", clsName='" + clsName + '\'' +
                            ", sid='" + sid + '\'' +
                            ", duration='" + duration + '\'' +
                            ", startDate=" + startDate +
                            ", endDate=" + endDate +
                            ", status=" + status +
                            '}';
                }
            }
        }
    }
}
