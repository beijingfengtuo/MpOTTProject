package cn.cibnmp.ott.ui.search;

import java.io.Serializable;
import java.util.List;

public class SearchRecord implements Serializable {
	public int TotalNum ;
	public int StartIndex;
	public int CurNum;
	private int LocalFlag;
	public List<RecordContent> videoCollectList;

	public int getTotalNum() {
		return TotalNum;
	}

	public void setTotalNum(int totalNum) {
		TotalNum = totalNum;
	}

	public int getStartIndex() {
		return StartIndex;
	}

	public void setStartIndex(int startIndex) {
		StartIndex = startIndex;
	}

	public int getCurNum() {
		return CurNum;
	}

	public void setCurNum(int curNum) {
		CurNum = curNum;
	}

	public int getLocalFlag() {
		return LocalFlag;
	}

	public void setLocalFlag(int localFlag) {
		LocalFlag = localFlag;
	}

	public List<RecordContent> getVideoCollectList() {
		return videoCollectList;
	}

	public void setVideoCollectList(List<RecordContent> videoCollectList) {
		this.videoCollectList = videoCollectList;
	}
}
