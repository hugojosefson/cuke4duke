package cuke4duke.internal.language;

import cuke4duke.internal.JRuby;
import org.jruby.RubyArray;
import org.jruby.runtime.builtin.IRubyObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractProgrammingLanguage implements ProgrammingLanguage {
    protected final LanguageMixin languageMixin;
    private List<StepDefinition> stepDefinitions;
    private Map<Class<?>, Transformable> transforms;

    public AbstractProgrammingLanguage(LanguageMixin languageMixin) {
        this.languageMixin = languageMixin;
    }

    final public RubyArray step_matches(String step_name, String formatted_step_name) throws Throwable {
        return JRuby.newArray(step_match_list(step_name, formatted_step_name));
    }

    public abstract void load_code_file(String file) throws Throwable;

    public final List<IRubyObject> step_match_list(String step_name, String formatted_step_name) throws Throwable {
        List<IRubyObject> matches = new ArrayList<IRubyObject>();
        for (StepDefinition stepDefinition : stepDefinitions) {
            List<StepArgument> arguments = stepDefinition.arguments_from(step_name);
            if (arguments != null) {
                matches.add(languageMixin.create_step_match(stepDefinition, step_name, formatted_step_name, arguments));
            }
        }
        return matches;
    }

    protected void clearHooksAndStepDefinitions() {
        transforms = new HashMap<Class<?>, Transformable>();
        stepDefinitions = new ArrayList<StepDefinition>();
    }

    public final Map<Class<?>, Transformable> getTransforms() {
        return transforms;
    }

    public void addTransform(Class<?> type, Transformable transform) {
        this.transforms.put(type, transform);
    }

    public void addBeforeHook(Hook before) {
        languageMixin.add_hook("before", before);
    }

    public void addStepDefinition(StepDefinition stepDefinition) {
        stepDefinitions.add(stepDefinition);
    }

    public void addAfterHook(Hook after) {
        languageMixin.add_hook("after", after);
    }

    protected abstract void begin_scenario(IRubyObject scenario) throws Throwable;

    public abstract void end_scenario() throws Throwable;

    public void availableStepDefinition(String regexp_source, String file_colon_line) {
        languageMixin.available_step_definition(regexp_source, file_colon_line);
    }

    public void invokedStepDefinition(String regexp_source, String file_colon_line) {
        languageMixin.invoked_step_definition(regexp_source, file_colon_line);
    }
}
