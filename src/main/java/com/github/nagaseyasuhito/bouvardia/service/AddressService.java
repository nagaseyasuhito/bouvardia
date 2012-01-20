package com.github.nagaseyasuhito.bouvardia.service;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Provider;

import jp.sf.orangesignal.csv.Csv;
import jp.sf.orangesignal.csv.CsvConfig;
import jp.sf.orangesignal.csv.filters.SimpleCsvNamedValueFilter;
import jp.sf.orangesignal.csv.handlers.ColumnNameMappingBeanListHandler;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.QueryBuilder;

import com.github.nagaseyasuhito.bouvardia.dao.AddressDao;
import com.github.nagaseyasuhito.bouvardia.entity.Address;
import com.github.nagaseyasuhito.bouvardia.entity.AddressSearchResult;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

public class AddressService {

    private static final Pattern TOWN_NAME_PATTERN = Pattern.compile("(.*?)([一二三四五六七八九十]+)丁目");

    private static final Pattern FIRST_ZERO_PATTERN = Pattern.compile("([^１２３４５６７８９]|^)０");

    private static final List<Character> NUMBERS = Lists.charactersOf("〇一二三四五六七八九");

    private static final List<Character> LOWER_DIGITS = Lists.charactersOf("十百千");

    private static final List<Character> HIGHER_DIGITS = Lists.charactersOf("万億兆");

    @Inject
    private AddressDao addressDao;

    @Inject
    private Provider<FullTextEntityManager> fullTextEntityManager;

    private static final String[] QUERY_TARGETS = new String[] {"postalCode", "prefectureName", "prefectureNameKana", "cityName", "cityNameKana", "townName", "townNameKana", "streetName",
            "blockNumber", "blockNumberKana", "officeName", "officeNameKana", "officeAddress" };

    public void bulkPersist(List<Address> addresses) throws IOException {
        for (Address address : addresses) {
            this.addressDao.persist(address);
        }
    }

    public List<Address> parseLocationData(Reader reader) throws IOException {
        ColumnNameMappingBeanListHandler<Address> columnNameMappingBeanListHandler = new ColumnNameMappingBeanListHandler<Address>(Address.class);

        columnNameMappingBeanListHandler.addColumn("都道府県コード", "prefectureCode");
        columnNameMappingBeanListHandler.addColumn("都道府県名", "prefectureName");
        columnNameMappingBeanListHandler.addColumn("市区町村コード", "cityCode");
        columnNameMappingBeanListHandler.addColumn("市区町村名", "cityName");
        // columnNameMappingBeanListHandler.addColumn("大字町丁目コード", "");
        columnNameMappingBeanListHandler.addColumn("大字町丁目名", "townName");
        columnNameMappingBeanListHandler.addColumn("緯度", "latitude");
        columnNameMappingBeanListHandler.addColumn("経度", "longitude");
        // columnNameMappingBeanListHandler.addColumn("原典資料コード", "");
        // columnNameMappingBeanListHandler.addColumn("大字・字・丁目区分コード", "");

        CsvConfig csvConfig = new CsvConfig();
        csvConfig.setQuoteDisabled(false);
        csvConfig.setIgnoreTrailingWhitespaces(true);
        csvConfig.setIgnoreLeadingWhitespaces(true);
        csvConfig.setIgnoreEmptyLines(true);
        csvConfig.setNullString("");

        List<Address> addresses = Csv.load(reader, csvConfig, columnNameMappingBeanListHandler);

        for (Address address : addresses) {
            this.splitTownNameAndBlockNumber(address);
        }
        return addresses;
    }

    public void splitTownNameAndBlockNumber(Address address) {
        Matcher matcher = TOWN_NAME_PATTERN.matcher(address.getTownName());
        if (matcher.matches()) {
            address.setTownName(matcher.group(1));
            address.setBlockNumber(this.convertToNumeric(matcher.group(2)) + "丁目");
        }
    }

    public String convertToNumeric(String source) {
        if (source == null) {
            return null;
        }

        int destination = 0, delta = 0, digit = 0;
        boolean carried = false;
        for (Character character : source.toCharArray()) {
            {
                int index = NUMBERS.indexOf(character);
                if (index != -1) {
                    if (carried) {
                        destination = destination * 10 + index % 10;
                    } else {
                        destination += index % 10;
                        carried = true;
                    }
                    continue;
                }
            }
            {
                int index = LOWER_DIGITS.indexOf(character);
                if (index != -1) {
                    digit += destination;
                    delta = digit % 10;
                    int carrying = (int) ((delta == 0 ? 1 : delta) * Math.pow(10, index % 3 + 1));
                    digit += carrying - delta;
                    destination = 0;
                    carried = false;
                    continue;
                }
            }
            {
                int index = HIGHER_DIGITS.indexOf(character);
                if (index != -1) {
                    digit += destination;
                    delta = digit % 10000;
                    int carrying = (int) (delta * Math.pow(10000, index % 3 + 1));
                    digit += carrying - delta;
                    destination = 0;
                    carried = false;
                    continue;
                }
            }
        }
        return StringUtils.replaceEach(Integer.toString(digit + destination), "0123456789".split(""), "０１２３４５６７８９".split(""));
    }

    public String removeFirstZero(String source) {
        if (source == null) {
            return null;
        }
        Matcher matcher = FIRST_ZERO_PATTERN.matcher(source);
        return matcher.find() ? matcher.replaceAll("$1") : source;
    }

    public List<Address> parsePostalCodeData(Reader reader) throws IOException {
        ColumnNameMappingBeanListHandler<Address> columnNameMappingBeanListHandler = new ColumnNameMappingBeanListHandler<Address>(Address.class);
        columnNameMappingBeanListHandler.addColumn("住所CD", "code");
        columnNameMappingBeanListHandler.addColumn("都道府県CD", "prefectureCode");
        columnNameMappingBeanListHandler.addColumn("市区町村CD", "cityCode");
        columnNameMappingBeanListHandler.addColumn("町域CD", "townCode");
        columnNameMappingBeanListHandler.addColumn("郵便番号", "postalCode");
        columnNameMappingBeanListHandler.addColumn("事業所フラグ", "office");
        // columnNameMappingBeanListHandler.addColumn("廃止フラグ", "obsoleted");
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
        // columnNameMappingBeanListHandler.addColumn("新住所CD", "");
        columnNameMappingBeanListHandler.filter(new SimpleCsvNamedValueFilter().eq("廃止フラグ", "0"));

        CsvConfig csvConfig = new CsvConfig();
        csvConfig.setQuoteDisabled(false);
        csvConfig.setIgnoreTrailingWhitespaces(true);
        csvConfig.setIgnoreLeadingWhitespaces(true);
        csvConfig.setIgnoreEmptyLines(true);
        csvConfig.setNullString("");

        List<Address> addresses = Csv.load(reader, csvConfig, columnNameMappingBeanListHandler);

        for (Address address : addresses) {
            address.setTownNameKana("　".equals(address.getTownNameKana()) ? null : address.getTownNameKana());

            address.setBlockNumber(this.removeFirstZero(address.getBlockNumber()));
            address.setBlockNumberKana(this.removeFirstZero(address.getBlockNumberKana()));
            address.setTownNameKana(this.removeFirstZero(address.getTownNameKana()));
        }
        return addresses;
    }

    public Multimap<String, Address> buildBlockNumberAddressMap(List<Address> addresses) {
        return Multimaps.index(Collections2.filter(addresses, new Predicate<Address>() {

            @Override
            public boolean apply(Address input) {
                return input.isOffice() == null || !input.isOffice();
            }

        }), new Function<Address, String>() {

            @Override
            public String apply(Address input) {
                return AddressService.this.obtainBlockNumberAddressKey(input);
            }
        });
    }

    private String obtainBlockNumberAddressKey(Address address) {
        return address.getPrefectureName() + " " + address.getCityName() + " " + address.getTownName() + " " + address.getBlockNumber();
    }

    public List<Address> mergeLocationFromLocationAddressToPostalCodeAddress(List<Address> postalCodeAddresses, List<Address> locationAddresses) {
        Multimap<String, Address> postalCodeAddressMap = this.buildBlockNumberAddressMap(postalCodeAddresses);

        for (Address locationAddress : locationAddresses) {
            String key = this.obtainBlockNumberAddressKey(locationAddress);
            if (postalCodeAddressMap.containsKey(key)) {
                for (Address postalCodeAddress : postalCodeAddressMap.get(key)) {
                    postalCodeAddress.setLatitude(locationAddress.getLatitude());
                    postalCodeAddress.setLongitude(locationAddress.getLongitude());
                }
            }
        }

        return postalCodeAddresses;
    }

    @SuppressWarnings("unchecked")
    public AddressSearchResult findByQuery(String query, int offset, int maxResults) {
        QueryBuilder addressQueryBuilder = this.fullTextEntityManager.get().getSearchFactory().buildQueryBuilder().forEntity(Address.class).get();

        Query luceneQuery = addressQueryBuilder.keyword().onFields("address").matching(query).createQuery();
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
