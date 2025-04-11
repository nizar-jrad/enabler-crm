# enabler-crm


Contents
========

* [Description](#description)
* [Build](#Build)
* [Configuration](#Démarage application)
* [Lancement des tests unitaires](#Lancement des tests unitaires)
* [Configuration Spécifique sur le serveur](#Configuration Spécifique sur le serveur)
* [Release Note](/CHANGELOG.md)


### Description

Le projet enabler-crm est le composant de la couche soa permetant
la laison entre le composant en charge de la relation client (DCRM) et le autre composant du SI.


### Build

+ Configuration du projet en build maven
+ Lancement de la generation des sources

### Démarage application

+ Lancement de l'application JAVA a partir de la classe **_EnablerCrmConfigration_**
+ Ajout de l'emplacement du fichier application.properties dans les VM options :  
  **-Dspring.config.location=**
+ Ajout de l'emplacement du fichier de log  dans les VM options :  
  **-Dlogging.config=**

Les fichiers de configuration sont disponibles dans le dossier src/conf/development du projet.

### Lancement des tests unitaires

+ Lancement de tout les tests unitaires
    + Choisir le projet et lancer les test unitaire en java8 avec junit4
+ Lancement d'un tests unitaire spécifique
    + Ce possitioner sur le test a lancer et executer un run junit 4 avec java 8

### Configuration Spécifique sur le serveur

Afin d'améliorer les performance mémoire du composants il est recommandé d'utiliser la configuraiton de JVM suivant lors du démarage de l'application sur les environement d'intégration continue, de recette et de production :  
-Xms1g  
-Xmx1g  
-XX:+UseG1GC  
-XX:MaxGCPauseMillis=500  
-XX:+UseStringDeduplication   
-XX:+UnlockExperimentalVMOptions   
-XX:ParallelGCThreads=6  
-XX:ConcGCThreads=3  
-XX:+PrintGC  
-XX:+PrintGCDateStamps   
-XX:G1MixedGCLiveThresholdPercent=60

#### Configuration proxy

-Dhttp.proxySet=true
-Dhttp.proxyHost=192.168.106.229
-Dhttp.proxyPort=8080
-Dhttps.proxyHost=192.168.106.229
-Dhttps.proxyPort=8080
-Dhttp.nonProxyHosts="172.23.*|172.24.*|in-tst-ecatalogue-as1|tst-promo-as1.ms.fcm"