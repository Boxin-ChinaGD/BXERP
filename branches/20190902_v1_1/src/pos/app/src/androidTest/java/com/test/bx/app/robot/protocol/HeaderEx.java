package com.test.bx.app.robot.protocol;

import com.test.bx.app.robot.ConfigEx;

public class HeaderEx {
    protected int command;
    protected int bodyLength;

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }

    public enum EnumCommandType {
        ECT_Run("ECT_Run", 0), //
        ECT_Stop("ECT_Stop", 1), //
        ECT_DoneRun("ECT_Run", 2), //
        ECT_DoneStop("ECT_Stop", 3), //
        ECT_Logout("ECT_Logout", 4), //
        ECT_DoneLogout("ECT_DoneLogout", 5); //

        private String name;
        private int index;

        private EnumCommandType(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumCommandType ept : EnumCommandType.values()) {
                if (ept.getIndex() == index) {
                    return ept.name;
                }
            }
            return null;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIndex() {
            return index;
        }
    }

    /**
     * 调用前必须确保setBodyLength()已经被调用，以设置body的长度
     */
    public String toBufferString() {
        String s = String.format("%" + ConfigEx.commandLength + "s" + "%" + (ConfigEx.HEADER_Length - 4) + "s", command, bodyLength);
        System.out.println("s=" + s + "||legnth=" + s.length());

        return s;
    }
}
