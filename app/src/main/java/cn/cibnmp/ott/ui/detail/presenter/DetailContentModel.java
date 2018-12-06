package cn.cibnmp.ott.ui.detail.presenter;

/**
 * Created by axl on 2018/3/12.
 */

public class DetailContentModel {


//    private DetailInterFace myInterFace;
//
//
//    private long vid;
//    private long sid;
//
//    public long getVid() {
//        return vid;
//    }
//
//    public void setVid(long vid) {
//        this.vid = vid;
//    }
//
//    public long getSid() {
//        return sid;
//    }
//
//    public void setSid(long sid) {
//        this.sid = sid;
//    }
//
//    public String getEpgIdParam() {
//        return epgIdParam;
//    }
//
//    public void setEpgIdParam(String epgIdParam) {
//        this.epgIdParam = epgIdParam;
//    }
//
//    private String epgIdParam;
//
//    public void requestDetailContent() {
//        // epgIdParam = "1031";
//        //   vid = 194580L;
//        HttpRequest.getInstance().excute("getDetailContent", new Object[]{App.epgUrl,
//                epgIdParam, vid + "", 10 * 60, new SimpleHttpResponseListener<DetailInfoResultBean>() {
//
//            @Override
//            public void onSuccess(DetailInfoResultBean response) {
//                handleRequestContentSuccess(response);
//                //mHandler.sendEmptyMessageDelayed(1003, 200);
//            }
//
//            @Override
//            public void onError(String error) {
//
//               myInterFace.onDetailError();
//            }
//        }});
//
//    }
//
//
//    protected DetailInfoResultBean resultBean;
//    protected DetailBean detailBean;
//
//    protected void handleRequestContentSuccess(DetailInfoResultBean response) {
//
//        try {
//      //      Log.d(TAG, "getDetailNavContent : result--> " + response.toString());
//            resultBean = response;
//            //数据判断
//            if (resultBean == null || !resultBean.getCode().equalsIgnoreCase("1000") || resultBean.getData() == null) {
//             //   Log.e(TAG, "getDetailNavContent response parse to entity failed , data is invalid !!!");
//                myInterFace.onDetailError();
//                return;
//            } else if (resultBean.getCode().equalsIgnoreCase("1001")) {
//           //     Log.e(TAG, "getDetailNavContent result's data is 1001 !!!");
//                myInterFace.onDetailError();
//                return;
//            } else {
//
//        //        videoType = resultBean.getData().getVideoType();
//
//                if (resultBean.getData().getChild() != null && resultBean.getData().getChild().size() > 0) {
//                    if (sid<=1) {
//                        sid = resultBean.getData().getChild().get(0).getSid();
//                    }
//                } else {
//                    Lg.i("DetailModel", " playerFragment.setPlaySource:null");
//
////                    mHandler.postDelayed(new Runnable() {
////                        @Override
////                        public void run() {
//////                            if (playerPage == null) {
//////                                initPlayer(video_C);
//////                            }
////                            url = "";
////                            playerFragment.setPlaySource("", 0l);
////                        }
////                    }, 0);
//                }
//
//                mergeData(resultBean.getData());
//
//                //更新数据
//                myInterFace.onDetailResult(resultBean);
//               // mHandler.sendEmptyMessageDelayed(2001, 200);
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//}
//    public List<LayoutItem> newLaytItemList = new ArrayList<>();
//
//    public List<NavigationInfoItemBean> newInfoItemBeanList = new ArrayList<>();
//    /**
//     * 带有容错处理的数据合并
//     *
//     * @param data
//     */
//    private void mergeData(DetailInfoBean data) {
//        newLaytItemList.clear();
//        newInfoItemBeanList.clear();
//        if (data.getBlocks() == null || data.getBlocks().isEmpty()
//                || data.getContent() == null || data.getContent().isEmpty()) {
////            Lg.e(TAG, "mergeData failed , navId = " + navId);
////            sendContentError("数据异常");
//            return;
//        }
//
//        int blockCount;  //block的数量
//        NavigationBlock block;
//        NavigationInfoBlockBean infoBlockBean;
//        NavigationInfoItemBean infoItemBean;
//
//        blockCount = Math.min(data.getBlocks().size(), data.getContent().size());
//
//        if (newLaytItemList == null || newInfoItemBeanList == null)
//            return;
//
//        newLaytItemList.clear();
//        newInfoItemBeanList.clear();
//
//
//        JSONObject object = null;
//        LayoutItem layoutItem = null;
//        JSONObject jsonObject = null;
//        JSONArray array = null;
//        String layout = null;
//        NavigationInfoItemBean itemBean;
//
//        for (int j = 0; j < blockCount; j++) {
//            block = data.getBlocks().get(j);
//            infoBlockBean = data.getContent().get(j);
//            if (block.getLayout() != null && !TextUtils.isEmpty(block.getLayout().getLayoutJson())) {
//                layout = block.getLayout().getLayoutJson();
//
//                try {
//
//                    //设置block的标题的布局和内容，只要当nameType都是0的时候，才显示标题内容
//                    if (block.getNameType() == NavigationInfoBlockBean.SHOWNAV
//                            && infoBlockBean.getNameType() == NavigationInfoBlockBean.SHOWNAV) {
//
//                        layoutItem = new LayoutItem();
//                        layoutItem.setC(720l);
//                        layoutItem.setR(120l);
//                        newLaytItemList.add(layoutItem);
//
//                        itemBean = new NavigationInfoItemBean();
//                        itemBean.setViewtype(HomeOneViewType.title_viewType);
//                        itemBean.setName(infoBlockBean.getName());
//                        newInfoItemBeanList.add(itemBean);
//                    }
//                    jsonObject = new JSONObject(layout);
//
//                    array = jsonObject.getJSONArray("layout");
//
//                    for (int i = 0; i < array.length(); i++) {
//                        object = array.getJSONObject(i);
//                        layoutItem = new LayoutItem();
//                        if (object.has("c"))
//                            layoutItem.setC(object.getDouble("c"));
//                        else if (object.has("C"))
//                            layoutItem.setC(object.getDouble("C"));
//
//                        if (object.has("r"))
//                            layoutItem.setR(object.getDouble("r"));
//                        else if (object.has("R"))
//                            layoutItem.setR(object.getDouble("R"));
//                        newLaytItemList.add(layoutItem);
//
//                        if (infoBlockBean.getIndexContents() != null && i < infoBlockBean.getIndexContents().size()) {
//                            infoItemBean = infoBlockBean.getIndexContents().get(i);
//                            newInfoItemBeanList.add(infoItemBean);
//                        } else {
//                            newInfoItemBeanList.add(new NavigationInfoItemBean(HomeOneViewType.space_viewType));
//                        }
//                    }
////                    if (layoutLoadListener != null) {
////                        layoutLoadListener.OnLayoutLoad(navId, newLaytItemList);
////                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
////                    sendContentError("数据异常");
//                }
//
//            } else if (block.getLayout() == null && block.getLayout().getLayoutJson() != null) {
//
//            } else {
//
//                if (infoBlockBean.getIndexContents() != null && infoBlockBean.getIndexContents().size() > 0 && (infoBlockBean.getIndexContents().get(0).getViewtype() == 10 || infoBlockBean.getIndexContents().get(0).getViewtype() == 11)) {
//                    layoutItem = new LayoutItem();
//                    layoutItem.setC(12l);
//                    layoutItem.setR(6l);
//                    newLaytItemList.add(layoutItem);
//
//                    itemBean = new NavigationInfoItemBean();
//                    itemBean.setViewtype(HomeOneViewType.title_viewType);
//                    itemBean.setName(infoBlockBean.getName());
//                    newInfoItemBeanList.add(itemBean);
//                    //   initTag(infoBlockBean);
//                }
////                sendContentError("数据异常");
////                Lg.e(TAG, "mergeLayout : navId = " + navId + " , blockId = " + block.getBlockId()
////                        + " , layout is invalid , layout is null or layoutJson is null !");
//            }
//        }
//
//
//        if (newInfoItemBeanList.size() > 0 && newLaytItemList.size() > 0) {
//          myInterFace.onMergeDataLayout(newLaytItemList,newInfoItemBeanList);
//        }
//
////        Log.d(TAG, "------merge layout result --> " + newLaytItemList.toString());
//
//    }
}
