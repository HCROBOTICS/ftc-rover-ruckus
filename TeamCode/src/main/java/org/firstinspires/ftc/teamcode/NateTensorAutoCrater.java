package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.ftccommon.internal.RunOnBoot;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.hardware.NateHardware;
import static org.firstinspires.ftc.teamcode.NateTensorAutoCrater.Task.*;
import java.util.List;

@Autonomous(name = "Nate Tensor Auto Crater", group = "Nate")
public class NateTensorAutoCrater extends LinearOpMode {
    NateHardware robot = new NateHardware();

    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    private static final String VUFORIA_KEY = "AfypGhD/////AAABmfthsllptEbKpJLWTp1613szVUTl5xQJQBKWo" +
            "aUbDyLjOgOEF38/3fUHjGFD6pAlPmSTrW/ipYTOHpA48kfCl8o6PTWjR8X3220E5rDaANVOtluML1xOfvSl5fwb" +
            "XrAtj4kv8fpf2oFyu/ZYNOE5UCFaNzldW4BkJJ9w9YG5kNz4K0So/SrzZhqxPW+XbT0eTTjyx3Uox7VqRwM/DFF" +
            "Abh5kGzx8gGE+jQOAh9fVzy3rDLgQ/HQNszX7Iqwnnh/w836FuXrBbajfDun3qUQkCQKEJuaFyUEwEyZPZ+cRDy" +
            "m2WJigiXsw724H0pv050q0N67W+No/keaLi2mZVMuySZijkNjnsnhKrBCerryW9MJQ";

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    //ROBOT_SPEED changes the speed at which the robot moves (rotations and forward/back movement)
    //ROBOT_SPEED is negative to make positive ROBOT_SPEED = forwards and right turns
    public static final double ROBOT_SPEED = NateTensorAutoDepot.ROBOT_SPEED;

    //SLEEP_BETWEEN_MOVEMENTS is how long the robot waits between maneuvers
    private static final int SLEEP_BETWEEN_MOVEMENTS = 500;

    //teamPiece servo's potition
    private static final double SERVO_DROP_POSITION = 0.7;
    private static final double SERVO_HOLD_POSITION = 0.0;

    //these next values are used later to average the encoder readings
    double getLfPosition() {return robot.lf.getCurrentPosition();}
    double getRfPosition() {return robot.rf.getCurrentPosition();}
    double getLbPosition() {return robot.lb.getCurrentPosition();}
    double getRbPosition() {return robot.rb.getCurrentPosition();}

    //the averages are used to get better turns and movements
    double getEncoderAverageLeft() {return Math.abs((getLfPosition() + getLbPosition()) / 2);}
    double getEncoderAverageRight() {return Math.abs((getRfPosition() + getRbPosition()) / 2);}
    double getEncoderAverageAll() {return Math.abs((getEncoderAverageLeft() + getEncoderAverageRight()) / 2);}

    /*
    TURN_COEFFICIENT is used to change how much the robot turns without having to go through each
    line and change numbers. It assumes that 1000 encoder ticks is a 90 degree turn. This number
    should be changed to reflect the actual ratio:
    TURN_COEFFICIENT = (actual number of ticks) / (1000)
   */
    public static final double TURN_COEFFICIENT = 1.2;

    enum Task {Lower, Rotate, LookAtMinerals, ManeuverRight, ManeuverLeft, ManeuverCenter,
        ManeuverDepot, ManeuverCrater, End}
    Task task;

    /*
     * Lower- lower the robot
     * Rotate- turn to face the minerals and clear hook of latch
     * LookAtMinerals- use tensor to look at mineral positions
     * ManeuverRight- maneuver when the block is in the right position
     * ManeuverLeft- maneuver when the block is in the left position
     * ManeuverCenter- maneuver when the block is in the center position
     * Maneuver[R,L,C] will return the robot to the same position, regardless of which spot the gold is in.
     * ManeuverDepot- drive to the depot and deposit team marker
     * ManeuverCrater- drive to the crater and park
     * End- ends the program
     */

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);

        //for some reason the below line of code was not commented out. it worked fine withour it,
        //but i don't want it there if we don't need it there
        //task = Lower;

        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        // Wait for the game to begin
        telemetry.addData("", "Press Play to start tracking");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
            // Activate Tensor Flow Object Detection.
            if (tfod != null) {
                tfod.activate();
            }

            while (opModeIsActive()) {

                switch (task) {
                    case Lower: lower(); break;
                    case Rotate: rotate(); break;
                    case LookAtMinerals: lookAtMinerals(); break;
                    case ManeuverRight: maneuverRight(); break;
                    case ManeuverLeft: maneuverLeft(); break;
                    case ManeuverCenter: maneuverCenter(); break;
                    case ManeuverDepot: maneuverDepot(); break;
                    case ManeuverCrater: maneuverCrater(); break;
                    default: break;
                }
                telemetry.update();
            }
        }
        telemetry.update();
        task = Lower;
    }

    //Initialize the Vuforia localization engine.
    private void initVuforia() {

        //Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    // Initialize the Tensor Flow Object Detection engine.
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }

    void lower() {
        robot.omniWheels.stop_and_reset();
        telemetry.addData("Currently:","Lowering");
        telemetry.addData("Distance",robot.elevator.getDistance() - 1000);

        while (robot.elevator.getDistance() > 1000) {
            robot.elevator.elevate(1);
        } robot.elevator.elevate(0);
          sleep(SLEEP_BETWEEN_MOVEMENTS);

        task = Rotate;
    }

    void rotate() {
        robot.omniWheels.stop_and_reset();
        telemetry.addData("Currently:", "Rotating");
        telemetry.update();

        while (getEncoderAverageAll() < (800 * TURN_COEFFICIENT))
            robot.omniWheels.rotate(ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();

        while (getEncoderAverageAll() < 75)
            robot.omniWheels.go(-ROBOT_SPEED, -ROBOT_SPEED, -ROBOT_SPEED, -ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();

        task = LookAtMinerals;
    }

    void lookAtMinerals() {
        telemetry.addData("Currently:", "Looking at Minerals");
        telemetry.update();

        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {

                telemetry.addData("# Object Detected", updatedRecognitions.size());
                if (updatedRecognitions.size() == 3) {
                    int goldMineralX = -1;
                    int silverMineral1X = -1;
                    int silverMineral2X = -1;
                    for (Recognition recognition : updatedRecognitions) {
                        if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                            goldMineralX = (int) recognition.getLeft();
                        } else if (silverMineral1X == -1) {
                            silverMineral1X = (int) recognition.getLeft();
                        } else {
                            silverMineral2X = (int) recognition.getLeft();
                        }
                    }

                    if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                        if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                            telemetry.addData("Gold Mineral Position:", "Left");
                            telemetry.update();
                            task = ManeuverLeft;
                        } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                            telemetry.addData("Gold Mineral Position:", "Right");
                            telemetry.update();
                            task = ManeuverRight;
                        } else {
                            telemetry.addData("Gold Mineral Position:", "Center");
                            telemetry.update();
                            task = ManeuverCenter;
                        }
                    }
                }
                telemetry.update();
            }
        }
        sleep((SLEEP_BETWEEN_MOVEMENTS / 2));
    }

    void maneuverLeft() {
        robot.omniWheels.stop_and_reset();
        telemetry.addData("Currently:", "Moving Left Mineral");
        telemetry.update();

        while (getEncoderAverageAll() < (500 * TURN_COEFFICIENT))
            robot.omniWheels.rotate(-ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep (SLEEP_BETWEEN_MOVEMENTS);

        while (getEncoderAverageAll() < 1000)
            robot.omniWheels.go(ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (getEncoderAverageAll() < (500 * TURN_COEFFICIENT))
            robot.omniWheels.rotate(ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (getEncoderAverageAll() < 500)
            robot.omniWheels.go(ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (getEncoderAverageAll() < 500)
            robot.omniWheels.go(-ROBOT_SPEED, -ROBOT_SPEED, -ROBOT_SPEED, -ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (getEncoderAverageAll() < (500 * TURN_COEFFICIENT))
            robot.omniWheels.rotate(-ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (getEncoderAverageAll() < 1000)
            robot.omniWheels.go(-ROBOT_SPEED, -ROBOT_SPEED, -ROBOT_SPEED, -ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (getEncoderAverageAll() < (500 * TURN_COEFFICIENT))
            robot.omniWheels.rotate(ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        task = ManeuverDepot;
    }

    void maneuverRight() {
        robot.omniWheels.stop_and_reset();
        telemetry.addData("Currently:", "Moving Left Mineral");
        telemetry.update();

        while (getEncoderAverageAll() < (500 * TURN_COEFFICIENT))
            robot.omniWheels.rotate(ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep (SLEEP_BETWEEN_MOVEMENTS);

        while (getEncoderAverageAll() < 1000)
            robot.omniWheels.go(-ROBOT_SPEED, -ROBOT_SPEED, -ROBOT_SPEED, -ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (getEncoderAverageAll() < (500 * TURN_COEFFICIENT))
            robot.omniWheels.rotate(-ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (getEncoderAverageAll() < 500)
            robot.omniWheels.go(-ROBOT_SPEED, -ROBOT_SPEED, -ROBOT_SPEED, -ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (getEncoderAverageAll() < 500)
            robot.omniWheels.go(ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (getEncoderAverageAll() < (500 * TURN_COEFFICIENT))
            robot.omniWheels.rotate(ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (getEncoderAverageAll() < 1000)
            robot.omniWheels.go(ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (getEncoderAverageAll() < (500 * TURN_COEFFICIENT))
            robot.omniWheels.rotate(-ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        task = ManeuverDepot;
    }

    void maneuverCenter() {
        robot.omniWheels.stop_and_reset();
        telemetry.addData("Currently:", "Moving Center Mineral");
        telemetry.update();

        while (getEncoderAverageAll() < 1500)
            robot.omniWheels.go(ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (getEncoderAverageAll() < 1500)
            robot.omniWheels.go(-ROBOT_SPEED, -ROBOT_SPEED, -ROBOT_SPEED, -ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        task = ManeuverDepot;
    }

    void maneuverDepot() {
        robot.omniWheels.reset();
        telemetry.addData("Currently:", "Driving to Depot");
        telemetry.update();

        while ((getEncoderAverageAll() * TURN_COEFFICIENT) < 500) {
            robot.omniWheels.rotate(-ROBOT_SPEED);
        } robot.omniWheels.stop_and_reset();
          sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (getEncoderAverageAll() < 1000) {
            robot.omniWheels.go(ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED);
        } robot.omniWheels.stop_and_reset();
          sleep(SLEEP_BETWEEN_MOVEMENTS);


        while ((getEncoderAverageAll() * TURN_COEFFICIENT) < 250) {
            robot.omniWheels.rotate(-ROBOT_SPEED);
        } robot.omniWheels.stop_and_reset();
          sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (getEncoderAverageAll() < 1000) {
            robot.omniWheels.go(ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED);
        } robot.omniWheels.stop_and_reset();
          sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (((getEncoderAverageAll() * TURN_COEFFICIENT) < 250)) {
            robot.omniWheels.rotate(-ROBOT_SPEED);
        } robot.omniWheels.stop_and_reset();
          sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (getEncoderAverageAll() < 1000) {
            robot.omniWheels.go(ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED);
        } robot.omniWheels.stop_and_reset();
          sleep(SLEEP_BETWEEN_MOVEMENTS);

        robot.teamPiece.setPosition(SERVO_DROP_POSITION);
        sleep(SLEEP_BETWEEN_MOVEMENTS);
        robot.teamPiece.setPosition(SERVO_HOLD_POSITION);
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        task = ManeuverCrater;
    }

    void maneuverCrater() {
        robot.omniWheels.stop_and_reset();
        telemetry.addData("Currently:", "Driving to Crater");
        telemetry.update();

        while (getEncoderAverageAll() < 5000)
            robot.omniWheels.go(-ROBOT_SPEED, -ROBOT_SPEED, -ROBOT_SPEED, -ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();

        task = End;
    }
}