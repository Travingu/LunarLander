package com.bitsforabetterworld.lunarlander;

import java.util.EnumSet;

import com.bitsforabetterworld.lunarlander.ui.Display;

public class Lander {
	/* How do you get an instance of Lander?
	 * First, create a Lander.Builder, and call the various methods like x() and dy() to
	 * set the desired construction parameters.
	 * Then call build() on the Lander.Builder, to get an instance of Lander.
	 * This is called the "Builder Pattern".
	 */
	public static class Builder {
		public Builder() {}
		
		public Lander build() { return new Lander(this); }
		public Builder x(double x) { this.m_x = x; return this; }
		public Builder y(double y) { this.m_y = y; return this; }
		public Builder dx(double dx) { this.m_dx = dx; return this; }
		public Builder dy(double dy) { this.m_dy = dy; return this; }
		public Builder theta(double theta) { this.m_theta = theta; return this; }
		public Builder dtheta(double dtheta) { this.m_dtheta = dtheta; return this; }
		public Builder thrusterAcceleration(double accel) { this.m_thrusterAcceleration = accel; return this; }
		public Builder rotationMotorAcceleration(double accel) { this.m_thrusterAcceleration = accel; return this; }
		public Builder gravityAcceleration(double grav) { this.m_gravityAcceleration = grav; return this; }
		public Builder fuel(double fuel) { this.m_fuel = fuel; return this; }
		double m_x = 0.0;
		double m_y = 0.0;
		double m_dx = 0.0;
		double m_dy = 0.0;
		double m_theta = 0.0;
		double m_dtheta = 0.0;
		double m_thrusterAcceleration = 10.0;
		double m_rotationMotorAcceleration = 0.7;
		double m_gravityAcceleration = -1.0;
		double m_fuel = 100000.0;
	}
	
	/**
	 * This constructor is private. To create an instance of Lander, user Builder.build()
	 * @param builder
	 */
	private Lander(Builder builder) {
		this.m_x = builder.m_x;
		this.m_y = builder.m_y;
		this.m_dx = builder.m_dx;
		this.m_dy = builder.m_dy;
		this.m_theta = builder.m_theta % (2.0 * Math.PI);
		this.m_dtheta = builder.m_dtheta;
		this.m_thrusterAcceleration = builder.m_thrusterAcceleration;
		this.m_rotationMotorAcceleration = builder.m_rotationMotorAcceleration;
		this.m_gravityAcceleration = builder.m_gravityAcceleration;	
		this.m_fuel = builder.m_fuel;
	}
	
	public Position getPosition()
	{
		return new Position(m_x, m_y, m_theta);
	}

	public Velocity getVelocity() {
		return new Velocity(m_dx, m_dy, m_dtheta);
	}
	
	public double getFuelRemaining() {
		return m_fuel;
	}
	
	public boolean isLanded() {
		return m_isLanded;
	}
	
	public boolean isCrashed() {
		return m_isCrashed;
	}
	
	double getGravityAcceleration() {
		return m_gravityAcceleration;
	}
	
	public boolean isThrusterOn() {
		return m_isThrusterOn;
	}
	
	public void clockTick(double dt, EnumSet<Command> commands) {
		// With apologies to Isaac Newton.
		// Let's update position and rotation according to their velocities
		if (m_isLanded) {
			return;
		}

		double dx_next = m_dx;
		double dy_next = m_dy;
		
		if (m_fuel < 0.0) {
			m_fuel = 0.0;
		}
		// And let's apply gravity
		dy_next += m_gravityAcceleration * dt;
		
		// And account for our engines!
		if (commands.contains(Command.Thrust) && m_fuel > 0.0) {
			dx_next += m_thrusterAcceleration * dt * Math.sin(m_theta);
			dy_next += m_thrusterAcceleration * dt * Math.cos(m_theta);
			m_fuel -= dt;
			m_isThrusterOn = true;
		}
		else {
			m_isThrusterOn = false;
		}

		m_x = (m_x + ((m_dx + dx_next) / 2.0) * dt) % Constants.WIDTH_OF_SCREEN;
		m_y += ((m_dy + dy_next) / 2.0) * dt;
		m_theta = (m_theta + m_dtheta * dt) % (2.0 * Math.PI);

		m_dx = dx_next;
		m_dy = dy_next;
		
		if (commands.contains(Command.RollClockwise)) {
			m_dtheta += m_rotationMotorAcceleration * dt;
		}
		if (commands.contains(Command.RollCounterclockwise)) {
			m_dtheta -= m_rotationMotorAcceleration * dt;
		}

		
		
		if (m_y <= 0) {
			m_isLanded = true;
			if ((Math.abs(m_dx) > 5.0) || (Math.abs(m_dy) > 8.0)) {
				// too much speed at landing
				m_isCrashed = true;
			}
			else if (Math.abs(m_theta) > 0.3) {
				// too steep an angle at landing
				m_isCrashed = true;
			}
			else if (Math.abs(m_dtheta) > 0.2) {
				m_isCrashed = true;
			}
			else {
				// Safe landing!
				double landingPadBonus = (Math.abs(m_x - (Constants.WIDTH_OF_SCREEN / 2.0))) < (Display.LANDING_PAD_WIDTH / 2) ? 100.0 : 0.0;
				LanderLevel.reportSuccessfulLanding(m_fuel + landingPadBonus);
			}
		}
	}		
	
	
	// Current position along the X axis, in meters
	private double m_x;
	
	// Current position along the Y axis, in meters
	private double m_y;
	
	// Current velocity along the X axis, in meters/second
	private double m_dx;
	
	// Current velocity along the Y axis, in meters/second
	private double m_dy;
	
	// Current angle of the lander in radians. Upright is 0.0, +Pi/2 points the nose to the right, -Pi/2 points the nose to the left
	private double m_theta;

	// Current rotational velocity of the lander in radians/second
	private double m_dtheta;
	
	// Has the lander crashed?
	private boolean m_isCrashed = false;
	
	// Has the lander landed yet?
	private boolean m_isLanded = false;
	
	// How much fuel (which is burned at a rate of 1 unit/second while thrusting) is left?
	private double m_fuel;
	
	// How much thrust does the thruster give when it's on? In meters/second**2
	private final double m_thrusterAcceleration;
	
	// How much does the rotation motor change our rotation speed? In radians/second**2
	private final double m_rotationMotorAcceleration;
	
	// What is the acceleration of gravity? In meters/second**2 (and generally negative!!!)
	private final double m_gravityAcceleration;
	
	private boolean m_isThrusterOn = false;
}
