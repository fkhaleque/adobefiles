package com.adobe.cms.exemptions.core.models.adaptiveforms;

public class GeneratePDFOptionsSpec {

	private String templatePath;
	private String renderAction;
	private boolean readerExtend;
	
	
	public String getTemplatePath() {
		return templatePath;
	}
	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}
	public String getRenderAction() {
		return renderAction;
	}
	public void setRenderAction(String renderAction) {
		this.renderAction = renderAction;
	}
	public boolean isReaderExtend() {
		return readerExtend;
	}
	public void setReaderExtend(boolean readerExtend) {
		this.readerExtend = readerExtend;
	}	
}
