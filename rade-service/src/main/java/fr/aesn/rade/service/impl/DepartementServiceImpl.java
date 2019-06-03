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
package fr.aesn.rade.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.aesn.rade.common.InvalidArgumentException;
import fr.aesn.rade.common.modelplus.DepartementWithGenealogie;
import fr.aesn.rade.common.util.SharedBusinessRules;
import fr.aesn.rade.persist.dao.DepartementJpaDao;
import fr.aesn.rade.persist.model.Departement;
import fr.aesn.rade.persist.model.EntiteAdministrative;
import fr.aesn.rade.persist.model.GenealogieEntiteAdmin;
import fr.aesn.rade.service.DepartementService;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Service Implementation for Departement.
 * @author Marc Gimpel (mgimpel@gmail.com)
 */
@Service
@Transactional
@NoArgsConstructor @Slf4j
public class DepartementServiceImpl
  implements DepartementService {
  /** Data Access Object for Departement. */
  @Autowired @Setter
  private DepartementJpaDao departementJpaDao;

  /**
   * List all Departement.
   * @return a List of all the Departement.
   */
  @Override
  @Transactional(readOnly = true)
  public List<Departement> getAllDepartement() {
    log.debug("Departement list requested");
    return departementJpaDao.findAll();
  }

  /**
   * List all Departement valid at the given date.
   * @param date the date at which the code was valid
   * @return a List of all the Departement.
   */
  @Override
  @Transactional(readOnly = true)
  public List<Departement> getAllDepartement(final Date date) {
    log.debug("Departement list requested for Date: date={}", date);
    List<Departement> list = departementJpaDao.findAll();
    Date testDate = (date == null ? new Date() : date);
    list.removeIf(e -> !SharedBusinessRules.isEntiteAdministrativeValid(e, testDate));
    return list;
  }

  /**
   * Returns a Map of all Departement indexed by ID.
   * @return a Map of all Departement indexed by ID.
   */
  @Override
  public Map<Integer, Departement> getDepartementMap() {
    log.debug("Departement map requested");
    List<Departement> list = getAllDepartement();
    HashMap<Integer, Departement> map = new HashMap<>(list.size());
    for (Departement item : list) {
      map.put(item.getId(), item);
    }
    return map;
  }

  /**
   * Returns a Map of all Departement valid at the given date and indexed by code.
   * @param date the date at which the Departements are valid.
   * @return a Map of all Departement indexed by code INSEE.
   */
  @Override
  public Map<String, Departement> getDepartementMap(final Date date) {
    log.debug("Departement map requested for Date: date={}", date);
    List<Departement> list = getAllDepartement(date);
    HashMap<String, Departement> map = new HashMap<>(list.size());
    for (Departement item : list) {
      map.put(item.getCodeInsee(), item);
    }
    return map;
  }

  /**
   * Get the Departement with the given ID.
   * @param id the Departement ID.
   * @return the Departement with the given ID.
   */
  @Override
  @Transactional(readOnly = true)
  public Departement getDepartementById(final int id) {
    log.debug("Departement requested by ID: ID={}", id);
    Optional<Departement> result = departementJpaDao.findById(id);
    if (result.isPresent()) {
      return result.get();
    } else {
      return null;
    }
  }

  /**
   * Get the Departement with the given code.
   * @param code the Departement code.
   * @return list of Departements that have historically had the given code.
   */
  @Override
  @Transactional(readOnly = true)
  public List<Departement> getDepartementByCode(final String code) {
    log.debug("Departement requested by code: code={}", code);
    return departementJpaDao.findByCodeInsee(code);
    /*
    // Can also be done by using an Example:
    Departement criteria = new Departement();
    criteria.setCodeInsee(code);
    Example<Departement> example = Example.of(criteria);
    return departementJpaDao.findAll(example);
    */
  }

  /**
   * Get the Departement with the given code at the given date.
   * @param code the Departement code.
   * @param date the date at which the code was valid
   * @return the Departement with the given code at the given date.
   */
  @Override
  public Departement getDepartementByCode(final String code, final Date date) {
    log.debug("Departement requested by code and date: code={}, date={}", code, date);
    List<Departement> list = getDepartementByCode(code);
    if (list == null) {
      return null;
    }
    Date testdate = (date == null ? new Date() : date);
    for (Departement dept : list) {
      if (SharedBusinessRules.isEntiteAdministrativeValid(dept, testdate)) {
        // Suppose database correct (au plus 1 valeur valide)
        return dept;
      }
    }
    return null;
  }

  /**
   * Get the Departement with the given code at the given date.
   * @param code the Departement code.
   * @param date the date at which the code was valid
   * @return the Departement with the given code at the given date.
   */
  @Override
  public Departement getDepartementByCode(final String code,
                                          final String date) {
    log.debug("Departement requested by code and date: code={}, date={}", code, date);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    try {
      return getDepartementByCode(code, sdf.parse(date));
    } catch (ParseException e) {
      log.warn("Departement requested by code and date: Exception parsing date {}", date, e);
      return null;
    }
  }

  /**
   * Get all the Departements within a given region, at the given date.
   * @param region the regions code.
   * @param date the date at which the Departments are valid.
   * @return list of Departements within the given region, at the given date.
   */
  @Override
  public List<Departement> getDepartementForRegion(String region, Date date) {
    log.debug("Departement list requested for region: region={}, date={}",
              region, date);
    List<Departement> list = getAllDepartement(date);
    if (region == null || region.isEmpty()) {
      return list;
    } else {
      list.removeIf(e -> !region.equals(e.getRegion()));
      return list;
    }
  }

  /**
   * Return a list of departments with the given code or with the given name
   * and region criteria, at the given date.
   * @param code the Departement code.
   * @param region the Region code.
   * @param nameLike name pattern.
   * @param date the date at which the code was valid
   * @return list of departments with the given code or with the given name
   * and region criteria, at the given date.
   */
  public List<DepartementWithGenealogie> getDepartementByCriteria(final String code,
                                                                  final String region,
                                                                  final String nameLike,
                                                                  final Date date) {
    log.debug("Departement with Genealogie requested with criteria: " +
              "code={}, region={}, name={}, date={}",
              code, region, nameLike, date);
    List<DepartementWithGenealogie> departementPlus = new ArrayList<>();
    if(code != null && !code.isEmpty()) { // Rechercher par code INSEE (ignorer region, ...)
      DepartementWithGenealogie deptGenealogie = getDepartementWithGenealogie(code, date);
      if(deptGenealogie != null) {
          departementPlus.add(deptGenealogie);
      }
      return departementPlus;
    }
    // Rechercher les communes en fonction du nom, dept, region
    Date testdate = (date == null ? new Date() : date);
    String testname = (nameLike == null || nameLike.isEmpty() ? "%" : "%" + nameLike + "%");
    String testregion = (region == null || region.isEmpty() ? "%" : region);
    List<Departement> depts = departementJpaDao.findByRegionLikeAndNomEnrichiLikeIgnoreCaseValidOnDate(testregion, testname, testdate);
    for (Departement dept : depts) {
      departementPlus.add(buildDepartementWithGenealogie(dept));
    }
    return departementPlus;
  }

  /**
   * Get the Departement with the given code at the given date, and all it's
   * genealogie.
   * @param code the Departement code.
   * @param date the date at which the code was valid
   * @return the Departement with the given code at the given date, and all
   * it's genealogie.
   */
  @Override
  public DepartementWithGenealogie getDepartementWithGenealogie(final String code,
                                                                final Date date) {
    Departement dept = getDepartementByCode(code, date);
    if (dept == null) {
      return null;
    }
    return buildDepartementWithGenealogie(dept);
  }

  /**
   * Build a DepartementWithGenealogie from the given Departement.
   * @param dept the Departement
   * @return a DepartementWithGenealogie built from the given Departement.
   */
  private DepartementWithGenealogie buildDepartementWithGenealogie(final Departement dept) {
    log.debug("Building Genealogie for {}", dept);
    DepartementWithGenealogie result = new DepartementWithGenealogie(dept);
    EntiteAdministrative tempEntity;
    Optional<Departement> opt;
    Set<GenealogieEntiteAdmin> parents = dept.getParents();
    if (parents != null) {
      for (GenealogieEntiteAdmin parent : parents) {
        tempEntity = parent.getParentEnfant().getParent();
        assert "DEP".equals(tempEntity.getTypeEntiteAdmin().getCode());
        opt = departementJpaDao.findById(tempEntity.getId());
        if (opt.isPresent()) {
          try {
            result.addParent(parent.getTypeGenealogie(), opt.get());
          } catch (InvalidArgumentException e) {
            log.warn("This should never happen! parent must exist: {}", parent);
          }
        } else {
          log.warn("This should never happen! parent must exist: {}", parent);
        }
      }
    }
    Set<GenealogieEntiteAdmin> enfants = dept.getEnfants();
    if (enfants != null) {
      for (GenealogieEntiteAdmin enfant : enfants) {
        tempEntity = enfant.getParentEnfant().getEnfant();
        assert "DEP".equals(tempEntity.getTypeEntiteAdmin().getCode());
        opt = departementJpaDao.findById(tempEntity.getId());
        if (opt.isPresent()) {
          try {
            result.addEnfant(enfant.getTypeGenealogie(), opt.get());
          } catch (InvalidArgumentException e) {
            log.warn("This should never happen! enfant must exist: {}", enfant);
          }
        } else {
          log.warn("This should never happen! parent must exist: {}", enfant);
        }
      }
    }
    return result;
  }

  /**
   * Invalidates the given departement by setting the departements finValidite
   * field to the given date.
   * @param dept the departement to invalidate.
   * @param date the date of end of validity for the departement.
   * @return the now invalidated departement.
   */
  @Override
  @Transactional(readOnly = false)
  public Departement invalidateDepartement(final Departement dept,
                                           final Date date) {
    if ((dept == null) || (date == null)) {
      return null;
    }
    Departement oldDept = getDepartementById(dept.getId());
    if (!(dept.equals(oldDept))
        || (oldDept.getFinValidite() != null)) {
      // given departement has other changes
      return null;
    }
    if (!(date.after(oldDept.getDebutValidite()))) {
      // given end of validity if before departements beginning of validity
      return null;
    }
    oldDept.setFinValidite(date);
    return departementJpaDao.save(oldDept);
  }
}
