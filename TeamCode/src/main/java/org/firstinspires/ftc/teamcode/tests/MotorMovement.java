package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Disabled
@TeleOp(name = "Driving", group = "test")

public class MotorMovement extends LinearOpMode {

    private DcMotor frontRightMotor;
    private DcMotor frontLeftMotor;
    private DcMotor backRightMotor;
    private DcMotor backLeftMotor;

    //private DcMotor stoneArmMotor;

    double power = 0.5;
    int directie = 0;
    double stoneArmPower = 0.1;

    Init init = new Init();
    Motors motors = new Motors();

    public void runOpMode(){

        init.wheels();

        while(!opModeIsActive() && !isStopRequested()){
            telemetry.addData("Waiting for ", "start.");
            telemetry.update();
        }

        while(opModeIsActive()){

            motors.smoothMovement();
            //motors.stoneGrabber();

            telemetry.addData("Front Right: ", frontRightMotor.getCurrentPosition());
            telemetry.addData("Back Right: ", backRightMotor.getCurrentPosition());
            telemetry.addData("Front Left: ", frontLeftMotor.getCurrentPosition());
            telemetry.addData("Back Left: ", backLeftMotor.getCurrentPosition());
            telemetry.update();

            if(gamepad1.a)
                new Init().resetEncoders();
        }
    }

    class Init {

        public void resetEncoders(){
            frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            frontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            backLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            backRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        public void wheels() {
            frontRightMotor = hardwareMap.dcMotor.get("frontRightMotor");
            frontLeftMotor = hardwareMap.dcMotor.get("frontLeftMotor");
            backRightMotor = hardwareMap.dcMotor.get("backRightMotor");
            backLeftMotor = hardwareMap.dcMotor.get("backLeftMotor");

            //stoneArmMotor = hardwareMap.dcMotor.get("stoneArmMotor");
            //////////////////////////////////////////////////////////////////////////////

            frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            frontRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            backRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);

            //stoneArmMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            /////////////////////////////////////////////////////////////////////////////

            //stoneArmMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            /////////////////////////////////////////////////////////////////////////////

            resetEncoders();
        }

    }

    class Motors{

        public void smoothMovement(){
            if (directie==0) {
                if (gamepad1.b) {

                    directie = 1;
                    sleep(500);
                }
            }
            if(directie==1) {
                if (gamepad1.b) {
                    directie = 0;
                    sleep(500);
                }
            }
            telemetry.addData("Directie", directie);
            if(gamepad1.right_bumper){
                power=0.25;
            }
            else {
                power=0.5;
            }
            if(directie==0){
                if(gamepad1.dpad_up){
                    frontLeftMotor.setPower(power+gamepad1.right_stick_x/2);
                    frontRightMotor.setPower(power-gamepad1.right_stick_x/2);
                    backRightMotor.setPower(power-gamepad1.right_stick_x/2);
                    backLeftMotor.setPower(power+gamepad1.right_stick_x/2);
                }else
                if(gamepad1.dpad_down){
                    frontLeftMotor.setPower(-power+gamepad1.right_stick_x/2);
                    frontRightMotor.setPower(-power-gamepad1.right_stick_x/2);
                    backRightMotor.setPower(-power-gamepad1.right_stick_x/2);
                    backLeftMotor.setPower(-power+gamepad1.right_stick_x/2);
                }else
                if(gamepad1.dpad_right){
                    frontLeftMotor.setPower(power+gamepad1.right_stick_x/2);
                    frontRightMotor.setPower(-power-gamepad1.right_stick_x/2);
                    backRightMotor.setPower(power-gamepad1.right_stick_x/2);
                    backLeftMotor.setPower(-power+gamepad1.right_stick_x/2);
                }else
                if(gamepad1.dpad_left){
                    frontLeftMotor.setPower(-power+gamepad1.right_stick_x/2);
                    frontRightMotor.setPower(power-gamepad1.right_stick_x/2);
                    backRightMotor.setPower(-power-gamepad1.right_stick_x/2);
                    backLeftMotor.setPower(power+gamepad1.right_stick_x/2);
                }else{
                    frontLeftMotor.setPower(gamepad1.right_stick_x/2);
                    frontRightMotor.setPower(-gamepad1.right_stick_x/2);
                    backRightMotor.setPower(-gamepad1.right_stick_x/2);
                    backLeftMotor.setPower(gamepad1.right_stick_x/2);
                }
            }
            else if (directie==1){
                if(gamepad1.dpad_left){
                    frontLeftMotor.setPower(power+gamepad1.right_stick_x/4);
                    frontRightMotor.setPower(power-gamepad1.right_stick_x/4);
                    backRightMotor.setPower(power-gamepad1.right_stick_x/4);
                    backLeftMotor.setPower(power+gamepad1.right_stick_x/4);
                }else
                if(gamepad1.dpad_right){
                    frontLeftMotor.setPower(-power+gamepad1.right_stick_x/4);
                    frontRightMotor.setPower(-power-gamepad1.right_stick_x/4);
                    backRightMotor.setPower(-power-gamepad1.right_stick_x/4);
                    backLeftMotor.setPower(-power+gamepad1.right_stick_x/4);
                }else
                if(gamepad1.dpad_up){
                    frontLeftMotor.setPower(power+gamepad1.right_stick_x/4);
                    frontRightMotor.setPower(-power-gamepad1.right_stick_x/4);
                    backRightMotor.setPower(power-gamepad1.right_stick_x/4);
                    backLeftMotor.setPower(-power+gamepad1.right_stick_x/4);
                }else
                if(gamepad1.dpad_down){
                    frontLeftMotor.setPower(-power+gamepad1.right_stick_x/4);
                    frontRightMotor.setPower(power-gamepad1.right_stick_x/4);
                    backRightMotor.setPower(-power-gamepad1.right_stick_x/4);
                    backLeftMotor.setPower(power+gamepad1.right_stick_x/4);
                }else{
                    frontLeftMotor.setPower(gamepad1.right_stick_x/4);
                    frontRightMotor.setPower(-gamepad1.right_stick_x/4);
                    backRightMotor.setPower(-gamepad1.right_stick_x/4);
                    backLeftMotor.setPower(gamepad1.right_stick_x/4);
                }
            }


        }

        /**
        public void stoneGrabber(){

            if(gamepad2.dpad_right) {
                stoneArmPower += 0.05;
                sleep(200);
            }
            else if(gamepad2.dpad_left) {
                stoneArmPower -= 0.05;
                sleep(200);
            }

            if(gamepad2.dpad_up)
                stoneArmMotor.setPower(stoneArmPower);
            else if(gamepad2.dpad_down)
                stoneArmMotor.setPower(-stoneArmPower);
            else stoneArmMotor.setPower(0);
        }
         */
    }
}
