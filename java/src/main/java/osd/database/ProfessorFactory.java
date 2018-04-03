package osd.database;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class ProfessorFactory {
	
	private static SessionFactory factory;
	public static void main(String[] args) {
		
		try {
			factory = new Configuration().configure().buildSessionFactory();
		} catch(Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex); 
		}

		ProfessorFactory profFactory = new ProfessorFactory();
		profFactory.listProf();
	}
	
	public void listProf() {
		Session session = factory.openSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			session.isConnected();
			List professors = session.createQuery("FROM ProfessorRecord").list();
			for(Iterator iterator = professors.iterator(); iterator.hasNext();) {
				ProfessorRecord prof = (ProfessorRecord) iterator.next();
				System.out.print("Name: " + prof.getFirstName() + " " + prof.getLastName());
				System.out.print(" Division: " + prof.getDivisionId());
				System.out.println(" ID: " + prof.getId());
			}
		} catch (HibernateException e) {
			if (tx!=null) tx.rollback();
	        e.printStackTrace(); 
		} finally {
			session.close();
		}
	}

}
