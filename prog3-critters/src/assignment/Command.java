package assignment;

public enum Command {
    
    //Enum constants for every possible instruction
    //boolean in parentheses gets passed into constructor (obscure enum syntax)
	HOP(true), LEFT(true), RIGHT(true), INFECT(true), EAT(true), 
	GO(false), IFRANDOM(false), IFHUNGRY(false), IFSTARVING(false),
	IFEMPTY(false), IFALLY(false), IFENEMY(false), IFWALL(false), IFANGLE(false),
	WRITE(false), ADD(false), SUB(false), INC(false), DEC(false), IFLT(false), IFEQ(false), IFGT(false);
	
	private boolean isAction;
	
	public boolean isAction() {
		return this.isAction;
	}
	
	private Command(boolean act) {
		isAction = act;
	}
}

