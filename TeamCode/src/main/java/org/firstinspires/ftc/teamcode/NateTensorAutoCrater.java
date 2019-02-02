/*
 This is a test class for programming team 11357's autonomous mode on the crater side. It may or
 may not be used during competitions.
 */

/*
 Make sure to add SLEEP_BETWEEN_MANEUVERS, to add predictability. SLEEP_BETWEEN_MANEUVERS should be
 as large as possible, while allowing ample time for the robot to finish the autonomous.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.hardware.NateHardware;

import static org.firstinspires.ftc.teamcode.NateAutoCrater.Task.TURN_TOWARDS_MINERALS;
import static org.firstinspires.ftc.teamcode.NateTensorAutoCrater.Task.*;

import java.util.List;

public class NateTensorAutoCrater extends LinearOpMode {
    NateHardware robot = new NateHardware();

    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    private static final String VUFORIA_KEY = "AfypGhD/////AAABmfthsllptEbKpJLWTp1613szVUTl5xQJQBKWoaUbDyLjOgOEF38/3fUHjGFD6pAlPmSTrW/ipYTOHpA48kfCl8o6PTWjR8X3220E5rDaANVOtluML1xOfvSl5fwbXrAtj4kv8fpf2oFyu/ZYNOE5UCFaNzldW4BkJJ9w9YG5kNz4K0So/SrzZhqxPW+XbT0eTTjyx3Uox7VqRwM/DFFAbh5kGzx8gGE+jQOAh9fVzy3rDLgQ/HQNszX7Iqwnnh/w836FuXrBbajfDun3qUQkCQKEJuaFyUEwEyZPZ+cRDym2WJigiXsw724H0pv050q0N67W+No/keaLi2mZVMuySZijkNjnsnhKrBCerryW9MJQ";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    private static final int SLEEP_BETWEEN_MOVEMENTS = 500;
    private static final double SERVO_DROP_POSITION = 0.7;
    private static final double SERVO_HOLD_POSITION = 0.0;

    enum Task {Lower, Rotate, LookAtMinerals, ManeuverRight, ManeuverLeft, ManeuverCenter, ManeuverDepot, ManeuverCrater, End}
    Task task;
    /*
    Lower- lower the robot
    Rotate- turn to face the minerals and clear hook of latch
    LookAtMinerals- use tensor to look at mineral positions
    ManeuverRight- maneuver when the block is in the right position
    ManeuverLeft- maneuver when the block is in the left position
    ManeuverCenter- maneuver when the block is in the center position
    //Maneuver[R,L,C] will return the robot to the same position, regardless of which spot the gold is in.
    ManeuverDepot- drive to the depot and deposit team marker
    ManeuverCrater- drive to the crater and park
    End- ends the program
     */

    @Override
    public void runOpMode() {

        robot.init(hardwareMap);
        task = Lower;
        robot.teamPiece.setPosition(SERVO_HOLD_POSITION);
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
    }

    void lower() {
        telemetry.addData("Currently:","Lowering");
        telemetry.addData("Distance",robot.elevator.getDistance() - 1000);
        if (robot.elevator.getDistance() > 1000) {
            robot.elevator.elevate(1);
        } else {
            robot.elevator.elevate(0);
            task = Task.Rotate;
        }
    }

    void rotate() {
        telemetry.addData("Currently:", "Rotating");
        telemetry.update();
        robot.omniWheels.goByDriver(0, -0.5, 0);
        sleep(125);
        robot.omniWheels.stop();
        task = Task.LookAtMinerals;
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
                            task = Task.ManeuverLeft;
                        } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                            telemetry.addData("Gold Mineral Position:", "Right");
                            task = Task.ManeuverRight;
                        } else {
                            telemetry.addData("Gold Mineral Position:", "Center");
                            task = Task.ManeuverCenter;
                        }
                    }
                }
                telemetry.update();
            }
        }
    }

    void maneuverLeft() {
        telemetry.addData("Currently:", "Moving Left Mineral");
        telemetry.update();
        // do some movements to move the object on the left
        task = Task.ManeuverDepot;
    }

    void maneuverRight() {
        telemetry.addData("Currently:", "Moving Right Mineral");
        telemetry.update();
        // do some movements to move the object on the right
        task = Task.ManeuverDepot;
    }

    void maneuverCenter() {
        telemetry.addData("Currently:", "Moving Center Mineral");
        telemetry.update();
        // do some movements to move the object in the center
        task = Task.ManeuverDepot;
    }

    void maneuverDepot() {
        telemetry.addData("Currently:", "Driving to Depot");
        telemetry.update();
        // do some movements to move to the depot and drop the team marker
        task = Task.ManeuverCrater;
    }

    void maneuverCrater() {
        telemetry.addData("Currently:", "Driving to Crater");
        telemetry.update();
        // do some movements to drive to the crater and park on the edge
        task = Task.End;
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
}