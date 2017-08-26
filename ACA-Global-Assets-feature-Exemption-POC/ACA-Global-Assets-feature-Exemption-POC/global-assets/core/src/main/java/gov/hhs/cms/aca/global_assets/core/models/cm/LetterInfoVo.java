package gov.hhs.cms.aca.global_assets.core.models.cm;

public class LetterInfoVo {
	
	private String data;
	private String letterId;
	private String letterName;
	private String userEmail;
	private String comment;
	
	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getLetterId() {
		return letterId;
	}
	public void setLetterId(String letterId) {
		this.letterId = letterId;
	}
	public String getLetterName() {
		return letterName;
	}
	public void setLetterName(String letterName) {
		this.letterName = letterName;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
