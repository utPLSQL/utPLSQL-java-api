#!/bin/bash
set -ev
cd $(dirname $(readlink -f $0))

if [ -d $MAVEN_HOME ]; then
    echo "Using cached maven install..."
    $MAVEN_HOME/bin/mvn -v
    exit 0
fi

if [ "$ORACLE_OTN_USER" == "" ] || [ "$ORACLE_OTN_PASSWORD" == "" ]; then
    echo "Oracle OTN username/password not specified."
    exit 1
fi

# If not cached, download and install maven.
# Then create the settings file, with username/password for oracle server.
curl -L -O "http://mirror.nbtelecom.com.br/apache/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.tar.gz"
tar -xzf apache-maven-3.3.9-bin.tar.gz
mv apache-maven-3.3.9 $MAVEN_HOME
rm apache-maven-3.3.9-bin.tar.gz

settingsFile=$MAVEN_HOME/conf/settings.xml
cp settings.tmpl.xml $settingsFile
sed -i -e "s|###USERNAME###|$ORACLE_OTN_USER|g" $settingsFile
sed -i -e "s|###PASSWORD###|$ORACLE_OTN_PASSWORD|g" $settingsFile

$MAVEN_HOME/bin/mvn -v
