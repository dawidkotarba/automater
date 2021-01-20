package dawid.kotarba.automater.executor

import dawid.kotarba.automater.Beans
import dawid.kotarba.automater.device.Keyboard
import dawid.kotarba.automater.device.Mouse

abstract class AbstractStep implements Step {
    protected Mouse mouse = Beans.mouse
    protected Keyboard keyboard = Beans.keyboard

    private shallExecute(String executionLine) {
        return executionLine.startsWith(getStepType().name()) & executionLine.contains(getSupportedMethod().orElseGet({
            return ''
        }))
    }

    @Override
    final void executeIfApplicable(String executionLine) {
        if (shallExecute(executionLine)) {
            execute(executionLine)
        }
    }

    abstract void execute(String executionLine);

    List<String> getParams(String executionLine) {
        def parameters = executionLine.tokenize()
        parameters.remove(getStepType().name())
        getSupportedMethod().ifPresent({
            parameters.remove(it)
        })
        return parameters
    }

    Integer getIntParam(String executionLine) {
        def params = getParams(executionLine)
        params[0] as int
    }

    Tuple2<Integer, Integer> getTwoIntParams(String executionLine) {
        def params = getParams(executionLine)
        new Tuple2<Integer, Integer>(params[0] as int, params[1] as int)
    }
}
