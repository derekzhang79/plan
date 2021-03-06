<%@ page language="java" contentType="text/html; charset=UTF-8" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="form"  uri="http://www.springframework.org/tags/form" %> 

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
    <meta charset="UTF-8">
    <title>计划管理</title>
  
    <link href="${ctx}/static/dingding/weui.min.css" rel="stylesheet" />
    <script src="${ctx}/static/dingding/zepto.min.js"></script>
    <!-- 钉钉js -->
 <script src='${ctx}/static/dingding/dingtalk.js'></script>
<link href="${ctx}/static/plancss/index.css" rel="stylesheet" />
<link href="${ctx}/static/plancss/lookview.css" rel="stylesheet" />
    <style>
         body {
            background-color: #f0eff4;
        }

		
    </style>
</head>
<body>


<div class="weui_panel">
    <div class="weui_panel_hd">待审核列表</div>
    <div class="weui_panel_bd" id="deptlistdiv">
       

    </div>
</div>
</body>
<script type="text/javascript" charset="utf-8" >
var authconfig=$.parseJSON('${authconfig}');
dd.config({
		agentId : authconfig.agentid,
		corpId : authconfig.corpId,
		timeStamp : authconfig.timeStamp,
		nonceStr : authconfig.nonceStr,
		signature : authconfig.signature,
		jsApiList : [ 'runtime.info', 'biz.contact.choose',
						'device.notification.confirm', 'device.notification.alert',
						'device.notification.prompt', 'biz.ding.post',
						'biz.util.openLink','biz.navigation.setRight','biz.navigation.setLeft',
						'device.notification.showPreloader','device.notification.hidePreloader','biz.navigation.close' ]
});
dd.ready(function(){
	
	    $.ajax({
	    	  type: 'POST',
	    	  url: '${ctx}/check/getcheck',
	    	  data: {  },
	    	  dataType: 'json',
	    	  success: function(data){
	    	    var listtask="";
		    	    $.each(data.userdata,function(i,n){
		    	    	if(n.type=="计划审核"){
		    	    		listtask+=" <a href=\"${ctx}/plan/planview?dd_nav_bgcolor=FF30A8A5&checkid="+n.id+"&deptid="+n.deptid+"&power=shenpi\" class=\"weui_media_box weui_media_text \" name=\""+n.id+"\" >";
				  	    	listtask+="<p class=\"weui_media_desc\">"+n.year+"年"+n.deptname+n.type+"</p>";
				  	    	listtask+="</a>";
		    	    	}else if(n.type=="变更"){
		    	    		listtask+=" <a href=\"${ctx}/change/shenhechange?dd_nav_bgcolor=FF30A8A5&id="+n.id+"&tid="+n.taskid+"&cid="+n.changeid+"\" class=\"weui_media_box weui_media_text \" name=\""+n.id+"\" >";
				  	    	listtask+="<p class=\"weui_media_desc\">"+n.year+"年"+n.deptname+"任务"+n.type+"申请</p>";
				  	    	listtask+="</a>";
		    	    	}else if(n.type=="撤销"){
		    	    		listtask+=" <a href=\"${ctx}/change/shenhedel?dd_nav_bgcolor=FF30A8A5&id="+n.id+"&tid="+n.taskid+"\" class=\"weui_media_box weui_media_text \" name=\""+n.id+"\" >";
				  	    	listtask+="<p class=\"weui_media_desc\">"+n.year+"年"+n.deptname+"任务"+n.type+"申请</p>";
				  	    	listtask+="</a>";
		    	    	}
			               
				  	    	 
		    	     });
		            document.getElementById("deptlistdiv").innerHTML=listtask;
	    	      
	    	  },
	    	  error: function(xhr, type,error){
	    	    alert('Ajax error!');
	    	  }
	    	});
	    
	   
});
dd.error(function(err) {
	alert('sdfs');
	alert('dd error: ' + JSON.stringify(err));
});

function addpower(){
	window.location.href="${ctx}/power/poweradd?dd_nav_bgcolor=FF30A8A5";
	
}

</script>
</html>