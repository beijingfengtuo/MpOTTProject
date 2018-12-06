package cn.cibnmp.ott.bean;

import java.io.Serializable;
import java.util.List;

/**
 *  产品包实体类
 *
 * Created by cibn-lyc on 2018/1/26.
 */

public class VipProductPackageBean implements Serializable{

    private String resultCode;
    private String resultDesc;
    private List<VipProductInfoBean> dataList;

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

    public List<VipProductInfoBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<VipProductInfoBean> dataList) {
        this.dataList = dataList;
    }

    public class VipProductInfoBean {
        private String productName;
        private String productId;
        private String productTypeId;
        private String productDesc;

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductTypeId() {
            return productTypeId;
        }

        public void setProductTypeId(String productTypeId) {
            this.productTypeId = productTypeId;
        }

        public String getProductDesc() {
            return productDesc;
        }

        public void setProductDesc(String productDesc) {
            this.productDesc = productDesc;
        }
    }
}
