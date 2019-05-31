[![Build Status](https://travis-ci.com/nblxa/just-the-cons-list.svg?branch=master)](https://travis-ci.com/nblxa/just-the-cons-list)

# Just the Cons List and nothing more

The ultimate thread-safe and immutable
[Cons List](https://en.wikipedia.org/wiki/Cons) implementation in Java
that satisfies the `java.util.Collection` interface, giving the access
to all its methods such as `stream()` that further provides monadic
operations on the list.

This implementation of Cons List does not use recursion, so it will not
abort with a `StackOverflowError`.

Cons List, due to its immutability, is an ideal data structure
for multi-threaded processing of ordered collections of data.

## Building

Linux/MacOS:
```bash
./mvnw clean install
```

Windows:
```cmd
.\mvnw.cmd clean install
```

## Usage

Static imports for easy use:

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

