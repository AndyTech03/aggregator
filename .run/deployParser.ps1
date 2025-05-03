# Сборка Java
mvn clean package
# Деплой Java
$aggregator = "$env:CATALINA_HOME\webapps\ROOT"
if (Test-Path $aggregator) {
    Remove-Item $aggregator -Force -Recurse
}
if (Test-Path "$aggregator.war") {
    Remove-Item "$aggregator.war"
}
Copy-Item .\target\aggregator-0.0.1-SNAPSHOT.war "$aggregator.war"
catalina start -Dspring-boot.run.profiles=parser