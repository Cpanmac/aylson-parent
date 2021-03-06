package com.aylson.dc.ytt.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aylson.core.easyui.EasyuiDataGridJson;
import com.aylson.core.frame.controller.BaseController;
import com.aylson.core.frame.domain.Result;
import com.aylson.core.frame.domain.ResultCode;
import com.aylson.dc.ytt.search.YttIncomeSetSearch;
import com.aylson.dc.ytt.service.YttIncomeSetService;
import com.aylson.dc.ytt.vo.YttIncomeSetVo;
import com.aylson.dc.sys.common.SessionInfo;
import com.aylson.utils.DateUtil2;
import com.aylson.utils.UUIDUtils;

/**
 * 提现金额配置
 * @author Minbo
 */
@Controller
@RequestMapping("/ytt/yttIncomeSet")
public class YttIncomeSetController extends BaseController {
	
	protected static final Logger logger = Logger.getLogger(YttIncomeSetController.class);

	@Autowired
	private YttIncomeSetService yttIncomeSetService;
	
	/**
	 * 后台-首页
	 * @return
	 */
	@RequestMapping(value = "/admin/toIndex", method = RequestMethod.GET)
	public String toIndex() {
		return "/jsp/ytt/admin/yttIncomeSet/index";
	}
	
	/**
	 * 获取列表
	 * @return list
	 */
	@RequestMapping(value = "/admin/list", method = RequestMethod.GET)
	@ResponseBody
	public EasyuiDataGridJson list(YttIncomeSetSearch yttIncomeSetSearch){
		EasyuiDataGridJson result = new EasyuiDataGridJson();//页面DataGrid结果集
		try{
			yttIncomeSetSearch.setIsPage(true);
			List<YttIncomeSetVo> list = this.yttIncomeSetService.getList(yttIncomeSetSearch);
			result.setTotal(this.yttIncomeSetService.getRowCount(yttIncomeSetSearch));
			result.setRows(list);
			return result;
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * 后台-添加页面
	 * @param yttIncomeSetVo
	 * @return
	 */
	@RequestMapping(value = "/admin/toAdd", method = RequestMethod.GET)
	public String toAdd(YttIncomeSetVo yttIncomeSetVo) {
		this.request.setAttribute("yttIncomeSetVo", yttIncomeSetVo);
		return "/jsp/ytt/admin/yttIncomeSet/add";
	}
	
	/**
	 * 后台-添加保存
	 * @param yttIncomeSetVo
	 * @return
	 */
	@RequestMapping(value = "/admin/add", method = RequestMethod.POST)
	@ResponseBody
	public Result add(YttIncomeSetVo yttIncomeSetVo) {
		Result result = new Result();
		try{
			SessionInfo sessionInfo = (SessionInfo)this.request.getSession().getAttribute("sessionInfo");
			yttIncomeSetVo.setId(UUIDUtils.create());
			String cTime = DateUtil2.getCurrentLongDateTime();
			yttIncomeSetVo.setCreatedBy(sessionInfo.getUser().getUserName() + "/" + sessionInfo.getUser().getRoleName());
			yttIncomeSetVo.setCreateDate(cTime);
			yttIncomeSetVo.setUpdateDate(cTime);
			Boolean flag = this.yttIncomeSetService.add(yttIncomeSetVo);
			if(flag){
				result.setOK(ResultCode.CODE_STATE_200, "操作成功");
			}else{
				result.setError(ResultCode.CODE_STATE_4006, "操作失败");
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			result.setError(ResultCode.CODE_STATE_500, e.getMessage());
		}
		return result;
	}
	
	/**
	 * 后台-编辑页面
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/admin/toEdit", method = RequestMethod.GET)
	public String toEdit(String id) {
		if(id != null){
			YttIncomeSetVo yttIncomeSetVo = this.yttIncomeSetService.getById(id);
			this.request.setAttribute("yttIncomeSetVo", yttIncomeSetVo);
		}
		return "/jsp/ytt/admin/yttIncomeSet/add";
	}
	
	/**
	 * 后台-编辑保存
	 * @param yttIncomeSetVo
	 * @return
	 */
	@RequestMapping(value = "/admin/update", method = RequestMethod.POST)
	@ResponseBody
	public Result update(YttIncomeSetVo yttIncomeSetVo) {
		Result result = new Result();
		try {
			SessionInfo sessionInfo = (SessionInfo)this.request.getSession().getAttribute("sessionInfo");
			yttIncomeSetVo.setUpdatedBy(sessionInfo.getUser().getUserName() + "/" + sessionInfo.getUser().getRoleName());
			yttIncomeSetVo.setUpdateDate(DateUtil2.getCurrentLongDateTime());
			Boolean flag = this.yttIncomeSetService.edit(yttIncomeSetVo);
			if(flag){
				result.setOK(ResultCode.CODE_STATE_200, "操作成功");
			}else{
				result.setError(ResultCode.CODE_STATE_4006, "操作失败");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setError(ResultCode.CODE_STATE_500, e.getMessage());
		}
		return result;
	}
	
	/**
	 * 根据id删除
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/admin/deleteById", method = RequestMethod.POST)
	@ResponseBody
	public Result deleteById(String id) {
		Result result = new Result();
		try{
			Boolean flag = this.yttIncomeSetService.deleteById(id);
			if(flag){
				result.setOK(ResultCode.CODE_STATE_200, "删除成功");
			}else{
				result.setError(ResultCode.CODE_STATE_4006, "删除失败");
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			result.setError(ResultCode.CODE_STATE_500, e.getMessage());
		}
		return result;
	}
	
	@RequestMapping(value = "/admin/changeStatus", method = RequestMethod.POST)
	@ResponseBody
	public Result changeStatus(YttIncomeSetVo yttIncomeSetVo) {
		Result result = new Result();
		try {
			if(yttIncomeSetVo.getStatus() == null){
				result.setError(ResultCode.CODE_STATE_4006, "操作失败");
				return result;
			}
			SessionInfo sessionInfo = (SessionInfo)this.request.getSession().getAttribute("sessionInfo");
			yttIncomeSetVo.setUpdatedBy(sessionInfo.getUser().getUserName() + "/" + sessionInfo.getUser().getRoleName());
			yttIncomeSetVo.setUpdateDate(DateUtil2.getCurrentLongDateTime());
			Boolean flag = this.yttIncomeSetService.edit(yttIncomeSetVo);
			if(flag){
				result.setOK(ResultCode.CODE_STATE_200, "操作成功");
			}else{
				result.setError(ResultCode.CODE_STATE_4006, "操作失败");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setError(ResultCode.CODE_STATE_500, e.getMessage());
		}
		return result;
	}
}
