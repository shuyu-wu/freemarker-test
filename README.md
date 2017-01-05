Very simple [Freemarker] test tools using json data model.

How to use it?
=============
1) Put your static files (js, css, images, ...) into the static folder.
2) Put your templates files (*.ftl) and your data model json files (*.json) into the template folder (ex: sample.ftl and sample.json)
3) Run the jar file: `java -jar freemarket-test-1.0-SNAPSHOT.jar`
4) Test your template file on browser (ex: http://localhost:8080/sample.ftl)

* If you add any new folder to static file you need to restart the application server.
* Any template or data model changes does not need to restart the application server.
* Use `java -jar freemarket-test-1.0-SNAPSHOT.jar -h` for help (change port number, host & etc).
* Use `mvn clean install` if you want to build it or download binary version in `releases`.

[Freemarker]: http://freemarker.org