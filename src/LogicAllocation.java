public class LogicAllocation {

	/* 
	 * High-level method for (SIMPLE) ADDING TASKS
	 * This method assumes that some logical breakdown has already happened, 
	 * and that the add's command type and the instruction have been separated out.
	 * It distribures the work out to baser functions, based on the command type.
	 * Somewhat inspired by Dave Jun's CityConnect.
	 */ 
	public static String executeCommandAdd(COMMAND_TYPE commandType, String instruction) {
		switch (commandType) {
		case ADD_FLOATING:
			return addFloating(instruction);
		case ADD_TIMED:
			return addTimed(instruction);
		case ADD_RECURRING:
			return addRecurring(instruction);
		case ADD_TIMED_DATED:
			return addTimedDated(instruction);		
		case INVALID:
			return String.format(MESSAGE_INVALID_FORMAT, userCommand); //or rasie some sort of exception
		case EXIT:
			System.exit(0);
		default:
			//throw an error if the command is not recognized
			throw new Error("Unrecognized command type");
		}
	}