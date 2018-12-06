package cn.cibnmp.ott.bean;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by cibn-lyc on 2018/1/24.
 */

public class UserRecordInfoBean implements Serializable {

    private String cibnUserId;
    private String productId;
    private String productTypeId;
    private String productName;
    private String tradeNo;
    private String orderStatus;
    private String payMethod;
    private long timeLength;
    private long productPrice;
    private int discountType;
    private String dropCount;
    private long dropPrice;
    private long deductibleAmount;
    private long paymentAmount;
    private String payTime;
    private String unsubscrieStatus;
    private String expTime;
    private String imageUrl;
    private String chargingPoint;
    private String chargingType;
    private String voucherId;

    public String getCibnUserId() {
        return cibnUserId;
    }

    public void setCibnUserId(String cibnUserId) {
        this.cibnUserId = cibnUserId;
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

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
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

    public int getDiscountType() {
        return discountType;
    }

    public void setDiscountType(int discountType) {
        this.discountType = discountType;
    }

    public String getDropCount() {
        return dropCount;
    }

    public void setDropCount(String dropCount) {
        this.dropCount = dropCount;
    }

    public long getDropPrice() {
        return dropPrice;
    }

    public void setDropPrice(long dropPrice) {
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

    public String getUnsubscrieStatus() {
        return unsubscrieStatus;
    }

    public void setUnsubscrieStatus(String unsubscrieStatus) {
        this.unsubscrieStatus = unsubscrieStatus;
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

    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    @Override
    public String toString() {
        return "UserRecordInfoBean{" +
                "cibnUserId='" + cibnUserId + '\'' +
                ", productId='" + productId + '\'' +
                ", productTypeId='" + productTypeId + '\'' +
                ", productName='" + productName + '\'' +
                ", tradeNo='" + tradeNo + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", payMethod='" + payMethod + '\'' +
                ", timeLength='" + timeLength + '\'' +
                ", productPrice='" + productPrice + '\'' +
                ", discountType='" + discountType + '\'' +
                ", dropCount='" + dropCount + '\'' +
                ", dropPrice='" + dropPrice + '\'' +
                ", deductibleAmount='" + deductibleAmount + '\'' +
                ", paymentAmount='" + paymentAmount + '\'' +
                ", payTime='" + payTime + '\'' +
                ", unsubscrieStatus='" + unsubscrieStatus + '\'' +
                ", expTime='" + expTime + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", chargingPoint='" + chargingPoint + '\'' +
                ", chargingType='" + chargingType + '\'' +
                ", voucherId='" + voucherId + '\'' +
                '}';
    }
}
