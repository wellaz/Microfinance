/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soc.helpers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import com.toedter.calendar.JDateChooser;

import javafx.util.StringConverter;

/**
 *
 * @author Wellington
 */
public class SetDateCreated {

	public String setDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date dates = new Date();
		String text = dateFormat.format(dates);
		return text;
	}

	public StringConverter<LocalDate> dateForm() {
		StringConverter<LocalDate> dt = new StringConverter<LocalDate>() {
			private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

			@Override
			public String toString(LocalDate t) {
				if (t == null)
					return "";
				return dateTimeFormatter.format(t);
			}

			@Override
			public LocalDate fromString(String string) {
				if (string == null || string.trim().isEmpty()) {
					return null;
				}
				return LocalDate.parse(string, dateTimeFormatter);
			}
		};
		return dt;
	}

	public String getTime() {
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		DateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss");
		return dateFormatter.format(date);
	}

	public String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date1 = new Date();
		return dateFormat.format(date1);
	}

	public String getYesterdayDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return dateFormat.format(cal.getTime());
	}

	public String timeStamp() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH-mm-ss");
		Date dates = new Date();
		String text = dateFormat.format(dates);
		return text;
	}

	public String getYear() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, 0);
		return dateFormat.format(cal.getTime());
	}

	public String getThirtythDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 30);
		return dateFormat.format(cal.getTime());
	}

	public int getMonth() {
		Date date = new Date();
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return localDate.getMonthValue();
	}

	public Date today() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_YEAR, 0);
		Date when = cal.getTime();
		return when;
	}

	public String getExactDate(JDateChooser whendate) {
		Date d = whendate.getDate();
		String date = String.format("%1$tY-%1$tm-%1$td", d);
		return date;
	}

}
