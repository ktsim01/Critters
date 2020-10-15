package assignment;

public class Instruction {

	private Command name;
	private String[] params;

	public Instruction(Command name, String[] params) {
		this.name = name;
		this.params = params;
	}

	public static Instruction parseCode(String input) {
        try {
            //string array of each term in the instruction
            String splitInput[] = input.split(" ");

            //parse first term into command
            //toUpperCase used since Java's bultin valueOf is case sensitive
            Command c = Command.valueOf(splitInput[0].toUpperCase());

            //everything else becomes the parameters
            String[] params = new String[splitInput.length - 1];
            for (int i = 0; i < splitInput.length - 1; i++) {
                params[i] = splitInput[i + 1];
            }

            //instantiate instruction based off these values
            return new Instruction(c, params);
        } catch(Exception e) {
            System.err.println("Something went wrong while parsing the instruction: " + input);
            return null;
        }
	}
	
    //getter methods for encapsulated variables
    //no set methods - Instructions should not be changed after instantiation
	public Command getCommandName() {
		return name;
	}
	
	public String[] getParams() {
		return params;
	}
}
