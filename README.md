# Openhab3 Ruleengine for java


This library allows you to write your home automation rules in java


## Installation


## Getting started

### Connecting to Openhab

Connecting to your openhab is simple as it:

```java
var client = new OpenhabClientImpl("http://192.168.0.132:8080", "login", "password");

new OpenhabRuleEngine(
client,
List.of(
    new TestRule()
)
).start().join();
```

## Examples


### Cron based conditions

Set `Bedroom_Light` state to `ON` at 8 AM
```java
class TestRule implements ModyfingRule {

    @Setter
    private OpenhabClient openhabClient;
    
    @Override
    public Condition when() {
        return cron("0 0 8 * * *");
    }

    @Override
    public void run() {
        openhabClient.item().setState("Bedroom_Light", "ON");
    }

}
```

### Event based conditions

Print log when light in office is turned off
```java
@Slf4j
class TestRule implements Rule {

    @Override
    public Condition when() {
        return itemChangedTo("Office_Light", "OFF");
    }

    @SneakyThrows
    @Override
    public void run() {
        log.warn("Office light has ben turned off");
    }
}
```


### Or/And condions

```java
@Slf4j
class TestRule implements Rule {

    @Override
    public Condition when() {
        return itemChangedTo("Office_Light", "OFF")
                .or(itemChangedTo("Bedroom_Light", "OFF"))
    }

    @SneakyThrows
    @Override
    public void run() {
        log.warn("Office light has ben turned off");
    }
}
```

##Supported conditions

* Or
* And
* ItemStateIs
* ItemStateChanged
* ItemStateChangedTo
* Cron
