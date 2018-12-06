package cn.cibnmp.ott.player;

public interface PlayerCallBack {

	public void start();

	public void pause();

	public void stop();

	public void isprepared();

	public void ffStart(); // firstFrameStart

	public void error(int what, int extra, String desc);

	public void bufStart();

	public void bufEnd();

	public void bufUpdate(int po);

	public void currUpdate(int po);  //进度更新

	public void completion();
	
	public void loadFail();

	public void netchange(int what); //网络状态更新
}
