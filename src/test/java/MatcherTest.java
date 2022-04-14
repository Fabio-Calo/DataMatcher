import matcher.Matcher;
import matcher.Normalizer;
import matcher.exception.FieldNotAnnotatedException;
import objects.AdminObject;
import objects.UserObject;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


class MatcherTest {

  Matcher matcher = new Matcher();

  @Test
  void shouldMatchStringWithAValueOfAnListOfStrings(){


    var listOfPossibleSolution = List.of("first","second","three","four");
    var myString = "tree";
    //Act
    var matchedString= matcher.matchStringWithList(myString,listOfPossibleSolution);

    //Assert
    assertThat(matchedString).isEqualTo(listOfPossibleSolution.get(2));
    assertThat((int)matcher.getMatchPercentage()).isEqualTo(83);
  }

  @Test
  void  shouldMatchStringWithAListOfObject() throws IllegalAccessException, FieldNotAnnotatedException {

    var listOfPossibleSolution =
        List.of(
            new UserObject(1,"Mark","mark@mail.com"),
            new UserObject(2,"Clark","klark@gmail.com"),
            new UserObject(4,"Mars","mars@man.com"),
            new UserObject(3,"Ben","ben@dover.com"));

    var myName = "Mars";
    var matchedUser =  matcher.matchStringWithObjectList(myName,listOfPossibleSolution);

    assertThat(matchedUser).isEqualTo((listOfPossibleSolution.get(2)));
    assertThat((int)matcher.getMatchPercentage()).isEqualTo(100);
  }

  @Test
  void  shouldMatchObjectWithAListOfObject() throws FieldNotAnnotatedException, NoSuchFieldException, IllegalAccessException {

    var listOfPossibleSolution =
        List.of(
            new UserObject(1,"Mark","mark@mail.com"),
            new UserObject(2,"Clark","clark@gmail.com"),
            new UserObject(4,"Mars","mars@man.com"),
            new UserObject(3,"Ben","ben@dover.com"));

    var myAdmin = new AdminObject(1,"Clark","clark@sov.com","clark@gmail.com");

    var matchedUser = matcher.matchObjectWithObjectList(myAdmin,listOfPossibleSolution);

    assertThat(matchedUser).isEqualTo((listOfPossibleSolution.get(1)));
    assertThat((int)matcher.getMatchPercentage()).isEqualTo(100);

  }

  @Test
  void  shouldNormalizeTheMatching(){

    var listOfPossibleSolution = List.of("noreply@no-service.com","second@email.com","meetme@meet.net","mymail@yourmail.com");
    var myString = "meetme@gmail.com";

    var matchedStringNoRegex= matcher.matchStringWithList(myString,listOfPossibleSolution);
    var noRegexPercentage = matcher.getMatchPercentage();
    //with normalizer also with regex
    var normalizer = new Normalizer(Map.of("@+\\X*",""));
    matcher = new Matcher(normalizer);
    //Act
    var matchedString= matcher.matchStringWithList(myString,listOfPossibleSolution);

    //Assert
    //normalisation improves matching!
    assertThat(matchedStringNoRegex).isNotEqualTo(matchedString);
    assertThat(noRegexPercentage).isLessThan(matcher.getMatchPercentage());
    assertThat(matchedString).isEqualTo(listOfPossibleSolution.get(2));
    assertThat((int)matcher.getMatchPercentage()).isEqualTo(100);
  }
}
