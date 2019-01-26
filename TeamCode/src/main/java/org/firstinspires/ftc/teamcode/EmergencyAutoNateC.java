package org.firstinspires.ftc.teamcode;

        import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
        import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

        import org.firstinspires.ftc.teamcode.hardware.NateHardware;

        import static org.firstinspires.ftc.teamcode.EmergencyAutoNateC.Task.*;

@Autonomous(name="EmergencyAutoNateC", group ="Nate")
public class EmergencyAutoNateC extends LinearOpMode {
    NateHardware robot = new NateHardware();

    public static final double SERVO_DROP_POSITION = .7;
    public static final double SERVO_HOLD_POSITION = 0;
    public static final int SLEEP_BETWEEN_TASKS = 500;

    enum Task {TURN_TOWARDS_MINERALS, END}
    Task task;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        task = TURN_TOWARDS_MINERALS;
        robot.teamPiece.setPosition(SERVO_HOLD_POSITION);
        telemetry.addData("Robot", "Ready");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            switch (task) {

                case TURN_TOWARDS_MINERALS: turnTowardsMinerals(); break;
                default: break;
            }

            telemetry.update();
        }

        robot.stop();
        telemetry.addData("Robot", "Stopping");
        telemetry.update();
    }

    void turnTowardsMinerals() {
        telemetry.addData("Robot", "Turning towards the Minerals");
        telemetry.update();
        robot.omniWheels.reset();

        //drive forward
        robot.omniWheels.goByDriver(0,-0.5,0);
        sleep(500);
        robot.omniWheels.stop();
        sleep(SLEEP_BETWEEN_TASKS);

        //drive back
        robot.omniWheels.goByDriver(0,.5,0);
        sleep(300);
        robot.omniWheels.stop();
        sleep(SLEEP_BETWEEN_TASKS);

        //left turn #1
        robot.omniWheels.goByDriver(0,0,-0.5);
        sleep(450);
        robot.omniWheels.stop();
        sleep(SLEEP_BETWEEN_TASKS);

        //drive forward a bit
        robot.omniWheels.goByDriver(0,-0.5,0);
        sleep(900);
        robot.omniWheels.stop();
        sleep(SLEEP_BETWEEN_TASKS);

        //left turn #2
        robot.omniWheels.goByDriver(0,0,-0.5);
        sleep(400);
        robot.omniWheels.stop();
        sleep(SLEEP_BETWEEN_TASKS);

        //drive forward to depot
        robot.omniWheels.goByDriver(0,-0.5,0);
        sleep(1000);
        robot.omniWheels.stop();

        //drop team piece
        robot.teamPiece.setPosition(SERVO_DROP_POSITION);
        sleep(SLEEP_BETWEEN_TASKS);

        //drive backwards to crater
        robot.omniWheels.goByDriver(0,0.5,0);
        sleep (1700);
        robot.omniWheels.stop();


        task = END;
    }
}