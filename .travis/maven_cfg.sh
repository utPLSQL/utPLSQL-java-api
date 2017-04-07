#!/bin/bash
set -ev
cd $(dirname $(readlink -f $0))

mavenSettings=$HOME/.m2/settings.xml
mavenCached=$HOME/.m2/.cached

if [ -f $mavenCached ]; then
    echo "Using cached maven user config..."
    exit 0
fi

if [ "$ORACLE_OTN_USER" == "" ] || [ "$ORACLE_OTN_PASSWORD" == "" ]; then
    echo "Oracle OTN username/password not specified."
    exit 1
fi

# Download wagon-http recommended by Oracle.
# On maven latest version this is not needed, but travis doesn't have it.
curl -L -O "http://central.maven.org/maven2/org/apache/maven/wagon/wagon-http/2.8/wagon-http-2.8-shaded.jar"
sudo mv wagon-http-2.8-shaded.jar $MAVEN_HOME/lib/ext/

# Create the settings file with oracle server config.
cp settings.tmpl.xml $mavenSettings
sed -i -e "s|###USERNAME###|$ORACLE_OTN_USER|g" $mavenSettings
sed -i -e "s|###PASSWORD###|$ORACLE_OTN_PASSWORD|g" $mavenSettings

touch $mavenCached
