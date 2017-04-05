#!/bin/bash
set -e

# Download the specified version of utPLSQL.
curl -L -O "https://github.com/utPLSQL/utPLSQL/releases/download/$UTPLSQL_VERSION/$UTPLSQL_FILE.tar.gz"

# Create a temporary install script.
cat > install.sh.tmp <<EOF
tar -xzf $UTPLSQL_FILE.tar.gz
rm $UTPLSQL_FILE.tar.gz
cd /$UTPLSQL_FILE/source
sqlplus -S -L sys/oracle@//$CONNECTION_STR AS SYSDBA @install_headless.sql
EOF

# Copy utPLSQL files to the container and install it.
docker cp ./$UTPLSQL_FILE.tar.gz $ORACLE_VERSION:/$UTPLSQL_FILE.tar.gz
docker cp ./install.sh.tmp $ORACLE_VERSION:/install.sh

# Remove temporary files.
rm $UTPLSQL_FILE.tar.gz
rm install.sh.tmp

# Execute the utPLSQL installation inside the container.
docker exec $ORACLE_VERSION bash install.sh
