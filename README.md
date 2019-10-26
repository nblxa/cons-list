[![Build Status](https://travis-ci.com/nblxa/just-the-cons-list.svg?branch=master)](https://travis-ci.com/nblxa/just-the-cons-list)
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
* `size()` iterates through the list and correctly handles lists with
  more than `Integer.MAX_VALUE` elements.
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

No releases exists at the moment, and the project is not published to Maven
Central. If you still want to use the current `SNAPSHOT` version as a dependency,
you can use [JitPack](https://jitpack.io):

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

```xml
<dependency>
    <groupId>com.github.nblxa</groupId>
    <artifactId>cons-list</artifactId>
    <version>master-SNAPSHOT</version>
</dependency>
```

## Usage

Static imports for ease of use:

```java
import static just.the.ConsList.*;
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

To run it on your machine, build the project and run:
```bash
java -jar cons-list-jmh/target/benchmarks.jar
```
