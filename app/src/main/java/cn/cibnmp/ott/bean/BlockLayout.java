package cn.cibnmp.ott.bean;

import java.io.Serializable;

/**
 * Created by wanqi on 2017/3/9.
 */

public class BlockLayout implements Serializable{

    private int layoutId;

    private String name;

    private int layoutType;

    private String layoutJson;

    private String description;

    private int state;

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    public int getLayoutId() {
        return this.layoutId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public int getLayoutType() {
        return this.layoutType;
    }

    public void setLayoutJson(String layoutJson) {
        this.layoutJson = layoutJson;
    }

    public String getLayoutJson() {
        return this.layoutJson;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return this.state;
    }

}
