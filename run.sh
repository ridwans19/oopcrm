#!/bin/bash
# Compile and run DMS application

cd src
javac -cp ".:../lib/postgresql-42.7.10.jar" com/dms/**/*.java com/dms/*.java
java -cp ".:../lib/postgresql-42.7.10.jar" com.dms.Main
