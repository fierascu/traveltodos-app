package com.traveltodos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
public class GlobalProperties {

	@Value("${spring.activemq.broker-url}")
	private String brokerurl;

	@Value("${spring.activemq.que-name}")
	private String activemqQueName;

	@Value("${traveltodos.rulesxls-filename}")
	private String rulesXlsFilename;

	@Value("${traveltodos.app-name}")
	private String appName;

	@Value("${traveltodos.frameY}")
	private int frameY;

	@Value("${traveltodos.frameX}")
	private int frameX;

	public int getFrameY() {
		return frameY;
	}

	public void setFrameY(int frameY) {
		this.frameY = frameY;
	}

	public int getFrameX() {
		return frameX;
	}

	public void setFrameX(int frameX) {
		this.frameX = frameX;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getBrokerurl() {
		return brokerurl;
	}

	public void setBrokerurl(String brokerurl) {
		this.brokerurl = brokerurl;
	}

	public String getActivemqQueName() {
		return activemqQueName;
	}

	public void setActivemqQueName(String activemqQueName) {
		this.activemqQueName = activemqQueName;
	}

	public String getRulesXlsFilename() {
		return rulesXlsFilename;
	}

	public void setRulesXlsFilename(String rulesXlsFilename) {
		this.rulesXlsFilename = rulesXlsFilename;
	}

}