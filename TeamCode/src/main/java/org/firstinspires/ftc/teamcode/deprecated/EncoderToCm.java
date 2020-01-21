package org.firstinspires.ftc.teamcode.deprecated;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Disabled
@TeleOp(group = "test")
public class EncoderToCm extends LinearOpMode {

    private static final double ticksToCM = 35.1;

    private DcMotor frontRightMotor;
    private DcMotor frontLeftMotor;
    private DcMotor backLeftMotor;
    private DcMotor backRightMotor;

    Movement move = new Movement();

    @Override
    public void runOpMode() throws InterruptedException {
        new Init().wheels();

        while (!opModeIsActive() && !isStopRequested()){
            telemetry.addData("Waiting for ", "start.");
            telemetry.update();
        }

        while (opModeIsActive()){

            if (gamepad1.dpad_up)
                move.forwards(0.5);
            else if (gamepad1.dpad_down)
                move.backwards(0.5);
            else if (gamepad1.dpad_left)
                move.leftX(0.75);
            else if (gamepad1.dpad_right)
                move.rightX(0.75);
            else if (gamepad1.right_stick_x < -0.1)
                move.leftRot(0.5);
            else if (gamepad1.right_stick_x > 0.1)
                move.rightRot(0.5);
            else
                move.stop();

            telemetry.addData("Encoder ", frontRightMotor.getCurrentPosition());
            telemetry.addData("Centimeters ", new Movement().centimeters);
            telemetry.update();
        }
    }

    class Init{

        public void wheels(){
            frontLeftMotor = hardwareMap.dcMotor.get("frontLeft");
            frontRightMotor = hardwareMap.dcMotor.get("frontRight");
            backLeftMotor = hardwareMap.dcMotor.get("backLeft");
            backRightMotor = hardwareMap.dcMotor.get("backRight");

            ////////////////////////////////////////////////////

            frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            ////////////////////////////////////////////////////

            frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            ////////////////////////////////////////////////////

            frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            ///////////////////////////////////////////////////

            frontRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            backRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        }
    }

    class Movement{


        double centimeters = 0;
        long overallTicks = 0;
        long ticks = 0;
        String direction = "none";

        public void stop(){

            frontRightMotor.setPower(0);
            frontLeftMotor.setPower(0);
            backLeftMotor.setPower(0);
            backRightMotor.setPower(0);
        }

        public void forwards(double power){

            if (direction.equals("forwards")){
                if (overallTicks == 0)
                    overallTicks = frontRightMotor.getCurrentPosition();

                ticks = frontRightMotor.getCurrentPosition() - overallTicks;
                centimeters = ticks / ticksToCM;
            }
            else {
                ticks = 0;
                centimeters = 0;
                overallTicks = 0;
            }

            direction = "forwards";

            frontRightMotor.setPower(power);
            frontLeftMotor.setPower(power);
            backRightMotor.setPower(power);
            backLeftMotor.setPower(power);
        }

        public void backwards(double power){

            if (direction.equals("backwards")){
                if (overallTicks == 0)
                    overallTicks = frontRightMotor.getCurrentPosition();

                ticks = frontRightMotor.getCurrentPosition() - overallTicks;
                centimeters = ticks / ticksToCM;
            }
            else {
                ticks = 0;
                centimeters = 0;
                overallTicks = 0;
            }

            direction = "backwards";

            power *= -1;

            frontRightMotor.setPower(power);
            frontLeftMotor.setPower(power);
            backRightMotor.setPower(power);
            backLeftMotor.setPower(power);
        }

        public void leftX(double power){

            direction = "none";

            frontRightMotor.setPower(power);
            frontLeftMotor.setPower(-power);
            backRightMotor.setPower(-power);
            backLeftMotor.setPower(power);
        }

        public void rightX(double power){

            direction = "none";

            frontRightMotor.setPower(-power);
            frontLeftMotor.setPower(power);
            backRightMotor.setPower(power);
            backLeftMotor.setPower(-power);
        }

        public void leftRot(double power){

            direction = "none";

            frontRightMotor.setPower(power);
            frontLeftMotor.setPower(-power);
            backRightMotor.setPower(power);
            backLeftMotor.setPower(-power);
        }

        public void rightRot(double power){

            direction = "none";

            frontRightMotor.setPower(-power);
            frontLeftMotor.setPower(power);
            backRightMotor.setPower(-power);
            backLeftMotor.setPower(power);
        }
    }
}
