package cn.cibnmp.ott.bean;

import java.io.Serializable;

/**
 * Created by cibn-lyc on 2018/2/6.
 */

public class KaMiBean implements Serializable {
    private String resultCode;
    private String resultDesc;
    private KaMiOrderBean bossOrder;

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

    public KaMiOrderBean getBossOrder() {
        return bossOrder;
    }

    public void setBossOrder(KaMiOrderBean bossOrder) {
        this.bossOrder = bossOrder;
    }

    public class KaMiOrderBean {
        private String productName;
        private String tradeNo;
        private String expTime;
        private String buyTime;

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getTradeNo() {
            return tradeNo;
        }

        public void setTradeNo(String tradeNo) {
            this.tradeNo = tradeNo;
        }

        public String getExpTime() {
            return expTime;
        }

        public void setExpTime(String expTime) {
            this.expTime = expTime;
        }

        public String getBuyTime() {
            return buyTime;
        }

        public void setBuyTime(String buyTime) {
            this.buyTime = buyTime;
        }
    }
}
