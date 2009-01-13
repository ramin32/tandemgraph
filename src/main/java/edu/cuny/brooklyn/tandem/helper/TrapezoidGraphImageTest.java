package edu.cuny.brooklyn.tandem.helper;

import static org.junit.Assert.*;

import org.junit.Test;

public class TrapezoidGraphImageTest {

	@Test
	public void testTranslateXPoint() {
		fail("Not yet implemented");
	}

	@Test
	public void testTranslateYPoint() {
		fail("Not yet implemented");
	}

	@Test
	public void testLeftTrapezoidPoint() {
		double xLeftBase = -0.5;
		int	yVertex = 1;
		double log10y = Math.log10(yVertex);
		int result = TrapezoidGraphImage.leftTrapezoidPoint(log10y,xLeftBase);
		System.out.println(result);
		assertTrue(result > 0);
	}

	@Test
	public void testRightTrapezoidPoint() {
		fail("Not yet implemented");
	}

}
