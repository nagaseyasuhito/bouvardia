package com.github.nagaseyasuhito.dao.impl;

import com.github.nagaseyasuhito.dao.AddressDao;
import com.github.nagaseyasuhito.entity.Address;
import com.github.nagaseyasuhito.fatsia.dao.impl.BaseDaoImpl;

public class AddressDaoImpl extends BaseDaoImpl<Address> implements AddressDao {

    @Override
    public Class<Address> getEntityClass() {
        return Address.class;
    }
}
