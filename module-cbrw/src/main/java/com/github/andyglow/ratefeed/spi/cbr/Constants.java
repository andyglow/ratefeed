package com.github.andyglow.ratefeed.spi.cbr;

import java.util.Currency;

public interface Constants {

    public static final Currency HOME_CURRENCY  = Currency.getInstance("RUR");

	static final String HOST                    = "www.cbr.ru";
	static final String WS_HOST                 = "web.cbr.ru";
	static final String WS_PATH                 = "/DailyInfoWebServ/DailyInfo.asmx";
	static final String SCRIPT_PATH             = "/scripts/XML_daily.asp";
	static final String DYNAMICS_SCRIPT_PATH    = "/scripts/XML_dynamic.asp";
	
}
