# Étape 1 : Utilisation de l'image JDK 17 : contient l'environnement d'exécution Java nécessaire pour exécuter des applications Java.
FROM openjdk:17-alpine

# Auteur de l'image
LABEL authors="karmi"

WORKDIR /app

# hadhi mnin jat "pier le fichier JAR généré de la machine locale vers le répertoire de travail du conteneur
COPY target/Foyer-1.0.0.jar /app/Foyer-1.0.0.jar

# Cette ligne indique que le conteneur écoutera sur le port 8089
EXPOSE 8089

# Commande pour exécuter l'application Java dans le conteneur
ENTRYPOINT ["java", "-jar", "/app/Foyer-1.0.0.jar"]