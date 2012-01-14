package com.github.nagaseyasuhito.bouvardia.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.github.nagaseyasuhito.fatsia.entity.BaseEntity;

@Entity
public class Address extends BaseEntity<Long> {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	@Column
	private CharSequence blockNumber;

	@Column
	private CharSequence blockNumberKana;

	@Column(nullable = false)
	private Long cityCode;

	@Column(nullable = false)
	private CharSequence cityName;

	@Column(nullable = false)
	private CharSequence cityNameKana;

	@Column(nullable = false, unique = true)
	private Long code;

	@Column
	private CharSequence note;

	@Column(nullable = false)
	private Boolean obsoleted;

	@Column(nullable = false)
	private Boolean office;

	@Column
	private CharSequence officeAddress;

	@Column
	private CharSequence officeName;

	@Column
	private CharSequence officeNameKana;

	@Column(nullable = false)
	private CharSequence postalCode;

	@Column(nullable = false)
	private Long prefectureCode;

	@Column(nullable = false)
	private CharSequence prefectureName;

	@Column(nullable = false)
	private CharSequence prefectureNameKana;

	@Column
	private CharSequence streetName;

	@Column(nullable = false)
	private Long townCode;

	@Column
	private CharSequence townName;

	@Column
	private CharSequence townNameKana;

	@Column
	private CharSequence townNote;

	@Override
	public Long getId() {
		return this.id;
	}

	public CharSequence getBlockNumber() {
		return this.blockNumber;
	}

	public CharSequence getBlockNumberKana() {
		return this.blockNumberKana;
	}

	public Long getCityCode() {
		return this.cityCode;
	}

	public CharSequence getCityName() {
		return this.cityName;
	}

	public CharSequence getCityNameKana() {
		return this.cityNameKana;
	}

	public Long getCode() {
		return this.code;
	}

	public CharSequence getNote() {
		return this.note;
	}

	public CharSequence getOfficeAddress() {
		return this.officeAddress;
	}

	public CharSequence getOfficeName() {
		return this.officeName;
	}

	public CharSequence getOfficeNameKana() {
		return this.officeNameKana;
	}

	public CharSequence getPostalCode() {
		return this.postalCode;
	}

	public Long getPrefectureCode() {
		return this.prefectureCode;
	}

	public CharSequence getPrefectureName() {
		return this.prefectureName;
	}

	public CharSequence getPrefectureNameKana() {
		return this.prefectureNameKana;
	}

	public CharSequence getStreetName() {
		return this.streetName;
	}

	public Long getTownCode() {
		return this.townCode;
	}

	public CharSequence getTownName() {
		return this.townName;
	}

	public CharSequence getTownNameKana() {
		return this.townNameKana;
	}

	public CharSequence getTownNote() {
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

	public void setBlockNumber(CharSequence blockNumber) {
		this.blockNumber = blockNumber;
	}

	public void setBlockNumberKana(CharSequence blockNumberKana) {
		this.blockNumberKana = blockNumberKana;
	}

	public void setCityCode(Long cityCode) {
		this.cityCode = cityCode;
	}

	public void setCityName(CharSequence cityName) {
		this.cityName = cityName;
	}

	public void setCityNameKana(CharSequence cityNameKana) {
		this.cityNameKana = cityNameKana;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public void setNote(CharSequence note) {
		this.note = note;
	}

	public void setObsoleted(Boolean obsoleted) {
		this.obsoleted = obsoleted;
	}

	public void setOffice(Boolean office) {
		this.office = office;
	}

	public void setOfficeAddress(CharSequence officeAddress) {
		this.officeAddress = officeAddress;
	}

	public void setOfficeName(CharSequence officeName) {
		this.officeName = officeName;
	}

	public void setOfficeNameKana(CharSequence officeNameKana) {
		this.officeNameKana = officeNameKana;
	}

	public void setPostalCode(CharSequence postalCode) {
		this.postalCode = postalCode;
	}

	public void setPrefectureCode(Long prefectureCode) {
		this.prefectureCode = prefectureCode;
	}

	public void setPrefectureName(CharSequence prefectureName) {
		this.prefectureName = prefectureName;
	}

	public void setPrefectureNameKana(CharSequence prefectureNameKana) {
		this.prefectureNameKana = prefectureNameKana;
	}

	public void setStreetName(CharSequence streetName) {
		this.streetName = streetName;
	}

	public void setTownCode(Long townCode) {
		this.townCode = townCode;
	}

	public void setTownName(CharSequence townName) {
		this.townName = townName;
	}

	public void setTownNameKana(CharSequence townNameKana) {
		this.townNameKana = townNameKana;
	}

	public void setTownNote(CharSequence townNote) {
		this.townNote = townNote;
	}
}
