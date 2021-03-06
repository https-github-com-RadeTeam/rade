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
package fr.aesn.rade.persist.dao;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import fr.aesn.rade.persist.model.Commune;

/**
 * JUnit Test for CommuneJpaDao.
 * 
 * @author Marc Gimpel (mgimpel@gmail.com)
 */
public class TestCommuneJpaDao extends AbstractTestJpaDao {
  /** DAO to be tested. */
  @Autowired
  private CommuneJpaDao jpaDao;

  /**
   * Set up the Test Environment.
   */
  @BeforeClass
  public static void setUpClass() {
    // create temporary database for Hibernate
    db = new EmbeddedDatabaseBuilder()
        .setType(EmbeddedDatabaseType.DERBY)
        .setScriptEncoding("UTF-8")
        .setName("testdb")
        .addScript("db/sql/create-tables.sql")
        .addScript("db/sql/insert-StatutModification.sql")
        .addScript("db/sql/insert-TypeEntiteAdmin.sql")
        .addScript("db/sql/insert-TypeGenealogieEntiteAdmin.sql")
        .addScript("db/sql/insert-TypeNomClair.sql")
        .addScript("db/sql/insert-Audit.sql")
        .addScript("db/sql/insert-CirconscriptionBassin.sql")
        .addScript("db/sql/insert-Region.sql")
        .addScript("db/sql/insert-Departement.sql")
        .addScript("db/sql/insert-Commune-Test.sql")
        .addScript("db/sql/insert-CommuneGenealogie-Test.sql")
        .build();
  }

  /**
   * Test getting a the list of all Entity.
   */
  @Test
  public void testGettingEntityList() {
    List<Commune> list = jpaDao.findAll();
    assertNotNull("JpaDao returned a null list", list);
    assertEquals(639, list.size());
    for (Commune obj : list) {
      assertNotNull("Hibernate returned a List but an Entity is null",
                    obj);
    }
  }

  /**
   * Test existence of Entity.
   */
  @Test
  public void testExistsEntity() {
    assertTrue(jpaDao.existsById(37062));
    assertTrue(jpaDao.existsById(37570));
    assertFalse(jpaDao.existsById(200000));
    assertFalse(jpaDao.existsById(0));
    assertFalse(jpaDao.existsById(-1));
  }

  /**
   * Test getting an Entity.
   * @throws ParseException failed to parse date.
   */
  @Test
  public void testGettingEntity() throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Optional<Commune> result = jpaDao.findById(37570);
    assertTrue("Hibernate didn't return an Entity", result.isPresent());
    Commune com = result.get();
    assertEquals("Hibernate returned a Commune, but the Id doesn't match",
                 37570, com.getId().intValue());
    assertEquals("Hibernate returned a Commune, but a field doesn't match",
                 sdf.parse("1999-01-01"), com.getDebutValidite());
    assertNull("Hibernate returned a Commune, but a field doesn't match",
               com.getFinValidite());
    assertNull("Hibernate returned a Commune, but a field doesn't match",
               com.getArticleEnrichi());
    assertEquals("Hibernate returned a Commune, but a field doesn't match",
                 "BASSE-TERRE", com.getNomMajuscule());
    assertEquals("Hibernate returned a Commune, but a field doesn't match",
                 "Basse-Terre", com.getNomEnrichi());
    assertEquals("Hibernate returned a Commune, but a field doesn't match",
                 "", com.getCommentaire());
    assertEquals("Hibernate returned a Commune, but a field doesn't match",
                 "0", com.getTypeNomClair().getCode());
    assertEquals("Hibernate returned a Commune, but a field doesn't match",
                 "COM", com.getTypeEntiteAdmin().getCode());
    assertEquals("Hibernate returned a Commune, but a field doesn't match",
                 1, com.getAudit().getId().intValue());
    assertEquals("Hibernate returned a Commune, but a field doesn't match",
                 "97105", com.getCodeInsee());
    assertEquals("Hibernate returned a Commune, but a field doesn't match",
                 "971", com.getDepartement());
  }

  /**
   * Test getting an Entity using search criteria.
   */
  @Test
  public void testGettingEntitySearch() {
    Commune criteria = new Commune();
    criteria.setId(37570);
    List<Commune> list = jpaDao.findAll(Example.of(criteria));
    assertEquals("", 1, list.size());
    Commune resultat = list.get(0);
    assertNotNull("", resultat);
  }

  /**
   * Test custom repository method.
   * @throws ParseException failed to parse date.
   */
  @Test
  public void testFindByCodeInsee() throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    List<Commune> result = jpaDao.findByCodeInsee("97105");
    assertEquals("Hibernate returned the wrong number of results",
                 1, result.size());
    Commune com = result.get(0);
    assertEquals("Hibernate returned a Commune, but the Id doesn't match",
                 37570, com.getId().intValue());
    assertEquals("Hibernate returned a Commune, but a field doesn't match",
                 sdf.parse("1999-01-01"), com.getDebutValidite());
    assertNull("Hibernate returned a Commune, but a field doesn't match",
               com.getFinValidite());
    assertNull("Hibernate returned a Commune, but a field doesn't match",
               com.getArticleEnrichi());
    assertEquals("Hibernate returned a Commune, but a field doesn't match",
                 "BASSE-TERRE", com.getNomMajuscule());
    assertEquals("Hibernate returned a Commune, but a field doesn't match",
                 "Basse-Terre", com.getNomEnrichi());
    assertEquals("Hibernate returned a Commune, but a field doesn't match",
                 "", com.getCommentaire());
    assertEquals("Hibernate returned a Commune, but a field doesn't match",
                 "0", com.getTypeNomClair().getCode());
    assertEquals("Hibernate returned a Commune, but a field doesn't match",
                 "COM", com.getTypeEntiteAdmin().getCode());
    assertEquals("Hibernate returned a Commune, but a field doesn't match",
                 1, com.getAudit().getId().intValue());
    assertEquals("Hibernate returned a Commune, but a field doesn't match",
                 "97105", com.getCodeInsee());
    assertEquals("Hibernate returned a Commune, but a field doesn't match",
                 "971", com.getDepartement());
  }

  /**
   * Test custom repository method.
   * @throws ParseException failed to parse date.
   */
  @Test
  public void testFindAllValidOnDate() throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    List<Commune> result;
    // Aucune Commune n'est valable avant 01/01/1999
    result = jpaDao.findAllValidOnDate(sdf.parse("1998-01-01"));
    assertEquals("Hibernate returned the wrong number of results",
                 0, result.size());
    // Import complet au 01/01/1999
    result = jpaDao.findAllValidOnDate(sdf.parse("1999-01-01"));
    assertEquals("Hibernate returned the wrong number of results",
                 618, result.size());
    // St-Barthelemy et St-Martin deviennent Collectivité d'outre-mer 15/7/2007
    result = jpaDao.findAllValidOnDate(sdf.parse("2007-07-15"));
    assertEquals("Hibernate returned the wrong number of results",
                 616, result.size());
    // Changement de nom de Arnouville 11/07/20010
    result = jpaDao.findAllValidOnDate(sdf.parse("2010-07-11"));
    assertEquals("Hibernate returned the wrong number of results",
                 616, result.size());
    // Mayotte devient un DOM
    result = jpaDao.findAllValidOnDate(sdf.parse("2012-01-01"));
    assertEquals("Hibernate returned the wrong number of results",
                 633, result.size()); 
    // Fusion de Avernes et Gadancourt au 01/01/2018
    result = jpaDao.findAllValidOnDate(sdf.parse("2017-12-31"));
    assertEquals("Hibernate returned the wrong number of results",
                 633, result.size());
    result = jpaDao.findAllValidOnDate(sdf.parse("2018-01-01"));
    assertEquals("Hibernate returned the wrong number of results",
                 632, result.size());
  }

  /**
   * Test custom repository method.
   * @throws ParseException failed to parse date.
   */
  @Test
  public void testFindByCodeInseeValidOnDate() throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Commune com;
    com = jpaDao.findByCodeInseeValidOnDate("97105", sdf.parse("2018-01-01"));
    assertNotNull("Hibernate returned null", com);
    assertEquals("Hibernate returned a Commune, but the Id doesn't match",
                 37570, com.getId().intValue());
    assertEquals("Hibernate returned a Commune, but a field doesn't match",
                 sdf.parse("1999-01-01"), com.getDebutValidite());
    assertNull("Hibernate returned a Commune, but a field doesn't match",
               com.getFinValidite());
    assertNull("Hibernate returned a Commune, but a field doesn't match",
               com.getArticleEnrichi());
    assertEquals("Hibernate returned a Commune, but a field doesn't match",
                 "BASSE-TERRE", com.getNomMajuscule());
    assertEquals("Hibernate returned a Commune, but a field doesn't match",
                 "Basse-Terre", com.getNomEnrichi());
    assertEquals("Hibernate returned a Commune, but a field doesn't match",
                 "", com.getCommentaire());
    assertEquals("Hibernate returned a Commune, but a field doesn't match",
                 "0", com.getTypeNomClair().getCode());
    assertEquals("Hibernate returned a Commune, but a field doesn't match",
                 "COM", com.getTypeEntiteAdmin().getCode());
    assertEquals("Hibernate returned a Commune, but a field doesn't match",
                 1, com.getAudit().getId().intValue());
    assertEquals("Hibernate returned a Commune, but a field doesn't match",
                 "97105", com.getCodeInsee());
    assertEquals("Hibernate returned a Commune, but a field doesn't match",
                 "971", com.getDepartement());
    com = jpaDao.findByCodeInseeValidOnDate("97105", sdf.parse("1998-01-01"));
    assertNull("Hibernate return a Commune it shouldn't have", com);
  }

  /**
   * Test custom repository method.
   * @throws ParseException failed to parse date.
   */
  @Test
  public void testFindByDepartementValidOnDate() throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    List<Commune> result;
    result = jpaDao.findByDepartementValidOnDate("91", sdf.parse("2018-01-01"));
    assertEquals("Hibernate returned the wrong number of results",
                 196, result.size());
    result = jpaDao.findByDepartementValidOnDate("95", sdf.parse("2018-01-01"));
    assertEquals("Hibernate returned the wrong number of results",
                 184, result.size());
    result = jpaDao.findByDepartementValidOnDate("95", sdf.parse("2017-12-31"));
    assertEquals("Hibernate returned the wrong number of results",
                 185, result.size());
    result = jpaDao.findByDepartementValidOnDate("100", sdf.parse("2018-01-01"));
    assertEquals("Hibernate returned the wrong number of results",
                 0, result.size());
  }

  /**
   * Test custom repository method.
   * @throws ParseException failed to parse date.
   */
  @Test
  public void testFindByNameLikeValidOnDate() throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    List<Commune> result;
    result = jpaDao.findByNameLikeValidOnDate("SaInt-", sdf.parse("2018-01-01"));
    assertEquals("Hibernate returned the wrong number of results",
                 60, result.size());
    result = jpaDao.findByNameLikeValidOnDate("aaaa", sdf.parse("2018-01-01"));
    assertEquals("Hibernate returned the wrong number of results",
                 0, result.size());
  }

  /**
   * Test custom repository method.
   * @throws ParseException failed to parse date.
   */
  @Test
  public void testFindByDepartementAndNameLikeValidOnDate()
    throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    List<Commune> result;
    result = jpaDao.findByDepartementAndNameLikeValidOnDate("976", "dZi", sdf.parse("2018-01-01"));
    assertEquals("Hibernate returned the wrong number of results",
                 2, result.size());
    result = jpaDao.findByDepartementAndNameLikeValidOnDate("90", "aaaa", sdf.parse("2018-01-01"));
    assertEquals("Hibernate returned the wrong number of results",
                 0, result.size());
  }

  /**
   * Test custom repository method.
   * @throws ParseException failed to parse date.
   */
  @Test
  public void testFindByDepartementLikeAndNomEnrichiLikeIgnoreCase()
    throws ParseException {
    List<Commune> result;
    result = jpaDao.findByDepartementLikeAndNomEnrichiLikeIgnoreCaseOrderByNomEnrichiAsc("%", "%");
    assertEquals("Hibernate returned the wrong number of results",
                 639, result.size());
    result = jpaDao.findByDepartementLikeAndNomEnrichiLikeIgnoreCaseOrderByNomEnrichiAsc("91", "%");
    assertEquals("Hibernate returned the wrong number of results",
                 196, result.size());
    result = jpaDao.findByDepartementLikeAndNomEnrichiLikeIgnoreCaseOrderByNomEnrichiAsc("91", "%évi%");
    assertEquals("Hibernate returned the wrong number of results",
                 2, result.size());
    result = jpaDao.findByDepartementLikeAndNomEnrichiLikeIgnoreCaseOrderByNomEnrichiAsc("%", "%éVi%");
    assertEquals("Hibernate returned the wrong number of results",
                 3, result.size());
    result = jpaDao.findByDepartementLikeAndNomEnrichiLikeIgnoreCaseOrderByNomEnrichiAsc("91", "%aaa%");
    assertEquals("Hibernate returned the wrong number of results",
                 0, result.size());
  }

  /**
   * Test custom repository method.
   * @throws ParseException failed to parse date.
   */
  @Test
  public void testFindByDepartementLikeAndNomEnrichiLikeIgnoreCaseValidOnDate()
    throws ParseException {
    List<Commune> result;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    result = jpaDao.findByDepartementLikeAndNomEnrichiLikeIgnoreCaseValidOnDate("91", "%", sdf.parse("2018-01-01"));
    assertEquals("Hibernate returned the wrong number of results",
                 196, result.size());
    result = jpaDao.findByDepartementLikeAndNomEnrichiLikeIgnoreCaseValidOnDate("%", "%", sdf.parse("2018-01-01"));
    assertEquals("Hibernate returned the wrong number of results",
                 632, result.size());
    result = jpaDao.findByDepartementLikeAndNomEnrichiLikeIgnoreCaseValidOnDate("%", "%évi%", sdf.parse("2018-01-01"));
    assertEquals("Hibernate returned the wrong number of results",
                 3, result.size());
    result = jpaDao.findByDepartementLikeAndNomEnrichiLikeIgnoreCaseValidOnDate("91", "%évi%", sdf.parse("2018-01-01"));
    assertEquals("Hibernate returned the wrong number of results",
                 2, result.size());
    result = jpaDao.findByDepartementLikeAndNomEnrichiLikeIgnoreCaseValidOnDate("91", "%aaa%", sdf.parse("2018-01-01"));
    assertEquals("Hibernate returned the wrong number of results",
                 0, result.size());
  }

  /**
   * Test custom repository method.
   * @throws ParseException failed to parse date.
   */
  @Test
  public void testFindByRegionLikeAndNomEnrichiLikeIgnoreCase()
    throws ParseException {
    List<Commune> result;
    result = jpaDao.findByRegionLikeAndNomEnrichiLikeIgnoreCase("%", "%");
    assertEquals("Hibernate returned the wrong number of results",
                 639, result.size());
    result = jpaDao.findByRegionLikeAndNomEnrichiLikeIgnoreCase("11", "%");
    assertEquals("Hibernate returned the wrong number of results",
                 507, result.size());
    result = jpaDao.findByRegionLikeAndNomEnrichiLikeIgnoreCase("%", "%éVi%");
    assertEquals("Hibernate returned the wrong number of results",
                 3, result.size());
    result = jpaDao.findByRegionLikeAndNomEnrichiLikeIgnoreCase("11", "%aaa%");
    assertEquals("Hibernate returned the wrong number of results",
                 0, result.size());
  }

  /**
   * Test custom repository method.
   * @throws ParseException failed to parse date.
   */
  @Test
  public void testFindByRegionLikeAndNomEnrichiLikeIgnoreCaseValidOnDate()
    throws ParseException {
    List<Commune> result;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    result = jpaDao.findByRegionLikeAndNomEnrichiLikeIgnoreCaseValidOnDate("%", "%", sdf.parse("2018-01-01"));
    assertEquals("Hibernate returned the wrong number of results",
                 632, result.size());
    result = jpaDao.findByRegionLikeAndNomEnrichiLikeIgnoreCaseValidOnDate("11", "%", sdf.parse("2018-01-01"));
    assertEquals("Hibernate returned the wrong number of results",
                 503, result.size());
    result = jpaDao.findByRegionLikeAndNomEnrichiLikeIgnoreCaseValidOnDate("%", "%éVi%", sdf.parse("2018-01-01"));
    assertEquals("Hibernate returned the wrong number of results",
                 3, result.size());
    result = jpaDao.findByRegionLikeAndNomEnrichiLikeIgnoreCaseValidOnDate("11", "%aaa%", sdf.parse("2018-01-01"));
    assertEquals("Hibernate returned the wrong number of results",
                 0, result.size());
  }
}
