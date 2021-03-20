package com.kon.EShop.to;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentTo {
    private Long id;
    private String description;
    private Date time;
    private String userName;
    private Long parent;

}
