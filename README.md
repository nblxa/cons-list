[![Build Status](https://travis-ci.com/nblxa/cons-list.svg?branch=master)](https://travis-ci.com/nblxa/cons-list)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=cons-list&metric=alert_status)](https://sonarcloud.io/dashboard?id=cons-list)
[![Coverage Status](https://coveralls.io/repos/github/nblxa/cons-list/badge.svg?branch=master)](https://coveralls.io/github/nblxa/cons-list?branch=master)

# Just the Cons List and nothing else

This repository contains just one Java class of production code:
[ConsList.java](cons-list/src/main/java/io/github/nblxa/ConsList.java).

It is the ultimate thread-safe and immutable Cons List implementation
in Java that implements the `java.util.Collection` interface, giving
the programmers the access to all its methods such as `stream()`,
`toString()` and others.

This implementation of Cons List does not use recursion, so it will
not cause a `StackOverflowError`. A list is allowed to contain more
than `Integer.MAX_VALUE` elements and will produce an overhead of two
object references per element.

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
    <version>1.0.0</version>
</dependency>
```

The library uses [Semantic Versioning](https://semver.org).

## Usage

Static imports for ease of use:

```java
import static io.github.nblxa.ConsList.*;
```

Create an empty Cons list:

```java
Collection<?> empty = nil();
```

Create a list of an arbitrary length:

```java
Collection<String> strings = list("Hello", "functional", "programming", "!");
```

Note that since Cons List is immutable, it must be initialized with elements
at the time of creation. Another option is to initialize it with elements of
another `Iterable`:

```java
Collection<String> colors = consList(Arrays.asList("red", "black", "magenta"));
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

Benchmark | Collection | Avg time, ms
--------- | ---------- | ----:
Flatten a hierarchy | io.github.nblxa.ConsList | 30,090
Flatten a hierarchy | java.util.ArrayList | 74,186
Flatten a hierarchy | java.util.LinkedList | 107,474
Grow a list | io.github.nblxa.ConsList | 39,448
Grow a list | java.util.ArrayList | 9,192
Grow a list | java.util.LinkedList | 14,279
Iterate | io.github.nblxa.ConsList | 4,542
Iterate | java.util.ArrayList | 1,753
Iterate | java.util.LinkedList | 4,213

The benchmark is written with [JMH](https://openjdk.java.net/projects/code-tools/jmh/).
To test the performance on your machine, build the project and run:
```bash
java -jar cons-list-jmh/target/benchmarks.jar
```

