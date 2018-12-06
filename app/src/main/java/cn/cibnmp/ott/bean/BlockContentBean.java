package cn.cibnmp.ott.bean;

import java.util.List;

/**
 * Created by cibn-lyc on 2018/1/18.
 */

public class BlockContentBean {
    private String code;
    private String msg;
    private BlockBean data;

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

    public BlockBean getData() {
        return data;
    }

    public void setData(BlockBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BlockContentBean{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public static class BlockBean {
        private int blockId;
        private String name;
        private List<ContentBean> indexContents;

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

        public List<ContentBean> getIndexContents() {
            return indexContents;
        }

        public void setIndexContents(List<ContentBean> indexContents) {
            this.indexContents = indexContents;
        }

        @Override
        public String toString() {
            return "BlockBean{" +
                    "blockId=" + blockId +
                    ", name='" + name + '\'' +
                    ", indexContents=" + indexContents +
                    '}';
        }
    }
}
