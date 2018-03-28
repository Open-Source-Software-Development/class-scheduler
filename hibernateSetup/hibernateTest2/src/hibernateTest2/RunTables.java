package hibernateTest2;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class RunTables {

	private static SessionFactory factory;
	public static void main(String[] args) {
		
		try {
			factory = new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) { 
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex); 
		}
		RunTables tableRunner = new RunTables();
		int tId = tableRunner.addProf("Soren", "Bjergson", "TSM");
		int tId2 = tableRunner.addProf("Hai", "Liam", "C9");
	}
	
	public Integer addProf(String fName, String lName, String div) {
		Session session = factory.openSession();
		Transaction tx = null;
		int profId = -1;
		
		try {
			tx = session.beginTransaction();
			Professor professor = new Professor(fName, lName, div);
			profId = (Integer) session.save(professor);
			tx.commit();
		} catch (HibernateException e) {
			if (tx!=null) tx.rollback();
	        e.printStackTrace(); 
		} finally {
			session.close();
		}
		return profId;
	}
	
	public void listProf() {
		Session session = factory.openSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			List employees = (List) session.createQuery("FROM TestTable");
		} catch (HibernateException e) {
			if (tx!=null) tx.rollback();
	        e.printStackTrace(); 
		} finally {
			session.close();
		}
	}
}