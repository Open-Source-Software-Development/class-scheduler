package osd.database;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

public class TestRunner {

	private static SessionFactory factory;
	public static void main(String[] args) {
		try {
			factory = new AnnotationConfiguration().configure().addAnnotatedClass(ProfessorRecord.class).buildSessionFactory();
		}catch(Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
	         throw new ExceptionInInitializerError(ex); 
		}
		TestRunner runner = new TestRunner();
		runner.listProf();
		System.out.println("end");
	}
	public void listProf() {
		Session session = factory.openSession();
	    Transaction tx = null;
	    System.out.println("Start");
	    try {
	    	tx = session.beginTransaction();
	        List professors = session.createQuery("FROM scheduler_professor SELECT *").list();
	        System.out.println(professors);
	        for (Iterator iterator = professors.iterator(); iterator.hasNext();){
	        	ProfessorRecord prof = (ProfessorRecord) iterator.next(); 
	            System.out.print("First Name: " + prof.getFirstName()); 
	            System.out.print("  Last Name: " + prof.getLastName()); 
	            System.out.println("  Div: " + prof.getDivisionId()); 
	        }
	        tx.commit();
	    } catch (HibernateException e) {
	    	if (tx!=null) tx.rollback();
	        e.printStackTrace(); 
	    } finally {
	    	session.close(); 
	    }
	}
}
 