package com.grid.hdsyt.print;

public class Const {
 
	public static final String print_Port = "9100";
	public static final String print_IP = "192.168.1.87";
	public static final String MD_Name = "SYSTEM_GRID";
	
	
	
	/**
	 * 打印demo
	 */
	private void demoPrint(){
		
		NetPrinter printer = new NetPrinter();
		printer.Open(Const.print_IP, Integer.parseInt(Const.print_Port));
		
		printer.PrintText("聚商城.收银台", 1, 1, 0);
		printer.PrintEnter();				
		printer.PrintText("------------------------------------------------", 0, 0, 0);
		printer.PrintEnter();
		printer.PrintText("订单号：000000000　　　　操作员：系统管理员", 0, 0, 0);
		printer.PrintEnter();
		printer.PrintText("------------------------------------------------", 0, 0, 0);
		printer.PrintEnter();
		printer.PrintText("商品　　　　单价　　 数量　　小计", 0, 0, 0);
		printer.PrintEnter();
		printer.PrintText("------------------------------------------------", 0, 0, 0);
		printer.PrintEnter();
		printer.PrintText("示例商品1　　　　2.00　　5　　10.0", 0, 0, 0);
		printer.PrintEnter();
		printer.PrintText("示例商品2　　　　2.00　　5　　10.0", 0, 0, 0);
		printer.PrintEnter();
		printer.PrintText("示例商品3　　　　2.00　　5　　10.0", 0, 0, 0);
		printer.PrintEnter();
		printer.PrintText("------------------------------------------------", 0, 0, 0);
		printer.PrintEnter();
		printer.PrintText("总金额　30.0", 2, 1, 0);
		printer.PrintEnter();
		printer.PrintText("------------------------------------------------", 0, 0, 0);
		printer.PrintEnter();
		printer.PrintText("2015-12-30 11:03:00", 2, 0, 0);
		printer.PrintEnter();
		printer.PrintText("谢谢惠顾", 1, 1, 0);
		printer.PrintNewLines(5);
		printer.CutPage(0);
		printer.Close();
		
	}
	
}
