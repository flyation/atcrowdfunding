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
        console.log(role)
        $('#rolePageBody').append(`
                            <tr>
                                <td>${i + 1}</td>
                                <td><input type="checkbox"><input type="hidden" name="id" value="${role.id}"></td>
                                <td>${role.name}</td>
                                <td>
                                    <button type="button" class="btn btn-success btn-xs"><i class=" glyphicon glyphicon-check"></i></button>
                                    <button type="button" class="btn btn-primary btn-xs"><i class=" glyphicon glyphicon-pencil"></i></button>
                                    <button type="button" class="btn btn-danger btn-xs"><i class=" glyphicon glyphicon-remove"></i></button>
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
