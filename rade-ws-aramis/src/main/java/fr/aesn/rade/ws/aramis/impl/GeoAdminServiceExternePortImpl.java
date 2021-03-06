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
package fr.aesn.rade.ws.aramis.impl;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;

import fr.aesn.rade.service.CommunePlusService;
import fr.aesn.rade.service.DelegationService;
import fr.aesn.rade.service.DepartementService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Concrete Implementation of GeoAdminServiceExterneImpl WSDL for Aramis.
 * This class was generated by Apache CXF 3.2.2, then completed manually.
 * The following changes were made:
 * <ul>
 *   <li>The "wsdlLocation" in the Annotation was changed from an absolute
 *       path on the local filesystem to a relative path in the JAR:
 *       META-INF/wsdl/GeoAdminServiceExterneImpl.wsdl</li>
 *   <li>The logging framework was changes from java.util.logging to SLF4J
 *       (a logging facade compatible with the applications underlying logging
 *       implementation). Log levels and messages were adapted</li>
 *   <li>The various business services needed were added (CommuneService, ...)
 *       as well as there respective setters (via Lombok) so that Spring can
 *       populate them</li>
 *   <li>The core of the service methods (findAllDepartements, ...) were completed,
 *       and use the above mentioned services to recover the reference data</li>
 *   <li>PrintStackTraces were replaced with Error Logging</li>
 *   <li>The imports were cleaned up to remove useless entries</li>
 *   <li>This javadoc and a Copyright header were added</li>
 * </ul>
 * The class can be regenerated by modifying the projects pom.xml,
 * in particular the "cxf-codegen-plugin" configuration,
 * and uncommenting the "extraarg":"-impl".
 * It can then by copied from "target/generated-sources/cxf" to "src/main/java"
 * and re-completed.
 * @author Marc Gimpel (mgimpel@gmail.com)
 */
@Slf4j
@javax.jws.WebService(
                      serviceName = "GeoAdminServiceExterneImplService",
                      portName = "GeoAdminServiceExternePort",
                      targetNamespace = "http://services.externes.aramis.com/v1/",
                      wsdlLocation = "META-INF/wsdl/GeoAdminServiceExterneImpl.wsdl",
                      endpointInterface = "fr.aesn.rade.ws.aramis.impl.GeoAdminServiceExterneImpl")
public class GeoAdminServiceExternePortImpl implements GeoAdminServiceExterneImpl {
    /** Departement Service. */
    @Setter
    private DepartementService departementService;
    /** Commune Service. */
    @Setter
    private CommunePlusService communePlusService;
    /** Delegation Service. */
    @Setter
    private DelegationService delegationService;

    /* (non-Javadoc)
     * @see fr.aesn.rade.ws.aramis.impl.GeoAdminServiceExterneImpl#findAllDepartements()*
     */
    public java.util.List<fr.aesn.rade.ws.aramis.impl.DepartementVO> findAllDepartements() {
        log.info("Processing WebService findAllDepartements");
        try {
            if (departementService == null) {
                log.error("Could not findAllDepartements, service was null (configuration error)");
                return Collections.<fr.aesn.rade.ws.aramis.impl.DepartementVO>emptyList();
            }
            return Entity2VoMapper.departementEntity2VoList(departementService.getAllDepartement());
        } catch (java.lang.Exception ex) {
            log.error("Unexpected Exception while processing WebService Request (this should never happen)", ex);
            throw new RuntimeException(ex);
        }
    }

    /* (non-Javadoc)
     * @see fr.aesn.rade.ws.aramis.impl.GeoAdminServiceExterneImpl#findAllCommunes(java.lang.Integer annee)*
     */
    public java.util.List<fr.aesn.rade.ws.aramis.impl.CommuneVO> findAllCommunes(java.lang.Integer annee) {
        log.info("Processing WebService findAllCommunes for year {}", annee);
        Date date;
        if (annee == null) {
            // Si l'annee n'est pas fourni, on recherche toutes les Communes
            // valide a la date de la requête.
            date = new Date();
        } else {
            // Si l'annee est fourni, on recherche toutes les Communes valide au
            // 1er Janvier de l'annee fourni.
            date = Date.from(ZonedDateTime.of(annee, 1, 1, 12, 0, 0, 0, ZoneId.systemDefault()).toInstant()); // January 1st, 12:00
        }
        try {
            if (communePlusService == null) {
                log.error("Could not findAllCommunes, service was null (configuration error)");
                return Collections.<fr.aesn.rade.ws.aramis.impl.CommuneVO>emptyList();
            }
            return Entity2VoMapper.communePlusEntity2VoList(communePlusService.getAllCommuneValidOnDate(date));
        } catch (java.lang.Exception ex) {
            log.error("Unexpected Exception while processing WebService Request (this should never happen)", ex);
            throw new RuntimeException(ex);
        }
    }

    /* (non-Javadoc)
     * @see fr.aesn.rade.ws.aramis.impl.GeoAdminServiceExterneImpl#findAllDelegations()*
     */
    public java.util.List<fr.aesn.rade.ws.aramis.impl.DelegationVO> findAllDelegations() {
        log.info("Processing WebService findAllDelegations");
        try {
            if (delegationService == null) {
                log.error("Could not findAllDelegations, service was null (configuration error)");
                return Collections.<fr.aesn.rade.ws.aramis.impl.DelegationVO>emptyList();
            }
            return Entity2VoMapper.delegationEntity2VoList(delegationService.getAllDelegation());
        } catch (java.lang.Exception ex) {
            log.error("Unexpected Exception while processing WebService Request (this should never happen)", ex);
            throw new RuntimeException(ex);
        }
    }
}
