package com.bitsforabetterworld.lunarlander;

public class Lander {
	
	public static final double RotationMotorAcceleration = 1.0; // radians/second
	public static final double GravityAcceleration = -1.0; // meters/second^2

	public enum RotationDirection {
		Clockwise,
		CounterClockwise
	}
	
	
	/* How do you get an instance of Lander?
	 * First, create a Lander.Builder, and call the various methods like x() and dy() to
	 * set the desired construction parameters.
	 * Then call build() on the Lander.Builder, to get an instance of Lander.
	 * This is called the "Builder Pattern". It's pretty common in Java.
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
		double m_x = 0.0;
		double m_y = 0.0;
		double m_dx = 0.0;
		double m_dy = 0.0;
		double m_theta = 0.0;
		double m_dtheta = 0.0;
		double m_thrusterAcceleration = 10.0;
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
		this.m_theta = builder.m_theta;
		this.m_dtheta = builder.m_dtheta;
		this.m_thrusterAcceleration = builder.m_thrusterAcceleration;
	}
	public Position getPosition()
	{
		return new Position(m_x, m_y, m_theta);
	}

	public Velocity getVelocity() {
		return new Velocity(m_dx, m_dy, m_dtheta);
	}
	public void turnOnThruster() {
		m_isThrusterOn = true;
	}
	public void turnOffThruster() {
		m_isThrusterOn = false;
	}
	
	public void turnOnRotationMotor(RotationDirection rotationDirection) {
		m_rotationMotorDirection = rotationDirection;
		m_isRotationMotorOn = true;
	}
	public void turnOffRotationMotor() {
		m_isRotationMotorOn = false;
	}
	
	public void clockTick(double dt) {
		// With apologies to Isaac Newton.
		
		// Let's update position and rotation according to their velocities
		m_x += m_dx * dt;
		m_y += m_dy * dt;
		m_theta += m_dtheta * dt;
		
		// And let's apply gravity
		m_dy += GravityAcceleration * dt;
		
		// And account for our engines!
		if (m_isThrusterOn) {
			m_dx += m_thrusterAcceleration * dt * Math.sin(m_theta);
			m_dy += m_thrusterAcceleration * dt * Math.cos(m_theta);
		}
		
		if (m_isRotationMotorOn) {
			if (m_rotationMotorDirection == RotationDirection.Clockwise) {
				m_dtheta += RotationMotorAcceleration * dt;
			}
			else {
				m_dtheta -= RotationMotorAcceleration * dt;
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
	
	// Has the lander crashed yet?
	private boolean m_isCrashed = false;

	// Is the thruster currently on?
	private boolean m_isThrusterOn = false;
	
	// Is the rotational motor currently on?
	private boolean m_isRotationMotorOn = false;
	
	private RotationDirection m_rotationMotorDirection = RotationDirection.Clockwise;
	
	// How much thrust does the thruster give when it's on? In meters/second**2
	private final double m_thrusterAcceleration;
}