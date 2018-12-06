package cn.cibnmp.ott.ui.search;

import java.util.List;

/**
 *
 * 搜索结果
 */

public class SearchResultEntity {
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
        private String keywords;
        private int epgId;
        private int subjectId;
        private String name;

        private ListcontentBean listcontent;

        public String getKeywords() {
            return keywords;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
        }

        public int getEpgId() {
            return epgId;
        }

        public void setEpgId(int epgId) {
            this.epgId = epgId;
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

        public ListcontentBean getListcontent() {
            return listcontent;
        }

        public void setListcontent(ListcontentBean listcontent) {
            this.listcontent = listcontent;
        }

        public static class ListcontentBean {
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

        }
    }
}
