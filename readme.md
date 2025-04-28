# Настройка окружения для отладки Java + Tomcat
1. Перейти на сайт [Java 21 Downloads](https://www.oracle.com/java/technologies/downloads/?er=221886#jdk21-windows)
и скачать `Installer` или архив. 
Установить и запомнить директорию установки.
2. Перейти на сайт [Apache Tomcat](https://tomcat.apache.org/download-11.cgi)
и скачать `Windows zip` или `Windows Service Installer`. 
Установить и запомнить директорию установки.
3. Задать переменную окружения `JAVA_HOME` = `C:\path\to\Java\jdk-21`
4. Задать переменную окружения `CATALINA_HOME` = `C:\path\to\tomcat\apache-tomcat-11.0.6`
5. Добавить в `Path` путь до `catalina` = `C:\path\to\tomcat\apache-tomcat-11.0.6\bin`
6. Добавить в `Path` путь до `mvn` = `C:\path\to\IntelliJ IDEA Community Edition 2024.1\plugins\maven\lib\maven3\bin`
7. Перезапустите IDE и/или терминалы
