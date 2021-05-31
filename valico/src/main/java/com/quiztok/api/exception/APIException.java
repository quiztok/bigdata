package com.quiztok.api.exception;

import com.quiztok.api.util.statuscode.APIStatus;

import proto.node.ApproachStatus;

public class APIException extends RuntimeException {

	private static final long serialVersionUID = 5512443821225507586L;
	private int status;
	private String message;
	
	
	public APIException() {
		this.status = APIStatus.NODE_EXCEPTION;
		this.message = "please try again";
	}
	
	public APIException(int status) {
		this.status = status;
	}
	
	public APIException(int status, String message) {
		this.status = status;
		this.message = message;
	}
	
	public APIException(ApproachStatus status) {
		this.status = APIStatus.NODE_EXCEPTION;
		switch (status) {
		case ERROR:
			this.message = "critical exception";
			break;
		case FAIL_PRIVATE_KEY:
			this.message = "wrong privateKey";
			break;
		case FAIL_VERIFY:
			this.message = "verify failed";
			break;
		case TRY_AGAIN:
			this.message = "please try again";
			break;
		default:
			break;
		}
	}

	public int getStatus() {
		return status;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
