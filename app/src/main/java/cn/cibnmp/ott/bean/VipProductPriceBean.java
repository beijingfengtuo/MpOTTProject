package cn.cibnmp.ott.bean;

import java.io.Serializable;
import java.util.List;

/**
 *  产品包价格实体类
 *
 * Created by cibn-lyc on 2018/1/26.
 */

public class VipProductPriceBean implements Serializable{

    private String resultCode;
    private String resultDesc;
    private List<VipProductPriceInfoBean> dataList;

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

    public List<VipProductPriceInfoBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<VipProductPriceInfoBean> dataList) {
        this.dataList = dataList;
    }

    public class VipProductPriceInfoBean {
        private String productId;
        private String productName;
        private String productTypeId;
        private String bannerImg;
        private String backgroundImg;
        private String introduceImg;
        private List<PriceListBean> prices;

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductTypeId() {
            return productTypeId;
        }

        public void setProductTypeId(String productTypeId) {
            this.productTypeId = productTypeId;
        }

        public String getBannerImg() {
            return bannerImg;
        }

        public void setBannerImg(String bannerImg) {
            this.bannerImg = bannerImg;
        }

        public String getBackgroundImg() {
            return backgroundImg;
        }

        public void setBackgroundImg(String backgroundImg) {
            this.backgroundImg = backgroundImg;
        }

        public String getIntroduceImg() {
            return introduceImg;
        }

        public void setIntroduceImg(String introduceImg) {
            this.introduceImg = introduceImg;
        }

        public List<PriceListBean> getPrices() {
            return prices;
        }

        public void setPrices(List<PriceListBean> prices) {
            this.prices = prices;
        }

        public class PriceListBean {
            private String priceId;
            private String priceName;
            private String timeLength;
            private long productPrices;
            private long paymentPrice;
            private String discountType;
            private long dropPrice;
            private boolean isShowSelectBj = false;

            public boolean isShowSelectBj() {
                return isShowSelectBj;
            }

            public void setShowSelectBj(boolean showSelectBj) {
                isShowSelectBj = showSelectBj;
            }

            public String getPriceId() {
                return priceId;
            }

            public void setPriceId(String priceId) {
                this.priceId = priceId;
            }

            public String getPriceName() {
                return priceName;
            }

            public void setPriceName(String priceName) {
                this.priceName = priceName;
            }

            public String getTimeLength() {
                return timeLength;
            }

            public void setTimeLength(String timeLength) {
                this.timeLength = timeLength;
            }

            public long getProductPrices() {
                return productPrices;
            }

            public void setProductPrices(long productPrices) {
                this.productPrices = productPrices;
            }

            public long getPaymentPrice() {
                return paymentPrice;
            }

            public void setPaymentPrice(long paymentPrice) {
                this.paymentPrice = paymentPrice;
            }

            public String getDiscountType() {
                return discountType;
            }

            public void setDiscountType(String discountType) {
                this.discountType = discountType;
            }

            public long getDropPrice() {
                return dropPrice;
            }

            public void setDropPrice(long dropPrice) {
                this.dropPrice = dropPrice;
            }
        }
    }
}
