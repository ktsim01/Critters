package assignment;

import java.io.*;
import java.util.*;

/**
 * Responsible for loading critter species from text files and interpreting the
 * simple Critter language.
 * 
 * For more information on the purpose of the below two methods, see the
 * included API/ folder and the project description.
 */
public class Interpreter implements CritterInterpreter {

	public CritterSpecies loadSpecies(String filename) throws IOException {
		BufferedReader f = new BufferedReader(new FileReader(filename));
		String name = f.readLine();
		List<Instruction> instructions = new ArrayList<Instruction>();

        //continually get the next line from the file
		while (true) {
			String line = f.readLine();
			
            //notify user if the blank line was never seen
			if (line == null) {
				//check if there is an invisible blank line
				//BufferedReader doesn't count an ending blank line
				int prevChar = -1;
				f = new BufferedReader(new FileReader(filename));
				while(true) {
					int newChar = f.read();
					
					//EOF
					if(newChar == -1) break;
					
					prevChar = newChar;
				}
				if(prevChar != '\n') {
					System.err.println("End of file reached without terminating new line");
					f.close();
					return null;
				} else break;
			}

            //stop reading lines if we hit the blank line
			if (line.isEmpty())
				break;

            //add instruction to list
			instructions.add(Instruction.parseCode(line));
		}

        //instantiate given the values of the file
		CritterSpecies critter = new CritterSpecies(name, instructions);
		f.close();
		return critter;
	}

	public void executeCritter(Critter c) {
        //get list of instructions & current line
		List<Instruction> instructions = c.getCode();
		int curLine = c.getNextCodeLine();

        //only attempt to execute an instruction if it is in bounds
		while (1 <= curLine && curLine <= instructions.size()) {

            //curLine is 1-indexed
			Instruction inst = instructions.get(curLine - 1);

            if(inst == null) {
                System.err.println("Instruction on line " + curLine + " could not be parsed properly.");
                curLine = -1;
                break;
            }
			Command cmd = inst.getCommandName();

            //handle invalid parameters
            try {
                //call appropriate method for every Command
                switch (cmd) {
                case HOP:
                    curLine = hop(inst.getParams(), c, curLine);
                    break;

                case LEFT:
                    curLine= left(inst.getParams(), c, curLine);
                    break;

                case RIGHT:
                    curLine= right(inst.getParams(), c, curLine);
                    break;

                case INFECT:
                    curLine= infect(inst.getParams(), c, curLine);
                    break;

                case EAT:
                    curLine= eat(inst.getParams(), c, curLine);
                    break;

                case GO:
                    curLine= go(inst.getParams(), c, curLine);
                    break;

                case IFRANDOM:
                    curLine= ifRandom(inst.getParams(), c, curLine);
                    break;

                case IFHUNGRY:
                    curLine= ifHungry(inst.getParams(), c, curLine);
                    break;

                case IFSTARVING:
                    curLine= ifStarving(inst.getParams(), c, curLine);
                    break;

                case IFEMPTY:
                    curLine= ifEmpty(inst.getParams(), c, curLine);
                    break;

                case IFALLY:
                    curLine= ifAlly(inst.getParams(), c, curLine);
                    break;

                case IFENEMY:
                    curLine= ifEnemy(inst.getParams(), c, curLine);
                    break;

                case IFWALL:
                    curLine= ifWall(inst.getParams(), c, curLine);
                    break;

                case IFANGLE:
                    curLine= ifAngle(inst.getParams(), c, curLine);
                    break;

                case WRITE:
                    curLine= write(inst.getParams(), c, curLine);
                    break;

                case ADD:
                    curLine= add(inst.getParams(), c, curLine);
                    break;

                case SUB:
                    curLine= sub(inst.getParams(), c, curLine);
                    break;

                case INC:
                    curLine= inc(inst.getParams(), c, curLine);
                    break;

                case DEC:
                    curLine= dec(inst.getParams(), c, curLine);
                    break;

                case IFLT:
                    curLine= iflt(inst.getParams(), c, curLine);
                    break;

                case IFEQ:
                    curLine= ifeq(inst.getParams(), c, curLine);
                    break;

                case IFGT:
                    curLine= ifgt(inst.getParams(), c, curLine);
                    break;

                //handle unknown or unimplemented commands
                default:
                    System.err.println("Instruction not supported");
                    return;
                }

                //halt execution if the command was an action
                if (cmd.isAction())
                    break;

            //catch any errors that happen due to invalid parameters
            } catch(Exception e) {
                //if there was an issue, the critter should stop executing
                //user should be notified that there was an issue with their instructions
                System.err.println("An error occured while executing the instruction on line " + curLine + ". Perhaps a parameter was missing or formatted incorrectly?");
                curLine = -1;
                break;
            }
		}
        //store the code line for next execution
		c.setNextCodeLine(curLine);
		return;
	}

    //auxillary method that parses a jump location and returns the proper new line
    //it handles +n, -n, rn, and n -- but will throw an error otherwise
	public int jumpToLine(String s, Critter c, int curline) {
		if (s.charAt(0) == '+')
			return curline + Integer.parseInt(s.substring(1));
		else if (s.charAt(0) == '-')
			return curline - Integer.parseInt(s.substring(1));
		else if (s.charAt(0) == 'r')
			return c.getReg(parseRegister(s));
		else
            return Integer.parseInt(s);

	}

    //auxillary method that parses registers into indices
    //useful if register syntax ever changes
    public int parseRegister(String s) {
        return Integer.parseInt(s.substring(1));
    }
    /*
     * All of these methods take (parameters, Critter, current line) 
     * and return the next line of code that should be executed.
     * Also, they implement whatever the instruction is supposed to do.
     * Any exceptions/errors thrown are due to invalid parameters.
     */

	public int hop(String[] params, Critter c, int curline) {
		c.hop();
		return curline + 1;
	}

	public int left(String[] params, Critter c, int curline) {
		c.left();
		return curline + 1;
	}

	public int right(String[] params, Critter c, int curline) {
		c.right();
		return curline + 1;
	}

	public int infect(String[] params, Critter c, int curline) {
		if (params.length == 0)
			c.infect();
		else
			c.infect(Integer.parseInt(params[0]));
		return curline + 1;
	}
	public int eat(String[] params, Critter c, int curline) {
		c.eat();
		return curline + 1;
	}

	public int go(String[] params, Critter c, int curline) {
		return jumpToLine(params[0], c, curline);
	}
	public int ifRandom(String[] params, Critter c, int curline) {
		if (c.ifRandom())
			return jumpToLine(params[0], c, curline);
		else
			return curline + 1;
	}

	public int ifHungry(String[] params, Critter c, int curline) {
		if (c.getHungerLevel() == Critter.HungerLevel.HUNGRY) {
			return jumpToLine(params[0], c, curline);
		} else if (c.getHungerLevel() == Critter.HungerLevel.STARVING) {
			return jumpToLine(params[0], c, curline);
		} else
			return curline + 1;
	}

	public int ifStarving(String[] params, Critter c, int curline) {
		if (c.getHungerLevel() == Critter.HungerLevel.STARVING) {
			return jumpToLine(params[0], c, curline);
		} else
			return curline + 1;
	}

	public int ifEmpty(String[] params, Critter c, int curline) {
		int content = c.getCellContent(Integer.parseInt(params[0]));
		if (content == Critter.EMPTY)
			return jumpToLine(params[1], c, curline);
		else
			return curline + 1;
	}

	public int ifAlly(String[] params, Critter c, int curline) {
		int content = c.getCellContent(Integer.parseInt(params[0]));
		if (content == Critter.ALLY)
			return jumpToLine(params[1], c, curline);
		else
			return curline + 1;
	}
	public int ifEnemy(String[] params, Critter c, int curline) {
		int content = c.getCellContent(Integer.parseInt(params[0]));
		if (content == Critter.ENEMY)
			return jumpToLine(params[1], c, curline);
		else
			return curline + 1;
	}

	public int ifWall(String[] params, Critter c, int curline) {
		int content = c.getCellContent(Integer.parseInt(params[0]));
		if (content == Critter.WALL)
			return jumpToLine(params[1], c, curline);
		else
			return curline + 1;
	}

	public int ifAngle(String[] params, Critter c, int curline) {
		int angle = c.getOffAngle(Integer.parseInt(params[0]));
		if (angle == Integer.parseInt(params[1]))
			return jumpToLine(params[2], c, curline);
		else
			return curline + 1;
	}

	public int write(String[] params, Critter c, int curline) {
		c.setReg(parseRegister(params[0]), Integer.parseInt(params[1]));
		return curline + 1;
	}

	public int add(String[] params, Critter c, int curline) {
		int r2 = c.getReg(parseRegister(params[1]));
		int r1 = c.getReg(parseRegister(params[0]));
		c.setReg(parseRegister(params[0]), r1 + r2);
		return curline + 1;
	}
	public int sub(String[] params, Critter c, int curline) {
		int r2 = c.getReg(parseRegister(params[1]));
		int r1 = c.getReg(parseRegister(params[0]));
		c.setReg(parseRegister(params[0]), r1 - r2);
		return curline + 1;
	}
	public int inc(String[] params, Critter c, int curline) { 
		int r1 = c.getReg(parseRegister(params[0]));
		c.setReg(parseRegister(params[0]), r1+1);
		
		return curline + 1;
	}
	public int dec(String[] params, Critter c, int curline) { 
		int r1 = c.getReg(parseRegister(params[0]));
		c.setReg(parseRegister(params[0]), r1-1);
		
		return curline + 1;
	}
	public int iflt(String[] params, Critter c, int curline) { 
		int r1 = c.getReg(parseRegister(params[0]));
		int r2 = c.getReg(parseRegister(params[1]));
		if(r1<r2) return jumpToLine(params[2], c, curline);
		else return curline + 1;
	}
	public int ifeq(String[] params, Critter c, int curline) { 
		int r1 = c.getReg(parseRegister(params[0]));
		int r2 = c.getReg(parseRegister(params[1]));
		if(r1==r2) return jumpToLine(params[2], c, curline);
		else return curline + 1;
	}
	public int ifgt(String[] params, Critter c, int curline) { 
		int r1 = c.getReg(parseRegister(params[0]));
		int r2 = c.getReg(parseRegister(params[1]));
		if(r1>r2) return jumpToLine(params[2], c, curline);
		else return curline + 1;
	}
}
