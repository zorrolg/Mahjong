#!/bin/sh
#gameserver.sh
#To start or stop gameservermanagerclient.

#base dir of the application
APP_BASE=`pwd`
echo $APP_BASE

#name of the application
APP_NAME=com.citywar.GameServer
echo $APP_NAME

#name of the show to user
APP_SHOW=com.citywar.GameServerManagerClient

#name of the config file
CONFIG_FILE=$APP_BASE/config/GameServerConfig.properties
echo $CONFIG_FILE

MANAGER=manager
echo $MANAGER

#name of the file record the process id of the application
PROCESS_ID_FILE=$APP_BASE/gameserver_manager.pid
echo $PROCESS_ID_FILE

#process id of the application
PROCESS_ID=`cat $PROCESS_ID_FILE`
echo $PROCESS_ID

case "$1" in
    start)
        if [ "$PROCESS_ID" ]; then
            echo "PID file ($PROCESS_ID) found. Is $APP_SHOW still running? Start aborted."
            exit 1
        fi
        
        PATH=$CLASSPATH
        for i in $APP_BASE/*.jar;
        do
            PATH="$PATH":$i
        done
        
        for i in $APP_BASE/lib/*.jar;
        do
            PATH="$PATH":$i
        done
        echo $PATH
        
        /usr/bin/java -cp "$PATH" "$APP_NAME" "$CONFIG_FILE" "$MANAGER" 
        echo $! > $PROCESS_ID_FILE
        
        echo "$APP_SHOW started!"
    ;;
    stop)
        if [ "$PROCESS_ID" ]; then
            kill "$PROCESS_ID"
            rm -rf $PROCESS_ID_FILE
            echo "----------------------the $APP_SHOW been killed------------------"
        else
            echo "----------------------the $APP_SHOW is not running----------------"
        fi
    ;;
    *)
        echo "Usage: $0 start|stop"
    ;;
esac
exit 0
