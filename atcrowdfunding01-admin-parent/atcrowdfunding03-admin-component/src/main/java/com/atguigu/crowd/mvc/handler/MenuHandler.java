package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Menu;
import com.atguigu.crowd.service.api.MenuService;
import com.atguigu.crowd.util.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@RestController
public class MenuHandler {

    private Logger logger = LoggerFactory.getLogger(MenuHandler.class);

    @Autowired
    private MenuService menuService;

    /**
     * 使用双重嵌套循环构造树结构，时间复杂度太高，T(n)=O(n^2)
     */
    @RequestMapping("/menu/get/whole/tree/old.json")
//    @ResponseBody
    public ResultEntity<Menu> getWholeTreeOld() {
        // 查出所有menu对象
        List<Menu> menuList = menuService.getAll();
        // 记录根节点
        Menu root = null;
        // 遍历menuList
        for (Menu menu : menuList) {
            Integer pid = menu.getPid();
            if (pid == null) {
                // 无父节点，则为根节点
                root = menu;
                continue;
            }
            // 有父节点，尝试寻找父节点，建立父子关系
            for (Menu maybeFather : menuList) {
                if (Objects.equals(pid, maybeFather.getId())) {
                    // 找到父节点
                    maybeFather.getChildren().add(menu);
                    // 一个子节点只有一个父节点，找到即可跳出循环
                    break;
                }
            }
        }
        return ResultEntity.successWithData(root);
    }

    /**
     * 在第一次遍历时用一个hashMap记录id和menu的对应关系，降低时间复杂度（使用hashMap空间换时间），T(n)=O(n)
     */
    @RequestMapping("/menu/get/whole/tree.json")
//    @ResponseBody
    public ResultEntity<Menu> getWholeTree() {
        // 查出所有menu对象
        List<Menu> menuList = menuService.getAll();
        // 记录根节点
        Menu root = null;
        // 用一个map记录id和menu的对应关系
        HashMap<Integer, Menu> menuMap = new HashMap<>();
        // 第一次遍历，用于记录menu的id与自身的对应关系，方便第二次循环直接通过pid获取menu本身
        for (Menu menu : menuList) {
            menuMap.put(menu.getId(), menu);
        }
        // 第二次遍历，查找根节点，以及建立父子关系
        for (Menu menu : menuList) {
            Integer pid = menu.getPid();
            if (pid == null) {
                // 无父节点，则为根节点
                root = menu;
                continue;
            }
            // 有父节点，则建立父子关系
            Menu father = menuMap.get(pid);
            father.getChildren().add(menu);
        }
        return ResultEntity.successWithData(root);
    }

    /**
     * 保存菜单
     */
    @RequestMapping("/menu/save.json")
//    @ResponseBody
    public ResultEntity saveMenu(Menu menu) {
        menuService.saveMenu(menu);
        return ResultEntity.successWithoutData();
    }

    /**
     * 更新菜单
     */
    @RequestMapping("/menu/update.json")
//    @ResponseBody
    public ResultEntity updateMenu(Menu menu) {
        menuService.updateMenu(menu);
        return ResultEntity.successWithoutData();
    }

    /**
     * 删除菜单
     */
    @RequestMapping("/menu/remove.json")
//    @ResponseBody
    public ResultEntity removeMenu(Integer menuId) {
        menuService.removeMenu(menuId);
        return ResultEntity.successWithoutData();
    }
}
