mvn clean package
$aggregator = "$env:CATALINA_HOME\webapps\aggregator"
if (Test-Path $aggregator) {
    rm $aggregator -Force -Recurse
}
if (Test-Path "$aggregator.war") {
rm "$aggregator.war"
}
cp .\target\aggregator-0.0.1-SNAPSHOT.war "$aggregator.war"
catalina start