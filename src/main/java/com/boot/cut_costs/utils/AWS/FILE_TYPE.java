package com.boot.cut_costs.utils.AWS;

public enum FILE_TYPE {

	EXPENSE_PHOTO("cut-costs-expense-photo", "expense-photo"),
	USER_PHOTO("cut-costs-user-photo", "user-photo");

	private String awsBucketName, filePrefix;

	private FILE_TYPE(String awsBucketName, String filePrefix) {
		this.awsBucketName = awsBucketName;
		this.filePrefix = filePrefix;
	}

	public String getAwsBucketName() {
		return this.awsBucketName;
	}

	public String getFilePrefix() {
		return this.filePrefix;
	}

}
