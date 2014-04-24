package androidappnaoserver;

import java.util.Scanner;

import com.aldebaran.proxy.*;

public class RobotConnection {
	public ALMotionProxy motion;
	public ALRobotPostureProxy posture;

	public RobotConnection(String ip, int port) {
		motion = new ALMotionProxy(ip, port);
		posture = new ALRobotPostureProxy(ip, port);
		posture.goToPosture("StandZero", 1);
	}

	public void Stiffen() {
		motion.stiffnessInterpolation(new Variant(new String[] { "HeadYaw" }),
				new Variant(new float[] { 1.0f }), new Variant(
						new float[] { 1.0f }));
		System.out.println("Stiffening Joints");
	}

	/**
	 * Unsitffen Nao's joints.
	 */
	public void Unstiffen() {
		motion.stiffnessInterpolation(new Variant(new String[] { "HeadYaw" }),
				new Variant(new float[] { 0.0f }), new Variant(
						new float[] { 0.0f }));
		System.out.println("Relaxing Joints");
	}

	/**
	 * Have Nao walk forward.
	 */
	public void Walk() {
		motion.moveInit();
		System.out.println("Walking Forward");
	}

	/**
	 * Have Nao turn.
	 * 
	 * @param turnAmount
	 *            Degrees to turn from current heading.
	 */
	public void Turn(float turnAmount) {
		System.out.println("Turning " + turnAmount + " degrees.");
	}

	/**
	 * Have Nao Step left or right.
	 * 
	 * @param left
	 *            True to move left false to move right.
	 */
	public void Strafe(boolean left) {
		System.out.println("Stepping to the " + (left ? "left" : "right"));
	}

	/**
	 * Stand Nao up.
	 */
	public void Stand() {
		motion.stopMove();
		posture.goToPosture("Stand", 1);
		System.out.println("Standing Up.");
	}

	/**
	 * Sit Nao down.
	 */
	public void Sit() {
		motion.stopMove();
		posture.goToPosture("Sit", 1);
		System.out.println("Sitting Down.");
	}

	public void Stop() {
		motion.killAll();
		System.out.println("Stopping Movement.");
	}

	public void testMethod() {
		// This lets you use bound methods that expects ALValue from Java:
		Variant names = new Variant(new String[] { "HeadYaw" });
		Variant angles = new Variant(new float[] { -0.5f, 0.5f, 0.0f });
		Variant times = new Variant(new float[] { 1.0f, 2.0f, 3.0f });
		Scanner sc = new Scanner(System.in);
		String str = "";

		motion.setStiffnesses(new Variant(new String("Body")), new Variant(
				new Float(1.0f)));
		posture.goToPosture("Stand", 0.5f);

		while (str.compareTo("x") != 0) {
			str = sc.next();
			if (str.compareTo("h") == 0) {
				// motion.setStiffnesses(new Variant(new String[] {"HeadYaw"}),
				// new Variant(new float[] {1.0f}));
				motion.angleInterpolation(names, angles, times, true);
				// motion.setStiffnesses(new Variant(new String[] {"HeadYaw"}),
				// new Variant(new float[] {0.0f}));
			}
			if (str.compareTo("m") == 0) {
				// motion.setStiffnesses(new Variant(new String ("Body")), new
				// Variant(new Float (1.0f)));
				motion.moveTo(1.2f, 1.2f, 0.0f);
				// motion.setStiffnesses(new Variant(new String ("Body")), new
				// Variant(new Float (0.0f)));
			}
		}
		motion.setStiffnesses(new Variant(new String("Body")), new Variant(
				new Float(0.0f)));
		sc.close();
	}

}
