<#assign str="abcdef" />
<#assign number=123 />
<#assign isOk=false />
<#assign hello="hello ${str}" />
<!DOCTYPE html>
<html>
<head>
</head>
<body>
	<h2>访问模板变量：${id}--${hello}</h2>
	<#setting number_format="0.###" />
	<h2>数值类型：${1234.7?int}---${129999934.44789}---${num}</h2>
	<h2>数值格式插值：#{123;m1}---#{123.4567;m1M3}</h2>
	<h2>字符串类型：${"字符串"}--${"\"双引号\""}--${escapeStr}</h2>
	<h2>布尔类型：${isOk?string("正确","错误")}</h2>
	<h2>日期类型格式化：${date?string("yyyy-MM-dd HH:mm:ss")}</h2>
	<h2>字符串截取：${str[0]}---${str[0..4]}</h2>
	<h2>字符串连接语法：${"hello "+str}---${"或者 hello ${str}"}</h2>
	<h2>函数调用：${str?upper_case}</h2>
	<#-- 注释内容 -->
	<div>${data!"通过“!”运算符设置当变量不存在时指定默认值"}</div>
	
	<#-- 插值不会解析含有指令的字符串，当成普通字符串输出 -->
	<h2>${command}</h2>
	
	<#if str == " abcdef ">
		<span>是</span>
	<#else>
		<span>否</span>
	</#if>
	
	<#if var??>
		存在变量
	<#else>
		变量不存在
	</#if>
	
	<#noparse>
		<#if var??>不解析noparse指令的内容</#if>
	</#noparse>
	
	<#list ["a", "b", "c", "y"] as x>
		<span>
			第${x_index}个变量的值：${x}，是否存在下一个变量：${x_has_next?string("存在","不存在")}
			<#if x == "y">break指令中断了<#break/></#if>
		</span>
	</#list>
	
	<#-- 通过include指令引入一个文件，不把该文件当成ftl文件解析 -->
	<#include "include.txt" parse="false" />
	
	<#escape x as x?upper_case>
		1:${"abc"}
		2:${"Abcde"}
		3:${"ABCDE"}
		<#noescape>4:${"abc123"}</#noescape>
	</#escape>
</body>
</html>