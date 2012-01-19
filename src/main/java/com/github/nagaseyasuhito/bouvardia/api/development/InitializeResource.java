package com.github.nagaseyasuhito.bouvardia.api.development;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.VFS;

import com.github.nagaseyasuhito.bouvardia.entity.Address;
import com.github.nagaseyasuhito.bouvardia.service.AddressService;
import com.google.common.collect.Lists;
import com.google.inject.persist.Transactional;

@Path("initialize")
public class InitializeResource {

    @Inject
    private AddressService addressService;

    private static final String[] LOCATION_TARGET_FILENAMES = new String[] {"01000-04.1b.zip!01_2010.csv", "02000-04.0b.zip!02_2010.csv", "03000-04.0b.zip!03_2010.csv", "04000-04.1b.zip!04_2010.csv",
            "05000-04.0b.zip!05_2010.csv", "06000-04.0b.zip!06_2010.csv", "07000-04.1b.zip!07_2010.csv", "08000-04.0b.zip!08_2010.csv", "09000-04.1b.zip!09_2010.csv", "10000-04.0b.zip!10_2010.csv",
            "11000-04.1b.zip!11_2010.csv", "12000-04.0b.zip!12_2010.csv", "13000-04.0b.zip!13_2010.csv", "14000-04.1b.zip!14_2010.csv", "15000-04.0b.zip!15_2010.csv", "16000-04.0b.zip!16_2010.csv",
            "17000-04.0b.zip!17_2010.csv", "18000-04.0b.zip!18_2010.csv", "19000-04.1b.zip!19_2010.csv", "20000-04.1b.zip!20_2010.csv", "21000-04.0b.zip!21_2010.csv", "22000-04.0b.zip!22_2010.csv",
            "23000-04.0b.zip!23_2010.csv", "24000-04.0b.zip!24_2010.csv", "25000-04.0b.zip!25_2010.csv", "26000-04.0b.zip!26_2010.csv", "27000-04.0b.zip!27_2010.csv", "28000-04.0b.zip!28_2010.csv",
            "29000-04.0b.zip!29_2010.csv", "30000-04.0b.zip!30_2010.csv", "31000-04.0b.zip!31_2010.csv", "32000-04.0b.zip!32_2010.csv", "33000-04.0b.zip!33_2010.csv", "34000-04.0b.zip!34_2010.csv",
            "35000-04.0b.zip!35_2010.csv", "36000-04.0b.zip!36_2010.csv", "37000-04.0b.zip!37_2010.csv", "38000-04.0b.zip!38_2010.csv", "39000-04.0b.zip!39_2010.csv", "40000-04.0b.zip!40_2010.csv",
            "41000-04.0b.zip!41_2010.csv", "42000-04.0b.zip!42_2010.csv", "43000-04.0b.zip!43_2010.csv", "44000-04.0b.zip!44_2010.csv", "45000-04.0b.zip!45_2010.csv", "46000-04.1b.zip!46_2010.csv",
            "47000-04.0b.zip!47_2010.csv", };

    private static final String POSTAL_CODE_TARGET_FILENAME = "csv_zenkoku.zip!zenkoku.csv";

    private static final String STORAGE_URL = "zip:http://github.com/downloads/nagaseyasuhito/bouvardia/";

    @GET
    @Transactional
    public void initialize() throws IOException {
        List<Address> postalCodeAddresses = Lists.newArrayList();
        List<Address> locationAddresses = Lists.newArrayList();

        {
            FileObject fileObject = VFS.getManager().resolveFile(STORAGE_URL + POSTAL_CODE_TARGET_FILENAME);
            Reader reader = new InputStreamReader(fileObject.getContent().getInputStream(), Charset.forName("MS932"));

            postalCodeAddresses.addAll(this.addressService.parsePostalCodeData(reader));
        }

        for (String locationTargetFilename : LOCATION_TARGET_FILENAMES) {
            FileObject fileObject = VFS.getManager().resolveFile(STORAGE_URL + locationTargetFilename);
            Reader reader = new InputStreamReader(fileObject.getContent().getInputStream(), Charset.forName("MS932"));

            locationAddresses.addAll(this.addressService.parseLocationData(reader));
        }

        this.addressService.bulkPersist(this.addressService.mergeLocationFromLocationAddressToPostalCodeAddress(postalCodeAddresses, locationAddresses));
    }
}
