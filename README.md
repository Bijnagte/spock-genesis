Spock Genesis
===============
Mostly lazy data generators for property based testing using the [Spock](http://spockframework.org) test framework

Providing test data, especially when attempting to test for a wide range of inputs is tedious if not impossible to do by hand.
Generating inputs allows for more thorough testing without a dramatic increase in effort.
In Spock [data driven tests] (http://spock-framework.readthedocs.org/en/latest/data_driven_testing.html#data-pipes) can have data provided by any [Iterator](http://docs.oracle.com/javase/7/docs/api/java/util/Iterator.html).
Spock Genesis provides a variety of classes that extend from [Generator](./src/main/groovy/spock/genesis/generators/Generator.groovy) which meet that interface.
Where possible the generators are lazy and infinite.

![build status](https://circleci.com/gh/Bijnagte/spock-genesis.svg?style=shield&circle-token=ccab052d8c597ae916463f8319738d89e3d8a640)

Usage
-----
From gradle:

```groovy
repositories {
    jcenter()
}

dependencies {
    testCompile 'com.nagternal:spock-genesis:0.4.0'
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
        string << Gen.these('', 'foo').then(Gen.string).take(10000)
}
```

Given a Person class create a generator that can supply instances:

```groovy
    Gen.type(Person,
        id: Gen.integer(200..10000),
        name: Gen.string(~/[A-Z][a-z]+( [A-Z][a-z]+)?/),
        birthDate: Gen.date(Date.parse('MM/dd/yyyy','01/01/1940'), new Date()),
        title: Gen.these('', null).then(Gen.any('Dr.', 'Mr.', 'Ms.', 'Mrs.')),
        gender: Gen.character('MFTU'))
```

See [SamplesSpec](./src/test/groovy/spock/genesis/SamplesSpec.groovy) for more examples


Change log
----------
### 0.2.0
* Update dependencies

### 0.3.0
* Add support for using regular expressions for String generation. Thanks to Generex
* Using Groovy constructor selection for single arg Pojo construction

### 0.4.0
* improve toGenerator extension methods
* better error handling for POJO construction
* isFinite method to determine if generator will terminate

Building Spock Genesis
--------------
The only prerequisite is that you have JDK 7 or higher installed.

After cloning the project, type `./gradlew clean build` (Windows: `gradlew clean build`). All build dependencies,
including [Gradle](http://www.gradle.org) itself, will be downloaded automatically (unless already present).

Resources
---------
* Spock Homepage -- http://spockframework.org
* GitHub -- https://github.com/Bijnagte/spock-genesis
* Generex -- https://github.com/mifmif/Generex