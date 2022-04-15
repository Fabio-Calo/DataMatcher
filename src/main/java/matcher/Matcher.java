package matcher;

import matcher.exception.MatchingException;
import org.apache.airavata.samples.LevenshteinDistanceService;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Matcher {


    private Normalizer normalizer;
    private double matchPercentage;


    public Matcher() {
        this.normalizer = new Normalizer(Map.of(" ", ""));
    }

    /***
     * provide a Normalizer to normalize your data how you want.
     * If you don't provide any there will be no normalisation.
     * The return value will not be normalized. normalisation happens only for the matching algorithm
     * The normalisation gets performed on all fields that get matched
     * @param normalizer
     */
    public Matcher(Normalizer normalizer) {
        this.normalizer = normalizer;
    }

    /***
     * Returns the matched string
     * @param stringToMatch
     * @param listOfPossibleSolutions
     * @return
     */
    public String matchStringWithList(String stringToMatch, List<String> listOfPossibleSolutions) {
        matchPercentage = 0;
        String solution = "";
        for (var possibleSolution : listOfPossibleSolutions) {
            var currentPercentage = matchInPercentage(stringToMatch, possibleSolution);
            if (currentPercentage > matchPercentage) {
                matchPercentage = currentPercentage;
                solution = possibleSolution;
            }
        }
        return solution;
    }

    /***
     * Returns the matched object.
     * You have to annotate the object in the listOfPossibleSolutions with @MatchWith. If there are multiple @MatchedWith you need to specify one as @MatchedWith(primary = true)
     * @param stringToMatch
     * @param listOfPossibleSolutions
     * @return
     * @throws IllegalAccessException
     * @throws MatchingException
     */
    public Object matchStringWithObjectList(String stringToMatch, List listOfPossibleSolutions) throws IllegalAccessException, MatchingException {
        matchPercentage = 0;
        Object solution = null;

        if (!listOfPossibleSolutions.isEmpty()) {

            //List for saving all annotated fields
            List<Field> tmpFields = new ArrayList<>();
            //save primary fields
            Field primary = null;
            //getting all fields
            var fields = listOfPossibleSolutions.get(0).getClass().getDeclaredFields();

            //check if Field is annotated
            for (var field : fields) {
                Annotation a = field.getAnnotation(MatchWith.class);
                if (a instanceof MatchWith mw) {
                    tmpFields.add(field);
                    //check if primary
                    if (mw.primary()) {
                        if (primary != null) {
                            throw new MatchingException("There can only be one primary");
                        }
                        primary = field;
                    }
                }
            }
            //if there is only one annotated field proceed as if primary
            if (tmpFields.size() == 1) {
                primary = tmpFields.get(0);
            }
            //if there is a primary field
            if (primary != null) {
                //loop each possible solution
                for (var solutions : listOfPossibleSolutions) {
                    //get String value of field
                    primary.setAccessible(true);
                    var possibleSolution = primary.get(solutions).toString();
                    var currentPercentage = matchInPercentage(stringToMatch, possibleSolution);
                    if (currentPercentage > matchPercentage) {
                        matchPercentage = currentPercentage;
                        solution = solutions;
                    }
                }
            }
            return solution;
        }
        throw new MatchingException("No field annotated. missing proper annotation use @MatchWith or set primary to true. \nNote that only one field should be primary!");
    }

    /***
     * Returns the matched object.
     * You have to annotate the object in the listOfPossibleSolutions with @MatchWith(field="{the name of the field of your object}")
     * @param object
     * @param listOfPossibleSolutions
     * @return
     * @throws MatchingException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    public Object matchObjectWithObjectList(Object object, List listOfPossibleSolutions) throws MatchingException, IllegalAccessException, NoSuchFieldException {
        matchPercentage = 0.;
        Object solution = null;

        if (!listOfPossibleSolutions.isEmpty()) {

            //List for saving all annotated fields
            List<List<Object>> fieldsToMatchToObject = new ArrayList<>();

            //getting all fields
            var fieldsSolution = listOfPossibleSolutions.get(0).getClass().getDeclaredFields();

            //if objects are not the same
            if (!object.getClass().equals(listOfPossibleSolutions.get(0).getClass())) {
                //check if Fields are annotated for solutions
                for (var field : fieldsSolution) {
                    Annotation a = field.getAnnotation(MatchWith.class);
                    if (a instanceof MatchWith mw) {

                        var fieldToMatch = (mw.field());
                        if (fieldToMatch == null)
                            throw new MatchingException("field: " + field.getName() + " is missing proper annotation");
                        fieldsToMatchToObject.add(List.of(field, fieldToMatch));
                    }
                }
            } else {
                for (var field : fieldsSolution) {
                    //only string fields
                    if (field.getType().equals(String.class)) {
                        fieldsToMatchToObject.add(List.of(field, field.getName()));
                    }
                }
            }
            for (var solutions : listOfPossibleSolutions) {

                var solutionsPercentage = 0.;
                int counter = 0;
                for (var solutionField : fieldsToMatchToObject) {
                    counter++;
                    var field = (Field) solutionField.get(0);
                    var matchWith = (String) solutionField.get(1);
                    field.setAccessible(true);
                    var solutionString = field.get(solutions).toString();


                    var objectField = object.getClass().getDeclaredField(matchWith);
                    objectField.setAccessible(true);
                    var objectString = objectField.get(object).toString();

                    var currentPercentage = matchInPercentage(objectString, solutionString);
                    solutionsPercentage = (solutionsPercentage + currentPercentage) / counter;

                }
                if (solutionsPercentage > matchPercentage) {
                    matchPercentage = solutionsPercentage;
                    solution = solutions;
                }
            }

            return solution;
        }
        throw new MatchingException("No field annotated. missing proper annotation use @MatchWith or set field to the name of the field from the other object. \nNote that there is only a one to one match. Don't assign multiple fields to the same!");
    }


    /***
     * returns a percentage of how equal the two strings are.
     * @param first
     * @param second
     * @return
     */
    public double matchInPercentage(String first, String second) {
        LevenshteinDistanceService ld = new LevenshteinDistanceService();
        var distance = ld.computeDistance(normalizer.normalize(first), normalizer.normalize(second));
        return (1. - distance / Math.max(first.length() + 1., second.length() + 1.)) * 100.;
    }

    /***
     * matchPercentage is the percentage of the current match.
     * @return
     */
    public double getMatchPercentage() {
        return this.matchPercentage;
    }


    /***
     * Returns match percentage of two objects
     * @param object
     * @param objectToMatch
     * @return
     * @throws MatchingException
     */
    public double matchObjectWithObject(Object object, Object objectToMatch) throws MatchingException {

        //List for saving all annotated fields
        List<List<Object>> fieldsToMatchToObject = new ArrayList<>();

        //getting all fields
        var fieldsSolution = objectToMatch.getClass().getDeclaredFields();
        //if objects are not the same
        if (!object.getClass().equals(objectToMatch.getClass())) {
            //check if Fields are annotated for solutions
            for (var field : fieldsSolution) {
                Annotation a = field.getAnnotation(MatchWith.class);
                if (a instanceof MatchWith mw) {

                    var fieldToMatch = (mw.field());
                    if (fieldToMatch == null)
                        throw new MatchingException("field: " + field.getName() + " is missing proper annotation");
                    fieldsToMatchToObject.add(List.of(field, fieldToMatch));
                }
            }
        } else {
            for (var field : fieldsSolution) {
                //only string fields
                if (field.getType().equals(String.class)) {
                    fieldsToMatchToObject.add(List.of(field, field.getName()));
                }
            }
        }

        var solutionsPercentage = 0.;
        int counter = 0;
        for (var solutionField : fieldsToMatchToObject) {
            counter++;
            var field = (Field) solutionField.get(0);
            var matchWith = (String) solutionField.get(1);
            field.setAccessible(true);
            String solutionString = null;
            try {
                solutionString = field.get(objectToMatch).toString();
                var objectField = object.getClass().getDeclaredField(matchWith);
                objectField.setAccessible(true);
                var objectString = objectField.get(object).toString();
                var currentPercentage = matchInPercentage(objectString, solutionString);
                matchPercentage = solutionsPercentage = (solutionsPercentage + currentPercentage) / counter;

            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new MatchingException("field: " + field.getName() + " is not annotated properly used field =" + (String) solutionField.get(1), e);
            }
            return solutionsPercentage;
        }
        throw new MatchingException("Don't be funny you can't match null objects");
    }
}


