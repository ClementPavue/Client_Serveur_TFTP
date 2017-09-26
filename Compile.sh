mkdir dist
cd mtt
mkdir classes
cd ./src
javac -encoding ISO-8859-1 command/*.java -d ../classes  
javac -encoding ISO-8859-1 mtt/*.java -d ../classes
cd ../classes
jar cvmf ../META-INF/MANIFEST.MF ../../dist/mtt.jar mtt/*.class command/*.class
cd ../..
cd ./Serveur
mkdir classes
cd ./src
javac -encoding ISO-8859-1 *.java -d ../classes
cd ../classes
jar cvmf ../META-INF/MANIFEST.MF ../../dist/ServeurTFTP.jar *.class 
cd ../..
cd ./mttUi
mkdir classes
cd ./src
javac -encoding ISO-8859-1 command/*.java -d ../classes
javac -encoding ISO-8859-1 ihm/*.java -d ../classes
javac -encoding ISO-8859-1 mtt/*.java -d ../classes
cd ../classes
jar cvmf ../META-INF/MANIFEST.MF ../../dist/ClientTFTPUi.jar mtt/*.class command/*.class ihm/*.class 
cd ../..
cd ./mtt
rm classes -rf
cd ../Serveur 
rm classes -rf
cd ../mttUi
rm classes -rf
