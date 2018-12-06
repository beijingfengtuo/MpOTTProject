package cn.cibnmp.ott.jni;

interface HttpResponseListener2<T> {
	public void onSuccess(T response);
	public void onError(String error);
}
