package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;
import static org.junit.runners.Parameterized.*;

import java.util.*;
import assignment.*;

@RunWith(Parameterized.class)
public class InstructionTest {
    
    private String input;
    private Command cmd;
    private String[] params;

    public InstructionTest(String input, Command cmd, String[] params) {
        this.input = input;
        this.cmd = cmd;
        this.params = params;
    }
    @Parameters
    public static Collection<Object[]> getTestData() {
        Object[][] data = new Object[][] {
            {"hop", Command.HOP, new String[] {}},
            {"left", Command.LEFT, new String[] {}},
            {"right", Command.RIGHT, new String[] {}},
            //infect
            {"infect", Command.INFECT, new String[] {}},
            {"infect 0", Command.INFECT, new String[] {"0"}},
            {"infect 5", Command.INFECT, new String[] {"5"}},
            {"infect -5", Command.INFECT, new String[] {"-5"}},
            
            //go
            {"go +2", Command.GO, new String[] {"+2"}},
            {"go +100", Command.GO, new String[] {"+100"}},
            {"go -2", Command.GO, new String[] {"-2"}},
            {"go -200", Command.GO, new String[] {"-200"}},
            {"go 2", Command.GO, new String[] {"2"}},
            {"go 5", Command.GO, new String[] {"5"}},
            {"go 100", Command.GO, new String[] {"100"}},
            {"go 200", Command.GO, new String[] {"200"}},
            {"go 2", Command.GO, new String[] {"2"}},
            {"go 2.2", Command.GO, new String[] {"2.2"}},
            {"go", Command.GO, new String[] {}},
            
            
            //if random
            {"ifrandom 5", Command.IFRANDOM, new String[] {"5"}},
            {"ifrandom 10", Command.IFRANDOM, new String[] {"10"}},
            {"ifrandom -5", Command.IFRANDOM, new String[] {"-5"}},
            {"ifrandom a", Command.IFRANDOM, new String[] {"a"}},
            {"ifrandom 0", Command.IFRANDOM, new String[] {"0"}},
            {"ifrandom ", Command.IFRANDOM, new String[] {}},
            {"ifrandom 5.5", Command.IFRANDOM, new String[] {"5.5"}},
            
            
            //if hungry
            {"ifhungry 10", Command.IFHUNGRY, new String[] {"10"}},
            {"ifhungry -5", Command.IFHUNGRY, new String[] {"-5"}},
            {"ifhungry a", Command.IFHUNGRY, new String[] {"a"}},
            {"ifhungry 0", Command.IFHUNGRY, new String[] {"0"}},
            {"ifhungry 5.5", Command.IFHUNGRY, new String[] {"5.5"}},
            {"ifhungry r1", Command.IFHUNGRY, new String[] {"r1"}},
            {"ifhungry r10", Command.IFHUNGRY, new String[] {"r10"}},
            {"ifhungry r100", Command.IFHUNGRY, new String[] {"r100"}},
            
            //if starving
            {"ifstarving 10", Command.IFSTARVING, new String[] {"10"}},
            {"ifstarving -5", Command.IFSTARVING, new String[] {"-5"}},
            {"ifstarving a", Command.IFSTARVING, new String[] {"a"}},
            {"ifstarving 0", Command.IFSTARVING, new String[] {"0"}},
            {"ifstarving 100", Command.IFSTARVING, new String[] {"100"}},
            {"ifstarving !!!", Command.IFSTARVING, new String[] {"!!!"}},
            {"ifstarving 5.5", Command.IFSTARVING, new String[] {"5.5"}},
            {"ifstarving r1", Command.IFSTARVING, new String[] {"r1"}},
            {"ifstarving r10", Command.IFSTARVING, new String[] {"r10"}},
            {"ifstarving r100", Command.IFSTARVING, new String[] {"r100"}},
            
            //if empty
            {"ifempty 45 0", Command.IFEMPTY, new String[] {"45","0"}},
            {"ifempty 90 1", Command.IFEMPTY, new String[] {"90","1"}},
            {"ifempty 135.5 10", Command.IFEMPTY, new String[] {"135.5","10"}},
            {"ifempty 180 01", Command.IFEMPTY, new String[] {"180","01"}},
            {"ifempty 225 20", Command.IFEMPTY, new String[] {"225","20"}},
            {"ifempty 200 30", Command.IFEMPTY, new String[] {"200","30"}},
            {"ifempty -225 5", Command.IFEMPTY, new String[] {"-225","5"}},
            {"ifempty 45 r1", Command.IFEMPTY, new String[] {"45","r1"}},
            {"ifempty -25 r1", Command.IFEMPTY, new String[] {"-25","r1"}},
            {"ifempty 0 r2", Command.IFEMPTY, new String[] {"0","r2"}},
            {"ifempty 0 r22", Command.IFEMPTY, new String[] {"0","r22"}},
            
            
            
            //if ally
            {"ifally 45 0", Command.IFALLY, new String[] {"45","0"}},
            {"ifally 45 0", Command.IFALLY, new String[] {"45","0"}},
            {"ifally -45 1", Command.IFALLY, new String[] {"-45","1"}},
            {"ifally 45 5", Command.IFALLY, new String[] {"45","5"}},
            {"ifally 360 6", Command.IFALLY, new String[] {"360","6"}},
            {"ifally 315 2", Command.IFALLY, new String[] {"315","2"}},
            {"ifally 315 r2", Command.IFALLY, new String[] {"315","r2"}},
            {"ifally r2", Command.IFALLY, new String[] {"r2"}},
            {"ifally r2 r6", Command.IFALLY, new String[] {"r2","r6"}},
            
            
            //if enemy            
            {"ifenemy 45 0", Command.IFENEMY, new String[] {"45","0"}},
            {"ifenemy 90 5", Command.IFENEMY, new String[] {"90","5"}},
            {"ifenemy 135 r5", Command.IFENEMY, new String[] {"135","r5"}},
            {"ifenemy 0 r0", Command.IFENEMY, new String[] {"0","r0"}},
            {"ifenemy -45 100", Command.IFENEMY, new String[] {"-45","100"}},
            {"ifenemy 450 10", Command.IFENEMY, new String[] {"450","10"}},
            {"ifenemy 315 r1", Command.IFENEMY, new String[] {"315","r1"}},
            {"ifenemy 360 r0", Command.IFENEMY, new String[] {"360","r0"}},
            
            //if wall
            {"ifwall 45 0", Command.IFWALL, new String[] {"45","0"}},
            {"ifwall 4.5 pp", Command.IFWALL, new String[] {"4.5","pp"}},
            {"ifwall 365 rp", Command.IFWALL, new String[] {"365","rp"}},
            {"ifwall 360 r3", Command.IFWALL, new String[] {"360","r3"}},
            {"ifwall 69 r2", Command.IFWALL, new String[] {"69","r2"}},
            {"ifwall -123 r9", Command.IFWALL, new String[] {"-123","r9"}},
            {"ifwall 0 -50", Command.IFWALL, new String[] {"0","-50"}},
            {"ifwall lmao 10", Command.IFWALL, new String[] {"lmao","10"}},
            {"ifwall 456 0", Command.IFWALL, new String[] {"456","0"}},
            {"ifwall 456", Command.IFWALL, new String[] {"456"}},
            {"ifwall r1 r2", Command.IFWALL, new String[] {"r1", "r2"}},
            
            //if angle
            {"ifangle 45 45 0", Command.IFANGLE, new String[] {"45","45","0"}},
            {"ifangle 31 r1 r2", Command.IFANGLE, new String[] {"31","r1","r2"}},
            {"ifangle r1 r7", Command.IFANGLE, new String[] {"r1","r7"}},
            {"ifangle 314.5 45 0", Command.IFANGLE, new String[] {"314.5","45","0"}},
            {"ifangle r2 r3 10", Command.IFANGLE, new String[] {"r2","r3","10"}},
            {"ifangle r 45 0", Command.IFANGLE, new String[] {"r","45","0"}},
            {"ifangle r1 90 45.5", Command.IFANGLE, new String[] {"r1","90","45.5"}},
            {"ifangle r4 r5 r6", Command.IFANGLE, new String[] {"r4","r5","r6"}},
            {"ifangle r7 r10 135", Command.IFANGLE, new String[] {"r7","r10","135"}},
            
            //write
            {"write r1 5", Command.WRITE, new String[] {"r1","5"}},
            {"write r10 -5", Command.WRITE, new String[] {"r10","-5"}},
            {"write 5 r9", Command.WRITE, new String[] {"5","r9"}},
            {"write r5 r4", Command.WRITE, new String[] {"r5","r4"}},
            {"write 10 r5", Command.WRITE, new String[] {"10","r5"}},
            {"write r100 r5", Command.WRITE, new String[] {"r100","r5"}},
            {"write r1 5000", Command.WRITE, new String[] {"r1","5000"}},
            {"write r1 5.5", Command.WRITE, new String[] {"r1","5.5"}},
            {"write r1", Command.WRITE, new String[] {"r1"}},
            
            
            //add
            {"add r1 r2", Command.ADD, new String[] {"r1","r2"}},
            {"add r100 r200", Command.ADD, new String[] {"r100","r200"}},
            {"add r7 r8", Command.ADD, new String[] {"r7","r8"}},
            {"add 5 0", Command.ADD, new String[] {"5","0"}},
            {"add r9 5", Command.ADD, new String[] {"r9","5"}},
            {"add r0 r2", Command.ADD, new String[] {"r0","r2"}},
            {"add r1 r1", Command.ADD, new String[] {"r1","r1"}},
            {"add r4 5.5", Command.ADD, new String[] {"r4","5.5"}},
            {"add ", Command.ADD, new String[] {}},
            
            //sub
            {"sub r1 r2", Command.SUB, new String[] {"r1","r2"}},
            {"sub r100 r200", Command.SUB, new String[] {"r100","r200"}},
            {"sub r7 r8", Command.SUB, new String[] {"r7","r8"}},
            {"sub 5 0", Command.SUB, new String[] {"5","0"}},
            {"sub r9 5", Command.SUB, new String[] {"r9","5"}},
            {"sub r0 r2", Command.SUB, new String[] {"r0","r2"}},
            {"sub r1 r1", Command.SUB, new String[] {"r1","r1"}},
            {"sub r4 5.5", Command.SUB, new String[] {"r4","5.5"}},
            {"sub ", Command.SUB, new String[] {}},
            
            //increment
            {"inc r1", Command.INC, new String[] {"r1"}},
            {"inc r5", Command.INC, new String[] {"r5"}},
            {"inc -r1", Command.INC, new String[] {"-r1"}},
            {"inc 100", Command.INC, new String[] {"100"}},
            {"inc", Command.INC, new String[] {}},
            
            //decrement
            {"dec r12", Command.DEC, new String[] {"r12"}},
            {"dec r6", Command.DEC, new String[] {"r6"}},
            {"dec 100", Command.DEC, new String[] {"100"}},
            {"dec r11", Command.DEC, new String[] {"r11"}},
            {"dec r2", Command.DEC, new String[] {"r2"}},
            
            
            //if less than
            {"iflt r1 10 n", Command.IFLT, new String[] {"r1", "10", "n"}},
            {"iflt r01 r02 100", Command.IFLT, new String[] {"r01", "r02", "100"}},
            {"iflt r1 r2 n", Command.IFLT, new String[] {"r1", "r2", "n"}},
            {"iflt 5 6 r2", Command.IFLT, new String[] {"5", "6", "r2"}},
            {"iflt r10 r10 5", Command.IFLT, new String[] {"r10", "r10", "5"}},
            {"iflt 19 r7 9", Command.IFLT, new String[] {"19", "r7", "9"}},
            {"iflt r4 r5 r10", Command.IFLT, new String[] {"r4", "r5", "r10"}},
            
            //if equal
            {"ifeq r1 r2 n", Command.IFEQ, new String[] {"r1", "r2", "n"}},
            {"ifeq r1 r2 5", Command.IFEQ, new String[] {"r1", "r2", "5"}},
            {"ifeq r1 r5 -5", Command.IFEQ, new String[] {"r1", "r5", "-5"}},
            {"ifeq r6 r10 5", Command.IFEQ, new String[] {"r6", "r10", "5"}},
            {"ifeq r5 r5 100", Command.IFEQ, new String[] {"r5", "r5", "100"}},
            {"ifeq r-5 r2 1", Command.IFEQ, new String[] {"r-5", "r2", "1"}},
            
            
            //if greater than
            {"ifgt r1 r2 5", Command.IFGT, new String[] {"r1", "r2", "5"}},
            {"ifgt r1 r5 -5", Command.IFGT, new String[] {"r1", "r5", "-5"}},
            {"ifgt r6 r10 5", Command.IFGT, new String[] {"r6", "r10", "5"}},
            {"ifgt r5 r5 100", Command.IFGT, new String[] {"r5", "r5", "100"}},
            {"ifgt r-5 r2 1", Command.IFGT, new String[] {"r-5", "r2", "1"}},
            
            
        };
        return Arrays.asList(data);
    }

    @Test
    public void testCommand() {
        Instruction inst = Instruction.parseCode(input);
        assertArrayEquals(inst.getParams(), params);
        assertEquals(inst.getCommandName(), cmd);
    }
}
