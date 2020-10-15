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
public class CommandTest {
    
    private String input;
    private Command cmd;

    public CommandTest(String input, Command cmd) {
        this.input = input;
        this.cmd = cmd;
    }
    @Parameters
    public static Collection<Object[]> getTestData() {
        Object[][] data = new Object[][] {
            {"hop", Command.HOP},
            {"left", Command.LEFT},
            {"right", Command.RIGHT},
            {"infect", Command.INFECT},
            {"eat",Command.EAT},
        	{"go",Command.GO},
        	{"ifrandom", Command.IFRANDOM},
        	{"ifhungry", Command.IFHUNGRY},
        	{"ifstarving",Command.IFSTARVING},
        	{"ifempty",Command.IFEMPTY},
        	{"ifally",Command.IFALLY},
        	{"ifenemy",Command.IFENEMY},
        	{"ifwall",Command.IFWALL},
        	{"ifangle", Command.IFANGLE},
        	{"write",Command.WRITE},
        	{"add",Command.ADD},
        	{"sub",Command.SUB},
        	{"inc",Command.INC},
        	{"dec",Command.DEC},
        	{"iflt",Command.IFLT},
        	{"ifeq",Command.IFEQ},
        	{"ifgt", Command.IFGT},
        	
        };
        return Arrays.asList(data);
    }

    @Test
    public void testCommand() {
        Command result = Command.valueOf(input.toUpperCase());
        assertEquals(result, cmd);
    }
}
