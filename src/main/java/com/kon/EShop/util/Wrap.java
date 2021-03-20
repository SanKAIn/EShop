package com.kon.EShop.util;

import com.kon.EShop.to.CommentTo;

import java.util.ArrayList;
import java.util.List;

public class Wrap {
    List<CommentTo> list;

    public Wrap() {
        list = new ArrayList<>();
    }

    public Wrap(List<CommentTo> list) {
        this.list = list;
    }

    public List<CommentTo> getList() {
        return list;
    }

    public void setList(List<CommentTo> list) {
        this.list = list;
    }
}
