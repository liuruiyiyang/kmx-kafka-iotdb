#!/bin/sh

if [ -z "${JSON2KAFKA_HOME}" ]; then
  export JSON2KAFKA_HOME="$(cd "`dirname "$0"`"/..; pwd)"
fi

echo JSON2KAFKA_HOME

MAIN_CLASS=tsinghua.KafkaIoTDB

CLASSPATH=""
for f in ${JSON2KAFKA_HOME}/lib/*.jar; do
  CLASSPATH=${CLASSPATH}":"$f
done


if [ -n "$JAVA_HOME" ]; then
    for java in "$JAVA_HOME"/bin/amd64/java "$JAVA_HOME"/bin/java; do
        if [ -x "$java" ]; then
            JAVA="$java"
            break
        fi
    done
else
    JAVA=java
fi


exec "$JAVA" -Dlogback.configurationFile=${JSON2KAFKA_HOME}/conf/logback.xml  -cp "$CLASSPATH" "$MAIN_CLASS" "$@"

exit $?