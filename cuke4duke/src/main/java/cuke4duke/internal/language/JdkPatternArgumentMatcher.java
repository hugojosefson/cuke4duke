package cuke4duke.internal.language;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JdkPatternArgumentMatcher {
    public static List<StepArgument> argumentsFrom(Pattern regexp, String stepName) {
        Matcher matcher = regexp.matcher(stepName);
        if(matcher.matches()) {
            List<StepArgument> arguments = new ArrayList<StepArgument>();
            for(int i = 1; i <= matcher.groupCount(); i++) {
                arguments.add(new StepArgument(matcher.group(i), matcher.start(i)));
            }
            return arguments;
        } else {
            return null;
        }
    }
}
