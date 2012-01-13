package com.github.nagaseyasuhito.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang.StringUtils;

import com.github.nagaseyasuhito.fatsia.entity.BaseEntity;

@Entity
public class Address extends BaseEntity<Long> {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	@Column
	private String blockNumber;

	@Column
	private String blockNumberKana;

	@Column(nullable = false)
	private Long cityCode;

	@Column(nullable = false)
	private String cityName;

	@Column(nullable = false)
	private String cityNameKana;

	@Column(nullable = false, unique = true)
	private Long code;

	@Column
	private String note;

	@Column(nullable = false)
	private Boolean obsoleted;

	@Column(nullable = false)
	private Boolean office;

	@Column
	private String officeAddress;

	@Column
	private String officeName;

	@Column
	private String officeNameKana;

	@Column(nullable = false)
	private String postalCode;

	@Column(nullable = false)
	private Long prefectureCode;

	@Column(nullable = false)
	private String prefectureName;

	@Column(nullable = false)
	private String prefectureNameKana;

	@Column
	private String streetName;

	@Column(nullable = false)
	private Long townCode;

	@Column
	private String townName;

	@Column
	private String townNameKana;

	@Column
	private String townNote;

	@Override
	public Long getId() {
		return this.id;
	}

	public String getBlockNumber() {
		return this.blockNumber;
	}

	public String getBlockNumberKana() {
		return this.blockNumberKana;
	}

	public Long getCityCode() {
		return this.cityCode;
	}

	public String getCityName() {
		return this.cityName;
	}

	public String getCityNameKana() {
		return this.cityNameKana;
	}

	public Long getCode() {
		return this.code;
	}

	public String getNote() {
		return this.note;
	}

	public String getOfficeAddress() {
		return this.officeAddress;
	}

	public String getOfficeName() {
		return this.officeName;
	}

	public String getOfficeNameKana() {
		return this.officeNameKana;
	}

	public String getPostalCode() {
		return this.postalCode;
	}

	public Long getPrefectureCode() {
		return this.prefectureCode;
	}

	public String getPrefectureName() {
		return this.prefectureName;
	}

	public String getPrefectureNameKana() {
		return this.prefectureNameKana;
	}

	public String getStreetName() {
		return this.streetName;
	}

	public Long getTownCode() {
		return this.townCode;
	}

	public String getTownName() {
		return this.townName;
	}

	public String getTownNameKana() {
		return this.townNameKana;
	}

	public String getTownNote() {
		return this.townNote;
	}

	public Boolean isObsoleted() {
		return this.obsoleted;
	}

	public Boolean isOffice() {
		return this.office;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setBlockNumber(String blockNumber) {
		this.blockNumber = blockNumber;
	}

	public void setBlockNumberKana(String blockNumberKana) {
		this.blockNumberKana = blockNumberKana;
	}

	public void setCityCode(Long cityCode) {
		this.cityCode = cityCode;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public void setCityNameKana(String cityNameKana) {
		this.cityNameKana = cityNameKana;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setObsoleted(Boolean obsoleted) {
		this.obsoleted = obsoleted;
	}

	public void setOffice(Boolean office) {
		this.office = office;
	}

	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public void setOfficeNameKana(String officeNameKana) {
		this.officeNameKana = officeNameKana;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public void setPrefectureCode(Long prefectureCode) {
		this.prefectureCode = prefectureCode;
	}

	public void setPrefectureName(String prefectureName) {
		this.prefectureName = prefectureName;
	}

	public void setPrefectureNameKana(String prefectureNameKana) {
		this.prefectureNameKana = prefectureNameKana;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public void setTownCode(Long townCode) {
		this.townCode = townCode;
	}

	public void setTownName(String townName) {
		this.townName = townName;
	}

	public void setTownNameKana(String townNameKana) {
		this.townNameKana = townNameKana;
	}

	public void setTownNote(String townNote) {
		this.townNote = townNote;
	}

	public String toDisplayString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(StringUtils.stripToEmpty(this.getPrefectureName()));
		stringBuffer.append(StringUtils.stripToEmpty(this.getCityName()));
		if (this.isOffice()) {
			stringBuffer.append(StringUtils.stripToEmpty(this.getOfficeAddress()));
			stringBuffer.append(StringUtils.stripToEmpty(this.getOfficeName()));
		} else {
			stringBuffer.append(StringUtils.stripToEmpty(this.getTownName()));
			stringBuffer.append(StringUtils.stripToEmpty(this.getBlockNumber()));
			stringBuffer.append(StringUtils.stripToEmpty(this.getStreetName()));
		}

		return stringBuffer.toString();
	}
}
