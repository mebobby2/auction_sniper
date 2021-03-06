# auction_sniper

### Setup
* Java 8 with JUnit 4
* Openfire v 4.1.5 (XMPP message broker) is hosted at http://localhost:9090. Login is admin:password
* Ant (brew install ant)

### Testing
ant test.run

### Build app
ant app.compile

## Notes

### The TDD cycle (for a feature)
1. Write a failing acceptance test
2. Write a failing unit test
3. Make the unit test pass
4. Refactor
5. Repeat steps 2 - 4 until acceptance test passes

### Levels of Testing
* Acceptance - Does the whole system work?
* Integration - Does our code work against code we can't change?
* Unit - Do our objects do the right thing, are they convenient to work with?

### What is well designed code?
Classes must have explicit dependencies that can easily be substituted and clear responsibilities that can easily be invoked and verified. In software engineering terms, that means that the code must be loosely coupled and highly cohesive — in other words, well-designed.

Elements are coupled if a change in one forces a change in the other. For example, if two classes inherit from a common parent, then a change in one class might require a change in the other.

An element’s cohesion is a measure of whether its responsibilities form a mean- ingful unit. For example, a class that parses both dates and URLs is not coherent, because they’re unrelated concepts. Think of a machine that washes both clothes and dishes—it’s unlikely to do both well. At the other extreme, a class that parses only the punctuation in a URL is unlikely to be coherent, because it doesn’t represent a whole concept. To get anything done, the programmer will have to find other parsers for protocol, host, resource, and so on.

### Object Oriented Programming
The big idea is 'messaging'. The key in making great and growable systems is much more to design how its modules communicate rather than what their internal properties and behaviors should be.

#### Values and Objects

When designing a system, it’s important to distinguish between values that model unchanging quantities or measurements, and objects that have an identity, might change state over time, and model computational processes. In the object-oriented languages that most of us use, the confusion is that both concepts are implemented by the same language construct: classes.

Values are immutable instances that model fixed quantities. They have no in- dividual identity, so two value instances are effectively the same if they have the same state. This means that it makes no sense to compare the identity of two values; doing so can cause some subtle bugs—think of the different ways of comparing two copies of new Integer(999). That’s why we’re taught to use string1.equals(string2) in Java rather than string1 == string2.

Objects, on the other hand, use mutable state to model their behavior over time. Two objects of the same type have separate identities even if they have ex- actly the same state now, because their states can diverge if they receive different messages in the future.

In practice, this means that we split our system into two “worlds”: values, which are treated functionally, and objects, which implement the stateful behavior of the system.

#### Follow the Messages
We can benefit from this high-level, declarative approach only if our objects are designed to be easily pluggable. In practice, this means that they follow common communication patterns and that the dependencies between them are made ex- plicit. A communication pattern is a set of rules that govern how a group of ob- jects talk to each other: the roles they play, what messages they can send and when, and so on. In languages like Java, we identify object roles with (abstract) interfaces, rather than (concrete) classes—although interfaces don’t define everything we need to say.

In our view, the domain model is in these communication patterns, because they are what gives meaning to the universe of possible relationships between the objects. Thinking of a system in terms of its dynamic, communication structure is a significant mental shift from the static classification that most of us learn when being introduced to objects. The domain model isn’t even obviously visible because the communication patterns are not explicitly represented in the program- ming languages we get to work with. Tests and mock objects help us see the communication between our objects more clearly.

#### Roles, Responsibilities, Collaborators
We try to think about objects in terms of roles, responsibilities, and collaborators. An object is an implementation of one or more roles; a role is a set of related responsibilities; and a responsibility is an obligation to perform a task or know information. A collaboration is an interaction of objects or roles (or both).

#### Tell, Don’t Ask
Calling object should describe what it wants in terms of the role that its neighbor plays, and let the called object decide how to make that happen.

Of course we don’t “tell” everything; we “ask” when getting information from values and collections, or when using a factory to create new objects. Occasionally, we also ask objects about their state when searching or filtering, but we still want to maintain expressiveness and avoid chaining getters methods.

We try to be sparing with queries on objects (as opposed to values) because they can allow information to “leak” out of the object, making the system a little bit more rigid.

### Process of TDD

#### Test a walking skeleton
 A “walking skeleton” is an implementation of the thinnest possible slice of real functionality that we can automatically build, deploy, and test end-to-end

It should include just enough of the automation, the major com- ponents, and communication mechanisms to allow us to start working on the first feature

We keep the skeleton’s application functionality so simple that it’s obvious and uninteresting, leaving us free to concentrate on the infrastructure

A “walking skeleton” will flush out issues early in the project when there’s still time, budget, and goodwill to address them

Feedback is the fundamental tool

#### Maintaining the Test Driven Cycle
Start work on a new feature by writing failing acceptance tests that demonstrate that the system does not yet have the feature we’re about to write and track our progress towards completion of the feature

We prefer to start by testing the simplest success case. Once that’s working, we’ll have a better idea of the real structure of the solution and can prioritize between handling any possible failures we noticed along the way and further success cases. Of course, a feature isn’t complete until it’s robust. This isn’t an excuse not to bother with failure handling—but we can choose when we want to implement first.

We find it useful to keep a notepad or index cards by the keyboard to jot down failure cases, refactorings, and other technical tasks that need to be addressed. This allows us to stay focused on the task at hand without dropping detail. The feature is finished only when we’ve crossed off everything on the list

While writing the test, we ignore the fact that the test won’t run, or even compile, and just concentrate on its text; we act as if the supporting code to let us run the test already exists.
When the test reads well, we then build up the infrastructure to support the test. We know we’ve implemented enough of the supporting code when the test fails in the way we’d expect, with a clear error message describing what needs to be done. Only then do we start writing the code to make the test pass

##### Develop from the Inputs to the Outputs
We start developing a feature by considering the events coming into the system that will trigger the new behavior. The end-to-end tests for the feature will simu- late these events arriving. At the boundaries of our system, we will need to write one or more objects to handle these events. As we do so, we discover that these objects need supporting services from the rest of the system to perform their re- sponsibilities. We write more objects to implement these services, and discover what services these new objects need in turn.
In this way, we work our way through the system: from the objects that receive external events, through the intermediate layers, to the central domain model, and then on to other boundary objects that generate an externally visible response
It’s tempting to start by unit-testing new domain model objects and then trying to hook them into the rest of the application. It seems easier at the start—we feel we’re making rapid progress working on the domain model when we don’t have to make it fit into anything—but we’re more likely to get bitten by integration problems later. We’ll have wasted time building unnecessary or incorrect func- tionality, because we weren’t receiving the right kind of feedback when we were working on it.

##### Unit-Test Behavior, Not Methods
 Many developers who adopt TDD find their early tests hard to understand when they revisit them later, and one common mistake is thinking about testing methods. A test called testBidAccepted() tells us what it does, but not what it’s for.
We do better when we focus on the features that the object under test should provide, each of which may require collaboration with its neighbors and calling more than one of its methods. We need to know how to use the class to achieve a goal, not how to exercise all the paths through its code.

When you write a test for each method of each object, it maybe be hard to understand from those tests how each object was meant to behave—what the responsibilities of the object were and how the different methods of the object worked together.

##### Listen to the Tests
When we find a feature that’s difficult to test, we don’t just ask ourselves how to test it, but also why is it difficult to test. The same structure that makes the code difficult to test now will make it difficult to change in the future.

#### Object-Oriented Style
Separation of concerns and Higher levels of abstraction are two forces will push the structure of an application to be clean.

##### Encapsulation and Information Hiding
We want to be careful with the distinction between “encapsulation” and “information hiding.” The terms are often used interchangeably but actually refer to two separate, and largely orthogonal, qualities:

Encapsulation - Ensures that the behavior of an object can only be affected through its API. It lets us control how much a change to one object will impact other parts of the system by ensuring that there are no unexpected dependencies between unrelated components.

Information hiding - Conceals how an object implements its functionality behind the abstraction of its API. It lets us work with higher abstractions by ignoring lower-level details that are unrelated to the task at hand.

We’re most aware of encapsulation when we haven’t got it. When working with badly encapsulated code, we spend too much time tracing what the potential effects of a change might be, looking at where objects are created, what common data they hold, and where their contents are referenced.

##### Single Responsibility Principle
Every object should have a single, clearly defined responsibility; this is the “single responsibility”. A heuristic is that we should be able to describe what an object does without using any conjunctions (“and,” “or”). If we find ourselves adding clauses to the description, then the object probably should be broken up into collaborating objects, usually one for each clause.

##### Object Peer Stereotypes
We categorize an object’s peers (loosely) into three types of relationship:

Dependencies - Services that the object requires from its peers so it can perform its responsi- bilities. The object cannot function without these services. It should not be possible to create the object without them.

Notifications - Peers that need to be kept up to date with the object’s activity. The object will notify interested peers whenever it changes state or performs a significant action. Notifications are “fire and forget”.

Adjustments - Peers that adjust the object’s behavior to the wider needs of the system. This includes policy objects that make decisions on the object’s behalf.

We try to make sure that we always create a valid object. For dependencies, this means that we pass them in through the constructor. They’re required, so there’s no point in creating an instance of an object until its dependencies are available, and using the constructor enforces this constraint in the object’s definition.
Partially creating an object and then finishing it off by setting properties is brittle because the programmer has to remember to set all the dependencies. When the object changes to add new dependencies, the existing client code will still compile even though it no longer constructs a valid instance. At best this will cause a NullPointerException, at worst it will fail misleadingly.
Notifications and adjustments can be passed to the constructor as a convenience. Alternatively, they can be initialized to safe defaults and overwritten later (note that there is no safe default for a dependency). Adjustments can be initialized to common values, and notifications to a null object [Woolf98] or an empty collection. We then add methods to allow callers to change these default values, and add or remove listeners.

##### Composite Simpler Than the Sum of Its Parts
In software, a user interface component for editing money values might have two subcomponents: one for the amount and one for the currency. For the component to be useful, its API should manage both values together, otherwise the client code could just control it subcomponents directly.

```
moneyEditor.getAmountField().setText(String.valueOf(money.amount()); moneyEditor.getCurrencyField().setText(money.currencyCode());
```

The “Tell, Don’t Ask” convention can start to hide an object’s structure from its clients but is not a strong enough rule by itself. For example, we could replace the getters in the first version with setters:

```
moneyEditor.setAmountField(money.amount());
moneyEditor.setCurrencyField(money.currencyCode());
```

This still exposes the internal structure of the component, which its client still has to manage explicitly. We can make the API much simpler by hiding within the component everything about the way money values are displayed and edited, which in turn simplifies the client code:

```
moneyEditor.setValue(money);
```

As we grow the code, the “composite simpler than the sum of its parts” rule contributes to raising the level of abstraction.

##### Context Independence
While the “composite simpler than the sum of its parts” rule helps us decide whether an object hides enough information, the “context independence” rule helps us decide whether an object hides too much or hides the wrong information.

A system is easier to change if its objects are context-independent; that is, if each object has no built-in knowledge about the system in which it executes. This allows us to take units of behavior (objects) and apply them in new situations. To be context-independent, whatever an object needs to know about the larger environment it’s running in must be passed in. Those relationships might be “permanent” (passed in on construction) or “transient” (passed in to the method that needs them).

In this “paternalistic” approach, each object is told just enough to do its job and wrapped up in an abstraction that matches its vocabulary. Eventually, the chain of objects reaches a process boundary, which is where the system will find external details such as host names, ports, and user interface events.

#### Achieving Object Oriented Design
We describe how we use an incremental, test-driven approach to nudge our code towards the design principles outlined in the Object Oriented Style section.

##### Communication over Classification
We view a running system as a web of communicating objects, so we focus our design effort on how the objects collaborate to deliver the functionality we need. Obviously, we want to achieve a well-designed class structure, but we think the communication patterns between objects are more important.

#### Building on 3rd Party code
With the third-party API pushing back at our design, we must find the best balance between elegance and practical use of someone else’s ideas.

##### Only Mock Types That You Own
We find that tests that mock external libraries often need to be complex to get the code into the right state for the functionality we need to exercise.

##### Write an Adapter Layer
If we don’t want to mock an external API, how can we test the code that drives it? We write a layer of adapter objects that uses the third-party API to implement these interfaces.

### General workflow
1. Write an acceptance test
2. To make this one acceptance test pass, you may need to touch multiple classes 
3. Write/amend the unit tests first before touching the classes
4. If code is smelly, use OO design techniques to refactor it (Change tests before refactoring)
5. Continue until the acceptance test passes

So, even though we allow the tests to drive the architecture, you need to use code smells to write clean code so the architecture can emerge. Same thing in agile software development when designing high level architecture. You need to use smells (decoupling, encapsulation) to drive the high level architecture. 

### 20/20 Hindsight
Surely, if we’d spent more time on the design, we wouldn’t have to change it now? Sometimes that’s true. Our experience, however, is that nothing shakes out a design like trying to implement it, and between us we know just a handful of people who are smart enough to get their designs always right. Our coping mechanism is to get into the critical areas of the code early and to allow ourselves to change our collective mind when we could do better. We rely on our skills, on taking small steps, and on the tests to protect us when we make changes.

### Defect
In most systems we build, we end up writing a runtime exception called something like Defect (or perhaps StupidProgrammerMistakeException). We throw this when the code reaches a condition that could only be caused by a programming error, rather than a failure in the runtime environment.

### Ports and Adapter
As the code scales up, the only way we can continue to understand and maintain it is by structuring the functionality into objects, objects into packages, packages into programs, and programs into systems. We use two principal heuristics to guide this structuring: Separation of concerns and Higher levels of abstraction. Applied consistently, these two forces will push the structure of an appli- cation towards something like Cockburn’s “ports and adapters” architecture [Cockburn08], in which the code for the business domain is isolated from its dependencies on technical infrastructure, such as databases and user interfaces. We don’t want technical concepts to leak into the application model, so we write interfaces to describe its relationships with the outside world in its terminology (Cockburn’s ports). Then we write bridges between the application core and each technical domain (Cockburn’s adapters). This is related to what Eric Evans calls an “anticorruption layer”.

### “Inverse Salami” Development
We hope that by now you’re getting a sense of the rhythm of incrementally growing software, adding functionality in thin but coherent slices. For each new feature, write some tests that show what it should do, work through each of those tests changing just enough code to make it pass, restructure the code as needed either to open up space for new functionality or to reveal new concepts—then ship it. We discuss how this fits into the larger development picture in Chapter 5. In static languages, such as Java and C#, we can often use the compiler to help us navigate the chain of implementation dependencies: change the code to accept the new triggering event, see what breaks, fix that breakage, see what that change breaks in turn, and repeat the process until the functionality works.
The skill is in learning how to divide requirements up into incremental slices, always having something working, always adding just one more feature. The process should feel relentless—it just keeps moving. To make this work, we have to understand how to change the code incrementally and, critically, keep the code well structured so that we can take it wherever we need to go (and we don’t know where that is yet). This is why the refactoring part of a test-driven development cycle is so critical—we always get into trouble when we don’t keep up that side of the bargain.

### Listening to Tests
* Our intention in test-driven development is to use mock objects to bring out relation- ships between objects
* Do not override a class’ internal features—this just locks down your test to the quirks of the current implementation. Only override visible methods. This rule also prohibits exposing internal methods just to override them in a test. If you can’t get to the structure you need, then the tests are telling you that it’s time to break up the class into smaller, composable features.
* When extracting implicit components, we start by looking for two conditions: arguments that are always used together in the class, and those that have the same lifetime. Once we’ve found a coincidence, we have the harder task of finding a good name that explains the concept
* When a constructor is too large, and we don’t believe there’s an implicit new type amongst the arguments, we can use more default values and only overwrite them for particular test cases

#### What the Tests Will Tell Us
* Some of the test smells we’ve identified, such as needing “magic” to create mocks, are to do with knowledge leaking between components. If we can keep knowledge local to an object (either internal or passed in), then its im- plementation is independent of its context; we can safely move it wherever we like. Do this consistently and your application, built out of pluggable components, will be easy to change.
* One reason why we don’t like mocking concrete classes is that we like to have names for the relationships between objects as well the objects them- selves. As the legends say, if we have something’s true name, we can control it. If we can see it, we have a better chance of finding its other uses and so reducing duplication.
* We find that when we emphasize how objects communicate, rather than what they are, we end up with types and roles defined more in terms of the domain than of the implementation. This might be because we have a greater number of smaller abstractions, which gets us further away from the under- lying language. Somehow we seem to get more domain vocabulary into the code.
* We find that by applying “Tell, Don’t Ask” consistently, we end up with a coding style where we tend to pass behavior (in the form of callbacks) into the system instead of pulling values up through the stack. For example, in Chapter 17, we introduced a SniperCollector that responds when told about a new Sniper. Passing this listener into the Sniper creation code gives us better information hiding than if we’d exposed a collection to be added to. More precise interfaces give us better information-hiding and clearer abstractions.

### How Many Assertions in a Test Method?
Some TDD practitioners suggest that each test should only contain one expectation or assertion. This is useful as a training rule when learning TDD, to avoid asserting everything the developer can think of, but we don’t find it practical. A better rule is to think of one coherent feature per test, which might be represented by up to a handful of assertions. If a single test seems to be making assertions about different features of a target object, it might be worth splitting up. Once again, expressiveness is the key: as a reader of this test, can I figure out what’s significant?

### Diagnostics Are a First-Class Feature
Like everyone else, we find it easy to get carried away with the simple three-step TDD cycle: fail, pass, refactor. We’re making good progress and we know what the failures mean because we’ve just written the test. But nowadays, we try to follow the four-step TDD cycle (fail, report, pass, refactor). 
1. Write a failing test
2. Make the diagnostics clear (make sure the test says clearly what has fired so u don't need to hunt for it in the code)
3. Make the test pass
4. Refactor

### Test Flexibility
Common causes of test brittleness include:
1. The tests are too tightly coupled to unrelated parts of the system or unrelated behavior of the object(s) they’re testing;
2. The tests overspecify the expected behavior of the target code, constraining it more than necessary; and,
3. There is duplication when multiple tests exercise the same production code behavior.

#### Test for Information, Not Representation
Tests should be written in terms of the information passed between objects,
not of how that information is represented. Doing so will both make the tests more self-explanatory and shield them from changes in implementation controlled elsewhere in the system. Significant values, like NO_CUSTOMER_FOUND, should be defined in one place as a constant.

### Levels of Testing
Let us organize our tests into a chain of phases: unit tests that run very quickly in memory; slower integration tests that reach outside the process, usually through third-party APIs, and that depend on the configuration of external services such as databases and messaging brokers; and, finally, end-to-end tests that run against a system packaged and deployed into a production-like environment. This gives us rapid feedback if we break the application’s core logic, and incre- mental feedback about integration at increasingly coarse levels of granularity.

### Concurrency
Concurrency is a system-wide concern that should be controlled outside the objects that need to run concurrent tasks. By passing an appropriate Executor to the constructor, we’re following the “context independence” design principle. 


