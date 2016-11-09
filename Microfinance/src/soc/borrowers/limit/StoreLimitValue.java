package soc.borrowers.limit;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Wellington
 */
public class StoreLimitValue {
	Statement stm;
	ResultSet rs;

	public StoreLimitValue(Statement stm, ResultSet rs) {
		this.stm = stm;
		this.rs = rs;
	}

	public ArrayList<Double> storeLimitValue() {
		ArrayList<Double> values = new ArrayList<>();
		values.add(getFirstLimitValue());
		return values;
	}

	public double getFirstLimitValue() {
		GetLimitValue getlimt = new GetLimitValue(stm, rs);
		return getlimt.getLimitValue();
	}

	public double actualLimitValue() {
		ArrayList<Double> list = storeLimitValue();
		int listsize = list.size();
		return list.get(listsize - 1);
	}

}
