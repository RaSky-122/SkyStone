package org.firstinspires.ftc.teamcode.main.driving;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.TimeUnit;

@TeleOp(name = "Main", group = "main")

public class Main_TeleOp extends LinearOpMode {

    private DcMotor frontRightMotor;
    private DcMotor frontLeftMotor;
    private DcMotor backRightMotor;
    private DcMotor backLeftMotor;

    private DcMotor liftMotor1;
    private DcMotor liftMotor2;

    private Servo autoServo1;
    private Servo autoServo2;

    private Servo gripperServo;
    private CRServo extenderServo;

    Init init = new Init();
    Motors motors = new Motors();

    int gripperPosition=0;

    @Override
    public void runOpMode() throws InterruptedException {
        init.wheels();
        init.lift();
        init.servo();
        init.crServo();

        while(!opModeIsActive() && !isStopRequested()){
            telemetry.addData("Waiting for ", "start.");
            telemetry.update();
        }

        while(opModeIsActive()){

            telemetry.addData("Lift Motor 1: ", liftMotor1.getCurrentPosition());
            telemetry.addData("Lift Motor 2: ", liftMotor2.getCurrentPosition());
            telemetry.addData("~~~~~~~~~~~~~~~~~~~~~~~~~~~~", "");
            telemetry.addData("Time button is pressed ", new Motors().timePressed);
            telemetry.update();

            motors.smoothMovement();
            motors.liftMovement();
            motors.autoServo();
            motors.gripperServo();
            motors.extenderServo();
        }
    }
    class Init {

        public void wheels() {
            frontRightMotor = hardwareMap.dcMotor.get("frontRight");
            frontLeftMotor = hardwareMap.dcMotor.get("frontLeft");
            backRightMotor = hardwareMap.dcMotor.get("backRight");
            backLeftMotor = hardwareMap.dcMotor.get("backLeft");

            //////////////////////////////////////////////////////////////////////////////

            frontLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            backLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

            /////////////////////////////////////////////////////////////////////////////

            frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            //////////////////////////////////////////////////////////////////////////////

            frontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            backLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            backRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        private void lift(){
            liftMotor1 = hardwareMap.dcMotor.get("lift1");
            liftMotor2 = hardwareMap.dcMotor.get("lift2");

            /////////////////////////////////////////////////////////////////////////////

            liftMotor1.setDirection(DcMotorSimple.Direction.FORWARD);
            liftMotor2.setDirection(DcMotorSimple.Direction.REVERSE);
            /////////////////////////////////////////////////////////////////////////////

            liftMotor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            liftMotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            /////////////////////////////////////////////////////////////////////////////

            liftMotor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            liftMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            /////////////////////////////////////////////////////////////////////////////

            liftMotor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            liftMotor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        private void servo(){
            autoServo1 = hardwareMap.servo.get("autoServo1");
            autoServo2 = hardwareMap.servo.get("autoServo2");
            gripperServo = hardwareMap.servo.get("gripperServo");

            gripperServo.setDirection(Servo.Direction.FORWARD);
            autoServo1.setDirection(Servo.Direction.REVERSE);
            autoServo2.setDirection(Servo.Direction.FORWARD);

            autoServo1.scaleRange(1-0.66, 1);
            autoServo2.scaleRange(0, 0.66);

            autoServo1.setPosition(0);
            autoServo2.setPosition(0);
        }

        private void crServo(){
            extenderServo = hardwareMap.crservo.get("extenderServo");
        }
    }

    class Motors{

        int viteza = 0;
        double power = 0.8;
        double halfPower = 0.5;
        double stoneArmPower = 0.5;

        private ElapsedTime timer = new ElapsedTime();
        boolean buttonIsHeld = false;
        char buttonPressed = 'f';

        double timePressed = 0;

        private void smoothMovement(){

            if(gamepad1.right_bumper && viteza == 0){
                viteza = 1;
                sleep(200);
            }
            else
                if(gamepad1.right_bumper && viteza == 1) {
                    viteza = 0;
                    sleep(200);
                }

            if(viteza == 0){
                if(gamepad1.dpad_up){
                    frontLeftMotor.setPower(power-gamepad1.right_stick_x/2);
                    frontRightMotor.setPower(power+gamepad1.right_stick_x/2);
                    backRightMotor.setPower(power+gamepad1.right_stick_x/2);
                    backLeftMotor.setPower(power-gamepad1.right_stick_x/2);
                }else
                if(gamepad1.dpad_down){
                    frontLeftMotor.setPower(-power-gamepad1.right_stick_x/2);
                    frontRightMotor.setPower(-power+gamepad1.right_stick_x/2);
                    backRightMotor.setPower(-power+gamepad1.right_stick_x/2);
                    backLeftMotor.setPower(-power-gamepad1.right_stick_x/2);
                }else
                if(gamepad1.dpad_right){
                    frontLeftMotor.setPower(power-gamepad1.right_stick_x/2);
                    frontRightMotor.setPower(-power+gamepad1.right_stick_x/2);
                    backRightMotor.setPower(power+gamepad1.right_stick_x/2);
                    backLeftMotor.setPower(-power-gamepad1.right_stick_x/2);
                }else
                if(gamepad1.dpad_left){
                    frontLeftMotor.setPower(-power-gamepad1.right_stick_x/2);
                    frontRightMotor.setPower(power+gamepad1.right_stick_x/2);
                    backRightMotor.setPower(-power+gamepad1.right_stick_x/2);
                    backLeftMotor.setPower(power-gamepad1.right_stick_x/2);
                }else{
                    frontLeftMotor.setPower(-gamepad1.right_stick_x+frontLeftMotor.getPower()/4);
                    frontRightMotor.setPower(gamepad1.right_stick_x+frontRightMotor.getPower()/4);
                    backRightMotor.setPower(gamepad1.right_stick_x+backRightMotor.getPower()/4);
                    backLeftMotor.setPower(-gamepad1.right_stick_x+backLeftMotor.getPower()/4);
                }
            }
            else if(viteza == 1){
                if(gamepad1.dpad_up){
                    frontLeftMotor.setPower(halfPower-gamepad1.right_stick_x/2);
                    frontRightMotor.setPower(halfPower+gamepad1.right_stick_x/2);
                    backRightMotor.setPower(halfPower+gamepad1.right_stick_x/2);
                    backLeftMotor.setPower(halfPower-gamepad1.right_stick_x/2);
                }else
                if(gamepad1.dpad_down){
                    frontLeftMotor.setPower(-halfPower-gamepad1.right_stick_x/2);
                    frontRightMotor.setPower(-halfPower+gamepad1.right_stick_x/2);
                    backRightMotor.setPower(-halfPower+gamepad1.right_stick_x/2);
                    backLeftMotor.setPower(-halfPower-gamepad1.right_stick_x/2);
                }else
                if(gamepad1.dpad_right){
                    frontLeftMotor.setPower(halfPower-gamepad1.right_stick_x/2);
                    frontRightMotor.setPower(-halfPower+gamepad1.right_stick_x/2);
                    backRightMotor.setPower(halfPower+gamepad1.right_stick_x/2);
                    backLeftMotor.setPower(-halfPower-gamepad1.right_stick_x/2);
                }else
                if(gamepad1.dpad_left){
                    frontLeftMotor.setPower(-halfPower-gamepad1.right_stick_x/2);
                    frontRightMotor.setPower(halfPower+gamepad1.right_stick_x/2);
                    backRightMotor.setPower(-halfPower+gamepad1.right_stick_x/2);
                    backLeftMotor.setPower(halfPower-gamepad1.right_stick_x/2);
                }else{
                    frontLeftMotor.setPower(-gamepad1.right_stick_x/2 + frontLeftMotor.getPower()/2);
                    frontRightMotor.setPower(gamepad1.right_stick_x/2 + frontRightMotor.getPower()/2);
                    backRightMotor.setPower(gamepad1.right_stick_x/2 + backRightMotor.getPower()/2);
                    backLeftMotor.setPower(-gamepad1.right_stick_x/2 + backLeftMotor.getPower()/2);
                }
            }


        }

        private void liftMovement(){

            if(gamepad2.right_bumper){
                liftMotor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                liftMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                liftMotor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                liftMotor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }

            if(gamepad2.dpad_up && (liftMotor1.getCurrentPosition() <= 19400 || gamepad2.left_bumper)) {
                if(liftMotor1.getCurrentPosition()>liftMotor2.getCurrentPosition()){
                    liftMotor1.setPower(1 - 0.1);
                    liftMotor2.setPower(1);
                }
                else
                    if(liftMotor2.getCurrentPosition()>liftMotor1.getCurrentPosition()){
                        liftMotor1.setPower(1);
                        liftMotor2.setPower(1 - 0.1);
                    }
                    else{
                        liftMotor1.setPower(1);
                        liftMotor2.setPower(1);
                    }

            }
            else if(gamepad2.dpad_down && (liftMotor1.getCurrentPosition() >= 50 || gamepad2.left_bumper)) {
                if(liftMotor1.getCurrentPosition()<liftMotor2.getCurrentPosition()) {
                    liftMotor1.setPower(-1 + 0.1);
                    liftMotor2.setPower(-1);
                }
                else
                    if(liftMotor2.getCurrentPosition()<liftMotor1.getCurrentPosition()){
                        liftMotor1.setPower(-1);
                        liftMotor2.setPower(-1 + 0.1);
                    }
            }
            else {
                liftMotor1.setPower(0);
                liftMotor2.setPower(0);
            }
        }

        private void autoServo(){

            boolean leftServo = gamepad1.a;
            boolean rightServo = gamepad1.b;
            char leftServoButton = 'a';
            char rightServoButton = 'b';

            if(leftServo && ((timer.now(TimeUnit.MILLISECONDS) - timePressed < 300) || timePressed == 0)){
                buttonPressed = leftServoButton;
                buttonIsHeld = true;

                if(timePressed == 0)
                    timePressed = timer.now(TimeUnit.MILLISECONDS);
            }
            else if(rightServo && ((timer.now(TimeUnit.MILLISECONDS) - timePressed < 300) || timePressed == 0)){
                buttonPressed = rightServoButton;
                buttonIsHeld = true;

                if(timePressed == 0)
                    timePressed = timer.now(TimeUnit.MILLISECONDS);
            }
            else if(gamepad1.y && ((timer.now(TimeUnit.MILLISECONDS) - timePressed < 300) || timePressed == 0)){
                buttonIsHeld = true;
                buttonPressed = 'y';
            }
            else if(buttonIsHeld){
                if(buttonPressed == 'y'){
                    if(autoServo1.getPosition() <= 0.1 || autoServo2.getPosition() <= 0.9) {
                        autoServo1.setPosition(1);
                        autoServo2.setPosition(1);
                    }
                    else {
                        autoServo1.setPosition(0);
                        autoServo2.setPosition(0);
                    }
                }
                else if(timer.now(TimeUnit.MILLISECONDS) - timePressed < 300){
                    if(buttonPressed == leftServoButton){
                        if(autoServo1.getPosition() <= 0.1)
                            autoServo1.setPosition(0.82);
                        else {
                            autoServo1.setPosition(0);
                            autoServo2.setPosition(0);
                        }
                    }
                    else if(buttonPressed == rightServoButton){
                        if(autoServo2.getPosition() <= 0.1)
                            autoServo2.setPosition(0.82);
                        else {
                            autoServo1.setPosition(0);
                            autoServo2.setPosition(0);
                        }
                    }
                }

                buttonIsHeld = false;
                buttonPressed = 'f';
                timePressed = 0;
            }
        }

        private void gripperServo(){
            if(gamepad1.x && gripperPosition  == 0){
                gripperServo.setPosition(0.85);
                gripperPosition = 1;
                sleep(200);
            }
            else if(gamepad1.x && gripperPosition == 1) {
                gripperServo.setPosition(0.45);
                gripperPosition = 0;
                sleep(200);
            }

            if(gamepad1.left_bumper){
                gripperServo.setPosition(0.2);
                sleep(200);
            }
        }

        private void extenderServo(){
            if(gamepad2.a){
                extenderServo.setPower(-1);
            }
            else
                if(gamepad2.y){
                    extenderServo.setPower(1);
                }
                else
                    extenderServo.setPower(0);

        }
    }

}
