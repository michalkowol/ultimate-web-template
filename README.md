# [Play 2.4](https://www.playframework.com/), [React 0.14](https://facebook.github.io/react/) and [PostgreSQL 9.5](http://www.postgresql.org/) template.

## Run

```
sbt run
```

## Debug

```
sbt -jvm-debug 5005 run
```

## Assembly

```
sbt assembly
```

## Run dist

### Local

```
java -jar app.jar
```

### Dev

```
java -Dconfig.resource=dev.conf -jar app.jar
```

### QA

```
java -Dconfig.resource=qa.conf -jar app.jar
```

## Production

```
java -Dconfig.resource=prod.conf -jar app.jar
```