<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<div>
    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="importAll()">一键导入商品数据到索引库</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="deleteAll()">一键删除所有索引</a>
</div>

<script type="text/javascript">
// 导入全部索引
function importAll(){
    $.post("/index/importAll", function(data){
        if (data.status==200) {
            $.messager.alert('提示','商品数据导入成功！');
        } else {

            $.messager.alert('提示','商品数据导入失败！');
        }

    });
}

// 删除全部索引
function deleteAll(){
    $.post("/index/deleteAll", function(data){
        if (data.status==200) {
            $.messager.alert('提示','删除全部索引成功！');
        } else {

            $.messager.alert('提示','删除全部索引失败！');
        }

    });
}
</script>