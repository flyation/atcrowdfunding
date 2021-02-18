/**
 * 生成分页
 */
function generatePage() {
    var pageInfo = getPageInfoRemote();
    fillTableBody(pageInfo);
    generateNavigator(pageInfo);
}

/**
 * 获取远程分页数据
 * @returns {pageInfo|null}
 */
function getPageInfoRemote() {
    // 发送ajax请求
    var ajaxResult = $.ajax({
        url: 'role/get/info.json',
        type: 'post',
        async: false,
        data: {
            pageNum: window.pageNum,
            pageSize: window.pageSize,
            keyword: window.keyword
        },
        dataType: 'json'
    })

    // 请求失败
    if (ajaxResult.status !== 200) {
        layer.msg('服务端调用失败！响应状态码=' + ajaxResult.status + '，说明信息=' + ajaxResult.responseText)
        return null;
    }

    // 请求成功
    var resultEntity = ajaxResult.responseJSON;
    var result = resultEntity.result;
    if (result === 'FAILED') {
        layer.msg(result.message)
        return null;
    }
    // 确认result成功后返回pageInfo
    return resultEntity.data;
}

/**
 * 填充表格
 */
function fillTableBody(pageInfo) {
    // 判断pageInfo是否有效
    if (pageInfo === null || pageInfo === undefined || pageInfo.size === 0) {
        // 无分页数据时，清除旧数据
        $('#rolePageBody').empty();
        $('#rolePageBody').append('<tr><td colspan="4">抱歉！没有查到数据</td></tr>')
        return;
    }

    // 清除旧数据
    $('#rolePageBody').empty();
    // 遍历pageInfo.list，填充新数据
    for (let i = 0; i < pageInfo.size; i++) {
        let role = pageInfo.list[i];
        $('#rolePageBody').append(`
                            <tr>
                                <td>${i + 1}</td>
                                <td><input type="checkbox" id="${role.id}" class="itemBox"></td>
                                <td>${role.name}</td>
                                <td>
                                    <button type="button" id="${role.id}" class="btn btn-success btn-xs checkBtn"><i class=" glyphicon glyphicon-check"></i></button>
                                    <button type="button" id="${role.id}" class="btn btn-primary btn-xs showEditModalBtn"><i class=" glyphicon glyphicon-pencil"></i></button>
                                    <button type="button" id="${role.id}" class="btn btn-danger btn-xs removeBtn"><i class=" glyphicon glyphicon-remove"></i></button>
                                </td>
                            </tr>`)
    }
}

/**
 * 生成分页导航条
 */
function generateNavigator(pageInfo) {
    // 判断pageInfo是否有效
    if (pageInfo === null || pageInfo === undefined || pageInfo.size === 0) {
        // 无分页数据时，清除分页条
        $('#Pagination').empty();
        return;
    }

    var total = pageInfo.total;
    var properties = {
        num_edge_entries: 3,                //边缘页数
        num_display_entries: 5,             //主体页数
        items_per_page: pageInfo.pageSize,  //每页显示数量
        current_page: pageInfo.pageNum - 1, // pagination内部使用page_index来管理页码，所以这里要-1
        prev_text: '上一页',
        next_text: '下一页',
        callback: pageselectCallback
    };
    // 生成页码导航
    $("#Pagination").pagination(total, properties);
}

/**
 * 翻页回调
 */
function pageselectCallback(page_index, jq){
    // 根据page_index计算pageNum
    window.pageNum = page_index + 1;
    // 调用分页函数
    generatePage();
    // 由于每一个页码都是超链接，所以在最后取消超链接的默认行为
    return false;
}

/**
 * 删除确认模态框
 */
function showConfirmModal(roleArray) {
    // 打开模态框
    $('#confirmModal').modal('show');
    // 先清除旧数据
    $('#roleNameDiv').empty();
    // 在全局范围创建roleArray用于保存roleId
    window.roleIdArray = [];
    // 遍历roleArray，添加新数据
    for (let i = 0; i < roleArray.length; i++) {
        let role = roleArray[i];
        let roleId = role.roleId;
        let roleName = role.roleName;
        $('#roleNameDiv').append(roleName + '<br>');
        window.roleIdArray.push(roleId);
    }
}

/**
 * 填充分配权限的树
 */
function fillAuthTree() {
    // 1.发送ajax查询树形结构数据，初始化zTree
    $.ajax({
        url: 'assign/get/all/auth.json',
        type: 'get',
        dataType: 'json',
        async: false,
        success: function (response) {
            if (response.result === 'FAILED') {
                layer.msg('操作失败！' + response.message);
                return;
            }
            // 获取到的树形结构的JSON数据
            var authList = response.data;
            // zTree的设置
            var setting = {
                check: {
                    // 显示checkbox
                    enable: true
                },
                data: {
                    key: {
                        // 指定节点名称的属性名称
                        name: 'title'
                    },
                    simpleData: {
                        // 开启简单JSON数据模式
                        enable: true,
                        // 指定父节点唯一标识的属性名称
                        pIdKey: 'categoryId'
                    }
                }
            }
            // 初始化zTree
            $.fn.zTree.init($('#authTreeDemo'), setting, authList);
            // 通过zTreeObj设置默认展开所有节点
            let zTreeObj = $.fn.zTree.getZTreeObj('authTreeDemo');
            zTreeObj.expandAll(true);
        },
        error: function (xhr) {
            layer.msg('服务端调用失败！响应状态码=' + xhr.status + '，说明信息=' + xhr.responseText)
        }
    });

    // 2.查询已经分配的权限
    $.ajax({
        url: 'assign/get/assigned/auth/id/by/role/id.json',
        type: 'get',
        data: {
            roleId: window.roleId
        },
        dataType: 'json',
        async: false,
        success: function (response) {
            if (response.result === 'FAILED') {
                layer.msg('操作失败！' + response.message);
                return;
            }
            // 2.1获取已分配的权限的id
            var authIdArray = response.data;
            // 2.2勾选已分配的权限的节点
            // 2.2.1.获取zTreeObj
            let zTreeObj = $.fn.zTree.getZTreeObj('authTreeDemo');
            // 2.2.2 根据id遍历节点
            for (let i = 0; i < authIdArray.length; i++) {
                // 2.2.3 获取treeNode
                let treeNode = zTreeObj.getNodeByParam('id', authIdArray[i]);
                // 2.2.4 设置勾选状态
                // Function(treeNode, checked, checkTypeFlag, callbackFlag)
                // 第1个参数：treeNode
                // 第2个参数：是否选中
                // 第3个参数：是否联动父节点
                // 第4个参数：是否触发beforeCheck & onCheck 事件回调函数
                zTreeObj.checkNode(treeNode, true, false, false)
            }
        },
        error: function (xhr) {
            layer.msg('服务端调用失败！响应状态码=' + xhr.status + '，说明信息=' + xhr.responseText)
        }
    });
}
