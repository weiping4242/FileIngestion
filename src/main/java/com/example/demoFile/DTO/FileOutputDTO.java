package com.example.demoFile.DTO;

import java.util.Objects;

public class FileOutputDTO {

	private String countryCode;
	private String bankCode;
	private int count;
	
	public FileOutputDTO(String bankCode, String countryCode) {
		super();
		this.countryCode = countryCode;
		this.bankCode = bankCode;
	}
	
	public FileOutputDTO(String countryCode, String bankCode, int count) {
		super();
		this.countryCode = countryCode;
		this.bankCode = bankCode;
		this.count = count;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the countryCode
	 */
	public String getCountryCode() {
		return countryCode;
	}

	/**
	 * @param countryCode the countryCode to set
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	/**
	 * @return the bankCode
	 */
	public String getBankCode() {
		return bankCode;
	}

	/**
	 * @param bankCode the bankCode to set
	 */
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	
	 @Override
	 public boolean equals(Object o) {
		 if (this == o) return true;
		 if (o == null || getClass() != o.getClass()) return false;
		 FileOutputDTO that = (FileOutputDTO) o;
		 return Objects.equals(countryCode, that.countryCode) && Objects.equals(bankCode, that.bankCode);
	}

	@Override
    public int hashCode() {
        return Objects.hash(countryCode, bankCode);
    }

    @Override
    public String toString() {
        return "FileOutputDTO{" +
                "countryCode='" + countryCode + '\'' +
                ", bankCode='" + bankCode + '\'' +
                '}';
    }
}
