package org.firstinspires.ftc.teamcode.autonomous;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

public abstract class SkylerTensorAuto extends SkylerAuto {
    static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    static final String VUFORIA_KEY = "AfypGhD/////AAABmfthsllptEbKpJLWTp1613szVUTl5xQJQBKWoaUbDy" +
            "LjOgOEF38/3fUHjGFD6pAlPmSTrW/ipYTOHpA48kfCl8o6PTWjR8X3220E5rDaANVOtluML1xOfvSl5fwbXr" +
            "Atj4kv8fpf2oFyu/ZYNOE5UCFaNzldW4BkJJ9w9YG5kNz4K0So/SrzZhqxPW+XbT0eTTjyx3Uox7VqRwM/DF" +
            "FAbh5kGzx8gGE+jQOAh9fVzy3rDLgQ/HQNszX7Iqwnnh/w836FuXrBbajfDun3qUQkCQKEJuaFyUEwEyZPZ+" +
            "cRDym2WJigiXsw724H0pv050q0N67W+No/keaLi2mZVMuySZijkNjnsnhKrBCerryW9MJQ";

    VuforiaLocalizer vuforia;
    TFObjectDetector tfod;

    //ROBOT_SPEED changes the speed at which the robot moves (rotations and forward/back movement)
    //ROBOT_SPEED is negative to make positive ROBOT_SPEED = forwards and right turns
    static final double ROBOT_SPEED = -1;
    //SLEEP_BETWEEN_MOVEMENTS is how long the robot waits between maneuvers
    static final int SLEEP_BETWEEN_MOVEMENTS = 300;
    //teamPiece servo's position
    static final double SERVO_DROP_POSITION = 0.7;
    static final double SERVO_HOLD_POSITION = 0.0;
    //Used for turning the robot
    static final double TURN_COEFFICIENT = 1.2;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        robot.teamPiece.setPosition(SERVO_HOLD_POSITION);
        task = Task.LOWER;

        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) initTfod();
        else telemetry.addData("Sorry!", "This device is not compatible with TFOD");

        // Wait for the game to begin
        telemetry.addData("", "Ready to Run Autonomous");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
            // Activate Tensor Flow Object Detection.
            if (tfod != null) {
                tfod.activate();
            }

            // list tasks and names
            while (opModeIsActive()) {
                telemetry.addData("Robot", task.status);
                switch (task) {
                    case LOWER: lower(); break;
                    case ROTATE: rotate(); break;
                    case LOOK_AT_MINERALS: lookAtMinerals(); break;
                    case MANEUVER_RIGHT: maneuverRight(); break;
                    case MANEUVER_LEFT: maneuverLeft(); break;
                    case MANEUVER_CENTER: maneuverCenter(); break;
                    case MANEUVER_DEPOT: maneuverDepot(); break;
                    case MANEUVER_CRATER: maneuverCrater(); break;
                    default: break;
                }
                if (task == Task.END) break;
                telemetry.update();
            }
            robot.stop();
            telemetry.addData("Robot", "Stopping");
            telemetry.update();
        }
    }

    //Initialize the Vuforia localization engine.
    void initVuforia() {

        //Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    // Initialize the Tensor Flow Object Detection engine.
    void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }

    void lower() {
        telemetry.addData("Distance", robot.elevator.getDistance() - 1000);
        telemetry.update();
        robot.omniWheels.stop_and_reset();

        if (robot.elevator.getDistance() > 1000)
            robot.elevator.elevate(1);
        else {
            telemetry.addData("Lowering", "Done");
            telemetry.update();
            robot.elevator.elevate(0);
            sleep(SLEEP_BETWEEN_MOVEMENTS);

            robot.omniWheels.stop_and_reset();
            task = Task.ROTATE;
        }
    }

    void rotate() {
        telemetry.update();
        robot.omniWheels.stop_and_reset();

        while (robot.omniWheels.getEncoderAverage() < (950 * TURN_COEFFICIENT))
            robot.omniWheels.rotate(ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();

        while (robot.omniWheels.getEncoderAverage() < 100 )
            robot.omniWheels.go(-ROBOT_SPEED, -ROBOT_SPEED, -ROBOT_SPEED, -ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();

        task = Task.LOOK_AT_MINERALS;
    }

    void lookAtMinerals() {
        telemetry.update();
        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("Objects Detected", updatedRecognitions.size());
                telemetry.update();
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
                            task = Task.MANEUVER_LEFT;
                        } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                            telemetry.addData("Gold Mineral Position:", "Right");
                            telemetry.update();
                            task = Task.MANEUVER_RIGHT;
                        } else {
                            telemetry.addData("Gold Mineral Position:", "Center");
                            telemetry.update();
                            task = Task.MANEUVER_CENTER;
                        }
                    }
                }
            }
        }
    }

    void maneuverLeft() {
        telemetry.update();
        robot.omniWheels.stop_and_reset();

        while (robot.omniWheels.getEncoderAverage() < (400 * TURN_COEFFICIENT))
            robot.omniWheels.rotate(-ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep (SLEEP_BETWEEN_MOVEMENTS);

        while (robot.omniWheels.getEncoderAverage() < 1100)
            robot.omniWheels.go(ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (robot.omniWheels.getEncoderAverage() < (500 * TURN_COEFFICIENT))
            robot.omniWheels.rotate(ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (robot.omniWheels.getEncoderAverage() < 500)
            robot.omniWheels.go(ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (robot.omniWheels.getEncoderAverage() < 500)
            robot.omniWheels.go(-ROBOT_SPEED, -ROBOT_SPEED, -ROBOT_SPEED, -ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (robot.omniWheels.getEncoderAverage() < (500 * TURN_COEFFICIENT))
            robot.omniWheels.rotate(-ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (robot.omniWheels.getEncoderAverage() < 1100)
            robot.omniWheels.go(-ROBOT_SPEED, -ROBOT_SPEED, -ROBOT_SPEED, -ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (robot.omniWheels.getEncoderAverage() < (400 * TURN_COEFFICIENT))
            robot.omniWheels.rotate(ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        task = Task.MANEUVER_DEPOT;
    }

    void maneuverRight() {
        telemetry.update();
        robot.omniWheels.stop_and_reset();

        while (robot.omniWheels.getEncoderAverage() < (400 * TURN_COEFFICIENT))
            robot.omniWheels.rotate(ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep (SLEEP_BETWEEN_MOVEMENTS);

        while (robot.omniWheels.getEncoderAverage() < 1100)
            robot.omniWheels.go(ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (robot.omniWheels.getEncoderAverage() < (500 * TURN_COEFFICIENT))
            robot.omniWheels.rotate(-ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (robot.omniWheels.getEncoderAverage() < 500)
            robot.omniWheels.go(ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (robot.omniWheels.getEncoderAverage() < 500)
            robot.omniWheels.go(-ROBOT_SPEED, -ROBOT_SPEED, -ROBOT_SPEED, -ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (robot.omniWheels.getEncoderAverage() < (500 * TURN_COEFFICIENT))
            robot.omniWheels.rotate(ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (robot.omniWheels.getEncoderAverage() < 1100)
            robot.omniWheels.go(-ROBOT_SPEED, -ROBOT_SPEED, -ROBOT_SPEED, -ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (robot.omniWheels.getEncoderAverage() < (400 * TURN_COEFFICIENT))
            robot.omniWheels.rotate(-ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        task = Task.MANEUVER_DEPOT;
    }

    void maneuverCenter() {
        telemetry
                .update();
        robot.omniWheels.stop_and_reset();

        while (robot.omniWheels.getEncoderAverage() < 1500)
            robot.omniWheels.go(ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (robot.omniWheels.getEncoderAverage() < 1500)
            robot.omniWheels.go(-ROBOT_SPEED, -ROBOT_SPEED, -ROBOT_SPEED, -ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        task = Task.MANEUVER_DEPOT;
    }

    void maneuverCrater() {
        robot.omniWheels.stop_and_reset();
        telemetry.addData("Currently:", "Driving to Crater");
        telemetry.update();

        while (robot.omniWheels.getEncoderAverage() < 4000)
            robot.omniWheels.go(-ROBOT_SPEED, -ROBOT_SPEED, -ROBOT_SPEED, -ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();

        task = Task.END;
    }

    abstract void maneuverDepot();
}

