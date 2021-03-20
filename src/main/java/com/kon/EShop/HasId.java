package com.kon.EShop;

import org.springframework.util.Assert;

public interface HasId {
    Long getId();

    void setId(Long id);

    default boolean isNew() {
        return getId() == null;
    }

    // doesn't work for hibernate lazy proxy
    default long id() {
        Assert.notNull(getId(), "Entity must has id");
        return getId();
    }
}
