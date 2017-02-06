package com.demo.safeBodyGuard.db.dao.model;

/**
 * Created by iml1s on 2017/2/4.
 */

public class BlackRoll
{
    public int id;

    public String name;

    public int mode;

    public BlackRoll(){}

    public BlackRoll(int id, String name, int mode)
    {
        this.id = id;
        this.name = name;
        this.mode = mode;
    }
}
