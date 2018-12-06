package cn.cibnmp.ott.bean;

import java.io.Serializable;

/**
 * Created by cibn-lyc on 2018/2/27.
 */

public class UserWXPayBean implements Serializable {

    private String resultCode;
    private String resultDesc;
    private WXPayDataBean data;

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

    public WXPayDataBean getData() {
        return data;
    }

    public void setData(WXPayDataBean data) {
        this.data = data;
    }

    public class WXPayDataBean {
        private String mchId;
        private String tranNo;
        private String tradeNo;
        private String productName;
        private String createTime;
        private String prePayId;
        private String chargingPoint;
        private String chargingType;
        private String productPrice;
        private String paySingMd5;
        private String nonceStr;
        private String appId;
        private String timeStamp;
        private BuyInfoBean buyValidInfo;

        public String getMchId() {
            return mchId;
        }

        public void setMchId(String mchId) {
            this.mchId = mchId;
        }

        public String getTranNo() {
            return tranNo;
        }

        public void setTranNo(String tranNo) {
            this.tranNo = tranNo;
        }

        public String getTradeNo() {
            return tradeNo;
        }

        public void setTradeNo(String tradeNo) {
            this.tradeNo = tradeNo;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getPrePayId() {
            return prePayId;
        }

        public void setPrePayId(String prePayId) {
            this.prePayId = prePayId;
        }

        public String getChargingPoint() {
            return chargingPoint;
        }

        public void setChargingPoint(String chargingPoint) {
            this.chargingPoint = chargingPoint;
        }

        public String getChargingType() {
            return chargingType;
        }

        public void setChargingType(String chargingType) {
            this.chargingType = chargingType;
        }

        public String getProductPrice() {
            return productPrice;
        }

        public void setProductPrice(String productPrice) {
            this.productPrice = productPrice;
        }

        public String getPaySingMd5() {
            return paySingMd5;
        }

        public void setPaySingMd5(String paySingMd5) {
            this.paySingMd5 = paySingMd5;
        }

        public String getNonceStr() {
            return nonceStr;
        }

        public void setNonceStr(String nonceStr) {
            this.nonceStr = nonceStr;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        public BuyInfoBean getBuyValidInfo() {
            return buyValidInfo;
        }

        public void setBuyValidInfo(BuyInfoBean buyValidInfo) {
            this.buyValidInfo = buyValidInfo;
        }

        public class BuyInfoBean {
            private String productName;
            private long paymentPrice;
            private String productId;

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }

            public long getPaymentPrice() {
                return paymentPrice;
            }

            public void setPaymentPrice(long paymentPrice) {
                this.paymentPrice = paymentPrice;
            }

            public String getProductId() {
                return productId;
            }

            public void setProductId(String productId) {
                this.productId = productId;
            }
        }
    }
}
