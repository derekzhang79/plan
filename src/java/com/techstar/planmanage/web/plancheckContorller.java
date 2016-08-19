package com.techstar.planmanage.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javassist.expr.NewArray;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.sql.Select;
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
@RequestMapping("/check")
public class plancheckContorller {
	@Autowired
	private PlancheckService plancheckService;
	@Autowired
	private powerService powerService;
	@Autowired
	private ChecklogService checklogService;
	@RequestMapping("/getstate12")
	public String test(Model model,HttpServletRequest request) throws OApiException, UnsupportedEncodingException {
		//model.addAttribute("code", code);
		String configstr= AuthHelper.getConfig(request);
		model.addAttribute("authconfig", configstr);
	//	return "plan/plandeptlist";
		return "plan/index";
	}
	
	@RequestMapping("/checklist")
	public String checklist(Model model,HttpServletRequest request) throws OApiException, UnsupportedEncodingException {
		//model.addAttribute("code", code);
		String configstr= AuthHelper.getConfig(request);
		model.addAttribute("authconfig", configstr);
	//	return "plan/plandeptlist";
		return "plan/shenheindex";
	}
	@RequestMapping("/shenheview")
	public String shenheview(@RequestParam(value="checkid",required=false)String checkid,Model model,HttpServletRequest request) throws OApiException, UnsupportedEncodingException {
		//model.addAttribute("code", code);
		String configstr= AuthHelper.getConfig(request);
		model.addAttribute("authconfig", configstr);
		model.addAttribute("id", checkid);
	//	return "plan/plandeptlist";
		return "plan/shenheview";
	}
	@RequestMapping("/shenpiadd")
	public String shenpiadd(@RequestParam(value="checkid",required=false)String checkid,Model model,HttpServletRequest request) throws OApiException, UnsupportedEncodingException {
		plancheck planchecks=plancheckService.findById(Long.parseLong(checkid));
		String configstr= AuthHelper.getConfig(request);
		model.addAttribute("authconfig", configstr);
		model.addAttribute("plancheck", planchecks);
		return "plan/shenpiadd";
	}
	@RequestMapping("/getstate")
	public @ResponseBody Results  getstate(@RequestParam(value="deptid",required=false)String deptid,
			@RequestParam(value="year",required=false)String year,
			Model model,HttpServletRequest request,
			HttpServletResponse response) throws OApiException, UnsupportedEncodingException {
		List<plancheck> listcheckList=plancheckService.findByDeptidAndYear(deptid, year);
		String stateString="1";
		String checkidString="";
		if(listcheckList.size()<=0){
			stateString="1";
		}else{
			stateString=listcheckList.get(0).getState();
			checkidString=listcheckList.get(0).getId()+"";
		}
		return  new Results(checkidString,stateString);
	}
	
	@RequestMapping("/subplan")
	public @ResponseBody Results  subplan(@RequestParam(value="deptid",required=false)String deptid,
			@RequestParam(value="year",required=false)String year,
			@RequestParam(value="deptname",required=false)String deptname,
			Model model,HttpServletRequest request,
			HttpServletResponse response) throws OApiException, UnsupportedEncodingException {
		Cookie[] cookies = request.getCookies();//这样便可以获取一个cookie数组
		JSONObject userJsonObject = null;
		for(Cookie cookie : cookies){
			if(cookie.getName().equals("user")){
				 userJsonObject=JSON.parseObject(URLDecoder.decode(cookie.getValue(),"UTF-8"));
				//planform.setOperationer(userJsonObject.get("name").toString());
				//planform.setOperationerid(userJsonObject.get("userid").toString());
			}
		}
		List<plancheck> listcheckList=plancheckService.findByDeptidAndYear(deptid, year);
		plancheck subPlancheck=new plancheck();
		if(listcheckList.size()<=0){
			subPlancheck.setDeptid(deptid);
			subPlancheck.setDeptname(deptname);
			subPlancheck.setType("计划审核");
			subPlancheck.setOperationer(userJsonObject.get("name").toString());
			subPlancheck.setOperationerid(userJsonObject.get("userid").toString());
			subPlancheck.setYear(year);
			subPlancheck.setState("2");
		}else{
			subPlancheck=listcheckList.get(0);
			subPlancheck.setState("2");
		}
		plancheckService.save(subPlancheck);
		return  new Results(year+"年计划提交成功");
	}
	@RequestMapping("/getcheck")
	public @ResponseBody Results  getcheck(
			Model model,HttpServletRequest request,
			HttpServletResponse response) throws OApiException, UnsupportedEncodingException {
		Cookie[] cookies = request.getCookies();//这样便可以获取一个cookie数组
		JSONObject userJsonObject = null;
		for(Cookie cookie : cookies){
			if(cookie.getName().equals("user")){
				 userJsonObject=JSON.parseObject(URLDecoder.decode(cookie.getValue(),"UTF-8"));
				//planform.setOperationer(userJsonObject.get("name").toString());
				//planform.setOperationerid(userJsonObject.get("userid").toString());
			}
		}
		List<plancheck> planchecks=new ArrayList<plancheck>();
		List<power> listpowerList=powerService.findByAdminidAndType(userJsonObject.get("userid").toString(), "主管副总");
		if(listpowerList.size()>0){
			List<String> idList=new ArrayList<String>();
			String[] idstrStrings=listpowerList.get(0).getDeptid().split(",");
			Collections.addAll(idList, idstrStrings);
			planchecks=plancheckService.findByDeptidInAndState(idList, "2");
		}
		return  new Results(planchecks);
	}
	
	@RequestMapping("/shenpistate")
	public @ResponseBody Results  shenpistate(@RequestParam(value="state",required=false)String state,
			@RequestParam(value="checkid",required=false)String checkid,
			@RequestParam(value="shuju",required=false)String shuju,
			Model model,HttpServletRequest request,
			HttpServletResponse response) throws OApiException, UnsupportedEncodingException {
		plancheck plancheckstate=plancheckService.findById(Long.parseLong(checkid));
		plancheckstate.setState(state);
		plancheckService.save(plancheckstate);
		Cookie[] cookies = request.getCookies();//这样便可以获取一个cookie数组
		JSONObject userJsonObject = null;
		for(Cookie cookie : cookies){
			if(cookie.getName().equals("user")){
				 userJsonObject=JSON.parseObject(URLDecoder.decode(cookie.getValue(),"UTF-8"));
				//planform.setOperationer(userJsonObject.get("name").toString());
				//planform.setOperationerid(userJsonObject.get("userid").toString());
			}
		}
		checklog checklog=new checklog();
		checklog.setCheckid(plancheckstate.getId().toString());
		checklog.setDeptid(plancheckstate.getDeptid());
		checklog.setDeptname(plancheckstate.getDeptname());
		checklog.setOperationer(userJsonObject.get("name").toString());
		checklog.setOperationerid(userJsonObject.get("userid").toString());
		checklog.setShuoming("审核通过");
		if(state.equals("1")){
			checklog.setShuoming("审核不通过");
		}
		checklog.setShuju(shuju);
		checklog.setType("zhuguan");
		checklog.setYear(plancheckstate.getYear());
		checklog.setState(state);
		checklogService.save(checklog);
		return  new Results("审核完成");
	}
	
	
}
