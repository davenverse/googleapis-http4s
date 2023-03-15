# googleapis-http4s

Call Google APIs on JVM, Node.js, and Scala Native via [http4s-grpc] and [Ember].

@:callout(warning)

These modules are published for convenience, with versioning following the upstream Google artifacts. However, they do not follow Scala semantic versioning with respect to binary-compatibility, and indeed offer **no guarantee of binary-compatibility at all**.

@:@

## Quick start

```scala
libraryDependencies ++= Seq(
@MODULES@
)
```

[http4s-grpc]: https://github.com/davenverse/http4s-grpc
[Ember]: https://http4s.org/v0.23/docs/integrations.html#ember
