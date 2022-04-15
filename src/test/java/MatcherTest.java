import matcher.Matcher;
import matcher.Normalizer;
import matcher.exception.MatchingException;
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
  void  shouldMatchStringWithAListOfObject() throws IllegalAccessException, MatchingException {

    var listOfPossibleSolution =
        List.of(
            new UserObject("Mark","mark@mail.com"),
            new UserObject("Clark","clark@gmail.com"),
            new UserObject("Mars","mars@man.com"),
            new UserObject("Ben","ben@dover.com"));

    var myName = "Mars";
    var matchedUser =  matcher.matchStringWithObjectList(myName,listOfPossibleSolution);

    assertThat(matchedUser).isEqualTo((listOfPossibleSolution.get(2)));
    assertThat((int)matcher.getMatchPercentage()).isEqualTo(100);
  }

  @Test
  void  shouldMatchStringWithAListOfObjectNoPrimary() throws IllegalAccessException, MatchingException {

    var listOfPossibleSolution =
        List.of(
            new AdminObject("Mark","mark@mail.com","Mo@me.com"),
            new AdminObject("Clark","clark@gmail.com","clk@amg.com"),
            new AdminObject("Mars","mars@man.com","mars@solar.net"),
            new AdminObject("Ben","ben@dover.com","ben@d.it"));

    var myName = "Mars";
    var matchedUser =  matcher.matchStringWithObjectList(myName,listOfPossibleSolution);

    assertThat(matchedUser).isEqualTo((listOfPossibleSolution.get(2)));
    assertThat((int)matcher.getMatchPercentage()).isEqualTo(100);
  }

  @Test
  void  shouldMatchObjectWithAListOfObject() throws MatchingException, NoSuchFieldException, IllegalAccessException {

    var listOfPossibleSolution =
        List.of(
            new UserObject("Mark","mark@mail.com"),
            new UserObject("Clark","clark@gmail.com"),
            new UserObject("Mars","mars@man.com"),
            new UserObject("Ben","ben@dover.com"));

    var myAdmin = new AdminObject("Clark","clark@sov.com","clark@gmail.com");

    var matchedUser = matcher.matchObjectWithObjectList(myAdmin,listOfPossibleSolution);

    assertThat(matchedUser).isEqualTo((listOfPossibleSolution.get(1)));
    assertThat((int)matcher.getMatchPercentage()).isEqualTo(100);

  }

  @Test
  void  shouldMatchObjectWithAListOfTheSameObject() throws MatchingException, NoSuchFieldException, IllegalAccessException {

    var listOfPossibleSolution =
        List.of(
            new UserObject("Mark","mark@mail.com"),
            new UserObject("Clark","clark@gmail.com"),
            new UserObject("Mars","mars@man.com"),
            new UserObject("Ben","ben@dover.com"));

    var myUser = new UserObject("Clark","clark@gmail.com");

    var matchedUser = matcher.matchObjectWithObjectList(myUser,listOfPossibleSolution);

    assertThat(matchedUser).isEqualTo((listOfPossibleSolution.get(1)));
    assertThat((int)matcher.getMatchPercentage()).isEqualTo(100);

  }

  @Test
  void shouldMatchObjectWithObject() throws MatchingException {
    var myUser = new UserObject("Kevin","kwin@gmail.com");
    var myAdmin = new AdminObject("Kevin","kevin@company.com","kwin@gmail.com");

    double matchedUser = matcher.matchObjectWithObject(myAdmin,myUser);

    assertThat((int)matchedUser).isEqualTo(100);
  }
  @Test
  void shouldMatchObjectWithTheSameObject() throws MatchingException {
    var myUser = new UserObject("Kevin","kwin@gmail.com");
    var myOtherUser = new UserObject("Kevin","kwin@gmail.com");

    double matchedUser = matcher.matchObjectWithObject(myOtherUser,myUser);

    assertThat((int)matchedUser).isEqualTo(100);
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
