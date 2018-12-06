package cn.cibnmp.ott.ui.detail.bean;

/**
 * Created by yanjing on 2017/3/18.
 */

public class LiveAppointment {

    private Integer condition;
    private String id;

    public Integer getCondition() {
        return condition;
    }

    public void setCondition(Integer condition) {
        this.condition = condition;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "LiveAppointment{" +
                "condition=" + condition +
                ", id='" + id + '\'' +
                '}';
    }
}
