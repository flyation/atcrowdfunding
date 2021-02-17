package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.Menu;
import com.atguigu.crowd.mapper.MenuMapper;
import com.atguigu.crowd.service.api.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Menu> getAll() {
        return menuMapper.selectByExample(null);
    }

    @Override
    public void saveMenu(Menu menu) {
        menuMapper.insert(menu);
    }

    @Override
    public void updateMenu(Menu menu) {
        // 由于pid没有传，此处需要使用有选择地更新，保证pid字段不会被置空
        menuMapper.updateByPrimaryKeySelective(menu);

    }

    @Override
    public void removeMenu(Integer menuId) {
        menuMapper.deleteByPrimaryKey(menuId);
    }
}
