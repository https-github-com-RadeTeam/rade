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

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import fr.aesn.rade.persist.model.GenealogieEntiteAdmin;

/**
 * JPA DataAccessObject for GenealogieEntiteAdmin.
 * @author Marc Gimpel (mgimpel@gmail.com)
 */
public interface GenealogieEntiteAdminJpaDao
  extends JpaRepository<GenealogieEntiteAdmin, GenealogieEntiteAdmin.ParentEnfant> {
  /**
   * Delete all Genealogie of any Commune.
   */
  @Transactional
  @Modifying
  @Query("DELETE FROM GenealogieEntiteAdmin g"
             + " WHERE g.parentEnfant.parent IN (SELECT c FROM Commune c)")
  public void deleteAllCommune();

  /**
   * Delete all Genealogie of any Departements.
   */
  @Transactional
  @Modifying
  @Query("DELETE FROM GenealogieEntiteAdmin g"
             + " WHERE g.parentEnfant.parent IN (SELECT d FROM Departement d)")
  public void deleteAllDepartement();

  /**
   * Delete all Genealogie of any Region.
   */
  @Transactional
  @Modifying
  @Query("DELETE FROM GenealogieEntiteAdmin g"
             + " WHERE g.parentEnfant.parent IN (SELECT r FROM Region r)")
  public void deleteAllRegion();
}
