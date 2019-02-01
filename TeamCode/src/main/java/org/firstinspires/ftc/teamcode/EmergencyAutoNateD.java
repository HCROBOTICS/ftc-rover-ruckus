



package org.firstinspires.ftc.teamcode;

        import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
        import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

        import org.firstinspires.ftc.teamcode.hardware.NateHardware;

        import static org.firstinspires.ftc.teamcode.EmergencyAutoNateD.Task.*;

@Autonomous(name="EmergencyAutoNateD", group ="Nate")
public class EmergencyAutoNateD extends LinearOpMode {
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

        //elevator
        robot.motorElevator.setPower(1);
        sleep(13000);
        robot.motorElevator.setPower(0);
        sleep(SLEEP_BETWEEN_TASKS);

         //turn once unlatched
         while (robot.lf.getCurrentPosition() < 1100) {robot.omniWheels.rotate(-0.25);}
         robot.omniWheels.stop();
         sleep(SLEEP_BETWEEN_TASKS);

         //drive forward
        robot.omniWheels.goByDriver(0,-0.5,0);
        sleep(1200);
        robot.omniWheels.stop();

        //drop team piece
        robot.teamPiece.setPosition(SERVO_DROP_POSITION);
        sleep(SLEEP_BETWEEN_TASKS);

        //turn so back faces
        robot.omniWheels.goByDriver(0,0,0.5);
        sleep(350);
        robot.omniWheels.stop();
        sleep(SLEEP_BETWEEN_TASKS);

        //drive backward
        robot.omniWheels.goByDriver(0,0.5,0);
        sleep(2300);
        robot.omniWheels.stop();

        task = END;
    }
}