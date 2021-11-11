package com.test.bx.app.robot.protocol;


import com.test.bx.app.robot.Config;

/**
 * CS间收发信息的格式：Header+Body <br />
 * 先收到Header，知道Body长度后，再收Body，将Body转化为相应的机器餐、机器蜜 <br />
 */
public class Header {
    protected int bodyLength;

    protected int type;

    protected int command;

    protected int notice;

    protected int activitySequence;

    public String getPosName() {
        return posName;
    }

    public void setPosName(String posName) {
        this.posName = posName;
    }

    protected String posName;

    protected String dataFromServerToClient;

    public enum EnumCommandType {
        ECT_PosLogin("ECT_PosLogin", 0), //
        ECT_SyncData("ECT_SyncData", 1), //
        ECT_CreateRetailTrade("ECT_CreateRetailTrade", 2), //
        ECT_RunProgram("ECT_RunProgram", 3), //
        ECT_PosCloseConnection("ECT_PosCloseConnection", 4), //
        ECT_DonePosLogin("ECT_DonePosLogin", 5), //
        ECT_DoneSyncData("ECT_DoneSyncData", 6), //
        ECT_DoneCreateRetailTrade("ECT_DoneCreateRetailTrade", 7), //
        ECT_DoneStartProgram("ECT_DoneStartProgram", 8), //
        ECT_CloseConnected("ECT_CloseConnected", 9); //

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

    // BaseBuffer类型
    public enum EnumBodyType {
        EBT_CreateCommodity("EBT_CreateCommodity", 0), //
        EBT_CreatePurchasingOrder("EBT_CreatePurchasingOrder", 1), //
        EBT_ApprovePurchasingOrder("EBT_ApprovePurchasingOrder", 2), //
        EBT_CreateWarehousing("EBT_ApprovePurchasingOrder", 3), //
        EBT_ApproveWarehousing("EBT_ApproveWarehousing", 4); //

        private String name;
        private int index;

        private EnumBodyType(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumBodyType ept : EnumBodyType.values()) {
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

    public int getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public String getDataFromServerToClient() {
        return dataFromServerToClient;
    }

    public void setDataFromServerToClient(String dataFromServerToClient) {
        this.dataFromServerToClient = dataFromServerToClient;
    }

    public int getActivitySequence() {
        return activitySequence;
    }

    public void setActivitySequence(int activitySequence) {
        this.activitySequence = activitySequence;
    }

    /** 调用前必须确保setBodyLength()已经被调用，以设置body的长度 */
    public String toBufferString() {
        if(dataFromServerToClient == null) {
            dataFromServerToClient = "";
        }
        String s = String.format("   %s" + "%" + 4 + "s" + "%s" + "%" + 14 + "s" + "%" + (Config.HEADER_Length - 4 - 14 - 4 - 4) + "s", command, activitySequence, posName, dataFromServerToClient, bodyLength);
        System.out.println("s=" + s + "||legnth=" + s.length());

        return s;
    }
}
