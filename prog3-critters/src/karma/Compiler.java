package karma;
import java.util.*;
import java.io.*;

public class Compiler {
	public static void main(String[] args) throws Exception {
		String filename = "";
		if(args.length == 1) filename = args[0];
		else {
			Scanner in = new Scanner(System.in);
			System.out.println("File name:");
			filename = in.nextLine();
		}
		if(!filename.endsWith(".cri2")) {
			System.out.println("File type unsupported.");
			return;
		}
		Compiler c = new Compiler(filename);
		c.parse();
		c.write();
	}
	
	private ArrayList<String> file;
	private String filename;
	private InstructionBlock base;
	private String critterName;
	private static final char indentChar = '\t';
	public Compiler(String filename) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		critterName = reader.readLine();
		file = new ArrayList<>();
		this.filename = filename;
		String str = "";
		while((str = reader.readLine()) != null) file.add(str);
		base = new InstructionBlock();
	}
	public void parse() throws Exception {
		parse(0, "", base);
	}
	public void parse(int lineNum, String indent, InstructionBlock parent) throws Exception {
		if(lineNum >= file.size()) return;
		String line = file.get(lineNum);
		if(!line.startsWith(indent)) return;
		if(!line.startsWith(indent+indentChar)) {
			line = line.trim();
			if(line.startsWith("if")) {
				If cur = new If(line);
				parse(lineNum+1, indent+indentChar, cur);
				parent.appendInstruction(cur);
			} else if(line.startsWith("else")) {
				Else cur = new Else();
				parse(lineNum+1, indent+indentChar, cur);
				parent.appendInstruction(cur);
			} else if(RegisterInstruction.validInstruction(line)) {
				RegisterInstruction cur = new RegisterInstruction(line);
				parent.appendInstruction(cur);
			} else if(line.startsWith("loop")) {
				Loop cur = new Loop();
				parse(lineNum+1, indent+indentChar, cur);
				parent.appendInstruction(cur);
			} else if(line.equals("break")) {
				Break cur = new Break();
				parent.appendInstruction(cur);
			} else if(Instruction.validInstruction(line)) {
				Instruction cur = new Instruction(line);
				parent.appendInstruction(cur);
			} else throw new Exception("Unknown command: " + line);
			parse(lineNum+1, indent, parent);
		} else parse(lineNum+1, indent, parent);
	}
	public void write() throws Exception {
		PrintWriter out = new PrintWriter(new FileWriter(filename.substring(0,filename.length()-1)));
		out.println(critterName);
		List<String> instructions = base.getCompiledInstructions();
		for(String s : instructions)
				out.println(s);
		out.flush();
		out.close();
	}
}

class Instruction {
	private static final HashMap<String,String> INSTRUCTIONS = new HashMap<>();
	static {
		INSTRUCTIONS.put("hop", "hop");
		INSTRUCTIONS.put("left", "left");
		INSTRUCTIONS.put("right", "right");
		INSTRUCTIONS.put("infect", "infect");
		INSTRUCTIONS.put("eat", "eat");
		INSTRUCTIONS.put("restart", "go 1");
	}
	
	private Instruction nextInstruction;
	private String compiledInstruction;
	public Instruction(String line) throws Exception {
		if(line.equals("")) {
			compiledInstruction = "";
			return;
		}
		compiledInstruction = getInstructionName(line);
	}
	public int getLength() {
		return 1;
	}
	public List<String> getCompiledInstructions() throws Exception {
		ArrayList<String> list = new ArrayList<>();
		if(compiledInstruction.equals("")) throw new Exception("Empty line returned in compile");
		list.add(compiledInstruction);
		return list;
	}
	public Instruction getNextInstruction() {
		return nextInstruction;
	}
	public void setNextInstruction(Instruction i) {
		nextInstruction = i;
	}

	private static String getInstructionName(String s) throws Exception {
		if(INSTRUCTIONS.containsKey(s)) return INSTRUCTIONS.get(s);
		throw new Exception("Instruction not supported: " + s);
	}
	public static boolean validInstruction(String s) {
		if(INSTRUCTIONS.containsKey(s)) return true;
		return false;
	}
}
class InstructionBlock extends Instruction {
	private Instruction blockStart;
	private Instruction blockEnd;
	private int length;
	public InstructionBlock() throws Exception {
		super("");
		blockStart = blockEnd = new Instruction("");
	}
	public void appendInstruction(Instruction i) throws Exception {
		if(i instanceof Else) {
				if(!(blockEnd instanceof If))
				throw new Exception("else did not follow if" + this.getClass());
			else ((If) blockEnd).appendElse((Else) i);
		} else {
			blockEnd.setNextInstruction(i);
			blockEnd = i;
			length += i.getLength();
		}
	}
	public int getLength() {
		return length;
	}
	public List<String> getCompiledInstructions() throws Exception {
		ArrayList<String> list = new ArrayList<>();
		Instruction curInstruction = blockStart;
		while(curInstruction != blockEnd) {
			curInstruction = curInstruction.getNextInstruction();
			list.addAll(curInstruction.getCompiledInstructions());
		}
		return list;
	}
}
class RegisterInstruction extends Instruction {
	private static HashMap<String, String> registerMap = new HashMap<>();
	private String compiledInstruction;
	public static boolean validInstruction(String s) {
		return s.contains("++") || s.contains("--") || s.contains("=") || s.startsWith("define");
	}
	public static void setVariable(String var, String reg) throws Exception {
		if(!reg.matches("r\\d+")) throw new Exception("Invalid register: " + reg);
		registerMap.put(var, reg);
	}
	public static String getVariable(String var) throws Exception {
		if(!registerMap.containsKey(var)) throw new Exception("Variable not defined: " + var);
		return registerMap.get(var);
	}
	public RegisterInstruction(String s) throws Exception {
		super("");
		if(s.startsWith("define")) {
			String[] split = s.split("\\s+");
			setVariable(split[1], split[2]);
		} else if(s.contains("++")) {
			compiledInstruction = "inc " +  getVariable(s.replace("+", "").trim());
		} else if(s.contains("--")) {
			compiledInstruction = "dec " +  getVariable(s.replace("-", "").trim());
		} else if(s.contains("+=")) {
			String[] split = s.split("\\s*\\+=\\s*");
			compiledInstruction = "add " + getVariable(split[0].trim()) + " " + getVariable(split[1].trim());
		} else if(s.contains("-=")) {
			String[] split = s.split("\\s*\\-=\\s*");
			compiledInstruction = "sub " + getVariable(split[0].trim()) + " " + getVariable(split[1].trim());
		} else if(s.contains("=")) {
			String[] split = s.split("\\s*\\=\\s*");
			compiledInstruction = "write " + getVariable(split[0].trim()) + " " + split[1].trim();
			
		} else throw new Exception("Unknown register command: " + s);
	}
	public List<String> getCompiledInstructions() throws Exception {
		ArrayList<String> list = new ArrayList<>();
		if(compiledInstruction == null) return list;
		list.add(compiledInstruction);
		return list;
	}
}
class If extends InstructionBlock {
	private List<String> prefix;
	private String cond;
	public If(String s) throws Exception {
		int parenIndex = s.indexOf('(');
		if(parenIndex == -1) throw new Exception("Unsupported if syntax: " + s);
		prefix = new ArrayList<>();
		cond = s.substring(parenIndex+1, s.length()-1);
		if(cond.equals("hungry")) cond = "ifhungry";
		else if(cond.equals("random")) cond = "ifrandom";
		else if(cond.equals("starving")) cond = "ifstarving";
		else if(cond.contains("<")) {
			String[] regs = cond.split("\\s*<\\s*");
			cond = "iflt " + RegisterInstruction.getVariable(regs[0]) + " "  + RegisterInstruction.getVariable(regs[1]);
		}
		else if(cond.contains("=")) {
			String[] regs = cond.split("\\s*=\\s*");
			cond = "ifeq " + RegisterInstruction.getVariable(regs[0]) + " "  + RegisterInstruction.getVariable(regs[1]);
		}
		else if(cond.contains(">")) {
			String[] regs = cond.split("\\s*>\\s*");
			cond = "ifgt " + RegisterInstruction.getVariable(regs[0]) + " "  + RegisterInstruction.getVariable(regs[1]);
		}
		else {
			int parenInd = cond.indexOf('(');
			if(parenInd == -1) throw new Exception("if condition not supported: " + cond);
			String params = cond.substring(parenInd+1, cond.length()-1).replaceAll("\\s*,\\s*", " ");
			if(cond.startsWith("empty")) cond = "ifempty " + params;
			else if(cond.startsWith("ally")) cond = "ifally " + params;
			else if(cond.startsWith("enemy")) cond = "ifenemy " + params;
			else if(cond.startsWith("wall")) cond = "ifwall " + params;
			else if(cond.startsWith("angle")) cond = "ifangle " + params;
			else throw new Exception("if condition not supported: " + cond);
		}
	}
	public List<String> getCompiledInstructions() throws Exception {
		List<String> result = new ArrayList<>();
		List<String> instructions = super.getCompiledInstructions();
		result.add(cond + " +" + (prefix.size()+2));
		result.addAll(prefix);
		result.add("go +" + (instructions.size()+1));
		result.addAll(instructions);
		return result;
	}
	public void appendElse(Else e) throws Exception {
		if(!prefix.isEmpty()) throw new Exception("If followed by mutliple elses");
		prefix = e.getCompiledInstructions();
	}
}
class Else extends InstructionBlock {
	public Else() throws Exception {}
}

class Loop extends InstructionBlock {
	public Loop() throws Exception {}
	public List<String> getCompiledInstructions() throws Exception {
		List<String> result = new ArrayList<>();
		List<String> instructions = super.getCompiledInstructions();
		result.addAll(instructions);
		result.add("go -" + instructions.size());
		for(int i = 0; i < result.size(); i++)
			if(result.get(i).equals("break"))
				result.set(i, "go +" + (result.size()-i));
		return result;
	}
}
class Break extends Instruction {
	public Break() throws Exception {
		super("");
	}
	public List<String> getCompiledInstructions() throws Exception {
		ArrayList<String> list = new ArrayList<>();
		list.add("break");
		return list;
	}
}