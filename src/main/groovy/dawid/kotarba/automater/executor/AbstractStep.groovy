package dawid.kotarba.automater.executor


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

    private boolean shallExecute(String executionLine) {
        def tokenizedExecutionLine = executionLine.tokenize()
        checkArgument(tokenizedExecutionLine.size() >= 2)
        return tokenizedExecutionLine[0] == stepType.name() & tokenizedExecutionLine[1] == getSupportedMethod().orElseGet({
            return ''
        })
    }

    @Override
    final void executeIfApplicable(String executionLine) {
        try {
            if (shallExecute(executionLine)) {
                execute(executionLine)
            }
        } catch (Exception e) {
            throw new IllegalStateException("Cannot execute step: $executionLine")
        }
    }

    abstract void execute(String executionLine);

    List<String> getParams(String executionLine) {
        def tokenizedExecutionLine = executionLine.tokenize()
        tokenizedExecutionLine.remove(getStepType().name())
        getSupportedMethod().ifPresent({
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
