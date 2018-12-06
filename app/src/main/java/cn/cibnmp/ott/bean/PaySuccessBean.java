package cn.cibnmp.ott.bean;

import java.io.Serializable;

/**
 * Created by cibn-lyc on 2018/3/22.
 */

public class PaySuccessBean implements Serializable {
    private String resultCode;
    private String resultDesc;
    private PaySuccessInfo data;

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

    public PaySuccessInfo getData() {
        return data;
    }

    public void setData(PaySuccessInfo data) {
        this.data = data;
    }

    public class PaySuccessInfo {
        private long productId;
        private String productName;
        private String tradeNo;
        private String payMethod;
        private long timeLength;
        private long productPrice;
        private String discountType;
        private String dropCount;
        private String dropPrice;
        private long deductibleAmount;
        private long paymentAmount;
        private String payTime;
        private String expTime;
        private String imageUrl;

        public long getProductId() {
            return productId;
        }

        public void setProductId(long productId) {
            this.productId = productId;
        }

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

        public String getPayMethod() {
            return payMethod;
        }

        public void setPayMethod(String payMethod) {
            this.payMethod = payMethod;
        }

        public long getTimeLength() {
            return timeLength;
        }

        public void setTimeLength(long timeLength) {
            this.timeLength = timeLength;
        }

        public long getProductPrice() {
            return productPrice;
        }

        public void setProductPrice(long productPrice) {
            this.productPrice = productPrice;
        }

        public String getDiscountType() {
            return discountType;
        }

        public void setDiscountType(String discountType) {
            this.discountType = discountType;
        }

        public String getDropCount() {
            return dropCount;
        }

        public void setDropCount(String dropCount) {
            this.dropCount = dropCount;
        }

        public String getDropPrice() {
            return dropPrice;
        }

        public void setDropPrice(String dropPrice) {
            this.dropPrice = dropPrice;
        }

        public long getDeductibleAmount() {
            return deductibleAmount;
        }

        public void setDeductibleAmount(long deductibleAmount) {
            this.deductibleAmount = deductibleAmount;
        }

        public long getPaymentAmount() {
            return paymentAmount;
        }

        public void setPaymentAmount(long paymentAmount) {
            this.paymentAmount = paymentAmount;
        }

        public String getPayTime() {
            return payTime;
        }

        public void setPayTime(String payTime) {
            this.payTime = payTime;
        }

        public String getExpTime() {
            return expTime;
        }

        public void setExpTime(String expTime) {
            this.expTime = expTime;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}
