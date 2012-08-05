package foo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class PortScanner {
	private String sensorId;
	private String eventDescription; 

	public PortScanner(String sId, String eDesc) {
		sensorId = sId;
		eventDescription = eDesc;
	}

	public String getSensorId() {
		return sensorId;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public String toString() {
		return "sensorid: " + sensorId + " event desc: " + eventDescription;
	}

}

public class PortScan {

	/**
	 * @param args
	 */
	private Connection conn = null;

	public static void main(String[] args) {
		PortScan ps = new PortScan();
		ps.scanPortByRelation("5", "Less Than");
	}

	public PortScan() {
		// TODO Auto-generated method stub
		try {

			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection("jdbc:mysql://localhost/test?"
					+ "user=root&password=root");
			if (conn == null) {
				System.out
						.println("Did not get a connection... you need to investigate");
				return;
			}
			System.out.println("all seems to be ok");

		} catch (ClassNotFoundException cnfd) {
			System.out.println("clnfe exception");
		} catch (SQLException sqle) {
			System.out.println("sqlexception");
		}

	}

	public List<PortScanner> scanPortByRelation(String sensorid, String relation) {

		String sqlStat = "select sensorid, eventdescription from portscan where sensorid";
		List<PortScanner> pscanList = new ArrayList<PortScanner>();

		if (relation.equals("Equals"))
			sqlStat += " = ?";
		else if (relation.equals("Greater Than"))
			sqlStat += " > ?";
		else
			sqlStat += " < ?";

		System.out.println(sqlStat);
		try {
			PreparedStatement ps = conn.prepareStatement(sqlStat);
			ps.setString(1, sensorid);
			System.out.println("after setString");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String sId = rs.getString(1);
				String eDesc = rs.getString(2);
				System.out.println("sensor id:" + sId + " event description:"
						+ eDesc);
				pscanList.add(new PortScanner(sId, eDesc));

			}

			System.out.println("There are:" + pscanList.size()
					+ " entries in the list");

		} catch (SQLException sqle) {
			System.out.println("sql exception thrown" + sqle.getMessage());
		}

		return pscanList;
	}

}
