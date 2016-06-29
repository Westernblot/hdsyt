
package com.grid.hdsyt.function;


public class FinalValues {
	
	
	//服务器通用前缀
	public static final String PRE_URL="http://192.168.1.202/wawj/index.php/";
	//public static final String PRE_URL="http://ycfw.sinaapp.com/index.php/";
	
	//用户登陆
	public static final String CHECK_LOGIN_ADDRESS =PRE_URL+"App/Index/toLogin";
	
	//会员列表
	//public static final String MEMBER_LIST_ADDRESS =PRE_URL+"App/Index/loadMemberList";
	//public static final String MEMBER_DELETE_ADDRESS =PRE_URL+"App/Index/delMemberTest";
	//public static final String MEMBER_ADD_ADDRESS =PRE_URL+"App/Index/addMemberTest";
	
	//库存商品查询
	public static final String GOODS_SELECT_ALL =PRE_URL+"App/Index/selectAllStockList";
	public static final String GOODS_SELECT_BY_CODE =PRE_URL+"App/Index/selectStockByCode";
	
	//获取我的分类
	public static final String GET_STOCK_CATEGORY =PRE_URL+"App/Index/getMyStockCategory";
	
	//获取指定分类下的商品列表
	public static final String GET_STOCK_LIST_BY_CATEGORY = PRE_URL + "App/Index/getStockListByCategory";
	
	//结算
	public static final String DO_CLEARING = PRE_URL + "App/Index/doClearing";
	
	//查找会员by phone
	public static final String SEARCH_MEMBER_BY_PHONE = PRE_URL + "App/Index/selectMemberByPhone";
	
	//添加会员
	public static final String ADD_MEMBER = PRE_URL + "App/Index/addMemberWithName2Phone";
	
	
	
	
	//------------------------------------------分隔线---------------------------------------------------
	//获取本部门下所有用户信息
	public static final String GET_DEPT_TB_USER = PRE_URL + "App/Sync/pullDeptTbUser";
	
	//获取远程的库存表信息
	public static final String GET_TB_STOCK = PRE_URL +"App/Sync/pullTbStock";
	
	//获取远程会员表信息
	public static final String GET_TB_MEMBER = PRE_URL +"App/Sync/pullTbMember";
	
	//同步tb_detail 数据
	public static final String SYNC_TB_DETAIL = PRE_URL +"App/Sync/syncTbDetail";
	
	
	
	
	
	
	
	
}
