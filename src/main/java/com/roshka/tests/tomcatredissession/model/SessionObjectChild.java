package com.roshka.tests.tomcatredissession.model;

import java.io.Serializable;
import java.time.OffsetDateTime;

public class SessionObjectChild implements Serializable {

    private OffsetDateTime created;

    public OffsetDateTime getCreated() {
        return created;
    }

    public void setCreated(OffsetDateTime created) {
        this.created = created;
    }
}
