package com.session.dgjx.event;

/**
 * Created by user on 2016-09-13.
 */
public class EventMsg {
    private String smsg;
    private int imsg;

    public EventMsg(String smsg, int imsg) {
        this.smsg = smsg;
        this.imsg = imsg;
    }

    public EventMsg(int imsg) {
        this.imsg = imsg;
    }

    public String getSmsg() {
        return smsg;
    }

    public void setSmsg(String smsg) {
        this.smsg = smsg;
    }

    public int getImsg() {
        return imsg;
    }

    public void setImsg(int imsg) {
        this.imsg = imsg;
    }
}
