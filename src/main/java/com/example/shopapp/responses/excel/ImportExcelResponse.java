package com.example.shopapp.responses.excel;

public class ImportExcelResponse {
	private String message;

	  public ImportExcelResponse(String message) {
	    this.message = message;
	  }

	  public String getMessage() {
	    return message;
	  }

	  public void setMessage(String message) {
	    this.message = message;
	  }
}
