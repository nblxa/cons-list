[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.nblxa/cons-list/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.nblxa/cons-list)
[![Build Status](https://travis-ci.com/nblxa/cons-list.svg?branch=master)](https://travis-ci.com/nblxa/cons-list)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=cons-list&metric=alert_status)](https://sonarcloud.io/dashboard?id=cons-list)
[![Coverage Status](https://coveralls.io/repos/github/nblxa/cons-list/badge.svg?branch=master)](https://coveralls.io/github/nblxa/cons-list?branch=master)

# Just the Cons List and nothing else

`ConsList` is an immutable singly-linked list:
[ConsList.java](cons-list/src/main/java/io/github/nblxa/ConsList.java).

This reliable and performant version of singly-linked
Cons List in Java implements the `java.util.Collection` interface, giving
the programmers the access to all its methods such as `stream()`,
`toString()` and others.

This implementation of Cons List does not use recursion, so it will
not cause a `StackOverflowError`. A list is allowed to contain more
than `Integer.MAX_VALUE` elements and will produce an overhead of two
object references per element.

For further performance increase, primitive-type specializations can be used:
`IntConsList`, `LongConsList`, and `DoubleConsList`, all of which extend
the parent `ConsList` interface, while adding their own primitive-based methods
on top.

Collection methods are implemented:
* `size()` iterates through the list to avoid storing list length
* `isEmpty()` does not try to calculate the size to see if it is 0.

Java 8 Streams support:
* `spliterator()` has the right characteristics for the cons list:
  `ORDERED` and `IMMUTABLE`.
* a custom Collector `toConsCollector()` is provided.

Methods `equals(Object o)` and `hashCode()` are also implemented
without recursion.

Finally, the class is `Serializable`.

Cons List, due to its simplicity and immutability, is an ideal data
structure for multi-threaded processing of ordered collections of data.
Direct implementations however, suffer from heavy recsive calls
and may cause high stack memory consumption, if not more severe issues.
This implementation fuses the power of the immutable cons list
with the wide range of functionality offered by the Java Collections
and Streams API.

## Maven

```xml
<dependency>
    <groupId>io.github.nblxa</groupId>
    <artifactId>cons-list</artifactId>
    <version>2.0.0-SNAPSHOT</version>
</dependency>
```

The library uses [Semantic Versioning](https://semver.org).

## Usage

Static imports for ease of use:

```java
import static io.github.nblxa.cons.ConsList.*;
```

Create an empty Cons list:

```java
Collection<?> empty = nil();
```

Create a list of an arbitrary length:

```java
Collection<String> strings = list("Hello", "functional", "programming", "!");
```

Adding new elements to a list is just creating new immutable lists:

```java
Collection<String> strings2 = cons("ConsList", cons("says:", strings));
```

Note that since Cons List is immutable, it must be initialized with elements
at the time of creation. Another option is to initialize it with elements of
another `Iterable`:

```java
Collection<String> colors = consList(Arrays.asList("red", "black", "magenta"));
```

Create a primitive-based list of `long` values:

```java
LongConsList<Long> longs = longList(1L, 1L, 2L, 3L, 5L, 8L, 13L);
```

Create a list from a `Stream`:

```java
ConsList<String> fruit = Arrays.stream(new String[] {"Apples", "Bananas", "Oranges"})
    .filter(f -> !f.startsWith("O"))
    .collect(toConsCollector());
```

## Performance

Being an immutable collection, `ConsList` lets one save resources on defensive
copying where it would otherwise have been necessary for mutable collections,
such as `ArrayList`.  

See [ConsListBenchmark.java](cons-list-jmh/src/main/java/io/github/nblxa/ConsListBenchmark.java).

Specific problems like flattening a tree-like hierarchical structure can be
solved more performantly with `ConsList`, however the trivial list-growth and
iteration operations are more performant using `java.util.ArrayList`.

Here are the benchmark results on the author's machine:

Benchmark | Collection | Avg time, ms/op
--------- | ---------- | ----:
Flatten a hierarchy | `io.github.nblxa.cons.ConsList` | 29,659
Flatten a hierarchy | `java.util.ArrayList` | 78,018
Flatten a hierarchy | `java.util.LinkedList` | 112,173
Grow a list of integers | `io.github.nblxa.cons.IntConsList` | 6,767 [1]
Grow a list of integers | `io.github.nblxa.cons.ConsList` | 22,137 [1]
Grow a list of integers | `java.util.ArrayList` | 9,507
Grow a list of integers | `java.util.LinkedList` | 14,763
Iterate over a list of integers | `io.github.nblxa.cons.IntConsList` | 3,058
Iterate over a list of integers | `io.github.nblxa.cons.ConsList` | 4,845
Iterate over a list of integers | `java.util.ArrayList` | 1,989
Iterate over a list of integers | `java.util.LinkedList` | 10,451

[1]: The values for `ConsList` and `IntConsList` assume creating the list with
the reversed order of input values compared to those for `ArrayList` and `LinkedList`.
If the reversed order is not possible, a somewhat slower `reverse()` or `intReverse()`
operation is required, adding one single list iteration and re-construction.

The benchmark is written with [JMH](https://openjdk.java.net/projects/code-tools/jmh/).
To test the performance on your machine, build the project and run:
```bash
java -jar cons-list-jmh/target/benchmarks.jar
```
