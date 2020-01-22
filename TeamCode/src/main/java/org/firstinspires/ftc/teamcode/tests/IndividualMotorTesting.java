package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Test Individual Motor", group = "test")
public class IndividualMotorTesting extends LinearOpMode {

    private DcMotor motor;

    double power = 0.1;

    @Override
    public void runOpMode() throws InterruptedException {

        Init();

        while(!opModeIsActive() && !isStopRequested()){
            telemetry.addData("Waiting for ", "start.");
            telemetry.update();
        }

        while(opModeIsActive() && !isStopRequested()){

            move(power);
            speed();
            telemetry.addData("Power ", power);
            telemetry.update();
        }
    }

    private void Init(){

        motor = hardwareMap.dcMotor.get("motor");

        motor.setDirection(DcMotorSimple.Direction.FORWARD);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    private void move(double power){
        if(gamepad1.dpad_up){
            motor.setPower(power);
        }
        else if(gamepad1.dpad_down){
            motor.setPower(-power);
        }
        else {
            motor.setPower(0);
        }
    }

    private void speed(){
        if(gamepad1.dpad_right){
            power += 0.05;
        }
        else if(gamepad1.dpad_left){
            power -= 0.05;
        }

        sleep(200);
    }
}
