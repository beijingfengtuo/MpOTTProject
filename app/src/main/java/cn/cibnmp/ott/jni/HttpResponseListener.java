package cn.cibnmp.ott.jni;

public interface HttpResponseListener {
	public void onSuccess(String response);
	public void onError(String error);
}
