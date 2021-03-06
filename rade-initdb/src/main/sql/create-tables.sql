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

-- TODO: Automatically generate ddl from jpa annotations

-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
-- ZR_DELEGATION : Délégation
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
CREATE TABLE ZR_DELEGATION (
  CODE         varchar(5)   NOT NULL PRIMARY KEY,
  LIBELLE      varchar(60)  NOT NULL,
  ACHEMINEMENT varchar(255) NOT NULL,
  ADRESSE1     varchar(255),
  ADRESSE2     varchar(255),
  ADRESSE3     varchar(255),
  ADRESSE4     varchar(255),
  ADRESSE5     varchar(255),
  CODE_POSTAL  varchar(5)   NOT NULL,
  EMAIL        varchar(255),
  FAX          varchar(255),
  SITEWEB      varchar(255),
  TELEPHONE    varchar(255) NOT NULL,
  TELEPHONE2   varchar(255),
  TELEPHONE3   varchar(255)
);
/*
COMMENT ON TABLE ZR_DELEGATION
  IS 'Table for Delegation';
*/

-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
-- ZR_AUDIT : Trace des modifications
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
CREATE TABLE ZR_AUDIT (
  AUDIT_ID     integer       NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, -- type serial for PostgreSQL 9
  AUDIT_AUTEUR varchar(8)    NOT NULL,
  AUDIT_DATE   timestamp     NOT NULL,
  AUDIT_NOTE   varchar(4000)
);
/*
COMMENT ON TABLE ZR_AUDIT
  IS 'Table de suivie des modifications';
*/
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
-- ZR_STATUTMODIF : Statut de modification de commune
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
CREATE TABLE ZR_STATUTMODIF (
  CODE          varchar(1)   NOT NULL PRIMARY KEY,
  LIBELLE_COURT varchar(25)  NOT NULL,
  LIBELLE_LONG  varchar(250) NOT NULL
);
/*
COMMENT ON TABLE ZR_STATUTMODIF IS
  'Entité [STATUT MODIFICATION DE COMMUNE]';
COMMENT ON COLUMN ZR_STATUTMODIF.CODE IS
  '[Code statut de modification commune] de l''entité [STATUT MODIFICATION DE COMMUNE]';
COMMENT ON COLUMN ZR_STATUTMODIF.LIBELLE_COURT IS
  '[Libellé court statut de modification commune] de l''entité [STATUT MODIFICATION DE COMMUNE]';
COMMENT ON COLUMN ZR_STATUTMODIF.LIBELLE_LONG IS
  '[Libellé long statut de modification commune] de l''entité [STATUT MODIFICATION DE COMMUNE]';
*/
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
-- ZR_TYPENOMCLAIR : Type nom en clair entité administrative
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
CREATE TABLE ZR_TYPENOMCLAIR (
  CODE       varchar(1) NOT NULL PRIMARY KEY,
  ARTICLE    varchar(5),
  CHARNIERE  varchar(6),
  ARTICLEMAJ varchar(5)
);
/*
COMMENT ON TABLE ZR_TYPENOMCLAIR IS
  'Entité [TYPE NOM EN CLAIR ENTITE ADMINISTRATIVE]';
COMMENT ON COLUMN ZR_TYPENOMCLAIR.CODE IS
  '[code utilisation nom en clair entité administrative] de l''entité [TYPE NOM EN CLAIR ENTITE ADMINISTRATIVE]';
COMMENT ON COLUMN ZR_TYPENOMCLAIR.ARTICLE IS
  '[article nom en clair entité administrative] de l''entité [TYPE NOM EN CLAIR ENTITE ADMINISTRATIVE]';
COMMENT ON COLUMN ZR_TYPENOMCLAIR.CHARNIERE IS
  '[charnière nom en clair entité administrative] de l''entité [TYPE NOM EN CLAIR ENTITE ADMINISTRATIVE]';
COMMENT ON COLUMN ZR_TYPENOMCLAIR.ARTICLEMAJ IS
  '[article entité administrative majuscules] de l''entité [TYPE NOM EN CLAIR ENTITE ADMINISTRATIVE]';
*/
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
-- ZR_TYPEENTITEADMIN : Type d'entité administrative
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
CREATE TABLE ZR_TYPEENTITEADMIN (
  CODE          varchar(3)  NOT NULL PRIMARY KEY,
  LIBELLE_COURT varchar(20) NOT NULL
);
/*
COMMENT ON TABLE ZR_TYPEENTITEADMIN IS
  'Entité [TYPE D''ENTITE ADMINISTRATIVE]';
COMMENT ON COLUMN ZR_TYPEENTITEADMIN.CODE IS
  '[Code type d''entité administrative] de l''entité [TYPE D''ENTITE ADMINISTRATIVE]';
COMMENT ON COLUMN ZR_TYPEENTITEADMIN.LIBELLE_COURT IS
  '[Libellé court type d''entité administrative] de l''entité [TYPE D''ENTITE ADMINISTRATIVE]';
*/
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
-- ZR_TYPEGENEALOGIE : Type de Généalogie d'entité administrative
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
CREATE TABLE ZR_TYPEGENEALOGIE (
  CODE          varchar(3)   NOT NULL PRIMARY KEY,
  LIBELLE_COURT varchar(20)  NOT NULL,
  LIBELLE_LONG  varchar(100) NOT NULL,
  STATUT_DEFAUT varchar(1),
  FOREIGN KEY(STATUT_DEFAUT) REFERENCES ZR_STATUTMODIF
);
/*
COMMENT ON TABLE ZR_TYPEGENEALOGIE IS
  'Entité [TYPE DE GENEALOGIE D''ENTITE ADMINISTRATIVE]';
COMMENT ON COLUMN ZR_TYPEGENEALOGIE.CODE IS
  '[Code type de genealogie entité administrative] de l''entité [TYPE DE GENEALOGIE D''ENTITE ADMINISTRATIVE]';
COMMENT ON COLUMN ZR_TYPEGENEALOGIE.LIBELLE_COURT IS
  '[Libellé court type de genealogie entité administrative] de l''entité [TYPE DE GENEALOGIE D''ENTITE ADMINISTRATIVE]';
COMMENT ON COLUMN ZR_TYPEGENEALOGIE.LIBELLE_LONG IS
  '[Libellé long type de genealogie entité administrative] de l''entité [TYPE DE GENEALOGIE D''ENTITE ADMINISTRATIVE]';
COMMENT ON COLUMN ZR_TYPEGENEALOGIE.STATUT_DEFAUT IS
  '[Code statut de modification commune] vers l''entité [STATUT MODIFICATION DE COMMUNE] par l''association [STATUT PAR DEFAUT DU TYPE DE GENEALOGIE]';
*/
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
-- ZR_BASSIN : Circonscription de bassin
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
CREATE TABLE ZR_BASSIN (
  CODE          varchar(2)  NOT NULL PRIMARY KEY,
  LIBELLE_COURT varchar(5)  NOT NULL,
  LIBELLE_LONG  varchar(70) NOT NULL,
  AUDIT_ID      integer     NOT NULL,
  FOREIGN KEY(AUDIT_ID) REFERENCES ZR_AUDIT
);
/*
COMMENT ON TABLE ZR_BASSIN IS
  'Entité [CIRCONSCRIPTION  DE BASSIN]';
COMMENT ON COLUMN ZR_BASSIN.CODE IS
  '[code INSEE bassin] de l''entité [CIRCONSCRIPTION  DE BASSIN]';
COMMENT ON COLUMN ZR_BASSIN.LIBELLE_COURT IS
  '[Libellé court bassin] de l''entité [CIRCONSCRIPTION  DE BASSIN]';
COMMENT ON COLUMN ZR_BASSIN.LIBELLE_LONG IS
  '[Libellé  long bassin] de l''entité [CIRCONSCRIPTION  DE BASSIN]';
*/
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
-- ZR_ENTITEADMIN : Entité administrative (Commune, Département ou Région)
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
CREATE TABLE ZR_ENTITEADMIN (
  ID                integer       NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, -- type serial for PostgreSQL 9
  DEBUT_VALIDITE    date          NOT NULL,
  FIN_VALIDITE      date,
  ARTICLE_ENRICHI   varchar(5),
  NOM_MAJUSCULE     varchar(70)   NOT NULL,
  NOM_ENRICHI       varchar(70)   NOT NULL,
  COMMENTAIRE       varchar(4000),
  TYPE_NOM_CLAIR    varchar(1),
  TYPE_ENTITE_ADMIN varchar(3)    NOT NULL,
  AUDIT_ID          integer       NOT NULL,
  FOREIGN KEY(TYPE_NOM_CLAIR)    REFERENCES ZR_TYPENOMCLAIR,
  FOREIGN KEY(TYPE_ENTITE_ADMIN) REFERENCES ZR_TYPEENTITEADMIN,
  FOREIGN KEY(AUDIT_ID)          REFERENCES ZR_AUDIT
);
/*
COMMENT ON TABLE ZR_ENTITEADMIN IS
  'Entité [ENTITE ADMINISTRATIVE]';
*/
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
-- ZR_REGION : Region
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
CREATE TABLE ZR_REGION (
  ID        integer     NOT NULL PRIMARY KEY,
  CODE      varchar(2)  NOT NULL,
  CHEF_LIEU varchar(10) NOT NULL,
--  FOREIGN KEY(CHEF_LIEU) REFERENCES ZR_COMMUNE,
  FOREIGN KEY(ID)        REFERENCES ZR_ENTITEADMIN
);
/*
COMMENT ON TABLE ZR_REGION IS
  'Entité [REGION]';
COMMENT ON COLUMN ZR_REGION.ID IS
  '[Identifiant entité administrative] vers l''entité [ENTITE ADMINISTRATIVE]';
COMMENT ON COLUMN ZR_REGION.CODE IS
  '[code INSEE région] de l''entité [REGION]';
COMMENT ON COLUMN ZR_REGION.CHEF_LIEU IS
  '[Identifiant INSEE Commune] vers l''entité [CODE INSEE COMMUNE] par l''association [COMMUNE CHEF LIEU DE REGION]';
*/
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
-- ZR_DEPT : Département
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
CREATE TABLE ZR_DEPT (
  ID        integer     NOT NULL PRIMARY KEY,
  CODE      varchar(3)  NOT NULL,
  REGION    varchar(2)  NOT NULL,
  CHEF_LIEU varchar(10) NOT NULL,
--  FOREIGN KEY(REGION)    REFERENCES ZR_REGION,
--  FOREIGN KEY(CHEF_LIEU) REFERENCES ZR_COMMUNE,
  FOREIGN KEY(ID)        REFERENCES ZR_ENTITEADMIN
);
/*
COMMENT ON TABLE ZR_DEPT IS
  'Entité [DEPARTEMENT]';
COMMENT ON COLUMN ZR_DEPT.ID IS
  '[Identifiant entité administrative] vers l''entité [ENTITE ADMINISTRATIVE]';
COMMENT ON COLUMN ZR_DEPT.CODE IS
  '[code INSEE département] de l''entité [DEPARTEMENT]';
COMMENT ON COLUMN ZR_DEPT.CHEF_LIEU IS
  '[Identifiant INSEE Commune] vers l''entité [CODE INSEE COMMUNE] par l''association [COMMUNE CHEF LIEU DE DEPARTEMENT]';
*/
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
-- ZR_COMMUNE : Commune
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
CREATE TABLE ZR_COMMUNE (
  ID           integer     NOT NULL PRIMARY KEY,
  CODE         varchar(10) NOT NULL,
  DEPT         varchar(3)  NOT NULL,
--  FOREIGN KEY(DEPT)   REFERENCES ZR_DEPT,
  FOREIGN KEY(ID)     REFERENCES ZR_ENTITEADMIN
);
/*
COMMENT ON TABLE ZR_COMMUNE IS
  'Entité [COMMUNE]';
COMMENT ON COLUMN ZR_COMMUNE.ID IS
  '[Identifiant entité administrative] vers l''entité [ENTITE ADMINISTRATIVE]';
COMMENT ON COLUMN ZR_COMMUNE.CODE IS
  '[Identifiant INSEE Commune] vers l''entité [CODE INSEE COMMUNE] par l''association [CODE INSEE DE LA COMMUNE]';
COMMENT ON COLUMN ZR_COMMUNE.DEPT IS
  '[Identifiant entité administrative] vers l''entité [DEPARTEMENT] par l''association [COMMUNE APPARTIENT A DEPARTEMENT]';
*/
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
-- ZR_GENEALOGIE : Association sur la généalogie de l'entité administrative
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
CREATE TABLE ZR_GENEALOGIE (
  PARENT          integer       NOT NULL,
  ENFANT          integer       NOT NULL,
  TYPE_GENEALOGIE varchar(3)    NOT NULL,
  COMMENTAIRE     varchar(2000),
  PRIMARY KEY (PARENT, ENFANT),
  FOREIGN KEY (PARENT) REFERENCES ZR_ENTITEADMIN,
  FOREIGN KEY (ENFANT) REFERENCES ZR_ENTITEADMIN,
  FOREIGN KEY (TYPE_GENEALOGIE) REFERENCES ZR_TYPEGENEALOGIE  
);
/*
COMMENT ON TABLE ZR_GENEALOGIE IS
  'Association [GENEALOGIE DE L''ENTITE]';
COMMENT ON COLUMN ZR_GENEALOGIE.PARENT IS
  '[Identifiant entité administrative] vers l''entité [ENTITE ADMINISTRATIVE] par le rôle [ENTITE PARENT] de l''association [GENEALOGIE DE L''ENTITE]';
COMMENT ON COLUMN ZR_GENEALOGIE.ENFANT IS
  '[Identifiant entité administrative] vers l''entité [ENTITE ADMINISTRATIVE] par le rôle [ENTITE ENFANT] de l''association [GENEALOGIE DE L''ENTITE]';
COMMENT ON COLUMN ZR_GENEALOGIE.TYPE_GENEALOGIE IS
  '[Code type de genealogie entité administrative] vers l''entité [TYPE DE GENEALOGIE D''ENTITE ADMINISTRATIVE] par le rôle [TYPE DE GENEALOGIE] de l''association [GENEALOGIE DE L''ENTITE]';
COMMENT ON COLUMN ZR_GENEALOGIE.COMMENTAIRE IS
  '[Commentaire sur la modification de l''entité] de l''association [GENEALOGIE DE L''ENTITE]';
*/
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
-- ZR_EVENEMENT : Evenement commune a controler
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
CREATE TABLE ZR_EVENEMENT (
  ID            integer      NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, -- type serial for PostgreSQL 9
  DESCRIPTION   varchar(350),
  ENTITE_ADMIN  integer,
  STATUT_MODIF  varchar(1)   NOT NULL,
  TYPE_MODIF    varchar(3)   NOT NULL,
  AUDIT_ID      integer      NOT NULL,
  ZR_DSTATUT    date         NOT NULL,
  ZR_BORIGINE   varchar(1)   NOT NULL,
  ZR_DCREAEVT   date         NOT NULL,
  ZR_LEVTCOMN   varchar(70),
  FOREIGN KEY(ENTITE_ADMIN) REFERENCES ZR_ENTITEADMIN,
  FOREIGN KEY(TYPE_MODIF)   REFERENCES ZR_TYPEGENEALOGIE,
  FOREIGN KEY(STATUT_MODIF) REFERENCES ZR_STATUTMODIF,
  FOREIGN KEY(AUDIT_ID)     REFERENCES ZR_AUDIT
);
/*
COMMENT ON TABLE ZR_EVENEMENT IS
  'Entité [EVENEMENT COMMUNE A CONTROLER]';
*/
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
-- ZR_COMMUNESANDRE : Référentiel des Communes associées à leur circonscription bassin
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
CREATE TABLE ZR_COMMUNESANDRE (
  CODE_COMMUNE           varchar(5)  NOT NULL PRIMARY KEY,
  LIBELLE_COMMUNE        varchar(45) NOT NULL,
  STATUT_COMMUNE         varchar(20) NOT NULL,
  DATE_CREATION_COMMUNE  date,
  DATE_MAJ_COMMUNE       date        NOT NULL,
  CODE_BASSIN_DCE        varchar(2)  NOT NULL,
  CODE_EU_DISTRICT       varchar(24) NOT NULL,
  CIRCONSCRIPTION_BASSIN varchar(2)  NOT NULL,
  CODE_COMITE_BASSIN     varchar(8)  NOT NULL,
  AUDIT_ID               integer     NOT NULL,
  FOREIGN KEY(CIRCONSCRIPTION_BASSIN) REFERENCES ZR_BASSIN,
  FOREIGN KEY(AUDIT_ID)               REFERENCES ZR_AUDIT
);
/*
COMMENT ON TABLE ZR_COMMUNESANDRE IS
  'Entité [COMMUNE SANDRE]';
*/
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
-- ZR_HEXAPOSTE : Référentiel des codes postaux et des codes CEDEX de France
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
CREATE TABLE ZR_HEXAPOSTE (
  IDENTIFIANT                  varchar(6)  NOT NULL PRIMARY KEY,
  CODE_INSEE_COMMUNE           varchar(5),
  LIBELLE_COMMUNE              varchar(38),
  INDICATEUR_PLURIDISTRIBUTION integer     NOT NULL,
  TYPE_CODE_POSTALE            varchar(1)  NOT NULL,
  LIBELLE_LIGNE5               varchar(38),
  CODE_POSTALE                 varchar(5)  NOT NULL,
  LIBELLE_ACHEMINEMENT         varchar(32) NOT NULL,
  CODE_INSEE_ANCIENNE_COMMUNE  varchar(5),
  CODE_MAJ                     varchar(1),
  CODE_ETENDU_ADRESSE          varchar(10),
  AUDIT_ID                     integer     NOT NULL,
  FOREIGN KEY(AUDIT_ID) REFERENCES ZR_AUDIT
);
/*
COMMENT ON TABLE ZR_HEXAPOSTE IS
  'Entité [HEXAPOSTE]';
*/
