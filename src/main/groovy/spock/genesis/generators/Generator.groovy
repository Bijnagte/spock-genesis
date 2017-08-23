package spock.genesis.generators

import groovy.transform.CompileStatic
import spock.genesis.extension.ExtensionMethods
import spock.genesis.generators.values.NullGenerator

/**
 * An Iterator that generates a type (usually lazily)
 * @param < E >   the generated type
 */
@CompileStatic
abstract class Generator<E> implements Iterable<E>, Closeable {
    protected final Random random = new Random()

    /**
     * Wraps this generator in a generator that returns values that matches the supplied predicate
     * @param predicate
     * @return a FilteredGenerator
     */
    FilteredGenerator<E> filter(Closure predicate) {
        new FilteredGenerator<E>(this, predicate)
    }

    @SuppressWarnings(['UnnecessaryPublicModifier']) // needed for generic parsing issue
    public <T> TransformingGenerator<E, T> map(Closure<T> transform) {
        new TransformingGenerator<E, T>(this, transform)
    }

    @SuppressWarnings(['UnnecessaryPublicModifier']) // needed for generic parsing issue
    public <T> ChainedGenerator<E, T> flatMap(Closure<Generator<E>> transform) {
        new ChainedGenerator<E, T>(this, transform)
    }

    TransformingGenerator<E, E> with(Closure<?> transform) {
        Closure withClosure = { generatedValue ->
            generatedValue.with(transform)
            generatedValue
        }
        new TransformingGenerator<E, E>(this, withClosure)
    }

    LimitedGenerator<E> take(int qty) {
        new LimitedGenerator<E>(this, qty)
    }

    SequentialMultisourceGenerator<E> then(Iterable<E>... iterables) {
        Generator<E>[] all = new Generator<E>[iterables.length + 1]
        all[0] = this
        for (int i = 0; i < iterables.length; i++) {
            all[i + 1] = ExtensionMethods.toGenerator(iterables[i])

        }
        new SequentialMultisourceGenerator<E>(all)
    }

    CyclicGenerator<E> repeat() {
        new CyclicGenerator<E>(this)
    }

    LimitedGenerator<E> multiply(int qty) {
        take(qty)
    }

    CyclicGenerator<E> getRepeat() {
        repeat()
    }

    MultiSourceGenerator<E> getWithNulls() {
        withNulls(100)
    }

    /**Wraps this generator in a {@link spock.genesis.generators.MultiSourceGenerator} that randomly returns nulls
     * @param resultsPerNull the average number of results from this generator per null result
     * @return {@link spock.genesis.generators.MultiSourceGenerator}
     */
    @SuppressWarnings('SpaceAroundMapEntryColon')
    MultiSourceGenerator<E> withNulls(int resultsPerNull) {
        Map weightedGenerators = [(this): resultsPerNull, (new NullGenerator<E>()): 1]
        new MultiSourceGenerator<E>(weightedGenerators)
    }

    MultiSourceGenerator and(Iterable iterable) {
        if (MultiSourceGenerator.isAssignableFrom(this.getClass())) {
            ((MultiSourceGenerator) this) + iterable
        } else {
            new MultiSourceGenerator([this, iterable])
        }
    }

    abstract UnmodifiableIterator<E> iterator()

    /**
     * If false then the generator may still terminate when iterated
     * @return true if the Generator will terminate
     */
    boolean isFinite() {
        !iterator().hasNext()
    }

    List<E> getRealized() {
        this.collect().asList()
    }

    @SuppressWarnings('EmptyMethodInAbstractClass')
    void close() { }

    /**
     * Set the {@link Random} seed for this generator and all contained generators.
     * This method mutates the generator!
     * @param seed
     * @return this generator
     */
    Generator<E> seed(Long seed) {
        random.setSeed(seed)
        this
    }
}
