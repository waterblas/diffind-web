# diffind-web

**diffind-web** is a web MVC service project built with [Spring Boot](http://projects.spring.io/spring-boot/).
It support search service for the [bbs](http://bbs.byr.cn/).

Currently, diffind-web is link to a [elasticsearch](https://www.elastic.co/) engine, and its data come from
[Diffind](https://github.com/waterblas/Diffind), a common crawler at search engine pattern.

## Build
diffind-web is built with Gradle, so the preferred way is running Gradle Wrapper.

run the application at the project root dir using:

```
./gradlew bootRun
```

Or build a single executable JAR file that contains all the necessary dependencies, classes, and resources:

```
./gradlew build

```

***Note: Don't build as root user***

Then you can run the JAR file:

```
java -jar site/build/libs/diffind-web-0.1.0.jar
```

## License
Copyright (c) 2016 Donvan

Licensed under the Mozilla Public License Version 2.0,
You may obtain a copy of the License at [MPL](https://www.mozilla.org/en-US/MPL/2.0/).