Spock Genesis
===============
Mostly lazy data generators for property based testing using the [Spock](http://spockframework.org) test framework

Usage
-----
From gradle

```groovy
repositories {
    jcenter()
}

dependencies {
    testCompile 'com.nagternal:spock-genesis:0.1.0'
}
```
Examples
-----
See [SamplesSpec](./src/test/groovy/spock/genesis/SamplesSpec.groovy) for examples

Building Spock Genesis
--------------
The only prerequisite is that you have JDK 5 or higher installed.

After cloning the project, type `./gradlew clean build` (Windows: `gradlew clean build`). All build dependencies,
including [Gradle](http://www.gradle.org) itself, will be downloaded automatically (unless already present).

Resources
---------
* Spock Homepage -- http://spockframework.org
* GitHub -- http://github.Bijnagte/spock-genesis