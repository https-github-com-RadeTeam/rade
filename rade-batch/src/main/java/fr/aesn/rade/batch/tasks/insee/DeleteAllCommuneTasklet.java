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

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aesn.rade.persist.dao.CommuneJpaDao;
import fr.aesn.rade.persist.dao.GenealogieEntiteAdminJpaDao;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Delete All Commune Sandre Batch Task.
 * @author Marc Gimpel (mgimpel@gmail.com)
 */
@Slf4j
public class DeleteAllCommuneTasklet
  implements Tasklet {
  /** Service for Commune Sandre. */
  @Autowired @Setter
  private CommuneJpaDao communeJpaDao;
  @Autowired @Setter
  private GenealogieEntiteAdminJpaDao genealogieEntiteAdminJpaDao;

  /**
   * Given the current context in the form of a step contribution, do whatever
   * is necessary to process this unit inside a transaction.
   *
   * Implementations return RepeatStatus.FINISHED if finished. If not they
   * return RepeatStatus.CONTINUABLE. On failure throws an exception.
   *
   * @param contribution mutable state to be passed back to update the current
   * step execution.
   * @param chunkContext attributes shared between invocations but not between
   * restarts.
   */
  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
    throws Exception {
    log.info("About to delete all Commune INSEE details.");
    genealogieEntiteAdminJpaDao.deleteAllCommune();
    communeJpaDao.deleteAll();
    return RepeatStatus.FINISHED;
  }
}
