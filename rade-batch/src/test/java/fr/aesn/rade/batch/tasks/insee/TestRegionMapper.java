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
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.collections4.MapUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.validation.BindException;

import fr.aesn.rade.persist.model.Region;
import fr.aesn.rade.persist.model.TypeEntiteAdmin;
import fr.aesn.rade.persist.model.TypeNomClair;
import fr.aesn.rade.service.MetadataService;

import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.core.io.ClassPathResource;

/**
 * JUnit Test for RegionMapper.
 *
 * @author Marc Gimpel (mgimpel@gmail.com)
 */
public class TestRegionMapper {
  /** Test line from the INSEE Region file to import. */
  public static final String TEST_LINE =
    "11\t75056\t1\tILE-DE-FRANCE\tÎle-de-France";

  /** Metadata Service. */
  private static MetadataService metadataService = mock(MetadataService.class);

  /**
   * Set up the Test Environment.
   */
  @BeforeClass
  public static void setUpClass() {
    createServiceMocks();
  }

  /**
   * Build Service Mocks for use in the tests.
   */
  private static void createServiceMocks() {
    Map<String, TypeEntiteAdmin> tea = MapUtils.putAll(new HashMap<String, TypeEntiteAdmin>(),
                                                       new Object[][] {
        {"COM", TypeEntiteAdmin.of("COM", "Commune")},
        {"ARR", TypeEntiteAdmin.of("ARR", "Arrondissement")},
        {"CAN", TypeEntiteAdmin.of("CAN", "Canton")},
        {"DEP", TypeEntiteAdmin.of("DEP", "Département")},
        {"REG", TypeEntiteAdmin.of("REG", "Région")}
    });
    when(metadataService.getTypeEntiteAdminMap()).thenReturn(tea);
    Map<String, TypeNomClair> tncc = MapUtils.putAll(new HashMap<String, TypeNomClair>(),
                                                     new Object[][] {
        {"0", TypeNomClair.of("0", "", "DE", "")},
        {"1", TypeNomClair.of("1", "", "D'", "")},
        {"2", TypeNomClair.of("2", "Le", "DU", "LE")},
        {"3", TypeNomClair.of("3", "La", "DE LA", "LA")},
        {"4", TypeNomClair.of("4", "Les", "DES", "LES")},
        {"5", TypeNomClair.of("5", "L'", "DE L'", "L'")},
        {"6", TypeNomClair.of("6", "Aux", "DES", "AUX")},
        {"7", TypeNomClair.of("7", "Las", "DE LAS", "LAS")},
        {"8", TypeNomClair.of("8", "Los", "DE LOS", "LOS")}
    });
    when(metadataService.getTypeNomClairMap()).thenReturn(tncc);
  }

  /**
   * Test mapping one line from the Region file to import.
   * @throws BindException Mapper failed to parse test String.
   */
  @Test
  public void testMapping() throws BindException {
    DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
    tokenizer.setDelimiter("\t");
    FieldSet fieldSet = tokenizer.tokenize(TEST_LINE);
    RegionMapper mapper = new RegionMapper();
    mapper.setMetadataService(metadataService);
    Region region = mapper.mapFieldSet(fieldSet);
    assertEquals("Entity doesn't match expected value",
                 "REG", region.getTypeEntiteAdmin().getCode());
    assertEquals("Entity doesn't match expected value",
                 "11", region.getCodeInsee());
    assertEquals("Entity doesn't match expected value",
                 "75056", region.getChefLieu());
    assertEquals("Entity doesn't match expected value",
                 "1", region.getTypeNomClair().getCode());
    assertEquals("Entity doesn't match expected value",
                 "ILE-DE-FRANCE", region.getNomMajuscule());
    assertEquals("Entity doesn't match expected value",
                 "Île-de-France", region.getNomEnrichi());
    assertNull("Entity doesn't match expected null value",
               region.getArticleEnrichi());
    assertNull("Entity doesn't match expected null value",
               region.getCommentaire());
    assertNull("Entity doesn't match expected null value",
               region.getDebutValidite());
    assertNull("Entity doesn't match expected null value",
               region.getFinValidite());
    assertNull("Entity doesn't match expected null value",
               region.getAudit());
  }

  /**
   * Test mapping the whole Region file to import.
   * @throws Exception problem reading/mapping input file.
   */
  @Test
  public void testMappingFile() throws Exception {
    // Configure and open ItemReader (reading test input file)
    FlatFileItemReader<Region> reader = new FlatFileItemReader<>();
    reader.setResource(new ClassPathResource("batchfiles/insee/reg2018.txt"));
    reader.setLinesToSkip(1);
    DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
    tokenizer.setDelimiter("\t");
    RegionMapper mapper = new RegionMapper();
    mapper.setMetadataService(metadataService);
    DefaultLineMapper<Region> lineMapper = new DefaultLineMapper<>();
    lineMapper.setFieldSetMapper(mapper);
    lineMapper.setLineTokenizer(tokenizer);
    reader.setLineMapper(lineMapper);
    reader.afterPropertiesSet();
    ExecutionContext ec = new ExecutionContext();
    reader.open(ec);
    // Configure Validator and validate (@Size, @Min, ...) each line
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Region record;
    Set<ConstraintViolation<Region>> violations;
    int i = 0;
    while((record = reader.read()) != null) {
      violations = validator.validate(record);
      assertEquals("Record violates constraints", 0, violations.size());
      i++;
    }
    // Check all records from input file have been read
    assertNull(record);
    assertEquals(18, i);
  }
}
