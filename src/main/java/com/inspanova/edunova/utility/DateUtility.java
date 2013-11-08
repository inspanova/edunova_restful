/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inspanova.edunova.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author krishna
 */
public class DateUtility {
    public static String convertMMDDYYYYToMySQLDate(String dateString)
			throws ParseException {
		Date date = new SimpleDateFormat("MM/dd/yyyy").parse(dateString);
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}
    
}
