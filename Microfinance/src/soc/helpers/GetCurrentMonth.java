package soc.helpers;

import soc.months.MonthsList;

/**
 * @author Wellington
 *
 */
public class GetCurrentMonth {

	public static String currentMonth() {
		String[] da = new String[MonthsList.getMonths().size()];
		for (int i = 0; i < MonthsList.getMonths().size(); i++) {
			da[i] = MonthsList.getMonths().get(i);
		}
		int whichmonth = new SetDateCreated().getMonth() - 1;
		String month = da[whichmonth];
		return month;
	}
}
