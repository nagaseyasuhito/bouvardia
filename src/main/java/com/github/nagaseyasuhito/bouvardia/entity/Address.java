package com.github.nagaseyasuhito.bouvardia.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.lucene.document.Document;
import org.hibernate.search.annotations.ClassBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;

import com.github.nagaseyasuhito.bouvardia.entity.Address.AddressBridge;
import com.github.nagaseyasuhito.fatsia.entity.BaseEntity;
import com.google.common.base.Strings;

@Entity
@XmlRootElement
@Indexed
@ClassBridge(name = "address", store = Store.YES, impl = AddressBridge.class)
public class Address extends BaseEntity<Long> {
    private static final long serialVersionUID = 1L;

    public static class AddressBridge implements FieldBridge {

        @Override
        public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
            Address address = (Address) value;

            String fieldValue = Strings.nullToEmpty(address.getPostalCode()) + Strings.nullToEmpty(address.getPrefectureName()) + Strings.nullToEmpty(address.getCityName())
                    + Strings.nullToEmpty(address.getTownName()) + Strings.nullToEmpty(address.getStreetName()) + Strings.nullToEmpty(address.getBlockNumber())
                    + Strings.nullToEmpty(address.getOfficeName()) + Strings.nullToEmpty(address.getOfficeAddress());

            org.apache.lucene.document.Field field = new org.apache.lucene.document.Field(name, fieldValue, luceneOptions.getStore(), luceneOptions.getIndex(), luceneOptions.getTermVector());
            field.setBoost(luceneOptions.getBoost());

            document.add(field);
        }
    }

    @Column
    @Field
    private String blockNumber;

    @Column
    @Field
    private String blockNumberKana;

    @Column(nullable = false)
    private Long cityCode;

    @Column(nullable = false)
    @Field
    private String cityName;

    @Column(nullable = false)
    @Field
    private String cityNameKana;

    @Id
    @Column(nullable = false, unique = true)
    @Field
    private Long code;

    @Column
    private String note;

    // @Column(nullable = false)
    // private Boolean obsoleted;

    @Column(nullable = false)
    private Boolean office;

    @Column
    @Field
    private String officeAddress;

    @Column
    @Field
    private String officeName;

    @Column
    @Field
    private String officeNameKana;

    @Column(nullable = false)
    @Field
    private String postalCode;

    @Column(nullable = false)
    private Long prefectureCode;

    @Column(nullable = false)
    @Field
    private String prefectureName;

    @Column(nullable = false)
    @Field
    private String prefectureNameKana;

    @Column
    @Field
    private String streetName;

    @Column(nullable = false)
    private Long townCode;

    @Column
    @Field
    private String townName;

    @Column
    @Field
    private String townNameKana;

    @Column
    private String townNote;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Override
    public Long getId() {
        return this.getCode();
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

    // public Boolean isObsoleted() {
    // return this.obsoleted;
    // }

    public Boolean isOffice() {
        return this.office;
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

    // public void setObsoleted(Boolean obsoleted) {
    // this.obsoleted = obsoleted;
    // }

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

    public Double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
