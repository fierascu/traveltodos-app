package com.traveltodos.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	public static String getTimeStampAsTime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

}
