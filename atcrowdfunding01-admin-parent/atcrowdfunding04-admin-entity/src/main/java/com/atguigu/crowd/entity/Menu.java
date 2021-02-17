package com.atguigu.crowd.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
    private Integer id;

    private Integer pid;

    private String name;

    private String url;

    private String icon;

    private List<Menu> children = new ArrayList<>();

    private Boolean open = true;
}
