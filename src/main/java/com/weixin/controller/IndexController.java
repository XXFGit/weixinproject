package com.weixin.controller;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.weixin.base.TextMessage;
import com.weixin.utils.CheckUtil;
import com.weixin.utils.HttpClientUtil;
import com.weixin.utils.XmlUtils;

@RestController
public class IndexController {

	/*private static final String REQEST_HTTP = "http://api.qingyunke.com/api.php?key=free&appid=0&msg=";*/
	private static final String REQEST_HTTP = "http://i.itpk.cn/api.php?question=";
	
	@RequestMapping(value = "/dispatCherServlet", method = RequestMethod.GET)
	public String dispatCherServletGet(String signature, String timestamp, String nonce, String echostr) {
		// 1.验证是否微信来源
		boolean checkSignature = CheckUtil.checkSignature(signature, timestamp, nonce);
		// 2.如果是微信来源 返回 随机数echostr
		if (!checkSignature) {
			return null;
		}
		return echostr;
	}

	@RequestMapping(value = "/dispatCherServlet", method = RequestMethod.POST)
	public void dispatCherServletPost(HttpServletRequest reqest, HttpServletResponse response) throws Exception {
		reqest.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		Map<String, String> mapResult = XmlUtils.parseXml(reqest);
		if (mapResult == null) {
			return;
		}
		String msgType = mapResult.get("MsgType");
		PrintWriter writer = response.getWriter();
		if(msgType.equals("text")){
			// 获取消息内容
			String content = mapResult.get("Content");
			// 发送消息
			String toUserName = mapResult.get("ToUserName");
			// 来自消息
			String fromUserName = mapResult.get("FromUserName");
			// 調用青云客智能机器人接口
			/*String requestResultJson = HttpClientUtil.doGet(REQEST_HTTP + content);
			JSONObject jsonObject = new JSONObject().parseObject(requestResultJson);
			String result = jsonObject.getString("result");
			String msg = null;
			if (result.equals("0")) {
				msg = jsonObject.getString("content");
			} else {
				msg = "我也不知道回答什么！";
			}*/
			
			// 調用茉莉智能机器人接口
			String msg = null;
			String requestResultJson = HttpClientUtil.doGet(REQEST_HTTP + content);
			if(requestResultJson !=null){
				msg = requestResultJson;
			}else{
				msg = "这个我也不知道呢！";
			}
			String resultTestMsg = setText(msg, toUserName, fromUserName);
			writer.print(resultTestMsg);
		}
		writer.close();

	}

	public String setText(String content, String fromUserName, String toUserName) {
		TextMessage textMessage = new TextMessage();
		textMessage.setContent(content);
		textMessage.setMsgType("text");
		textMessage.setCreateTime(new Date().getTime());
		textMessage.setFromUserName(fromUserName);
		textMessage.setToUserName(toUserName);
		// 将实体类转换成xml格式
		String msg = XmlUtils.messageToXml(textMessage);
		return msg;
	}


	
	@RequestMapping("/index")
	@ResponseBody
	public String index(){
		return "这里是springboot的index页面！！";
	}
	
	
}
