# ObjectMatcher-Library
 Matching of diffrent of same objects using levenshtein distance
#### Java library
This library allows you to match:

- `String` to `List<String>`
- `String` to `List<Object>`
- `Object` to `List<Object>`
   + returns the matched Object
   and creates a Score %
- `Object` to `Object`
  + returns a Score %

## Setup

to match two different objects:
you have to annotate, the object you provide in a list or in the 2nd parameter, the fields of the object with the @MatchWith annotation.
If you have more than one annotation the `String` to `List<Object>` won't work unless one field is annotated as primary.

When matching two objects of the same class there is no annotation needed.

If you match `Object` to `List<Object>` you may want to match more than one field you have to provide the wanted field of the Object you want to match to.

#### example
```java 
public class ObjectInList {

    @MatchWith(primary = true, field = "specialName")
    String name;
    @MatchWith(field = "privateEMail")
    String eMail;

}

public class ObjectToMatch {

  String specialName;
  String companyEMail;
  String privateEMail;
 
}
```

after the setup you use the matcher as follows:

```java
//create a matcher
Matcher matcher = new Matcher();

//call matchStringWithList for matching `String` to `List<String>`
String matchedString= matcher.matchStringWithList(myString,listOfPossibleStrings);
//score of current match in %
double score = matcher.getMatchPercentage()

//call matchStringWithList for matching `String` to `List<Object>`
Object matchedObject = matcher.matchObjectWithObjectList(myString,listOfPossibleObjects);
//score of current match in %
score = matcher.getMatchPercentage()

//call matchStringWithList for matching `Object` to `List<Object>`
Object matchedObject = matcher.matchObjectWithObjectList(myObject,listOfPossibleObjects);
//score of current match in % average off all fields
score = matcher.getMatchPercentage()
```
#### More examples
For more examples look in the Tests 
+ src/test/java/MatcherTest.java
+ https://github.com/Fabio-Calo/DataMatcher/blob/main/src/test/java/MatcherTest.java

### Normalizer
For better resuls you can normalize your data. to do so you create the Matcher like this:
```java
    //with normalizer also with regex
    Normalizer normalizer = new Normalizer(Map.of("@+\\X*",""));
    matcher = new Matcher(normalizer);
```
The normalizer uses a Map to replaces the key with the value. Note that you can use Regex too. 

# More Information:
+ feel free to leave some feedback and critics
+ this code uses the library: 
org.apache.airavata for the levenshtein distance
+ this library uses reflection. I will create an alternative without using reflection. It will not be a library.
   
