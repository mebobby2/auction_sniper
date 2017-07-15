# auction_sniper

## The TDD cycle (for a feature)
1. Write a failing acceptance test
2. Write a failing unit test
3. Make the unit test pass
4. Refactor
5. Repeat steps 2 - 4 until acceptance test passes

## Levels of Testing
* Acceptance - Does the whole system work?
* Integration - Does our code work against code we can't change?
* Unit - Do our objects do the right thing, are they convenient to work with?

## What is well designed code?
Classes must have explicit dependencies that can easily be substituted and clear responsibilities that can easily be invoked and verified. In software engineering terms, that means that the code must be loosely coupled and highly cohesive — in other words, well-designed.

Elements are coupled if a change in one forces a change in the other. For example, if two classes inherit from a common parent, then a change in one class might require a change in the other.

An element’s cohesion is a measure of whether its responsibilities form a mean- ingful unit. For example, a class that parses both dates and URLs is not coherent, because they’re unrelated concepts. Think of a machine that washes both clothes and dishes—it’s unlikely to do both well. At the other extreme, a class that parses only the punctuation in a URL is unlikely to be coherent, because it doesn’t represent a whole concept. To get anything done, the programmer will have to find other parsers for protocol, host, resource, and so on.

## Object Oriented Programming
The big idea is 'messaging'. The key in making great and growable systems is much more to design how its modules communicate rather than what their internal properties and behaviors should be.

### Values and Objects

When designing a system, it’s important to distinguish between values that model unchanging quantities or measurements, and objects that have an identity, might change state over time, and model computational processes. In the object-oriented languages that most of us use, the confusion is that both concepts are implemented by the same language construct: classes.

Values are immutable instances that model fixed quantities. They have no in- dividual identity, so two value instances are effectively the same if they have the same state. This means that it makes no sense to compare the identity of two values; doing so can cause some subtle bugs—think of the different ways of comparing two copies of new Integer(999). That’s why we’re taught to use string1.equals(string2) in Java rather than string1 == string2.

Objects, on the other hand, use mutable state to model their behavior over time. Two objects of the same type have separate identities even if they have ex- actly the same state now, because their states can diverge if they receive different messages in the future.

In practice, this means that we split our system into two “worlds”: values, which are treated functionally, and objects, which implement the stateful behavior of the system.

### Follow the Messages
We can benefit from this high-level, declarative approach only if our objects are designed to be easily pluggable. In practice, this means that they follow common communication patterns and that the dependencies between them are made ex- plicit. A communication pattern is a set of rules that govern how a group of ob- jects talk to each other: the roles they play, what messages they can send and when, and so on. In languages like Java, we identify object roles with (abstract) interfaces, rather than (concrete) classes—although interfaces don’t define everything we need to say.

In our view, the domain model is in these communication patterns, because they are what gives meaning to the universe of possible relationships between the objects. Thinking of a system in terms of its dynamic, communication structure is a significant mental shift from the static classification that most of us learn when being introduced to objects. The domain model isn’t even obviously visible because the communication patterns are not explicitly represented in the program- ming languages we get to work with. Tests and mock objects help us see the communication between our objects more clearly.

### Roles, Responsibilities, Collaborators
We try to think about objects in terms of roles, responsibilities, and collaborators. An object is an implementation of one or more roles; a role is a set of related responsibilities; and a responsibility is an obligation to perform a task or know information. A collaboration is an interaction of objects or roles (or both).

### Tell, Don’t Ask
Calling object should describe what it wants in terms of the role that its neighbor plays, and let the called object decide how to make that happen.

Of course we don’t “tell” everything; we “ask” when getting information from values and collections, or when using a factory to create new objects. Occasionally, we also ask objects about their state when searching or filtering, but we still want to maintain expressiveness and avoid chaining getters methods.

We try to be sparing with queries on objects (as opposed to values) because they can allow information to “leak” out of the object, making the system a little bit more rigid.

## Upto
Page 54

Part II