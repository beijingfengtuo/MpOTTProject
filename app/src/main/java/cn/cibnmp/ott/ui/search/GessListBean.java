package cn.cibnmp.ott.ui.search;

import java.util.List;

/**
 * Created by geshuaipeng on 2017/12/27.
 */

public class GessListBean {

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

    public static class DataBean {

        private int blockId;
        private String name;
        private int positionCount;
        private LayoutBean layout;
        private List<IndexContentsBean> indexContents;

        public int getBlockId() {
            return blockId;
        }

        public void setBlockId(int blockId) {
            this.blockId = blockId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPositionCount() {
            return positionCount;
        }

        public void setPositionCount(int positionCount) {
            this.positionCount = positionCount;
        }

        public LayoutBean getLayout() {
            return layout;
        }

        public void setLayout(LayoutBean layout) {
            this.layout = layout;
        }

        public List<IndexContentsBean> getIndexContents() {
            return indexContents;
        }

        public void setIndexContents(List<IndexContentsBean> indexContents) {
            this.indexContents = indexContents;
        }

        public static class LayoutBean {
            private int layoutId;
            private String name;
            private int layoutType;
            private String layoutJson;
            private String description;
            private int state;

            public int getLayoutId() {
                return layoutId;
            }

            public void setLayoutId(int layoutId) {
                this.layoutId = layoutId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getLayoutType() {
                return layoutType;
            }

            public void setLayoutType(int layoutType) {
                this.layoutType = layoutType;
            }

            public String getLayoutJson() {
                return layoutJson;
            }

            public void setLayoutJson(String layoutJson) {
                this.layoutJson = layoutJson;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }
        }

        public static class IndexContentsBean {

            private int blockId;
            private int position;
            private String displayName;
            private String name;
            private String slogan;
            private String scrollMarquee;
            private String playTime;
            private String descInfo;
            private String img;
            private String imgh;
            private String imgBack;
            private String viewtype;
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
            private Object contents;
            private String sid;
            private String pkgName;
            private String clsName;

            public int getBlockId() {
                return blockId;
            }

            public void setBlockId(int blockId) {
                this.blockId = blockId;
            }

            public int getPosition() {
                return position;
            }

            public void setPosition(int position) {
                this.position = position;
            }

            public String getDisplayName() {
                return displayName;
            }

            public void setDisplayName(String displayName) {
                this.displayName = displayName;
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

            public String getDescInfo() {
                return descInfo;
            }

            public void setDescInfo(String descInfo) {
                this.descInfo = descInfo;
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

            public String getImgBack() {
                return imgBack;
            }

            public void setImgBack(String imgBack) {
                this.imgBack = imgBack;
            }

            public String getViewtype() {
                return viewtype;
            }

            public void setViewtype(String viewtype) {
                this.viewtype = viewtype;
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

            public Object getContents() {
                return contents;
            }

            public void setContents(Object contents) {
                this.contents = contents;
            }

            public String getSid() {
                return sid;
            }

            public void setSid(String sid) {
                this.sid = sid;
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
        }
    }
}
