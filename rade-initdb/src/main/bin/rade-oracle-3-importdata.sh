# This file is part of the Rade project (https://github.com/mgimpel/rade).
# Copyright (C) 2018 Marc Gimpel
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <https://www.gnu.org/licenses/>.
#
# $Id$

# Creates and fills the various Rade tables with data.

. rade-db-init-parameters.sh
cp ../sql/insert-*.sql $DUMP_DIR/rade-$DATE/
sqlplus RADE_DEV/password <<EOF
SPOOL $DUMP_DIR/rade-$DATE/import-data.log
START $DUMP_DIR/rade-$DATE/insert-TypeEntiteAdmin.sql
START $DUMP_DIR/rade-$DATE/insert-TypeNomClair.sql
START $DUMP_DIR/rade-$DATE/insert-StatutModification.sql
START $DUMP_DIR/rade-$DATE/insert-TypeGenealogieEntiteAdmin.sql
START $DUMP_DIR/rade-$DATE/insert-Audit.sql
START $DUMP_DIR/rade-$DATE/insert-CirconscriptionBassin.sql
START $DUMP_DIR/rade-$DATE/insert-Region.sql
START $DUMP_DIR/rade-$DATE/insert-Departement.sql
START $DUMP_DIR/rade-$DATE/insert-Commune.sql
START $DUMP_DIR/rade-$DATE/insert-Delegation.sql
SPOOL OFF
EXIT;
