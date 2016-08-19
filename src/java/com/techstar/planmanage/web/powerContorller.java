package com.techstar.planmanage.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javassist.expr.NewArray;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.techstar.planmanage.entity.checklog;
import com.techstar.planmanage.entity.plan;
import com.techstar.planmanage.entity.plancheck;
import com.techstar.planmanage.entity.power;
import com.techstar.planmanage.service.ChecklogService;
import com.techstar.planmanage.service.PlanService;
import com.techstar.planmanage.service.PlancheckService;
import com.techstar.planmanage.service.powerService;
import com.techstar.sys.Util.StringUtil;
import com.techstar.sys.config.Global;
import com.techstar.sys.dingAPI.OApiException;
import com.techstar.sys.dingAPI.auth.AuthHelper;
import com.techstar.sys.dingAPI.department.Department;
import com.techstar.sys.dingAPI.department.DepartmentHelper;
import com.techstar.sys.dingAPI.user.User;
import com.techstar.sys.dingAPI.user.UserHelper;
import com.techstar.sys.jpadomain.Results;



/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/power")
public class powerContorller {
	@Autowired
	private powerService powerService;
	
	//@RequestMapping("/getstate12")
	//public String test(Model model,HttpServletRequest request) throws OApiException, UnsupportedEncodingException {
		//model.addAttribute("code", code);
		//String configstr= AuthHelper.getConfig(request);
		//model.addAttribute("authconfig", configstr);
	//	return "plan/plandeptlist";
		//return "plan/index";
	//}

	@RequestMapping("/admin")
	public String test(Model model,HttpServletRequest request) throws OApiException, UnsupportedEncodingException {
		
		String configstr= AuthHelper.getConfig(request);
		model.addAttribute("authconfig", configstr);
		return "plan/adminindex";
	}
	@RequestMapping("/poweradd")
	public String poweradd(@RequestParam(value = "oper", required = false) String oper,
			@RequestParam(value = "id", required = false) String id,Model model,HttpServletRequest request) throws OApiException, UnsupportedEncodingException {
		if(StringUtil.isNotBlank(oper)&&oper.equals("edit")){
			model.addAttribute("power", powerService.findById(Long.parseLong(id)));
		}
		String configstr= AuthHelper.getConfig(request);
		model.addAttribute("authconfig", configstr);
		return "plan/poweradd";
	}
	

	@RequestMapping("/addpower")
	public @ResponseBody Results  addjindu(@ModelAttribute power powerform,
			@RequestParam(value = "oper", required = false) String oper,
			Model model,HttpServletRequest request,
			HttpServletResponse response) throws OApiException, UnsupportedEncodingException {
		
		JSONObject userJsonObject=null;
		Cookie[] cookies = request.getCookies();//这样便可以获取一个cookie数组
		
		for(Cookie cookie : cookies){
			if(cookie.getName().equals("user")){
				userJsonObject=JSON.parseObject(URLDecoder.decode(cookie.getValue(),"UTF-8"));
			}
		}
		powerform.setOperationer(userJsonObject.get("name").toString());
		powerform.setOperationerid(userJsonObject.get("userid").toString());
		powerService.save(powerform);
		return  new Results("保存成功",powerform.getId());
	}
	@RequestMapping("/getpower")
	public @ResponseBody Results  getpower(
			Model model,HttpServletRequest request,
			HttpServletResponse response) throws OApiException, UnsupportedEncodingException {
		
		List<power> powerlist=powerService.findAll();
		return  new Results(powerlist);
	}
}