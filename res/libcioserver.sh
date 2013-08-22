#!/bin/sh
export JAVA_HOME=/home/cavallinux/Desarrollo/vm/jdk1.6.0_35
export MAIN_CLASS=cl.bancochile.monitor.tx.Server
export LIB_CIO_MAIN_DIR=/home/cavallinux/Desarrollo/appservers/Mocks/LibCIOServerMock
export MAIN_CONFIG_FILE=$LIB_CIO_MAIN_DIR/conf/libcioserversocket.properties
export LOG4J_CONFIG_FILE=$LIB_CIO_MAIN_DIR/conf/log4j.properties
export JAVA_OPTS="-Dlog4j.config.file=$LOG4J_CONFIG_FILE -Dmain.config.file=$MAIN_CONFIG_FILE -Dcioserver.logs.dir=${LIB_CIO_MAIN_DIR}/logs -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=8880 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Djava.awt.headless=true -XX:+CMSClassUnloadingEnabled -XX:+UseCompressedOops -XX:MaxPermSize=1024m -Xms512m -Xmx1024m -Xdebug -Xnoagent -XX:-OmitStackTraceInFastThrow -Xrunjdwp:transport=dt_socket,address=7199,server=y,suspend=n"

cp=$(find $LIB_CIO_MAIN_DIR/lib -name "*.jar" -exec printf :{} ';')

"$JAVA_HOME/bin/java" -classpath "$cp" $JAVA_OPTS $MAIN_CLASS $@