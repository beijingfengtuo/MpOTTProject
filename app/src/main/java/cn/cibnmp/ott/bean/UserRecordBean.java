package cn.cibnmp.ott.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cibn-lyc on 2018/1/24.
 */

public class UserRecordBean implements Serializable {

    private String resultCode;
    private String resultDesc;
    private List<UserRecordInfoBean> dataList;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public List<UserRecordInfoBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<UserRecordInfoBean> dataList) {
        this.dataList = dataList;
    }
}
