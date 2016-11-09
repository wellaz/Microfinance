package soc.commission.swip;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import soc.helpers.SetDateCreated;

public class Check {
	ResultSet rs;
	Statement stm;

	public Check(ResultSet rs, Statement stm) {
		this.stm = stm;
		this.rs = rs;

	}

	public ArrayList<String> months() {
		ArrayList<String> arr = new ArrayList<>();
		String check = "SELECT month_of FROM swip_check WHERE year = '" + new SetDateCreated().getYear() + "'";
		try {
			rs = stm.executeQuery(check);
			if (!rs.next()) {

			} else {
				do {
					arr.add(rs.getString(1));
				} while (rs.next());
			}
		} catch (SQLException ee) {

		}
		return arr;
	}

	public boolean isMonthValid(String acc) {
		List<String> mnTypes = null;
		int size = months().size();
		String[] dat = new String[size];
		for (int i = 0; i < size; i++)
			dat[i] = months().get(i);
		mnTypes = Arrays.asList(dat);
		return mnTypes.stream().anyMatch(t -> acc.equals(t));
	}

	public ArrayList<String> ids(String month) {
		ArrayList<String> arr = new ArrayList<>();
		String check = "SELECT member_id FROM debts WHERE month_of = '" + month + "' AND year = '"
				+ new SetDateCreated().getYear() + "'";
		try {
			rs = stm.executeQuery(check);
			if (!rs.next()) {

			} else {
				do {
					arr.add(rs.getString(1));
				} while (rs.next());
			}
		} catch (SQLException ee) {

		}
		return arr;
	}

	public boolean isIdValid(String month, String acc) {
		List<String> mnTypes = null;
		int size = ids(month).size();
		String[] dat = new String[size];
		for (int i = 0; i < size; i++)
			dat[i] = ids(month).get(i);
		mnTypes = Arrays.asList(dat);
		return mnTypes.stream().anyMatch(t -> acc.equals(t));
	}

}
