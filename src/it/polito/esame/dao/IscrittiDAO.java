package it.polito.esame.dao;

import it.polito.esame.bean.Corso;
import it.polito.esame.bean.Studente;
import it.polito.esame.bean.StudenteIdMap;
import it.polito.esame.db.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IscrittiDAO {
	
	private Studente createStudente(ResultSet rs) throws SQLException {
		return new Studente(rs.getInt("matricola"),
				rs.getString("cognome"), rs.getString("nome"),
				rs.getString("cds")) ;
	}
	
	private Corso createCorso(ResultSet rs) throws SQLException {
		return new Corso(rs.getString("codins"),
				rs.getInt("crediti"), rs.getString("nome"),
				rs.getInt("pd")) ;
	}

	
	public List<Corso> getAllCorso() {
		final String sql = "SELECT codins, crediti, nome, pd FROM corso";

		List<Corso> result = new ArrayList<Corso>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Corso c = createCorso(rs);
				result.add(c);
			}

			rs.close();
			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public List<Studente> getAllStudente() {
		final String sql = "SELECT matricola, cognome, nome, cds FROM studente";

		List<Studente> result = new ArrayList<Studente>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Studente s = createStudente(rs) ;
				result.add(s);
			}

			rs.close();
			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	public List<ItemIscritto> getAllIscritti() {
		
		final String sql = "SELECT matricola, codins FROM iscrizione";

		List<ItemIscritto> result = new ArrayList<ItemIscritto>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				ItemIscritto iscr = new ItemIscritto(rs.getInt("matricola"),
						rs.getString("codins"));
				result.add(iscr);
			}

			rs.close();
			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	public List<Studente> getStudentiByCorso(Corso c) {
		
		final String sql = "SELECT studente.matricola, cognome, nome, cds " +
				"FROM iscrizione, studente " +
				"WHERE studente.matricola = iscrizione.matricola " +
				"AND iscrizione.codins = ?" ;
		
		List<Studente> result = new ArrayList<Studente>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			st.setString(1, c.getCodins()) ;
			
			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Studente s = createStudente(rs) ;
				result.add(s);
				
				//aggiungo lo studente al set di studenti di quel corso
				c.addStudente(s);
			}

			rs.close();
			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	public Set<Corso> getCorsiByStudente(Studente  s) {
		final String sql = "SELECT iscrizione.codins, crediti, nome, pd " +
				"FROM iscrizione, corso " +
				"WHERE corso.codins = iscrizione.codins " +
				"AND iscrizione.matricola= ? ";

		Set<Corso> result = new HashSet<Corso>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, s.getMatricola()) ;

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Corso c = createCorso(rs);
				result.add(c);
				
				s.addCorso(c);
			}

			rs.close();
			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Main method for TESTING the DAO methods
	 * @param args
	 */
	public static void main(String[] args) {
		IscrittiDAO dao = new IscrittiDAO() ;
		
		List<Studente> tuttiStudenti = dao.getAllStudente() ;
		System.out.println(tuttiStudenti.size()+" studenti") ;
		System.out.println(tuttiStudenti) ;
		
		List<Corso> tuttiCorsi = dao.getAllCorso() ;
		System.out.println(tuttiCorsi.size()+" corsi") ;
		System.out.println(tuttiCorsi) ;

		Studente esempioStudente = tuttiStudenti.get(0) ;
		Set<Corso> alcuniCorsi = dao.getCorsiByStudente(esempioStudente) ;
		System.out.println(alcuniCorsi.size()+" corsi di "+esempioStudente ) ;
		System.out.println(alcuniCorsi) ;
		
		Corso esempioCorso = tuttiCorsi.get(0) ;
		List<Studente> alcuniStudenti = dao.getStudentiByCorso(esempioCorso) ;
		System.out.println(alcuniStudenti.size()+" studenti di "+esempioCorso) ;
		System.out.println(alcuniStudenti) ;
		
		
	}

	public Map<Studente, Integer> getStudentiConCorsiInComune(Studente s, StudenteIdMap map) {
		final String sql = "select s.matricola, count(i.codins) as tot "+
							"from studente as s, iscrizione as i "+
							"where s.matricola = i.matricola and i.codins in (select c.codins "+
																	"from iscrizione as i, corso as c "+
																	"where i.matricola=? and i.codins=c.codins )"+
							"group by s.matricola "+
							"order by tot desc ";
		
		Map<Studente, Integer> result = new HashMap<Studente, Integer>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			st.setInt(1, s.getMatricola());
			
			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Studente stemp = map.get(rs.getInt("matricola"));
				
				if(stemp==null){
					System.out.println("Errore interno\n");
				}
				
				int corsiComuni = rs.getInt("tot");
				
				result.put(stemp, corsiComuni);
			}

			rs.close();
			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}
}
