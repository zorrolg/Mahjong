#! /bin/sh
#base dir of the application
APP_BASE=`pwd`
echo $APP_BASE

#name of the application
APP_NAME=com.road.dts.DtsTools
echo $APP_NAME

#name of the file record the process id of the application
PROCESS_ID_FILE=$APP_BASE/tooldts.pid
echo $PROCESS_ID_FILE

#process id of the application
PROCESS_ID=`cat $PROCESS_ID_FILE`
echo $PROCESS_ID
#name of the file record the process id of the application
PROCESS_ID_FILE=$APP_BASE/tooldts.pid
echo $PROCESS_ID_FILE

#process id of the application
PROCESS_ID=`cat $PROCESS_ID_FILE`
echo $PROCESS_ID

echo "Choice your template version"
read DTSVERSION

echo "input the file name you want to update,format file1.xls|file2.xls...."
echo "if you want to update all,you needn't input any character."
read UPDATEFILE

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

$JAVA_HOME/bin/java -cp "$PATH" "$APP_NAME" "$DTSVERSION" "$UPDATEFILE"
echo $! > $PROCESS_ID_FILE
echo "$APP_NAME done success!"

exit 0
