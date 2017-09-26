P08 - My TFTP Toolkit - Client Serveur TFTP.



Contributeurs

   - BROU BONI Kévin - Chef de projet
   - CHAOUCH Myriam - Responsable Client
   - PAVUE Clément - Responsable Technique
   - EL MOUTAOUAKKIL Fatima Ezzahra - Responsable Qualité/Documentation


Description 

   mtt My TFTP Toolkit est un client et serveur TFTP fonctionnant sous Windows, Linux et Mac OS X qui contient:
     -mtt, une application console dédiée au client.
     -mttUi, une extension de cette application dotée d'une interface graphique.
     -Serveur, une application console dédiée au serveur.


Fontionnalités

   - Client-Récupération et envoi d'un fichier.
   - Serveur-Envoi et récupération d'un fichier.
   - Serveur-Support de connexions simultanées.


Compilation et installation

   Numéro de révision du livrable : dernière révision
   Pour éxécuter le compilateur, il suffit de double clicker sur compil.bat sous Windows ou compil.bsh sous Linux ou Mac OS X.


Description des fonctionnalités et explication des commandes et de leurs options

Pour démarrer le serveur, il faut lancer le fichier ServerTFTP.bat. L'adresse IP s'affiche et le serveur attend une connexion.
Le serveur supporte des connexions simultanées.

   1. mtt
      Pour le démarrer, il faut lancer le fichier ClientTFTP.bat.
      1.1 Récupération d'un fichier.
         Le fichier doit obligatoirement se trouver dans le même répertoire que le fichier ServerTFTP.bat ou ServerTFTP.bsh selon l'OS.
         Pour récupérer un fichier depuis le client: Tapez "Get" + adresse IP du serveur + nom exacte du fichier avec l'extension.
         Exemple: pour récupérer un fichier nommé text.txt au serveur dont l'adresse IP est 10.117.61.11, il faut taper: Get 10.117.61.11 text.txt
      1.2 Envoi d'un fichier.
	 Le fichier doit obligatoirement se trouver dans le même répertoire que le fichier ClientTFTP.bat ou ClientTFTP.bsh selon l'OS.
         Pour envoyer un fichier vers le client: Tapez "Put" + adresse IP du serveur + nom exacte du fichier avec l'extension.
	 Exemple: pour envoyer un fichier nommé text.txt au serveur dont l'adresse IP est 10.117.61.11, il faut taper: Put 10.117.61.11 text.txt

   2. mttUi
      Pour le démarrer, il faut lancer le fichier ClientTFTPUi.bat.
      2.1 Récupération d'un fichier depuis le client.
	 Le fichier doit obligatoirement se trouver dans le même répertoire que le fichier ServerTFTP.bat ou ServerTFTP.bsh selon l'OS.
         Si vous souhaitez récupérer un fichier depuis le client, c'est à l'aide de l'interface graphique: Saisissez l'adresse IP du serveur, le nom exacte du fichier avec l'extension et en suite appuyez sur le boutton "Get".
	 Exemple: pour récupérer un fichier nommé text.txt au serveur dont l'adresse IP est 10.117.61.11, il faut saisir 10.117.61.11, puis text.txt dans les champs correspondant avant d'appuyer sur "Get".
      2.2 Envoi d'un fichier vers le client.
	 Le fichier doit obligatoirement se trouver dans le même répertoire que le fichier ClientTFTPUi.bat ou ClientTFTPUi.bsh selon l'OS.
         Si vous souhaitez envoyer un fichier vers le client, c'est à l'aide de l'interface graphique: Saisissez l'adresse IP du serveur, le nom exacte du fichier avec l'extension et en suite appuyez sur le boutton "Put".
	 Exemple: pour envoyer un fichier nommé text.txt au serveur dont l'adresse IP est 10.117.61.11, il faut saisir 10.117.61.11, puis text.txt dans les champs correspondant avant d'appuyer sur "Put".


Anomalies

   -Les fichiers de grande taille peuvent être altérés.
   -Le serveur peut ne pas marcher à cause d'un processus zombie.


Remarques

   -Le protocole UDP ne garantie pas l'envoi et/ou la réception totale de fichiers.