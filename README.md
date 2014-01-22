### First Responder


#### Description

The First Responder library is a small and simple library, written in pure Java without any external dependencies. It
tries to solve one very specific requirement, the ability to execute multiple tasks in parallel, and respond only to
the first response, discarding the rest.


#### Usage

Using the library is very straight forward. You instantiate an instance of the `FirstResponderManager` class. This class
allows you to submit a collection of tasks that implement the `Callable` interface. This collection will then be
executed in parallel, and the first non-null response will be considered valid, the remaining threads stopped (through
interruption), and the result returned.

In the case that all of the tasks return `null`, `null` will be returned.

The `FirstResponderManager` can be re-used in the case where a result has been returned (either a result or `null`). In
other words, as long as the previous tasks that were submitted have either finished or been interrupted.
