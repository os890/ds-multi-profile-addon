# DeltaSpike Multi-Profile Add-on

A CDI portable extension that supports multiple named profiles for bean activation,
built on top of Apache DeltaSpike configuration.

## Overview

This add-on extends DeltaSpike's project-stage concept by allowing beans to be
associated with one or more named profiles. At container startup the extension reads
the `active-profiles` configuration property and vetoes any bean whose declared
profiles do not intersect with the active set.

## Usage

### String-based profiles

Annotate a bean with `@Profile` and one or more profile names:

```java
@Profile("staging")
@ApplicationScoped
public class StagingService { }
```

### Type-safe profiles (stereotypes)

Define a custom stereotype that carries `@Profile`:

```java
@Target(TYPE)
@Retention(RUNTIME)
@Stereotype
@Profile("staging")
public @interface Staging { }
```

Then use it on your beans:

```java
@Staging
@ApplicationScoped
public class StagingService { }
```

### Configuration

Set the active profiles in `META-INF/apache-deltaspike.properties`:

```properties
active-profiles=production,staging
```

## Requirements

- **Java 25+**
- **Maven 3.6.3+**
- Apache DeltaSpike 2.0.x on the classpath

## Build

```bash
mvn clean verify
```

## Testing

Tests use the [Dynamic CDI Test Bean Addon](https://github.com/os890/dynamic-cdi-test-bean-addon)
with OpenWebBeans SE to boot a CDI container without an application server.

## Quality Plugins

| Plugin      | Purpose                                      |
|-------------|----------------------------------------------|
| Compiler    | `-Xlint:all`, fail on warnings               |
| Enforcer    | Java 25+, Maven 3.6.3+, dependency convergence, banned javax.* |
| Checkstyle  | Code style validation (no star imports, braces, whitespace) |
| RAT         | Apache 2.0 license header verification       |
| Surefire    | Test execution                               |
| JaCoCo      | Code coverage reporting                      |
| Javadoc     | API documentation generation                 |

## License

This project is licensed under the [Apache License, Version 2.0](LICENSE).
