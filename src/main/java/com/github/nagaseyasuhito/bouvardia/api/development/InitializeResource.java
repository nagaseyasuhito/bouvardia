package com.github.nagaseyasuhito.bouvardia.api.development;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.VFS;

import com.github.nagaseyasuhito.bouvardia.service.AddressService;
import com.google.inject.persist.Transactional;

@Path("initialize")
public class InitializeResource {

    @Inject
    private AddressService addressService;

    @GET
    @Transactional
    public void initialize() {
        try {
            FileObject fileObject = VFS.getManager().resolveFile("zip:http://jusyo.jp/downloads/new/csv/csv_zenkoku.zip!zenkoku.csv");
            Reader reader = new InputStreamReader(fileObject.getContent().getInputStream(), Charset.forName("MS932"));

            this.addressService.bulkImport(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
