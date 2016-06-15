package co.edu.usbcali.mascota.test;

import static org.junit.Assert.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.usbcali.modelo.Clientes;
import co.edu.usbcali.modelo.Mascotas;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCrudMascotas {
	private static final Logger log = LoggerFactory.getLogger(TestCrudMascotas.class);
	
	Configuration configuration = new Configuration().configure();
	StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder()
			.applySettings(configuration.getProperties());
	SessionFactory sessionFactory = null;

	@Before
	public void antes() {
		sessionFactory = configuration.buildSessionFactory(standardServiceRegistryBuilder.build());
	}

	@After
	public void despues() {
		sessionFactory.close();
	}

	/**
	 * Crear Mascota
	 */
	@Test
	public void testA() {
		Session session = sessionFactory.openSession();
		Clientes clientes =(Clientes) session.get(Clientes.class,10L);
		
		Mascotas mascotas = new Mascotas();
		mascotas.setClientes(clientes);
		mascotas.setMasCodigo(92L);
		mascotas.setMasNombre("chandita");
		mascotas.setMasEdad(9L);
		

		session.getTransaction().begin();
		session.save(mascotas);
		session.getTransaction().commit();

		session.close();
	}

	/**
	 * Consulta el cliente
	 */
	@Test
	public void testB() {

		Session session = sessionFactory.openSession();
		Mascotas mascotas = (Mascotas) session.get(Mascotas.class, 92L);
				

		assertNotNull("El cliente no existe", mascotas);

		log.info("" + mascotas.getMasCodigo());
		log.info("" + mascotas.getMasEdad());
		log.info("" + mascotas.getMasNombre());
		
		session.close();
	}

	/**
	 * Modificar Cliente
	 */
	@Test
	public void testC() {

		Session session = sessionFactory.openSession();
		
		Mascotas mascotas = (Mascotas) session.get(Mascotas.class, 92L);
		
		assertNotNull("El cliente no existe", mascotas);

		mascotas.setMasEdad(10L);
		mascotas.setMasNombre("franshesca");
		
		session.getTransaction().begin();
		session.update(mascotas);
		session.getTransaction().commit();

		session.close();
	}

	/**
	 * Borrar Cliente
	 */
	@Test
	public void testD() {

		Session session = sessionFactory.openSession();
		
		Mascotas mascotas = (Mascotas) session.get(Mascotas.class,92L);
		
		assertNotNull("El cliente no existe", mascotas);

		session.getTransaction().begin();
		session.delete(mascotas);
		session.getTransaction().commit();

		session.close();
	}

}
