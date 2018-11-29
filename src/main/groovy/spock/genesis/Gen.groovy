package spock.genesis

import groovy.transform.CompileStatic
import spock.genesis.extension.ExtensionMethods
import spock.genesis.generators.CyclicGenerator
import spock.genesis.generators.FactoryGenerator
import spock.genesis.generators.Generator
import spock.genesis.generators.IterableGenerator
import spock.genesis.generators.composites.DefinedMapGenerator
import spock.genesis.generators.composites.ListGenerator
import spock.genesis.generators.composites.PojoGenerator
import spock.genesis.generators.composites.RandomMapGenerator
import spock.genesis.generators.composites.TupleGenerator
import spock.genesis.generators.values.ByteArrayGenerator
import spock.genesis.generators.values.CharacterGenerator
import spock.genesis.generators.values.DateGenerator
import spock.genesis.generators.values.DoubleGenerator
import spock.genesis.generators.values.IntegerGenerator
import spock.genesis.generators.values.LocalDateGenerator
import spock.genesis.generators.values.LongGenerator
import spock.genesis.generators.values.RandomElementGenerator
import spock.genesis.generators.values.StringGenerator
import spock.genesis.generators.values.ValueGenerator

import java.time.LocalDate
import java.util.regex.Pattern

/**
 * Static factory methods for Generators
 */
@SuppressWarnings(['MethodCount'])
@CompileStatic
class Gen {

    /**
     * Produces a {@link StringGenerator} capable of producing values
     * of type {@link String}
     *
     * @return an infinite lazy String Generator
     */
    static StringGenerator getString() {
        new StringGenerator()
    }

    /**
     * Produces a {@link StringGenerator} capable of producing values
     * of type {@link String} with a maximum length passed as
     * parameter
     *
     * @param maxLength
     * @return an infinite lazy String Generator
     */
    static StringGenerator string(int maxLength) {
        new StringGenerator(maxLength)
    }

    /**
     * Produces a {@link StringGenerator} capable of producing values
     * of type {@link String}. These strings will take into account
     * potential values passed as parameter.
     *
     * @param potentialCharacters A {@link String} with the potential
     * characters that we would like to see in the generated values
     * @return an infinite lazy String Generator {@link
     * StringGenerator}
     */
    static StringGenerator string(String potentialCharacters) {
        new StringGenerator(potentialCharacters)
    }

    /**
     * Produces a {@link StringGenerator} capable of producing values
     * of type {@link String} with a maximum and minimum length passed
     * as parameters
     *
     * @param minLength minimum length of the generated values
     * @param maxLength maximum length of the generated values
     * @return an infinite lazy String Generator {@link
     * StringGenerator}
     */
    static StringGenerator string(int minLength, int maxLength) {
        new StringGenerator(minLength, maxLength)
    }

    /**
     * Produces a {@link StringGenerator} capable of producing values
     * of type {@link String} from a given {@link Pattern}
     *
     * @param regex the pattern expression used as template for
     * generated values
     * @return an infinite lazy String Generator {@link
     * StringGenerator}
     */
    static StringGenerator string(Pattern regex) {
        new StringGenerator(regex)
    }

    /**
     * Produces a {@link ByteArrayGenerator} capable of producing
     * random values of type {@link Byte}
     *
     * @return an infinite lazy {@link ByteArrayGenerator}
     */
    static ByteArrayGenerator getBytes() {
        new ByteArrayGenerator()
    }

    /**
     * Produces a {@link IntegerGenerator} capable of producing
     * random values of type {@link Integer}
     *
     * @return an infinite lazy {@link IntegerGenerator}
     */
    static IntegerGenerator getInteger() {
        new IntegerGenerator()
    }

    /**
     * Produces a {@link IntegerGenerator} capable of producing random
     * values of type {@link Integer} from a minimum number to a
     * maximum number.
     *
     * @param min minimum generated number allowed
     * @param max maximum generated number allowed
     * @return an infinite lazy {@link IntegerGenerator}
     */
    static IntegerGenerator integer(int min, int max) {
        new IntegerGenerator(min, max)
    }

    /**
     * Produces a {@link IntegerGenerator} capable of producing random
     * values of type {@link Integer} from a minimum number to a
     * maximum number.
     *
     * @param range range representing the minimum and maximum
     * possible number allowed
     * @return an infinite lazy {@link IntegerGenerator}
     */
    static IntegerGenerator integer(IntRange range) {
        new IntegerGenerator(range.from, range.to)
    }

    /**
     * Produces a {@link LongGenerator} capable of producing random
     * values of type {@link Long}
     *
     * @return an infinite lazy {@link LongGenerator}
     */
    static LongGenerator getLong() {
        new LongGenerator()
    }

    /**
     * Produces a {@link CharacterGenerator} capable of producing
     * random values of type {@link Character}
     *
     * @return an infinite lazy {@link CharacterGenerator}
     */
    static CharacterGenerator getCharacter() {
        new CharacterGenerator()
    }

    /**
     * Produces a {@link CharacterGenerator} capable of producing
     * random values of type {@link Character} from a given
     * set of potential characters
     *
     * @param potentialCharacters a {@link String} containing a set of
     * potential characters that can be used in new generated values
     * @return an infinite lazy {@link CharacterGenerator}
     */
    static CharacterGenerator character(String potentialCharacters) {
        new CharacterGenerator(potentialCharacters)
    }

    /**
     * Produces a {@link DoubleGenerator} capable of producing random
     * values of type {@link Double}
     *
     * @return an infinite lazy {@link DoubleGenerator}
     */
    static DoubleGenerator getDouble() {
        new DoubleGenerator()
    }

    /**
     * Produces a {@link ValueGenerator} capable of producing values
     * of the same kind passed as parameter
     *
     * @param value value you want to produce over an over again
     * @return an infinite lazy of type {@link ValueGenerator}
     */
    static <T> ValueGenerator<T> value(T value) {
        new ValueGenerator(value)
    }

    /**
     * Produces a lazy infinite generator that returns a random
     * element from a source {@link Collection}
     *
     * @param source {@link Collection} of type {@link Object} to pick
     * from
     * @return a {@link RandomElementGenerator}
     */
    static <T> RandomElementGenerator<T> any(Collection<T> source) {
        new RandomElementGenerator(source)
    }

    /**
     * Produces a lazy infinite generator that returns a random
     * element from a source {@link Collection}
     *
     * @param source variable arguments of type {@link Object} to pick
     * from
     * @return a {@link RandomElementGenerator}
     */
    static <T> RandomElementGenerator<T> any(T... source) {
        new RandomElementGenerator(source.toList())
    }

    /**
     * Produces a lazy infinite {@link CyclicGenerator} that repeats
     * an {@link Iterable}.
     *
     * @param source {@link Iterable} to repeat over
     * @return an instance of {@link CyclicGenerator}
     */
    static <T> CyclicGenerator cycle(Iterable<T> source) {
        new IterableGenerator<T>(source).repeat()
    }

    /**
     * Produces a lazy infinite {@link PojoGenerator} that creates
     * instances of objects of a given type
     *
     * @param keysToValueGenerators generators per each field
     * @param target type of the object we would like to generate
     * @return an instance of {@link PojoGenerator}
     */
    static <T> PojoGenerator<T,Map> type(Map<String, Object> keysToValueGenerators, Class<T> target) {
        new PojoGenerator(target, map(keysToValueGenerators))
    }

    /**
     * Produces a lazy infinite {@link PojoGenerator} that creates
     * instances of objects of a given type
     *
     * @param target type of the object we would like to generate
     * @param argGenerators generators per each field
     * @return an instance of {@link PojoGenerator}
     */
    static <T> PojoGenerator<T, List> type(Class<T> target, Iterable... argGenerators) {
        new PojoGenerator(target, tuple(argGenerators))
    }

    /**
     * Produces a lazy infinite {@link PojoGenerator} that creates
     * instances of {@link Map}
     *
     * @param keysToValueGenerators generators per each map field
     * @return an instance of {@link DefinedMapGenerator}
     */
    static DefinedMapGenerator map(Map keysToValueGenerators) {
        new DefinedMapGenerator(keysToValueGenerators)
    }

    /**
     * Produces a lazy infinite {@link PojoGenerator} that creates
     * instances of {@link Map}
     *
     * @param keyGenerator generators of map keys
     * @param valueGenerator generators of map values
     * @return an instance of {@link RandomMapGenerator}
     */
    static <K,V> RandomMapGenerator map(Iterable<K> keyGenerator, Iterable<V> valueGenerator) {
        new RandomMapGenerator<K,V>(keyGenerator, valueGenerator)
    }

    /**
     * Produces a lazy infinite {@link ListGenerator} that creates
     * instances of {@link List} based on a given value generator
     *
     * @param valueGenerator generates values of the produced list
     * @return an instance of {@link ListGenerator}
     */
    static <T> ListGenerator<T> list(Generator<T> valueGenerator) {
        new ListGenerator(valueGenerator)
    }

    /**
     * Produces a lazy infinite {@link ListGenerator} that creates
     * instances of {@link List} of a given length and it's based on a
     * given value generator
     *
     * @param valueGenerator generates values of the produced list
     * @param maxLength maximum length of generated lists
     * @return an instance of {@link ListGenerator}
     */
    static <T> ListGenerator<T> list(Generator<T> valueGenerator, int maxLength) {
        new ListGenerator(valueGenerator, maxLength)
    }

    /**
     * Produces a lazy infinite {@link ListGenerator} that creates
     * instances of {@link List} of a given max and min length and
     * it's based on a given value generator
     *
     * @param valueGenerator generates values of the produced list
     * @param minLength minimum length of generated lists
     * @param maxLength maximum length of generated lists
     * @return an instance of {@link ListGenerator}
     */
    static ListGenerator list(Generator valueGenerator, int minLength, int maxLength) {
        new ListGenerator(valueGenerator, minLength, maxLength)
    }

    /**
     * Produces a lazy infinite {@link TupleGenerator} that creates
     * tuples with values based on the generators passed as parameter
     *
     * @param generators generators for each tuple element
     * @return an instance of {@link TupleGenerator}
     */
    static <T> TupleGenerator<T> tuple(Iterable<T>... generators) {
        new TupleGenerator(generators)
    }

    /**
     * Produces a lazy infinite {@link DateGenerator} that generates
     * random instances of {@link Date}
     *
     * @return an instance of {@link DateGenerator}
     */
    static DateGenerator getDate() {
        new DateGenerator()
    }

    /**
     * Produces a lazy infinite {@link DateGenerator}. This generator
     * will create random instances of {@link Date} from a minimum
     * date to a maximum date.
     *
     * @param minDate minimum possible date
     * @param maxDate maximum possible date
     * @return an instance of {@link DateGenerator}
     */
    static DateGenerator date(Date minDate, Date maxDate) {
        new DateGenerator(minDate, maxDate)
    }

    /**
     * Produces a lazy infinite {@link LocalDateGenerator} that generates
     * random instances of {@link java.time.LocalDate}
     *
     * @return an instance of {@link LocalDateGenerator}
     */
    static LocalDateGenerator getLocalDate() {
        new LocalDateGenerator()
    }

    /**
     * Produces a lazy infinite {@link LocalDateGenerator}. This generator
     * will create random instances of {@link java.time.LocalDate} from a minimum
     * date to a maximum date.
     *
     * @param minDate minimum possible date
     * @param maxDate maximum possible date
     * @return an instance of {@link LocalDateGenerator}
     */
    static LocalDateGenerator localDate(LocalDate minDate, LocalDate maxDate) {
        new LocalDateGenerator(minDate, maxDate)
    }

    /**
     * Produces a lazy infinite {@link FactoryGenerator}. This
     * generator will produce random instances of the values returned
     * by a given {@link Closure}
     *
     * @param factory the closure that defines the generated value
     * @return an instance of {@link FactoryGenerator}
     */
    static <T> FactoryGenerator<T> using(Closure<T> factory) {
        new FactoryGenerator(factory)
    }

    /**
     * Produces a lazy infinite {@link Generator}. This
     * generator will produce the values taken from a given {@link
     * Iterable} in the order they were defined
     *
     * @param iterable {@link Iterable} declaring values to produce
     * @param finite sets the generator as finite or not
     * @return an instance of {@link Generator}
     */
    static <T> Generator<T> these(Iterable<T> iterable, boolean finite = false) {
        ExtensionMethods.toGenerator(iterable, finite)
    }

    /**
     * Produces a lazy infinite {@link Generator}. This
     * generator will produce classes of the type passed as parameter
     *
     * @param clazz the type of class you want to produce
     * @return an instance of {@link Generator}
     */
    static Generator these(Class clazz) {
        ExtensionMethods.toGenerator(clazz)
    }

    /**
     * Produces a lazy infinite {@link Generator}. This
     * generator will produce the values taken from the values passed
     * as arguments in the order they were defined
     *
     * @param values variable arguments to get values from
     * @return an instance of {@link Generator}
     */
    static <T> Generator<T> these(T... values) {
        ExtensionMethods.toGenerator(values)
    }

    /**
     * Produces a lazy infinite {@link Generator}. This
     * generator will produce the values taken from the values passed
     * as arguments in the order they were defined
     *
     * @param values collection to get values from
     * @return an instance of {@link Generator}
     */
    static <T> Generator<T> these(Collection<T> values) {
        ExtensionMethods.toGenerator(values)
    }

    /**
     * Produces a lazy infinite {@link Generator}. This
     * generator will produce copies of the value passed as parameter
     *
     * @param value sample value
     * @return an instance of {@link Generator} that produces copies
     * of the sample value
     */
    static <T> Generator<T> once(T value) {
        these([value])
    }
}
