# DataMatcher
#### Matching of diffrent objects using levenshtein distance
#### Java library
This library allows you to match

- `String` to `List<String>`
- `String` to `List<Object>`
- `Object` to `List<Object>`

 returns the matched Object
 and creates a Score %

## Setup

to match the object you provide in a list you have to annotate the fields of the object with the @MatchWith annotation.
If you have more than one annotation the `String` to `List<Object>` won't work unless you provide one field is annotated as primary 


If you match `Object` to `List<Object>` you may want to match more than one field you have to provide the wanted field of the Object you want to match to.

#### example
```java 
public class ObjectInList {

    int id;
    @MatchWith(primary = true, field = "specialName")
    String name;
    @MatchWith(field = "privateEMail")
    String eMail;

}


public class ObjectMatchTo {

  int id;
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
For more examples look in the Tests (src/test/java/MatcherTest.java)

### Normalizer
For better resuls you can normalize your data. to do so you create the Matcher like this:
```java
    //with normalizer also with regex
    Normalizer normalizer = new Normalizer(Map.of("@+\\X*",""));
    matcher = new Matcher(normalizer);
```
the normalizer uses a Map to replaces the Key with the value. Note that you can use Regex too. 

# More Information:
+ feel free to leave some feedback and critics
+ this code uses the library: 
org.apache.airavata for the levenshtein distance
   
