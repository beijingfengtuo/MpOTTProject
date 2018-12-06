package cn.cibnmp.ott.bean;

import java.util.List;

public class CarouselProgramBean {

	private String playDate;

	private List<Program> programs;

	public String getPlayDate() {
		return playDate;
	}

	public void setPlayDate(String playDate) {
		this.playDate = playDate;
	}

	public List<Program> getPrograms() {
		return programs;
	}

	public void setPrograms(List<Program> programs) {
		this.programs = programs;
	}

	public class Program {
		private String desImg;
		private String startTime;
		private String des;
		private String programId;
		private String programUrl;
		private String type;
		private String endTime;
		private String programName;

		public String getDesImg() {
			return desImg;
		}

		public void setDesImg(String desImg) {
			this.desImg = desImg;
		}

		public String getStartTime() {
			return startTime;
		}

		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}

		public String getDes() {
			return des;
		}

		public void setDes(String des) {
			this.des = des;
		}

		public String getProgramId() {
			return programId;
		}

		public void setProgramId(String programId) {
			this.programId = programId;
		}

		public String getProgramUrl() {
			return programUrl;
		}

		public void setProgramUrl(String programUrl) {
			this.programUrl = programUrl;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getEndTime() {
			return endTime;
		}

		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}

		public String getProgramName() {
			return programName;
		}

		public void setProgramName(String programName) {
			this.programName = programName;
		}

	}
}
