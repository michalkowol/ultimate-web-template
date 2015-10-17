# Play and React template

## Build

```
sbt assembly
```

## Run

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