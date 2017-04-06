#!/bin/bash
set -e
cd $(dirname $(readlink -f $0))

if [ "$ORACLE_OTN_USER" == "" ] || [ "$ORACLE_OTN_PASSWORD" == "" ]; then
    echo "Oracle OTN username/password not specified."
fi

MAVEN_SETTINGS=$HOME/.m2/settings.xml

# Copy the maven settings file to the right place, and set the username/password for oracle maven server.
cp settings.tmpl.xml $MAVEN_SETTINGS
sed -i -e "s|###USERNAME###|$ORACLE_OTN_USER|g" $MAVEN_SETTINGS
sed -i -e "s|###PASSWORD###|$ORACLE_OTN_PASSWORD|g" $MAVEN_SETTINGS
