package androidappnaoserver;

import java.util.Scanner;

import com.aldebaran.proxy.*;

public class RobotConnection {

    protected ALMotionProxy motion;
    protected ALRobotPostureProxy posture;
    protected ALTextToSpeechProxy tts;

    public RobotConnection(String ip, int port) {


        motion = new ALMotionProxy(ip, port);
        posture = new ALRobotPostureProxy(ip, port);
        tts = new ALTextToSpeechProxy(ip, port);

        try {
            tts.say("Hello, I am connected to the Android Application Nao Server.");
            Thread.sleep(2000);
            tts.say("Initializing Neaural Network");
            Thread.sleep(5000);
            tts.say("Gathering Back knowledge from internet.");
            Thread.sleep(10000);
            tts.say("Skynet online.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Stiffen() {
        System.out.println("Stiffening Joints");

        motion.stiffnessInterpolation(new Variant(new String[]{"Body"}),
                new Variant(new float[]{1.0f}), new Variant(
                new float[]{0.5f}));

        System.out.println(motion.getSummary());
    }

    /**
     * Unsitffen Nao's joints.
     */
    public void Unstiffen() {

        System.out.println("Relaxing Joints");

        motion.stiffnessInterpolation(new Variant(new String[]{"Body"}),
                new Variant(new float[]{0.0f}), new Variant(
                new float[]{0.5f}));

        System.out.println(motion.getSummary());
    }

    /**
     * Have Nao walk forward.
     */
    public void Walk() {
        // Set move posture
        motion.moveInit();

        // Wait for ready.
        motion.waitUntilMoveIsFinished();

        // Move forward
        motion.moveTo(0.5f, 0.0f, 0.0f);

        System.out.println(motion.getSummary());
    }

    /**
     * Have Nao turn.
     *
     * @param turnAmount Degrees to turn from current heading.
     */
    public void Turn(float turnAmount) {
        System.out.println("Turning " + turnAmount + " degrees.");

        // Get to ready position.
        motion.moveInit();

        // Wait for hardward to finish moving
        motion.waitUntilMoveIsFinished();

        // Convert the turn amount to radians.
        float moveAmountRadians = DegreesToRadians(turnAmount);

        // Send turn command.
        motion.moveTo(0.0f, 0.0f, moveAmountRadians);

        System.out.println(motion.getSummary());

    }

    /**
     * Have Nao Step left or right.
     *
     * @param left True to move left false to move right.
     */
    public void Strafe(boolean left) {
        System.out.println("Stepping to the " + (left ? "left" : "right"));

        // Get ready to turn.
        motion.moveInit();

        // Wait for hardward to finish moving.
        motion.waitUntilMoveIsFinished();

        // Start stepping
        if (left) {
            motion.moveTo(0.0f, -0.5f, 0.0f);
        } else {
            motion.moveTo(0.0f, 0.5f, 0.0f);
        }

        System.out.println(motion.getSummary());
    }

    /**
     * Stand Nao up.
     */
    public void Stand() {
        motion.stopMove();
        posture.goToPosture("Stand", 1.0f);
        System.out.println("Standing Up.");
    }

    /**
     * Sit Nao down.
     */
    public void Sit() {
        motion.stopMove();
        posture.goToPosture("Sit", 1.0f);
        System.out.println("Sitting Down.");
    }

    public void Stop() {
        motion.stopMove();
        System.out.println("Stopping Movement.");
    }

    public void emergencyStop() {
        motion.killAll();
    }

    public void testMethod() {
        // This lets you use bound methods that expects ALValue from Java:
        Variant names = new Variant(new String[]{"HeadYaw"});
        Variant angles = new Variant(new float[]{-0.5f, 0.5f, 0.0f});
        Variant times = new Variant(new float[]{1.0f, 2.0f, 3.0f});
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

    private static float DegreesToRadians(float deg) {
        return (deg * (float) (Math.PI / 180.0));
    }
}
