package com.bitsforabetterworld.lunarlander;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LanderTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testPosition() {
		Lander lander = new Lander.Builder().x(32.3).y(62.6).theta(11.1).build();
		Position position = lander.getPosition();
		assertEquals(32.3, position.getX());
		assertEquals(62.6, position.getY());
		assertEquals(11.1, position.getTheta());
	}
	
	@Test
	void testFreeFallin() {
		// All the vampires walkin' throught the Valley
		// Move west down Ventura Boulevard
		// --Tom Petty
		Lander lander = new Lander.Builder().y(100.0).build();
		lander.clockTick(1.0);
		Position position = lander.getPosition();
		Velocity velocity = lander.getVelocity();
		assertEquals(1.0 * Lander.GravityAcceleration, velocity.getDy());
		assertEquals(0.0, velocity.getDx());
		assertEquals(0.0, velocity.getDtheta());
		// Position hasn't changed yet, because velocity was 0 at the start of this tick
		assertEquals(100.0, position.getY());
	}
	
	@Test
	void testFreeFallin2() {
		Lander lander = new Lander.Builder().y(100.0).build();
		for (int i=0; i < 2; ++i) {
			lander.clockTick(1.0);
		}
		Position position = lander.getPosition();
		Velocity velocity = lander.getVelocity();
		assertEquals(2.0 * Lander.GravityAcceleration, velocity.getDy());
		// Position has changed by 0 + 1
		assertEquals(99.0, position.getY());		
	}

	@Test
	void testFreeFallin4() {
		Lander lander = new Lander.Builder().y(100.0).build();
		for (int i=0; i < 4; ++i) {
			lander.clockTick(1.0);
		}
		Position position = lander.getPosition();
		Velocity velocity = lander.getVelocity();
		assertEquals(4.0 * Lander.GravityAcceleration, velocity.getDy());
		// Position has changed by 0 + 1 + 2 + 3
		assertEquals(94.0, position.getY());		
	}
	@Test
	void testFreeFallin8() {
		Lander lander = new Lander.Builder().y(100.0).build();
		for (int i=0; i < 8; ++i) {
			lander.clockTick(1.0);
		}
		Position position = lander.getPosition();
		Velocity velocity = lander.getVelocity();
		assertEquals(8.0 * Lander.GravityAcceleration, velocity.getDy());
		// Position has changed by 0 + 1 + 2 + 3 + 4 + 5 + 6 + 7
		assertEquals(72.0, position.getY());
	}
	
	@Test
	void testThrustOn() {
		
	}
}