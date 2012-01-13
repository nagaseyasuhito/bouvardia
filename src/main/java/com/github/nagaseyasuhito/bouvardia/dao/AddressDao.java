package com.github.nagaseyasuhito.bouvardia.dao;

import com.github.nagaseyasuhito.bouvardia.entity.Address;
import com.github.nagaseyasuhito.fatsia.dao.impl.BaseDaoImpl;

public class AddressDao extends BaseDaoImpl<Address> {

	@Override
	public Class<Address> getEntityClass() {
		return Address.class;
	}
}
