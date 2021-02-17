/**
 * 生成菜单树
 */
function generateTree() {
    // zTree的设置
    var setting = {
        data: {
            key: {
                url: 'whatever' // 特殊用途：当后台数据只能生成 url 属性，又不想实现点击节点跳转的功能时，可以直接修改此属性为其他不存在的属性名称
            }
        },
        view: {
            addDiyDom: myAddDiyDom, // 用于在节点上固定显示用户自定义控件
            addHoverDom: myAddHoverDom,
            removeHoverDom: myRemoveHoverDom
        }
    };

    // ajax请求树形结构的JSON数据
    var zNodes = {};
    $.ajax({
        url: 'menu/get/whole/tree.json',
        type: 'get',
        async: false,
        dataType: 'json',
        success: function (response) {
            if (response.result === 'FAILED') {
                layer.msg('操作失败！' + response.message);
                return;
            }
            // 获取到的树形结构的JSON数据
            zNodes = response.data;
        },
        error: function (xhr) {
            layer.msg('服务端调用失败！响应状态码=' + xhr.status + '，说明信息=' + xhr.responseText)
        }
    });

    // 初始化
    $.fn.zTree.init($('#treeDemo'), setting, zNodes);
}

/**
 * 用于在节点上固定显示用户自定义控件
 * @param treeId tree容器的DOM的id
 * @param treeNode 每一个tree节点对象
 */
function myAddDiyDom(treeId, treeNode) {
    // 根据treeNode的tId属性拼接出节点的DOM的id
    let nodeDomId = treeNode.tId + '_ico';
    // 去掉节点原来的class，添加自定义的class
    $('#' + nodeDomId).removeClass().addClass(treeNode.icon)
}

/**
 * 鼠标移入时显示按钮组
 */
function myAddHoverDom(treeId, treeNode) {
    // 超链接的DOM的id
    let anchorId = treeNode.tId + '_a';
    // 按钮组的DOM的id
    let btnGropId = treeNode.tId + '_btnGrp';
    // 防止移入过程中多次触发生成按钮组，此处先判断是否已经生成
    if ($('#' + btnGropId).length > 0) {
        return;
    }

    // 准备各个按钮的html
    let addBtn = `<a id="${treeNode.id}" class="addBtn btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;" href="javascript:void(0);" title="添加子节点">&nbsp;&nbsp;<i class="fa fa-fw fa-plus rbg "></i></a>`;
    let editBtn = `<a id="${treeNode.id}" class="editBtn btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;" href="javascript:void(0);" title="修改权限信息">&nbsp;&nbsp;<i class="fa fa-fw fa-edit rbg "></i></a>`;
    let removeBtn = `<a id="${treeNode.id}" class="removeBtn btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;" href="javascript:void(0);" title="删除当前节点">&nbsp;&nbsp;<i class="fa fa-fw fa-times rbg "></i></a>`;
    // 存储拼装好的按钮组
    let btnHTML = '';
    // 获取当前节点的级别
    let level = treeNode.level
    if (level === 0) {
        // level为0则为根节点，只能添加子节点
        btnHTML = addBtn;
    } else if (level === 1) {
        // level为1则为分支节点，可以添加子节点和编辑节点，是否可删除当前节点还需进一步判断有无子节点
        btnHTML = addBtn + editBtn;
        // 判断当前节点有无子节点，无子节点则可以删除，有子节点则不能删除
        if (treeNode.children.length === 0) {
            btnHTML += removeBtn;
        }
    } else {
        // level为2则为叶子节点，只能编辑和删除
        btnHTML = editBtn + removeBtn;
    }

    // 追加按钮组
    $('#' + anchorId).after(`<span id="${btnGropId}">${btnHTML}</span>`);
}

/**
 * 鼠标移出时隐藏按钮组
 */
function myRemoveHoverDom(treeId, treeNode) {
    // 按钮组的DOM的id
    let btnGropId = treeNode.tId + '_btnGrp';
    // 移除按钮组
    $('#' + btnGropId).remove();
}
