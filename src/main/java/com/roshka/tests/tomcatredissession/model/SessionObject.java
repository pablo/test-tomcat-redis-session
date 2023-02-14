package com.roshka.tests.tomcatredissession.model;

import java.io.Serializable;

public class SessionObject implements Serializable  {

    private String status;
    private int counter;

    private SessionObjectChild sessionObjectChild;

    public SessionObject()
    {
        sessionObjectChild = new SessionObjectChild();
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public SessionObjectChild getSessionObjectChild() {
        return sessionObjectChild;
    }

    public void setSessionObjectChild(SessionObjectChild sessionObjectChild) {
        this.sessionObjectChild = sessionObjectChild;
    }
}
