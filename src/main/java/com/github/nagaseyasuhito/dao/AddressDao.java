package com.github.nagaseyasuhito.dao;

import com.github.nagaseyasuhito.dao.impl.AddressDaoImpl;
import com.github.nagaseyasuhito.entity.Address;
import com.github.nagaseyasuhito.fatsia.dao.BaseDao;
import com.google.inject.ImplementedBy;

@ImplementedBy(AddressDaoImpl.class)
public interface AddressDao extends BaseDao<Address> {
}
