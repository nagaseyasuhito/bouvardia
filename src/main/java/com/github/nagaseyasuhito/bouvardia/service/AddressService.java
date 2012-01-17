package com.github.nagaseyasuhito.bouvardia.service;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import jp.sf.orangesignal.csv.Csv;
import jp.sf.orangesignal.csv.CsvConfig;
import jp.sf.orangesignal.csv.handlers.ColumnNameMappingBeanListHandler;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.QueryBuilder;

import com.github.nagaseyasuhito.bouvardia.dao.AddressDao;
import com.github.nagaseyasuhito.bouvardia.entity.Address;
import com.github.nagaseyasuhito.bouvardia.entity.AddressSearchResult;

public class AddressService {

    @Inject
    private AddressDao addressDao;

    @Inject
    private Provider<FullTextEntityManager> fullTextEntityManager;

    private static final String[] QUERY_TARGETS = new String[] {"postalCode", "prefectureName", "prefectureNameKana", "cityName", "cityNameKana", "townName", "townNameKana", "streetName",
            "blockNumber", "blockNumberKana", "officeName", "officeNameKana", "officeAddress" };

    public void bulkImport(Reader reader) throws IOException {
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

        for (Address address : addresses) {
            address.setTownNameKana("　".equals(address.getTownNameKana()) ? null : address.getTownNameKana());
            this.addressDao.persist(address);
        }
    }

    @SuppressWarnings("unchecked")
    public AddressSearchResult findByQuery(String query, int offset, int maxResults) {
        QueryBuilder addressQueryBuilder = this.fullTextEntityManager.get().getSearchFactory().buildQueryBuilder().forEntity(Address.class).get();

        Query luceneQuery = addressQueryBuilder.keyword().onFields(QUERY_TARGETS).matching(query).createQuery();
        FullTextQuery fullTextQuery = this.fullTextEntityManager.get().createFullTextQuery(luceneQuery, this.addressDao.getEntityClass());
        fullTextQuery.setFirstResult(offset * maxResults);
        fullTextQuery.setMaxResults(maxResults);

        AddressSearchResult result = new AddressSearchResult();
        result.setAddresses(fullTextQuery.getResultList());
        result.setTotalNumberOfResults(fullTextQuery.getResultSize());
        result.setCurrentNumberOfResults(result.getAddresses().size());
        result.setOffset(offset);

        return result;
    }

    public Address findByCode(Long code) {
        Address criteria = new Address();
        criteria.setCode(code);
        return this.addressDao.findByCriteria(criteria);
    }
}
