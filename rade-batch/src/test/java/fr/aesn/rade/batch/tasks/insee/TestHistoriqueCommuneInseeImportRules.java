/*  This file is part of the Rade project (https://github.com/mgimpel/rade).
 *  Copyright (C) 2018 Marc Gimpel
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
/* $Id$ */
package fr.aesn.rade.batch.tasks.insee;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;

import org.springframework.core.io.ClassPathResource;
import org.junit.Ignore;
/**
 * JUnit Test for RegionMapper.
 *
 * @author Marc Gimpel (mgimpel@gmail.com)
 */
public class TestHistoriqueCommuneInseeImportRules {
  private static List<HistoriqueCommuneInseeModel> historyList;
  /**
   * Set up the Test Environment.
   * @throws Exception problem reading/mapping input file.
   */
  @BeforeClass
  public static void setUpClass() throws Exception {
    FlatFileItemReader<HistoriqueCommuneInseeModel> reader = new FlatFileItemReader<>();
    reader.setResource(new ClassPathResource("batchfiles/insee/mvtcommune2021-modified.csv"));
    reader.setLinesToSkip(1);
    DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
    tokenizer.setDelimiter(",");
    DefaultLineMapper<HistoriqueCommuneInseeModel> lineMapper = new DefaultLineMapper<>();
    lineMapper.setFieldSetMapper(new HistoriqueCommuneInseeMapper());
    lineMapper.setLineTokenizer(tokenizer);
    reader.setLineMapper(lineMapper);
    reader.afterPropertiesSet();
    ExecutionContext ec = new ExecutionContext();
    reader.open(ec);
    HistoriqueCommuneInseeModel record;
    historyList = new ArrayList<>();
    while((record = reader.read()) != null) {
      historyList.add(record);
    }
    historyList = historyList.stream()
    	.filter(history -> history.getTypeCommuneAprEven().equals("COM") 
    			&& history.getTypeCommuneAvantEven().equals("COM"))
    	.collect(Collectors.toList());
    assertEquals(8720, historyList.size());
    
  }

  /**
   * Test Building Mod=10 list.
   * @throws ParseException failed to parse date.
   */
  @Test
  public void testBuildMod10List() throws ParseException {
    List<HistoriqueCommuneInseeModel> testList = historyList;
    testList = HistoriqueCommuneInseeImportRules.buildModFilteredList(testList, "10");
    assertEquals(1341, testList.size());
    testList = filterList(historyList, "2018-01-01", "2019-01-01");
    testList = HistoriqueCommuneInseeImportRules.buildModFilteredList(testList, "10");
    assertEquals(11, testList.size());
  }

  /**
   * Test Building Mod=20 list.
   * @throws ParseException failed to parse date.
   */
  @Test
  public void testBuildMod20List() throws ParseException {
    List<HistoriqueCommuneInseeModel> testList = historyList;
    testList = HistoriqueCommuneInseeImportRules.buildModFilteredList(testList, "20");
    assertEquals(42, testList.size());
    testList = filterList(historyList, "2017-01-01", "2018-01-01");
    testList = HistoriqueCommuneInseeImportRules.buildModFilteredList(testList, "20");
    assertEquals(0, testList.size());
  }

  /**
   * Test Building Mod=21 list.
   * @throws ParseException failed to parse date.
   */
  @Test
  public void testBuildMod21List() throws ParseException {
    List<HistoriqueCommuneInseeModel> testList = historyList;
    List<HistoriqueCommuneInseeModel.Pair> pairList;
    pairList = HistoriqueCommuneInseeImportRules.buildModFilteredPairList(testList, "21", "21");
    assertEquals(571, pairList.size());
    for (HistoriqueCommuneInseeModel.Pair pair : pairList) {
      assertTrue(pair.isValid());
    }
    testList = filterList(historyList, "2018-01-01", "2019-01-01");
    pairList = HistoriqueCommuneInseeImportRules.buildModFilteredPairList(testList, "21", "21");
    assertEquals(0, pairList.size());
    for (HistoriqueCommuneInseeModel.Pair pair : pairList) {
      assertTrue(pair.isValid());
    }
  }

  /**
   * Test Building Mod=31x32 list.
   * @throws ParseException failed to parse date.
   */
  @Test
  public void testBuildMod31x32List() throws ParseException {
    List<HistoriqueCommuneInseeModel> testList = historyList;
    List<HistoriqueCommuneInseeModel.Pair> pairList;
    pairList = HistoriqueCommuneInseeImportRules.buildModFilteredPairList(testList, "31", "32");
    assertEquals(3882, pairList.size());
    for (HistoriqueCommuneInseeModel.Pair pair : pairList) {
      assertTrue(pair.isValid());
    }
    testList = filterList(historyList, "2018-01-01", "2019-01-01");
    pairList = HistoriqueCommuneInseeImportRules.buildModFilteredPairList(testList, "31", "32");
    assertEquals(95, pairList.size());
    for (HistoriqueCommuneInseeModel.Pair pair : pairList) {
      assertTrue(pair.isValid());
    }
  }

  /**
   * Test Building Mod=330x340 list.
   * @throws ParseException failed to parse date.
   */
  @Test
  public void testBuildMod33x34List() throws ParseException {
    List<HistoriqueCommuneInseeModel> testList = historyList;
    List<HistoriqueCommuneInseeModel.Pair> pairList;
    pairList = HistoriqueCommuneInseeImportRules.buildModFilteredPairList(testList, "33", "34");
    assertEquals(1898, pairList.size());
    for (HistoriqueCommuneInseeModel.Pair pair : pairList) {
      assertTrue(pair.isValid());
    }
    testList = filterList(historyList, "2018-01-01", "2019-01-01");
    pairList = HistoriqueCommuneInseeImportRules.buildModFilteredPairList(testList, "33", "34");
    assertEquals(5, pairList.size());
    for (HistoriqueCommuneInseeModel.Pair pair : pairList) {
      assertTrue(pair.isValid());
    }
  }


  /**
   * Test Building Mod=31x32 set.
   * @throws ParseException failed to parse date.
   */
  @Test
  public void testBuildMod31x32Set() throws ParseException {
    List<HistoriqueCommuneInseeModel> testList = historyList;
    List<HistoriqueCommuneInseeModel.Pair> pairList;
    List<HistoriqueCommuneInseeModel.Changeset> pairSet;
    pairList = HistoriqueCommuneInseeImportRules.buildModFilteredPairList(testList, "31", "32");
    int pairListSize = pairList.size();
    pairSet = HistoriqueCommuneInseeImportRules.buildModSet(pairList, "32","31");
    assertEquals(1434, pairSet.size());
    int count = 0;
    for (HistoriqueCommuneInseeModel.Changeset set : pairSet) {
      count += set.getPairs().size();
      assertTrue(set.isValid());
    }
    assertEquals(pairListSize, count);
    testList = filterList(historyList, "2018-01-01", "2019-01-01");
    pairList = HistoriqueCommuneInseeImportRules.buildModFilteredPairList(testList, "31", "32");
    pairListSize = pairList.size();
    pairSet = HistoriqueCommuneInseeImportRules.buildModSet(pairList, "32","31");
    assertEquals(37, pairSet.size());
    count = 0;
    for (HistoriqueCommuneInseeModel.Changeset set : pairSet) {
      count += set.getPairs().size();
      assertTrue(set.isValid());
    }
    assertEquals(pairListSize, count);
  }


  /**
   * Test Building Mod=330x340 set.
   * @throws ParseException failed to parse date.
   */
  @Test
  public void testBuildMod33x34Set() throws ParseException {
    List<HistoriqueCommuneInseeModel> testList = historyList;
    List<HistoriqueCommuneInseeModel.Pair> pairList;
    List<HistoriqueCommuneInseeModel.Changeset> pairSet;
    pairList = HistoriqueCommuneInseeImportRules.buildModFilteredPairList(testList, "33", "34");
    int pairListSize = pairList.size();
    pairSet = HistoriqueCommuneInseeImportRules.buildModSet(pairList, "34","33");
    assertEquals(847, pairSet.size());
    int count = 0;
    for (HistoriqueCommuneInseeModel.Changeset set : pairSet) {
      count += set.getPairs().size();
      assertTrue(set.isValid());
    }
    assertEquals(pairListSize, count);
    testList = filterList(historyList, "2018-01-01", "2019-01-01");
    pairList = HistoriqueCommuneInseeImportRules.buildModFilteredPairList(testList, "33", "34");
    pairListSize = pairList.size();
    pairSet = HistoriqueCommuneInseeImportRules.buildModSet(pairList, "34","33");
    assertEquals(5, pairSet.size());
    count = 0;
    for (HistoriqueCommuneInseeModel.Changeset set : pairSet) {
      count += set.getPairs().size();
      assertTrue(set.isValid());
    }
    assertEquals(pairListSize, count);
  }

  /**
   * Filter List for records between given dates.
   * @param list the list to filter.
   * @param start start Date.
   * @param end end Date.
   * @return the filtered list.
   * @throws ParseException failed to parse date.
   */
  private List<HistoriqueCommuneInseeModel> filterList(List<HistoriqueCommuneInseeModel> list,
                                                       String start,
                                                       String end)
    throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date startDate = sdf.parse(start);
    Date endDate = sdf.parse(end);
    return list.stream()
               .filter(history -> !history.getDateEffet().before(startDate)
                                && history.getDateEffet().before(endDate))
               .collect(Collectors.toList());
  }

  /**
   * Check that we can extract and sort a list of dates from the history file.
   * @throws ParseException failed to parse date.
   */
  @Test
  public void testBuildDistinctSortedDateList() throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    List<Date> dates = HistoriqueCommuneInseeImportRules.buildDistinctSortedDateList(historyList);
    assertEquals(858, dates.size());
    Date last = sdf.parse("1900-01-01");
    for (Date date : dates) {
      assertTrue(last.before(date));
      last = date;
    }
  }

  /**
   * Checks filtering History List by Date.
   */
  @Test
  public void testFilterListByDateWith1Date() {
    List<Date> dates = HistoriqueCommuneInseeImportRules.buildDistinctSortedDateList(historyList);
    int count = 0;
    int size;
    for (Date date : dates) {
      size = HistoriqueCommuneInseeImportRules.filterListByDate(historyList, date).size();
      assertTrue(size > 0);
      count += size;
    }
    assertEquals(historyList.size(), count);
  }
}
