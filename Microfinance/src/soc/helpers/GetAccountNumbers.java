package soc.helpers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
*
* @author Wellington
*/
public class GetAccountNumbers {
	
	ResultSet rs;
	Statement stm;

	public GetAccountNumbers(ResultSet rs, Statement stm) {
		this.stm = stm;
		this.rs = rs;

	}
	public ArrayList<Integer>getAccounts(){
		ArrayList<Integer> list = new ArrayList<>();
		String ql = "SELECT member_id FROM members";
		try {
			rs = stm.executeQuery(ql);
			if(!rs.next()) {
				
			}else {
				do {
					list.add(rs.getInt(1));
				}while(rs.next());
			}
		}catch(SQLException ee) {
			
		}
		
		return list;
	}

}
