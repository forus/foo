package foo.interpreter;

import foo.model.FunctionNode;
import foo.runtime.StdLib;
import org.junit.Test;

import static org.junit.Assert.*;

public class SystemPackageTest {

    private SystemPackage systemPackage = new SystemPackage(StdLib.class);

    @Test
    public void testPlus() {
        assertEquals(5.0, Interpreter.call((FunctionNode) systemPackage.item("plus"), 2.0, 3.0));
    }

    @Test
    public void testMinus() {
        assertEquals(-1.0, Interpreter.call((FunctionNode) systemPackage.item("minus"), 2.0, 3.0));
    }
}