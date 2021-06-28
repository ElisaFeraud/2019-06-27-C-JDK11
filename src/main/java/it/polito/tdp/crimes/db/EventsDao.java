package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.crimes.model.Collegamento;
import it.polito.tdp.crimes.model.Event;
import it.polito.tdp.crimes.model.TipoReato;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
 public List<String> getTipiDiReato(){
	 String sql = "SELECT DISTINCT e.offense_category_id "
	 		+ "FROM events e "
	 		+ "ORDER BY e.offense_type_id ";
	 try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<String> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(res.getString("e.offense_category_id"));
							
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
 }
 public List<Integer> getGiorni(){
	 String sql = "SELECT DISTINCT DAY(e.reported_date) "
	 		+ "FROM events e "
	 		+ "ORDER BY DAY(e.reported_date) ";
	 try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Integer> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(res.getInt("DAY(e.reported_date)"));
							
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
 }
 
 public void getVertici(Map<String,TipoReato> idMap, int giorno, String categoria ) {
	 String sql = "SELECT e.offense_type_id "
	 		+ "FROM events e "
	 		+ "WHERE e.offense_category_id=? AND DAY(e.reported_date)=? ";
	 try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1, categoria);
			st.setInt(2, giorno);
	    	ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					if(!idMap.containsKey(res.getString("e.offense_type_id"))) {
					TipoReato tr = new TipoReato(res.getString("e.offense_type_id"));
					idMap.put(res.getString("e.offense_type_id"), tr);
					}
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
 }
 public List<Collegamento> getArchi(Map<String,TipoReato> idMap, String categoria, int giorno){
	 String sql = "SELECT e1.offense_type_id, e2.offense_type_id, COUNT(DISTINCT e1.precinct_id) AS peso "
	 		+ "FROM events e1, events e2 "
	 		+ "WHERE e1.offense_type_id>e2.offense_type_id AND e1.precinct_id=e2.precinct_id AND e1.offense_category_id=? AND e2.offense_category_id=? AND DAY(e1.reported_date)=DAY(e2.reported_date) AND DAY(e1.reported_date)=? "
	 		+ "GROUP BY e1.offense_type_id, e2.offense_type_id ";
	 List<Collegamento> list = new ArrayList<>() ;	
	 try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1, categoria);
			st.setString(2, categoria);
			st.setInt(3, giorno);
		
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					if(idMap.containsKey(res.getString("e1.offense_type_id")) && idMap.containsKey(res.getString("e2.offense_type_id"))) {
						TipoReato tp1 = idMap.get(res.getString("e1.offense_type_id"));
						TipoReato tp2 = idMap.get(res.getString("e2.offense_type_id"));
						int peso = res.getInt("peso");
						Collegamento collegamento = new Collegamento(tp1,tp2,peso);
						list.add(collegamento);
						
					}					
							
				} catch (Throwable t) {
					t.printStackTrace();
					
				}
			}
			
			conn.close();
			
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
 }
}
