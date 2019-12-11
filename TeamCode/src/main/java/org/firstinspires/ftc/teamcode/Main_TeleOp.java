package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Main", group = "main")

public class Main_TeleOp extends LinearOpMode {

    private DcMotor frontRightMotor;
    private DcMotor frontLeftMotor;
    private DcMotor backRightMotor;
    private DcMotor backLeftMotor;
    private DcMotor stoneArmMotor;
    private DcMotor liftMotor;
    private DcMotor collectLeft;
    private DcMotor collectRight;

    private Servo stoneGrabServo;
    private CRServo craneServo;

    Init init = new Init();
    Motors motors = new Motors();

    @Override
    public void runOpMode() throws InterruptedException {
        init.wheels();
        init.stoneStuff();

        while(!opModeIsActive() && !isStopRequested()){
            telemetry.addData("Waiting for ", "start.");
            telemetry.update();
        }

        while(opModeIsActive()){
            motors.smoothMovement();
            motors.stoneGrabber();
            motors.stoneGrabberServo();
        }
    }
    class Init {

        public void wheels() {
            frontRightMotor = hardwareMap.dcMotor.get("frontRightMotor");
            frontLeftMotor = hardwareMap.dcMotor.get("frontLeftMotor");
            backRightMotor = hardwareMap.dcMotor.get("backRightMotor");
            backLeftMotor = hardwareMap.dcMotor.get("backLeftMotor");

            //////////////////////////////////////////////////////////////////////////////

            frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            frontRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            backRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);

            /////////////////////////////////////////////////////////////////////////////

            frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        public void stoneStuff(){
            stoneArmMotor = hardwareMap.dcMotor.get("stoneArmMotor");
            stoneGrabServo = hardwareMap.servo.get("stoneGrabServo");
            liftMotor = hardwareMap.dcMotor.get("liftMotor");
            craneServo = hardwareMap.crservo.get("craneServo");
            collectLeft = hardwareMap.dcMotor.get("collectLeft");
            collectRight = hardwareMap.dcMotor.get("collectRight");

            //////////////////////////////////////////////////////////////////////////////

            stoneArmMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            liftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            collectRight.setDirection(DcMotorSimple.Direction.FORWARD);
            collectLeft.setDirection(DcMotorSimple.Direction.REVERSE);
            /////////////////////////////////////////////////////////////////////////////

            stoneArmMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

    }

    class Motors{

        int directie = 0;
        double power = 0.5;
        double stoneArmPower = 0.5;

        double servoCurrentPosition;

        public void smoothMovement(){
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

        public void stoneGrabber(){

            if(gamepad2.right_bumper) {
                stoneArmPower += 0.05;
                sleep(200);
            }
            else if(gamepad2.left_bumper) {
                stoneArmPower -= 0.05;
                sleep(200);
            }

            if(gamepad2.dpad_up) {
                stoneArmMotor.setPower(stoneArmPower);
                liftMotor.setPower(stoneArmPower);
            }
            else if(gamepad2.dpad_down) {
                stoneArmMotor.setPower(-stoneArmPower);
                liftMotor.setPower(-stoneArmPower);
            }
            else {
                stoneArmMotor.setPower(0);
                liftMotor.setPower(0);
            }
        }

        public void stoneGrabberServo(){
            servoCurrentPosition = stoneGrabServo.getPosition();

            if(gamepad2.a && servoCurrentPosition <= 0.9){
                stoneGrabServo.setPosition(servoCurrentPosition + 0.01);
                sleep(10);
            }
            else if(gamepad2.y && servoCurrentPosition >= 0.1){
                stoneGrabServo.setPosition(servoCurrentPosition - 0.01);
                sleep(10);
            }
        }
        public void craneMovement(){
            if(gamepad2.dpad_right)
                craneServo.setPower(stoneArmPower);
            else
                if(gamepad2.dpad_left)
                    craneServo.setPower(-stoneArmPower);
        }
        public void collector(){
            if(gamepad2.x){
                collectLeft.setPower(0.8);
                collectRight.setPower(0.8);
            }
            else
                if(gamepad2.b){
                    collectRight.setPower(-0.8);
                    collectLeft.setPower(-0.8 );
                }
        }
    }

}
