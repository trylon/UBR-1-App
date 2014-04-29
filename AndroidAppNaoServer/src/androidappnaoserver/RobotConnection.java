package androidappnaoserver;

import com.aldebaran.proxy.*;

public class RobotConnection {

	protected ALMotionProxy motion;
	protected ALRobotPostureProxy posture;
	protected ALTextToSpeechProxy tts;
	protected ALVideoDeviceProxy video;
	
	private float walkSpeed = 0.2f;
	
	private boolean isWalking = false;

	public RobotConnection(String ip, int port) {

		isWalking = false;
		motion = new ALMotionProxy(ip, port);
		posture = new ALRobotPostureProxy(ip, port);
		tts = new ALTextToSpeechProxy(ip, port);
		video = new ALVideoDeviceProxy(ip, port);
		try {
			// tts.say("Hello, I am connected to the Android Application Nao Server.");
			// Thread.sleep(2000);
			// tts.say("Initializing Neural Network");
			// Thread.sleep(5000);
			// tts.say("Gathering Back knowledge from internet.");
			// Thread.sleep(10000);
			tts.say("Skynet online.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void Stiffen() {
		System.out.println("Stiffening Joints");
		
		isWalking = false;

		motion.stiffnessInterpolation(new Variant(new String[] { "Body" }),
				new Variant(new float[] { 1.0f }), new Variant(
						new float[] { 0.5f }));

		System.out.println(motion.getSummary());
	}

	/**
	 * Unsitffen Nao's joints.
	 */
	public void Unstiffen() {

		System.out.println("Relaxing Joints");
		
		isWalking = false;

		motion.stiffnessInterpolation(new Variant(new String[] { "Body" }),
				new Variant(new float[] { 0.0f }), new Variant(
						new float[] { 0.5f }));
		
		System.out.println(motion.getSummary());
	}

	/**
	 * Have Nao walk forward.
	 */
	public void Walk() {
		// Set move posture
		if (!isWalking)
		{
			motion.moveInit();			
		}

		// Wait for ready.
		// motion.waitUntilMoveIsFinished();
		
		// Move forward
		motion.move(walkSpeed, 0.0f, 0.0f);
		isWalking = true;

		System.out.println(motion.getSummary());
	}

	/**
	 * Have Nao turn.
	 * 
	 * @param turnAmount
	 *            Degrees to turn from current heading.
	 */
	public void Turn(float turnAmount) {
		System.out.println("Turning " + turnAmount + " degrees.");

		if (!isWalking)
		{
			// Get to ready position.
			motion.moveInit();
		}

		// Wait for hardward to finish moving
		// motion.waitUntilMoveIsFinished();

		// Convert the turn amount to radians.
		float moveAmountRadians = DegreesToRadians(turnAmount);

		if (moveAmountRadians < 0) {
			if(!isWalking)
			{
				motion.move(0.0f, 0.0f, -0.3f);
			}
			else
			{			
				motion.move(moveSpeed, 0.0f, -0.15f);
			}
		} else {
			if (!isWalking)
			{
				motion.move(0.0f, 0.0f, 0.3f);
			}
			else
			{
				motion.move(moveSpeed, 0.0f, 0.15f);
			}
		}

		// Send turn command.

		System.out.println("" + moveAmountRadians);
		System.out.println(motion.getSummary());

	}

	/**
	 * Have Nao Step left or right.
	 * 
	 * @param left
	 *            True to move left false to move right.
	 */
	public void Strafe(boolean left) {
		System.out.println("Stepping to the " + (left ? "left" : "right"));

		isWalking = false;
		// Get ready to turn.
		motion.moveInit();

		// Wait for hardward to finish moving.
		// motion.waitUntilMoveIsFinished();

		// Start stepping
		if (left) {
			motion.move(0.0f, 0.2f, 0.0f);
		} else {
			motion.move(0.0f, -0.2f, 0.0f);
		}

		System.out.println(motion.getSummary());
	}

	/**
	 * Stand Nao up.
	 */
	public void Stand() {
		isWalking = false;
		motion.stopMove();
		posture.goToPosture("StandInit", 0.8f);
		System.out.println("Standing Up.");
	}

	/**
	 * Sit Nao down.
	 */
	public void Sit() {
		isWalking = false;
		motion.stopMove();
		posture.goToPosture("Sit", 1.0f);
		System.out.println("Sitting Down.");
	}

	public void Stop() {
		isWalking = false;
		motion.stopMove();
		System.out.println("Stopping Movement.");
	}

	public void emergencyStop() {
		isWalking = false;
		Sit();
		Unstiffen();
	}

	public byte[] getVideoFrame() {

		final String subscriberID = "NaoServer";
		final int TOP_CAMERA = 0;
		final int RESOLUTION_320px_240px = 1;
		final int RESOLUTION_640px_480px = 2;
		final int COLOR_RGB = 11;
		final int FPS_5 = 5;
		final int FPS_30 = 30;
		try {
			video.subscribeCamera(subscriberID, TOP_CAMERA,
					RESOLUTION_640px_480px, COLOR_RGB, FPS_30);

			Variant remoteImage = video.getImageRemote(subscriberID);

			Variant imageV = remoteImage.getElement(6);
			byte[] imgData = imageV.toBinary();
			video.releaseImage(subscriberID);
			video.unsubscribe(subscriberID);
			if (imgData == null) {
				return null;
			} else {
				return imgData;
			}
		} catch (Exception ex) {
			return null;
		}

	}

	private static float DegreesToRadians(float deg) {
		return (deg * (float) (Math.PI / 180.0));
	}
}
