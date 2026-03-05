#!/bin/bash
echo "Compiling with PostgreSQL driver..."
javac -d bin -cp "lib/postgresql-42.7.10.jar" -sourcepath src src/com/dms/**/*.java src/com/dms/*.java
echo "Done! Run with: java -cp bin:lib/postgresql-42.7.10.jar com.dms.Main"
