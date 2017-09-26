P08 - My TFTP Toolkit - Client Serveur TFTP.



Contributeurs

   - BROU BONI K�vin - Chef de projet
   - CHAOUCH Myriam - Responsable Client
   - PAVUE Cl�ment - Responsable Technique
   - EL MOUTAOUAKKIL Fatima Ezzahra - Responsable Qualit�/Documentation


Description 

   mtt My TFTP Toolkit est un client et serveur TFTP fonctionnant sous Windows, Linux et Mac OS X qui contient:
     -mtt, une application console d�di�e au client.
     -mttUi, une extension de cette application dot�e d'une interface graphique.
     -Serveur, une application console d�di�e au serveur.


Fontionnalit�s

   - Client-R�cup�ration et envoi d'un fichier.
   - Serveur-Envoi et r�cup�ration d'un fichier.
   - Serveur-Support de connexions simultan�es.


Compilation et installation

   Num�ro de r�vision du livrable : derni�re r�vision
   Pour �x�cuter le compilateur, il suffit de double clicker sur compil.bat sous Windows ou compil.bsh sous Linux ou Mac OS X.


Description des fonctionnalit�s et explication des commandes et de leurs options

Pour d�marrer le serveur, il faut lancer le fichier ServerTFTP.bat. L'adresse IP s'affiche et le serveur attend une connexion.
Le serveur supporte des connexions simultan�es.

   1. mtt
      Pour le d�marrer, il faut lancer le fichier ClientTFTP.bat.
      1.1 R�cup�ration d'un fichier.
         Le fichier doit obligatoirement se trouver dans le m�me r�pertoire que le fichier ServerTFTP.bat ou ServerTFTP.bsh selon l'OS.
         Pour r�cup�rer un fichier depuis le client: Tapez "Get" + adresse IP du serveur + nom exacte du fichier avec l'extension.
         Exemple: pour r�cup�rer un fichier nomm� text.txt au serveur dont l'adresse IP est 10.117.61.11, il faut taper: Get 10.117.61.11 text.txt
      1.2 Envoi d'un fichier.
	 Le fichier doit obligatoirement se trouver dans le m�me r�pertoire que le fichier ClientTFTP.bat ou ClientTFTP.bsh selon l'OS.
         Pour envoyer un fichier vers le client: Tapez "Put" + adresse IP du serveur + nom exacte du fichier avec l'extension.
	 Exemple: pour envoyer un fichier nomm� text.txt au serveur dont l'adresse IP est 10.117.61.11, il faut taper: Put 10.117.61.11 text.txt

   2. mttUi
      Pour le d�marrer, il faut lancer le fichier ClientTFTPUi.bat.
      2.1 R�cup�ration d'un fichier depuis le client.
	 Le fichier doit obligatoirement se trouver dans le m�me r�pertoire que le fichier ServerTFTP.bat ou ServerTFTP.bsh selon l'OS.
         Si vous souhaitez r�cup�rer un fichier depuis le client, c'est � l'aide de l'interface graphique: Saisissez l'adresse IP du serveur, le nom exacte du fichier avec l'extension et en suite appuyez sur le boutton "Get".
	 Exemple: pour r�cup�rer un fichier nomm� text.txt au serveur dont l'adresse IP est 10.117.61.11, il faut saisir 10.117.61.11, puis text.txt dans les champs correspondant avant d'appuyer sur "Get".
      2.2 Envoi d'un fichier vers le client.
	 Le fichier doit obligatoirement se trouver dans le m�me r�pertoire que le fichier ClientTFTPUi.bat ou ClientTFTPUi.bsh selon l'OS.
         Si vous souhaitez envoyer un fichier vers le client, c'est � l'aide de l'interface graphique: Saisissez l'adresse IP du serveur, le nom exacte du fichier avec l'extension et en suite appuyez sur le boutton "Put".
	 Exemple: pour envoyer un fichier nomm� text.txt au serveur dont l'adresse IP est 10.117.61.11, il faut saisir 10.117.61.11, puis text.txt dans les champs correspondant avant d'appuyer sur "Put".


Anomalies

   -Les fichiers de grande taille peuvent �tre alt�r�s.
   -Le serveur peut ne pas marcher � cause d'un processus zombie.


Remarques

   -Le protocole UDP ne garantie pas l'envoi et/ou la r�ception totale de fichiers.