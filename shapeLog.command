#!/bin/bash
cd `dirname $0`
echo "ログファイルのパスを入力してください： "
read FILEPATH
echo ""
javac src/shape/LogShaper.java
java src/shape/LogShaper.java $FILEPATH
echo "Enterを押すと終了します"
read A
