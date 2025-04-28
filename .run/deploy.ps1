# Сборка React
$build = "react_app\build"
if (Test-Path $build) {
    Remove-Item $build -Force -Recurse
}
Set-Location "react_app"
pnpm build
Set-Location ..

$static = "src\main\resources\static"
if (Test-Path $static) {
    Remove-Item $static -Force -Recurse
}
Copy-Item $build $static -Force -Recurse
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
catalina start