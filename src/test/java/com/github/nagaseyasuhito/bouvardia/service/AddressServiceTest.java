package com.github.nagaseyasuhito.bouvardia.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.VFS;
import org.junit.Test;

import com.github.nagaseyasuhito.bouvardia.entity.Address;

public class AddressServiceTest {

    @Test
    public void parseLocationDataSuccessed() throws IOException {
        AddressService addressService = new AddressService();

        FileObject fileObject = VFS.getManager().resolveFile("zip:https://github.com/nagaseyasuhito/bouvardia/raw/master/data-source/01000-04.1b.zip!01_2010.csv");
        Reader reader = new InputStreamReader(fileObject.getContent().getInputStream(), Charset.forName("MS932"));

        addressService.parseLocationData(reader);
    }

    @Test
    public void parsePostalCodeDataSuccessed() throws IOException {
        AddressService addressService = new AddressService();

        FileObject fileObject = VFS.getManager().resolveFile("zip:https://github.com/nagaseyasuhito/bouvardia/raw/master/data-source/csv_zenkoku.zip!zenkoku.csv");
        Reader reader = new InputStreamReader(fileObject.getContent().getInputStream(), Charset.forName("MS932"));

        addressService.parsePostalCodeData(reader);
    }

    @Test
    public void mergeLocationFromLocationAddressToPostalCodeAddressSuccessed() throws IOException {
        AddressService addressService = new AddressService();

        List<Address> locationAddresses, postalCodeAddresses;
        {
            FileObject fileObject = VFS.getManager().resolveFile("zip:https://github.com/nagaseyasuhito/bouvardia/raw/master/data-source/01000-04.1b.zip!01_2010.csv");
            Reader reader = new InputStreamReader(fileObject.getContent().getInputStream(), Charset.forName("MS932"));

            locationAddresses = addressService.parseLocationData(reader);
        }

        {
            FileObject fileObject = VFS.getManager().resolveFile("zip:https://github.com/nagaseyasuhito/bouvardia/raw/master/data-source/csv_zenkoku.zip!zenkoku.csv");
            Reader reader = new InputStreamReader(fileObject.getContent().getInputStream(), Charset.forName("MS932"));

            postalCodeAddresses = addressService.parsePostalCodeData(reader);
        }

        addressService.mergeLocationFromLocationAddressToPostalCodeAddress(postalCodeAddresses, locationAddresses);
    }

    @Test
    public void removeFirstZeroSuccessed() {
        AddressService addressService = new AddressService();
        Assert.assertEquals("キタ１ジョウニシ", addressService.removeFirstZero("キタ０１ジョウニシ"));
        Assert.assertEquals("１０チョウメ", addressService.removeFirstZero("１０チョウメ"));
    }

    @Test
    public void convertToNumericSuccessed() {
        AddressService addressService = new AddressService();
        Assert.assertEquals("０", addressService.convertToNumeric("〇"));
        Assert.assertEquals("１", addressService.convertToNumeric("一"));
        Assert.assertEquals("１０", addressService.convertToNumeric("一〇"));
        Assert.assertEquals("１０", addressService.convertToNumeric("十"));
    }
}
