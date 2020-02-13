<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<div>
	 <ul id="contentCategory" class="easyui-tree">  </ul>
</div>
<div id="contentCategoryMenu" class="easyui-menu" style="width:120px;" data-options="onClick:menuHandler">
    <div data-options="iconCls:'icon-add',name:'add'">添加</div>
    <div data-options="iconCls:'icon-remove',name:'rename'">重命名</div>
    <div class="menu-sep"></div>
    <div data-options="iconCls:'icon-remove',name:'delete'">删除</div>
</div>
<script type="text/javascript">
// 文档加载后处理一下逻辑
$(function(){
    // 在 #contentCategory 所在的标签中创建一颗树（tree）
	$("#contentCategory").tree({
		url : '/content/category/list',
		animate: true,
		method : "GET",
        // 右击鼠标触发
		onContextMenu: function(e,node){
		    // 关闭原来鼠标的默认事件
            e.preventDefault();
            // 选中 右击鼠标的节点
            $(this).tree('select',node.target);
            // 展示菜单栏
            $('#contentCategoryMenu').menu('show',{
                // 在鼠标的位置显示
                left: e.pageX,
                top: e.pageY
            });
        },
        // 在选中的节点被编辑之后触发
        onAfterEdit : function(node){
		    // 获取树本身
        	var _tree = $(this);
        	if(node.id == 0){
        		// 新增节点
                // parentId:node.parentId,name:node.text
                // parentId：新增节点的父节点id
                // name：新增节点的文本（名称）
        		$.post("/content/category/create",{parentId:node.parentId,name:node.text},function(data){
        			if(data.status == 200){
        				_tree.tree("update",{
            				target : node.target, // 更新哪个节点
            				id : data.data.id // 更新新增节点的id
            			});
        			}else{
        				$.messager.alert('提示','创建'+node.text+' 分类失败!');
        			}
        		});
        	}else{
        		$.post("/content/category/update",{id:node.id,name:node.text});
        	}
        }
	});
});
// 处理点击菜单的事件
function menuHandler(item){
    // 获取树
	var tree = $("#contentCategory");
	// 获取被选中的节点
	var node = tree.tree("getSelected");
	// 判断的是添加、删除、还是重命名

    //在js里面： ==   1==1  true      1==="1"  true
    //在js里面： ===  1===1 true      1==="1"  false

	if(item.name === "add"){
	    // 在被点击的节点下追加一个子节点
		tree.tree('append', {
            parent: (node?node.target:null),
            data: [{
                text: '新建分类',
                id : 0,
                parentId : node.id
            }]
        });
		// 找到id为零的节点 也就是新增的节点
		var _node = tree.tree('find',0);
        // 找到id为零的节点 也就是新增的节点 并开启编辑
		tree.tree("select",_node.target).tree('beginEdit',_node.target);
	}else if(item.name === "rename"){
		tree.tree('beginEdit',node.target);
	}else if(item.name === "delete"){
		$.messager.confirm('确认','确定删除名为 '+node.text+' 的分类吗？',function(r){
			if(r){ // 如果是true，发送请求
				$.post("/content/category/delete/",{id:node.id},function(){
				    // 后台删除成功后，删除前端的节点
					tree.tree("remove",node.target);
				});
			}
		});
	}
}
</script>