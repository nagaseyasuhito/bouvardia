package com.github.nagaseyasuhito.bouvardia.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.VFS;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.StrReplace;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.Unique;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.CSVContext;

import com.github.nagaseyasuhito.bouvardia.dao.AddressDao;
import com.github.nagaseyasuhito.bouvardia.entity.Address;
import com.google.common.collect.Lists;
import com.ibm.icu.text.Transliterator;

public class AddressService {

	@Inject
	private AddressDao addressDao;

	private static class KatakanaToHiragana extends CellProcessorAdaptor implements StringCellProcessor {
		private static final Transliterator KATAKANA_TO_HIRAGANA = Transliterator.getInstance("Katakana-Hiragana");

		@Override
		public Object execute(Object value, CSVContext context) {
			return KatakanaToHiragana.KATAKANA_TO_HIRAGANA.transform(value.toString());
		}
	}

	private static final String[] PROPERTIES = new String[] { "code", "prefectureCode", "cityCode", "townCode", "postalCode", "office", "obsoleted", "prefectureName", "prefectureNameKana", "cityName", "cityNameKana", "townName", "townNameKana",
			"townNote", "streetName", "blockNumber", "blockNumberKana", "note", "officeName", "officeNameKana", "officeAddress" };

	private CellProcessor[] buildCellProcessors() {
		List<CellProcessor> cellProcessors = Lists.newArrayList();
		cellProcessors.add(new NotNull(new Unique(new ParseLong()))); // code
		cellProcessors.add(new NotNull(new ParseLong())); // prefectureCode
		cellProcessors.add(new NotNull(new ParseLong())); // cityCode
		cellProcessors.add(new NotNull(new ParseLong())); // townCode
		cellProcessors.add(new NotNull(new StrReplace("-", ""))); // postalCode
		cellProcessors.add(new NotNull(new ParseBool())); // office
		cellProcessors.add(new NotNull(new ParseBool())); // obsoleted
		cellProcessors.add(new NotNull()); // prefectureName
		cellProcessors.add(new NotNull(new KatakanaToHiragana())); // prefectureNameKana
		cellProcessors.add(new NotNull()); // cityName
		cellProcessors.add(new NotNull(new KatakanaToHiragana())); // cityNameKana
		cellProcessors.add(new StrReplace("　", "", new Optional())); // townName
		cellProcessors.add(new StrReplace("　", "", new Optional(new KatakanaToHiragana()))); // townNameKana
		cellProcessors.add(new Optional()); // townNote
		cellProcessors.add(new Optional()); // streetName
		cellProcessors.add(new Optional()); // blockNumber
		cellProcessors.add(new Optional(new KatakanaToHiragana())); // blockNumberKana
		cellProcessors.add(new Optional()); // note
		cellProcessors.add(new Optional()); // officeName
		cellProcessors.add(new Optional(new KatakanaToHiragana())); // officeNameKana
		cellProcessors.add(new Optional()); // officeAddress
		cellProcessors.add(new Optional());

		return cellProcessors.toArray(new CellProcessor[0]);
	}

	@PostConstruct
	public void postConstruct() {
		try {
			FileObject fileObject = VFS.getManager().resolveFile("zip:http://jusyo.jp/downloads/new/csv/csv_zenkoku.zip!zenkoku.csv");
			Reader reader = new InputStreamReader(fileObject.getContent().getInputStream(), Charset.forName("MS932"));

			this.bulkImport(reader, false);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void bulkImport(Reader reader, boolean differential) throws IOException {
		CsvBeanReader csvBeanReader = new CsvBeanReader(reader, CsvPreference.STANDARD_PREFERENCE);
		csvBeanReader.getCSVHeader(true);

		Address entity = null;
		do {
			entity = csvBeanReader.read(Address.class, PROPERTIES, this.buildCellProcessors());

			if (entity != null) {
				if (differential) {
					try {
						Address address = this.findByCode(entity.getCode());

						address.setBlockNumber(entity.getBlockNumber());
						address.setBlockNumberKana(entity.getBlockNumberKana());
						address.setCityCode(entity.getCityCode());
						address.setCityName(entity.getCityName());
						address.setCityNameKana(entity.getCityNameKana());
						address.setCode(entity.getCode());
						address.setNote(entity.getNote());
						address.setObsoleted(entity.isObsoleted());
						address.setOffice(entity.isOffice());
						address.setOfficeAddress(entity.getOfficeAddress());
						address.setOfficeName(entity.getOfficeName());
						address.setOfficeNameKana(entity.getOfficeNameKana());
						address.setPostalCode(entity.getPostalCode());
						address.setPrefectureCode(entity.getPrefectureCode());
						address.setPrefectureName(entity.getPrefectureName());
						address.setPrefectureNameKana(entity.getPrefectureNameKana());
						address.setStreetName(entity.getStreetName());
						address.setTownCode(entity.getTownCode());
						address.setTownName(entity.getTownName());
						address.setTownNameKana(entity.getTownNameKana());
						address.setTownNote(entity.getTownNote());

						entity = address;
					} catch (NoResultException e) {
					}
				}

				this.addressDao.persistOrMerge(entity);
			}
		} while (entity != null);
	}

	public Address findByCode(Long code) {
		Address criteria = new Address();
		criteria.setCode(code);
		return this.addressDao.findByCriteria(criteria);
	}
}
