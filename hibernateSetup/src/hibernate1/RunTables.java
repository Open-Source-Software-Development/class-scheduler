package hibernate1;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class RunTables {

	public static void main(String[] args) {
		Configuration cfg = new Configuration();
		cfg.configure("Hibernate.cfg.xml");
		SessionFactory sf = cfg.buildSessionFactory();
		Session s = sf.openSession();
		Transaction tx = s.beginTransaction();
		TestTable tester = new TestTable();
		tester.setId(1);
		tester.setFirstName("Al");
		tester.setLastName("Capone");
		tester.setDivision("TST");
		s.save(tester);
		s.flush();
		tx.commit();
		s.close();
	}

}
