package com.github.nagaseyasuhito.bouvardia.service;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import javax.inject.Inject;

import jp.sf.orangesignal.csv.Csv;
import jp.sf.orangesignal.csv.CsvConfig;
import jp.sf.orangesignal.csv.handlers.ColumnNameMappingBeanListHandler;

import com.github.nagaseyasuhito.bouvardia.dao.AddressDao;
import com.github.nagaseyasuhito.bouvardia.entity.Address;
import com.github.nagaseyasuhito.bouvardia.entity.AddressSearchResult;
import com.google.common.collect.ImmutableSortedMap;

public class AddressService {

	@Inject
	private AddressDao addressDao;

	public void bulkImport(Reader reader, boolean differential) throws IOException {
		ColumnNameMappingBeanListHandler<Address> columnNameMappingBeanListHandler = new ColumnNameMappingBeanListHandler<Address>(Address.class);
		columnNameMappingBeanListHandler.addColumn("住所CD", "code");
		columnNameMappingBeanListHandler.addColumn("都道府県CD", "prefectureCode");
		columnNameMappingBeanListHandler.addColumn("市区町村CD", "cityCode");
		columnNameMappingBeanListHandler.addColumn("町域CD", "townCode");
		columnNameMappingBeanListHandler.addColumn("郵便番号", "postalCode");
		columnNameMappingBeanListHandler.addColumn("事業所フラグ", "office");
		columnNameMappingBeanListHandler.addColumn("廃止フラグ", "obsoleted");
		columnNameMappingBeanListHandler.addColumn("都道府県", "prefectureName");
		columnNameMappingBeanListHandler.addColumn("都道府県カナ", "prefectureNameKana");
		columnNameMappingBeanListHandler.addColumn("市区町村", "cityName");
		columnNameMappingBeanListHandler.addColumn("市区町村カナ", "cityNameKana");
		columnNameMappingBeanListHandler.addColumn("町域", "townName");
		columnNameMappingBeanListHandler.addColumn("町域カナ", "townNameKana");
		columnNameMappingBeanListHandler.addColumn("町域補足", "townNote");
		columnNameMappingBeanListHandler.addColumn("京都通り名", "streetName");
		columnNameMappingBeanListHandler.addColumn("字丁目", "blockNumber");
		columnNameMappingBeanListHandler.addColumn("字丁目カナ", "blockNumberKana");
		columnNameMappingBeanListHandler.addColumn("補足", "note");
		columnNameMappingBeanListHandler.addColumn("事業所名", "officeName");
		columnNameMappingBeanListHandler.addColumn("事業所名カナ", "officeNameKana");
		columnNameMappingBeanListHandler.addColumn("事業所住所", "officeAddress");

		CsvConfig csvConfig = new CsvConfig();
		csvConfig.setQuoteDisabled(false);
		csvConfig.setIgnoreTrailingWhitespaces(true);
		csvConfig.setIgnoreLeadingWhitespaces(true);
		csvConfig.setIgnoreEmptyLines(true);
		csvConfig.setNullString("");

		List<Address> addresses = Csv.load(reader, csvConfig, columnNameMappingBeanListHandler);

		for (Address address : addresses.subList(0, 100)) {
			address.setTownNameKana("　".equals(address.getTownNameKana()) ? null : address.getTownNameKana());
			this.addressDao.persist(address);
		}
	}

	public AddressSearchResult findByQuery(String query) {
		AddressSearchResult result = new AddressSearchResult();

		Address criteria = new Address();
		result.setAddresses(this.addressDao.findByCriteria(criteria, ImmutableSortedMap.<String, Boolean> of("code", true)));
		result.setNumberOfResults((long) result.getAddresses().size());

		return result;
	}

	public Address findByCode(Long code) {
		Address criteria = new Address();
		criteria.setCode(code);
		return this.addressDao.findByCriteria(criteria);
	}
}
