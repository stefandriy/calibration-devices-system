package com.softserve.edu.service.utils;

import java.util.Date;

public class CustomResult {
private Integer maxProcessTime;
private Date expirationDate;
private String mailTo;

public CustomResult(Integer maxProcessTime, Date expirationDate, String mailTo) {
	this.maxProcessTime = maxProcessTime;
	this.expirationDate = expirationDate;
	this.mailTo = mailTo;
}
}
