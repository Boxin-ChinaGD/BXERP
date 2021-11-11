package com.test.bx.app.robot.program;

import java.util.Date;

/**
 * Created by WPNA on 2019/10/12.
 */

public class Vip extends Program {
    @Override
    protected void generateReport() {

    }

    @Override
    public boolean run(Date currentDatetime, StringBuilder sbError, Program[] programs, boolean bRunInRandomMode) throws Exception {
        return false;
    }

    @Override
    protected boolean canRunNow(Date d) {
        return false;
    }

    @Override
    public void resetForNextDay() {

    }
}
