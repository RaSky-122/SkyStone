package org.firstinspires.ftc.teamcode.impl;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Disabled
public class Lift_Impl extends LinearOpMode {

    private DcMotor leftMotor;
    private DcMotor rightMotor;

    @Override
    public void runOpMode() throws InterruptedException {
    }

    public Lift_Impl upwards(boolean button) {

        if (button) {
            leftMotor.setPower(0.7);
            rightMotor.setPower(0.7);
        } else {
            leftMotor.setPower(0);
            rightMotor.setPower(0);
        }

        return Lift_Impl.this;
    }

    public Lift_Impl downwards(boolean button) {

        if (button) {
            leftMotor.setPower(-0.7);
            rightMotor.setPower(-0.7);
        } else {
            leftMotor.setPower(0);
            rightMotor.setPower(0);
        }

        return Lift_Impl.this;
    }

    public int encoderValue() {

        return leftMotor.getCurrentPosition();
    }

    public void initLeft(String motorName) {

        leftMotor = hardwareMap.dcMotor.get(motorName);
        leftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void initRight(String motorName) {

        rightMotor = hardwareMap.dcMotor.get(motorName);
        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void initBoth(String leftName, String rightName) {
        initLeft(leftName);
        initRight(rightName);
    }

}

