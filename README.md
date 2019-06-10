[![Build Status](https://travis-ci.com/nblxa/just-the-cons-list.svg?branch=master)](https://travis-ci.com/nblxa/just-the-cons-list)
[![Coverage Status](https://coveralls.io/repos/github/nblxa/just-the-cons-list/badge.svg?branch=master)](https://coveralls.io/github/nblxa/just-the-cons-list?branch=master)

# Just the Cons List and nothing else

This repository contains just one Java class of production code:
[ConsList.java](src/main/java/just/the/ConsList.java).

It is the ultimate thread-safe and immutable Cons List implementation
in Java that satisfies the `java.util.Collection` interface, giving
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
* `spliterator()` has the right characteristics for the cons list:
  `ORDERED` and `IMMUTABLE`.

Methods `equals(Object o)` and `hashCode()` are also implemented
without recursion.

Cons List, due to its simplicity and immutability, is an ideal data
structure for multi-threaded processing of ordered collections of data.
Direct implementations however, suffer from heavy recsive calls
and may cause high stack memory consumption, if not more severe issues.
This implementation fuses the power of the immutable cons list
with the wide range of functionality offered by the Java Collections
and Streams API.

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
