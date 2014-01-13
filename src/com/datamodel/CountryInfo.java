package com.datamodel;

public class CountryInfo {
	private String countryName;
	private String countryId;
	private String countryCode;
	private int countryIcon;

	public CountryInfo(String countryName, String countryId, String countryCode,
			int countryIcon) {
		super();
		this.countryName = countryName;
		this.countryId = countryId;
		this.countryCode = countryCode;
		this.countryIcon = countryIcon;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@Override
	public String toString() {
		return "CountryInfo [countryName=" + countryName + ", countryId="
				+ countryId + ", countryCode=" + countryCode + ", countryIcon="
				+ countryIcon + "]";
	}

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public int getCountryIcon() {
		return countryIcon;
	}

	public void setCountryIcon(int countryIcon) {
		this.countryIcon = countryIcon;
	}
}
