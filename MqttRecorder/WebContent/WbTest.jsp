<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	if (request.getSession().getAttribute("processed") == null) {
			response.sendRedirect("ClientConstructor");
	}
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Mqtt Listener interface</title>

  <link href="CSS/Exsheet.css" type="text/css" rel="stylesheet">
  <script  type="text/javascript" src="lib/jquery-3.3.1.min.js"></script>
     
     
</head>

<script>
         
	function DisAlert(){
    	alert("${sessionScope.result}");
    }
         
    function DisWarn(){
        alert("${sessionScope.warning}");
    }
     
     
    //jquery section
    $(document).ready(function(){
      	$(".connect input[type=submit]").click(function(event){
    		var text = $(".connect input[type=url]").val();
    		var check1 = /^(?=[\x21-\x7E]+$).*$/gm;
            console.log(text);

    		if(!check1.test(text))
    		{
    			$(".connect input[type=url]").attr("placeholder","invalid input");
    			$(".connect input[type=text]").val("");
    			event.preventDefault();
    		}	
    	})
    	
    	$(".connect input[type=url]").blur(function(){
			$(".connect input[type=url]").attr("placeholder","Url");

    	})
    	
    	$(".sub input[type=submit]").click(function(event){
    		var text = $(".sub input[type=text]").val();
    		var check1 = /^(?=[\x21-\x7E]+$).*$/gm;
    		var check2 = /^[/].*/gm;
    		var check3 = /.*[/]$/gm;
    		var check4 = /[/][/]/gm;
    		var check5 = /[;][;]/gm;
            console.log(text);

    		if((!check1.test(text)) || (check2.test(text)) || (check3.test(text)) || (check4.test(text)) || (check5.test(text)))
    		{
    			$(".sub input[type=text]").attr("placeholder","invalid input");
    			$(".sub input[type=text]").css("border-color","#fc2f2f");
    			$(".sub input[type=text]").val("");
    			event.preventDefault();
    		}	
    	})
    	
    	$(".sub input[type=text]").on('input', function(){
    		var text = $(this).val();
    		var check1 = /^(?=[\x21-\x7E]+$).*$/gm;
    		var check2 = /^[/].*/gm;
    		var check3 = /.*[/]$/gm;
    		var check4 = /[/][/]/gm;
    		var check5 = /[;][;]/gm;
            console.log(text);

    		if((!check1.test(text)) || (check2.test(text)) || (check3.test(text)) || (check4.test(text)) || (check5.test(text)))
    		{
    			if(!text)
    			{
        			$(this).css("border","1px solid #43D1AF");
        			$(this).attr("placeholder","enter topics(seperate with &quot;;&quot;)");
    			}
    			else
    			{
    			    $(this).css("border","1px solid #fc2f2f");
    			}
    		}	
    		else
    		{
    			$(this).css("border","1px solid #43D1AF");
    			$(this).attr("placeholder","enter topics(seperate with &quot;;&quot;)");
    		}
 
    	})
    	
    	$(".sub input[type=text]").on('focus', function(){
    		var text = $(this).val();
    		if(!text)
    		{
    			$(this).css("border","1px solid #ccc");
    			$(this).attr("placeholder","enter topics(seperate with &quot;;&quot;)");
    		}
    	})
    	
    	
    	$(".sub input[type=text]").on('blur', function(){
    		var text = $(this).val();	
    		if($(this).css("border-top-color") == "rgb(67, 209, 175)")
    		{
    			$(this).css("border","1px solid #ccc");
    		}
    	})
    	
    	$(".pub input[type=submit]").click(function(event){
    		var text_topic = $(".pub input[name=topics]").val();
    		var text_message = $(".pub input[name=message]").val();
    		var check1 = /^(?=[\x21-\x7E]+$).*$/gm;
    		var check2 = /^[/].*/gm;
    		var check3 = /.*[/]$/gm;
    		var check4 = /[/][/]/gm;
    		var check5 = /[;][;]/gm;
    		var check6 = /^(?=[\x20-\x7E]+$).*$/gm;

    		if((!check1.test(text_topic)) || (check2.test(text_topic)) || (check3.test(text_topic)) || (check4.test(text_topic)) || (check5.test(text_topic)))
    		{
                console.log("test11");

    			$(".pub input[name=topics]").attr("placeholder","invalid input");
    			$(".pub input[name=topics]").css("border-color","#fc2f2f");
    			$(".pub input[name=topics]").val("");
    			event.preventDefault();
    		}	
    		if(!check6.test(text_message))
    		{
                console.log("test22");
                console.log(text_message);

    			$(".pub input[name=message]").attr("placeholder","invalid input");
    			$(".pub input[name=message]").css("border-color","#fc2f2f");
    			$(".pub input[name=message]").val("");
    			event.preventDefault();
    		}
    	})
    	
    	$(".pub input[name=topics]").on('input', function(){
    		var text = $(this).val();
    		var check1 = /^(?=[\x21-\x7E]+$).*$/gm;
    		var check2 = /^[/].*/gm;
    		var check3 = /.*[/]$/gm;
    		var check4 = /[/][/]/gm;
    		var check5 = /[;][;]/gm;
            console.log(text);

    		if((!check1.test(text)) || (check2.test(text)) || (check3.test(text)) || (check4.test(text)) || (check5.test(text)))
    		{
    			if(!text)
    			{
        			$(this).css("border","1px solid #ccc");
        			$(this).attr("placeholder","enter topics(seperate with &quot;;&quot;)");

    			}
    			else
    			{
    			    $(this).css("border","1px solid #fc2f2f");
    			}
    		}	
    		else
    		{
    			$(this).css("border","1px solid #43D1AF");
    			$(this).attr("placeholder","enter topics(seperate with &quot;;&quot;)");
    		}
 
    	})
    	
    	
    	$(".pub input[name=message]").on('input', function(){
    		var text = $(this).val();
    		var check1 = /^(?=[\x20-\x7E]+$).*$/gm;
            console.log(text);

    		if(!check1.test(text))
    		{
    			if(!text)
    			{
        			$(this).css("border","1px solid #ccc");
        			$(this).attr("placeholder","enter message");

    			}
    			else
    			{
    			    $(this).css("border","1px solid #fc2f2f");
    			}
    		}	
    		else
    		{
    			$(this).css("border","1px solid #43D1AF");
    			$(this).attr("placeholder","enter message");
    		}
 
    	})
    	
    	
    	$(".pub input[name=topics]").on('focus', function(){
    		var text = $(this).val();
    		if(!text)
    		{
    			$(this).css("border","1px solid #ccc");
    			$(this).attr("placeholder","enter topics(seperate with &quot;;&quot;)");
    		}
    	})
    	
    	$(".pub input[name=message]").on('focus', function(){
    		var text = $(this).val();
    		if(!text)
    		{
    			$(this).css("border","1px solid #ccc");
    			$(this).attr("placeholder","enter message");
    		}
    	})
    	
    	$(".pub input[type=text]").on('blur', function(){
    	var text = $(this).val();	
    		if($(this).css("border-top-color") == "rgb(67, 209, 175)")
    		{
    			$(this).css("border","1px solid #ccc");
    		}
    	})
    	
    });	
    	
    	
    	

    	
    	
    	
    	
    	

     
</script>

<body>   
     <c:if test="${not empty sessionScope.result}">
       <script> DisAlert(); </script>
     </c:if>
     <c:if test="${not empty sessionScope.warning}">
       <script> DisWarn();  </script>
     
     </c:if>
     <div id ="top_block">
     <h1 id="Title"> Mqtt Message Listenser</h1>
     </div>
     
     
     <div id ="Col1">
       <div class="input_form">
       <h1>Mqtt Broker Connection</h1>
         <form class = "connect" action="ClientConstructor" method="post">
           <input type="hidden" name="request" value="SetConnection" >
           <input type="url" name="url" placeholder="Url" />
           <input type="text" name="username" placeholder="Username" />
           <input type="text" name="password" placeholder="Password"/>
           <span class="radio_header">SetCleanSession</span>
           <label class="container">Yes
             <input type="radio" checked="checked" name="cleansession" value="yes">
             <span class="checkmark"></span>
           </label>
           <label class="container">No
             <input type="radio" name="cleansession" value ="no">
             <span class="checkmark"></span>
           </label>
           <input type="submit" value="Connect"/>
         </form>
         <p class="topic_n_status_p">Connection Status: </p>
         <p><c:out value="${sessionScope.Status}" escapeXml="false"/></p>
         
       </div>
       
       
       <div class="input_form">
       <h1>Subscribe & Unsubscribe</h1>
         <form class = "sub" action="ClientConstructor" method="get">
           <input type="hidden" name="request" value="sub" >
           <input type="text" name="topics" placeholder="enter topics(seperate with &quot;;&quot;)" />
           <p class="topic_n_status_p">Subscribed Topics:</p>
           <c:if test="${not empty sessionScope.topics}">
             <ul id = "topiclist">
             <c:forEach items="${sessionScope.topics}" var="topic">
               <li> 
                 <c:out value = "${topic}" />
               </li>
             </c:forEach>
             </ul>
           </c:if>
           <c:if test="${empty sessionScope.topics}">
           <p>No Topic Subscribed</p>
           </c:if>
           <input type="submit" name="sub" value="Subscribe"/>
           <input type="submit" name="sub" value="UnSubscribe"/>
         </form>
       </div>
       
       <div class="input_form">
       <h1>Publish</h1>
         <form class = "pub" action="ClientConstructor" method="get">
           <input type="hidden" name="request" value="pub" >
           <input type="text" name="topics" placeholder="enter topics(seperate with &quot;;&quot;)" />
           
           <input type="text" name="message" placeholder="enter message" />
           
           
           <span class="radio_header">SetQos</span>
           <label class="container">0
             <input type="radio" checked="checked" name="QoS" value="0">
             <span class="checkmark"></span>
           </label>
           <label class="container">1
             <input type="radio" name="QoS" value ="1">
             <span class="checkmark"></span>
           </label>
              <label class="container">2
             <input type="radio" name="QoS" value ="2">
             <span class="checkmark"></span>
           </label>
           
           <br><br>
           <span class="radio_header" >Retain message?</span>
           <label class="container">No
             <input type="radio" checked="checked" name="retained" value="no">
             <span class="checkmark"></span>
           </label>
           <label class="container">Yes
             <input type="radio" name="retained" value ="yes">
             <span class="checkmark"></span>
           </label>
           
           
           <input class = "pub" type="submit" name="pub" value="Publish"/>
           
         </form>
       </div>
       
              
     </div>
     <div id ="Col2">
     <table class = "overviewer">
       <tr> 
         <th class = "ov_header" id = "ov_corner_left">Message ID</th>
         <th class = "ov_header">Topic</th>
         <th class = "ov_header">Retained</th>
         <th class = "ov_header">Duplicated</th>
         <th class = "ov_header">Received Date</th>
         <th class = "ov_header" id = "ov_corner_right">Message Detail</th>        
       </tr>
       <c:if test="${not empty sessionScope.MessList}">
          <c:forEach items="${sessionScope.MessList}" var="MessAttri" varStatus = "loop">
             <tr class = "ov_element">
                <td>${MessAttri.mqttid}</td>
                <td>${MessAttri.topic}</td>
                <c:if test = "${MessAttri.retained}">
                  <td>Yes</td>
                </c:if>
                <c:if test = "${not MessAttri.retained}">
                  <td>No</td>
                </c:if>
                <c:if test = "${MessAttri.duplicate}">
                  <td>Yes</td>
                </c:if>
                <c:if test = "${not MessAttri.duplicate}">
                  <td>No</td>
                </c:if>
                <td>${MessAttri.receivedtime}</td>
                <form action="ClientConstructor">
                  <input type="hidden" name="Index" value="${loop.index}">
                  <input type="hidden" name="request" value="ViewDetail">
                  <td> 
                    <input class="detail_button" type="submit" value ="view" id="view_detail">
                  </td>
                </form>
             </tr>
          </c:forEach>
          <tr>
             <td colspan="6"></td>
          </tr>
       </c:if>
       <c:if test="${empty sessionScope.MessList}">
          <tr>
             <td colspan = "6" id= "empty_table">
                empty message cache
             </td>
          </tr>
       </c:if>
     </table>
     
     
     
     
   <!--  
     </div>
     <form>
       	<input id="message" type="text">
        <input onclick="wsSendMessage();" value="Echo" type="button">
        <input onclick="wsCloseConnection();" value="Disconnect" type="button">
     </form>
     <br>
     <textarea id="echoText" rows="5" cols="30"></textarea>
     </div>
     -->
     <c:remove var="Status" scope="session" />
     <c:remove var="processed" scope="session" />
     <c:remove var="MessList" scope="session" />
     <c:remove var="InfoList" scope="session" />
     
     <c:remove var="result" scope="session" />
     <c:remove var="warning" scope="session" />     
     <c:remove var="topics" scope="session" />     





<!--  

     <script type="text/javascript">
     	var webSocket = new WebSocket("ws://localhost:8080/MqttRecorder/wslistener");
        var echoText = document.getElementById("echoText");
        echoText.value = "";
        var message = document.getElementById("message");
        webSocket.onopen = function(message) { wsOpen(message);};
        webSocket.onmessage = function(message) { wsGetMessage(message);};
        webSocket.onclose = function(message){ wsClose(message);};
        webSocket.onerror = function(message){ wsError(message);};
        
        function wsOpen(message){
        	echoText.value += "Connected ... \n";
        	
        }
        
        function wsSendMessage(){
        	webSocket.send(message.value);
        	echoText.value += "Message sended to the server : " + message.value + "\n";
        	message.value = "";
        }
        
        function wsCloseConnection(){
        	webSocket.close();
        }
        
        function wsGetMessage(message){
        	echoText.value += "Message recoved from to the server : " + message.data + "\n";
        }
        
        function wsClose(message){
        	echoText.value += "Disconnect ... \n" ;	
        }
        
        function wserror(message){
        	echoText.value += "Error ... \n";
        }
     </script>
 -->    

</html>