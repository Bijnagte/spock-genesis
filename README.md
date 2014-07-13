Spock Genesis
===============
Mostly lazy data generators for property based testing using the [Spock](http://spockframework.org) test framework

Providing test data, especially when attempting to test for a wide range of inputs is tedious if not impossible to do by hand.
Generating inputs allows for more thorough testing without a dramatic increase in effort.
In Spock [data driven tests] (http://spock-framework.readthedocs.org/en/latest/data_driven_testing.html#data-pipes) can have data provided by any [Iterator](http://docs.oracle.com/javase/7/docs/api/java/util/Iterator.html).
Spock Genesis provides a variety of classes that extend from [Generator](./src/main/groovy/spock/genesis/generators/Generator.groovy) which meet that interface.
Where possible the generators are lazy and infinite.

Usage
-----
From gradle:

```groovy
repositories {
    jcenter()
}

dependencies {
    testCompile 'com.nagternal:spock-genesis:0.1.0'
}
```

The primary way of constructing generators is [spock.genesis.Gen](./src/main/groovy/spock/genesis/Gen.groovy) which provides static factory methods for data generators.
```groovy
@Unroll
def 'test reverse #string'() {
    when:
    def reversed = string.reverse()
    
    then:
    reversed.size() == string.size()
    if (string) {
        string.eachWithIndex { letter, i ->
            letter == reversed[-(i + 1)] 
        }
    }
    reversed.reverse() == string
    
    where:
    string << Gen.these('').then(Gen.string).take(10000)
}
```
See [SamplesSpec](./src/test/groovy/spock/genesis/SamplesSpec.groovy) for more examples

Building Spock Genesis
--------------
The only prerequisite is that you have JDK 5 or higher installed.

After cloning the project, type `./gradlew clean build` (Windows: `gradlew clean build`). All build dependencies,
including [Gradle](http://www.gradle.org) itself, will be downloaded automatically (unless already present).

Resources
---------
* Spock Homepage -- http://spockframework.org
* GitHub -- http://github.Bijnagte/spock-genesis