package dawid.kotarba.automater.service.executor


import dawid.kotarba.automater.Beans
import dawid.kotarba.automater.device.Keyboard
import dawid.kotarba.automater.device.Mouse
import dawid.kotarba.automater.device.Screen

import static com.google.common.base.Preconditions.checkArgument

abstract class AbstractStep implements Step {
    protected Mouse mouse = Beans.mouse
    protected Keyboard keyboard = Beans.keyboard
    protected Screen screen = Beans.screen
    protected Random random = new Random()

    @Override
    boolean isApplicable(String executionLine) {
        def tokenizedExecutionLine = executionLine.tokenize()
        def executionLineDefinitionSize = 2
        checkArgument(tokenizedExecutionLine.size() >= executionLineDefinitionSize)
        return tokenizedExecutionLine[0] == stepType.name() && tokenizedExecutionLine[1] == getMethod().orElseGet({
            return ''
        }) && tokenizedExecutionLine.size() == executionLineDefinitionSize + getArgumentsCount()
    }

    @Override
    final boolean executeIfApplicable(String executionLine) {
        try {
            if (isApplicable(executionLine)) {
                execute(executionLine)
                return true
            }
            return false
        } catch (Exception e) {
            throw new IllegalStateException("Cannot execute step: $executionLine")
        }
    }

    abstract void execute(String executionLine);

    Collection<String> getParams(String executionLine) {
        def tokenizedExecutionLine = executionLine.tokenize()
        tokenizedExecutionLine.remove(getStepType().name())
        getMethod().ifPresent({
            tokenizedExecutionLine.remove(it)
        })
        return tokenizedExecutionLine
    }

    String getStringParam(String executionLine) {
        getParams(executionLine)[0]
    }

    Tuple2<String, String> getTwoStringParams(String executionLine) {
        def params = getParams(executionLine)
        new Tuple2<String, String>(params[0], params[1])
    }

    Integer getIntParam(String executionLine) {
        getParams(executionLine)[0] as int
    }

    Tuple2<Integer, Integer> getTwoIntParams(String executionLine) {
        def params = getParams(executionLine)
        new Tuple2<Integer, Integer>(params[0] as int, params[1] as int)
    }
}
