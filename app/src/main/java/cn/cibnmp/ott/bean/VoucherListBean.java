package cn.cibnmp.ott.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cibn-lyc on 2018/1/26.
 */

public class VoucherListBean implements Serializable{
    private String resultCode;
    private String resultDesc;

    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "VoucherListBean{" +
                "resultCode='" + resultCode + '\'' +
                ", resultDesc='" + resultDesc + '\'' +
                ", data=" + data +
                '}';
    }

    public class DataBean {
        private String channelId;

        private List<VouchersBean> vouchers;

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public List<VouchersBean> getVouchers() {
            return vouchers;
        }

        public void setVouchers(List<VouchersBean> vouchers) {
            this.vouchers = vouchers;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "channelId='" + channelId + '\'' +
                    ", vouchers=" + vouchers +
                    '}';
        }

        public class VouchersBean {
            private String voucherCode;
            private String voucherName;
            private String voucherDesc;
            private long voucherMoney;
            private String backgroundImgUrl;
            private String expTime;
            private String thresholdAmount ;

            public String getVoucherCode() {
                return voucherCode;
            }

            public void setVoucherCode(String voucherCode) {
                this.voucherCode = voucherCode;
            }

            public String getVoucherName() {
                return voucherName;
            }

            public void setVoucherName(String voucherName) {
                this.voucherName = voucherName;
            }

            public String getVoucherDesc() {
                return voucherDesc;
            }

            public void setVoucherDesc(String voucherDesc) {
                this.voucherDesc = voucherDesc;
            }

            public long getVoucherMoney() {
                return voucherMoney;
            }

            public void setVoucherMoney(long voucherMoney) {
                this.voucherMoney = voucherMoney;
            }

            public String getBackgroundImgUrl() {
                return backgroundImgUrl;
            }

            public void setBackgroundImgUrl(String backgroundImgUrl) {
                this.backgroundImgUrl = backgroundImgUrl;
            }

            public String getExpTime() {
                return expTime;
            }

            public void setExpTime(String expTime) {
                this.expTime = expTime;
            }

            public String getThresholdAmount() {
                return thresholdAmount;
            }

            public void setThresholdAmount(String thresholdAmount) {
                this.thresholdAmount = thresholdAmount;
            }

            @Override
            public String toString() {
                return "VouchersBean{" +
                        "voucherCode='" + voucherCode + '\'' +
                        ", voucherName='" + voucherName + '\'' +
                        ", voucherDesc='" + voucherDesc + '\'' +
                        ", voucherMoney='" + voucherMoney + '\'' +
                        ", backgroundImgUrl='" + backgroundImgUrl + '\'' +
                        ", expTime='" + expTime + '\'' +
                        ", thresholdAmount='" + thresholdAmount + '\'' +
                        '}';
            }
        }
    }
}
