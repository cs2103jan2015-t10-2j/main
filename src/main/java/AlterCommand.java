public class AlterCommand implements ICommand {

    private Event eventToAlter;
    private Event eventWithUpdatedData;
    private Event eventWithOldData;

    //@author A0134704M
    public AlterCommand(Event eventToAlter, Event eventWithUpdatedData) {
        this.eventToAlter = eventToAlter;
        this.eventWithUpdatedData = eventWithUpdatedData;
        this.eventWithOldData = new Event();
    }

    //@author A0134704M
    @Override
    public boolean execute() {
        copyEventValues(eventToAlter, eventWithOldData);
        copyEventValues(eventWithUpdatedData, eventToAlter);
        return true;
    }

    //@author A0134704M
    @Override
    public boolean undo() {
        copyEventValues(eventWithOldData, eventToAlter);
        return true;
    }

    //@author A0134704M
    @Override
    public boolean redo() {
        copyEventValues(eventWithUpdatedData, eventToAlter);
        return true;
    }

    //@author A0134704M
    @Override
    public boolean isReversible() {
        return true;
    }

    //@author A0134704M
    private void copyEventValues(Event from, Event to) {
        to.setTaskName(from.getTaskName());
        to.setTaskDate(from.getTaskDate());
        to.setTaskDuration(from.getTaskDuration());
        to.setTaskLocation(from.getTaskLocation());
        to.setTaskDescription(from.getTaskDescription());
        to.setTaskPriority(from.getTaskPriority());
        to.setDone(from.isDone());
        to.setRecurring(from.isRecurring());
    }
}
