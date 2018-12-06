package cn.cibnmp.ott.ui.user;

/**
 *  支付接口url
 * Created by cibn-lyc on 2018/4/9.
 */

public class VipPayUrlUtil {

    // 测试域名
    public static String csDomainName = "http://weixtest2pay.hdv.ott.cibntv.net/cibn3api_02";

    // 产品包接口
    public static String reqProductUrl = "/new/boss/reqProductListGen";

    // 价格列表
    public static String reqPriceUrl = "/new/boss/reqProductPriceListGen";

    // 支付下单接口
    public static String payUrl = "/new/boss/itemVerificationWithLogInGen";

    // 支付成功后验证后台订单
    public static String reqOrderInfourl = "/new/boss/reqOrderInfo";

    // 卡密接口
    public static String reqCardUrl = "/card/successGen";

    // 代金券接口
    public static String reqVoucherUrl = "/new/boss/reqVoucherListGen";

}
