# OH3RuleEngine for java

OH3RuleEngine is an  pure JVM alternative to Openhab rules-dsl.


##  Why OH3RuleEngine rather than rule-dsl provided by openhab?

For most of the users rule-dsl would be enough, for more advanced users rule-dsl has some limits which does not exists when using OH3RuleEngine

Those limitations are:
* Pure IDE support for hand writing *.rules files
* No support for classes objects
* Limited code reuse

## Requirements
* Java 11

## Installation

TBD

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

## How to write rules

OH3RuleEngine provides you two ways of creating rules
### Classic Java object
```java
static class TestRule extends Rule {

    @Override
    public String name(){
        return "Test";
    }
    @Override
    public Condition when() {
        return onStartup();
    }

    @Override
    public void run() {
        System.out.println("Foobar");
    }
}
```

### Builder
```java
rule("Test")
.when(onStartup())
.then(() -> {
    System.out.println("Foobar");
})
```
*This approach is not recommended as it can lead to mess in the code.*


## Rule Examples

### Terratium temperature controller

```java
public class TerrariumTemperatureRule extends Rule {

    private final String currentTempItem;

    private final String targetTempItem;

    private final String heatingItem;

    public TerrariumTemperatureRule(String terrariumId) {
        this.currentTempItem = terrariumId+"_Actual_Temperature";
        this.targetTempItem = terrariumId+"_Target_Temperature";
        this.heatingItem = terrariumId+"_Heating";
    }

    @Override
    public Condition when() {
        return or(
                itemChanged(currentTempItem),
                itemChanged(targetTempItem),
                onStartup()
        );
    }

    @Override
    public void run() {
        if(getNumber(currentTempItem) > getNumber(targetTempItem)){
            setState(heatingItem, OFF);
        } else {
            setState(heatingItem, ON);
        }
    }
}

```

## Condition Examples

### Item changed 
```java
itemChanged("FOO");
```
### Item changed to value
```java
itemChangedTo("FOO", "12");
        
itemChanged("FOO").to("12");
```
### Item changed to value of other item
```java
itemChangedTo("FOO", item("BAR"));
        
itemChanged("FOO").to(item("BAR"));
```
### Or/anyOf
```java
anyOf(itemChanged("FOO"), itemChanged("BAR"));

or(itemChanged("FOO"), itemChanged("BAR"));

itemChanged("FOO").or(itemChanged("BAR"));
```

### And/allOf
```java
allOf(itemChanged("FOO"), itemChanged("BAR"));

and(itemChanged("FOO"), itemChanged("BAR"));

itemChanged("FOO").and(itemChanged("BAR"));
```

### Cron
```java
cron("* * * * * *"); // execute every second
cron("0 * * * * *"); // execute every minute
```

## Roadmap

* Define items via code
* Define things via code
* Code to sitemap compilator
