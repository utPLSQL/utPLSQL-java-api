#!/bin/bash
set -ev
cd $(dirname $(readlink -f $0))

mavenSettings=$HOME/.m2/settings.xml

if [ -d $mavenSettings ]; then
    echo "Using cached maven user config..."
    exit 0
fi

if [ "$ORACLE_OTN_USER" == "" ] || [ "$ORACLE_OTN_PASSWORD" == "" ]; then
    echo "Oracle OTN username/password not specified."
    exit 1
fi

# If not cached, download and install maven.
# Then create the settings file, with username/password for oracle server.
# curl -L -O "http://mirror.nbtelecom.com.br/apache/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.tar.gz"
# tar -xzf apache-maven-3.3.9-bin.tar.gz
# mv apache-maven-3.3.9 $MAVEN_HOME
# rm apache-maven-3.3.9-bin.tar.gz

curl -L -O "http://central.maven.org/maven2/org/apache/maven/wagon/wagon-http/2.8/wagon-http-2.8-shaded.jar"
sudo mv wagon-http-2.8-shaded.jar $MAVEN_HOME/lib/ext/

# MAVEN_CFG=$MAVEN_HOME/conf/settings.xml
cp settings.tmpl.xml $mavenSettings
sed -i -e "s|###USERNAME###|$ORACLE_OTN_USER|g" $mavenSettings
sed -i -e "s|###PASSWORD###|$ORACLE_OTN_PASSWORD|g" $mavenSettings

# $MAVEN_HOME/bin/mvn -v
