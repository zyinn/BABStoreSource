package com.sumscope.bab.store;

/**
 * Created by fan.bai on 2017/3/28.
 * 应用程序Leader全局变量，该属性由选主功能设置，当leader为true时表明多实例间该实例获取了领导权
 */
public final class ApplicationLeaderEnviornment {
    private ApplicationLeaderEnviornment() {
    }

    /**
     * 是否为领导者
     */
    private static boolean leader;

    public static boolean isLeader() {
        return leader;
    }

    public static void setLeader(boolean leader) {
        ApplicationLeaderEnviornment.leader = leader;
    }
}
