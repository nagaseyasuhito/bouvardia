package com.github.nagaseyasuhito.bouvardia.dao;

import com.github.nagaseyasuhito.bouvardia.entity.Address;
import com.github.nagaseyasuhito.fatsia.dao.BaseDao;

public class AddressDao extends BaseDao<Long, Address> {

	@Override
	public Class<Address> getEntityClass() {
		return Address.class;
	}
}
