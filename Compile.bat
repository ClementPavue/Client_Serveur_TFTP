@echo off
REM Création du .jar du Client
MD dist
cd mtt
MD classes
cd src
javac command\*.java -d ../classes
javac mtt\*.java -d ../classes
cd ../classes
jar cvmf ../META-INF/MANIFEST.MF ../../dist/mtt.jar mtt/*.class command/*.class
cd ../..
REM Création du .jar du Serveur
cd Serveur
MD classes
cd src
javac *.java -d ../classes
cd ../classes
jar cvmf ../META-INF/MANIFEST.MF ../../dist/ServeurTFTP.jar *.class 
cd ../..
REM Création du .jar de l'Ui
cd mttUi
MD classes
cd src
javac command/*.java -d ../classes
javac ihm/*.java -d ../classes
javac mtt/*.java -d ../classes
cd ../classes
jar cvmf ../META-INF/MANIFEST.MF ../../dist/ClientTFTPUi.jar mtt/*.class command/*.class ihm/*.class 
cd ../..
cd mtt
REM Suppression des dossiers classes
rmdir classes/S/Q
cd ../Serveur 
rmdir classes/S/Q 
cd ../mttUi
rmdir classes/S/Q 

pause