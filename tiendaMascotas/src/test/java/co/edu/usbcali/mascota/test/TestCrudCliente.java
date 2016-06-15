package co.edu.usbcali.mascota.test;

import static org.junit.Assert.assertNotNull;

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

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCrudCliente {
	private static final Logger log = LoggerFactory.getLogger(TestCrudCliente.class);

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
	 * Crear Cliente
	 */
	@Test
	public void testA() {
		Session session = sessionFactory.openSession();

		Clientes clientes = new Clientes();
		clientes.setCliNombre("juanito pedroloña");
		clientes.setCliCedula(1234L);
		clientes.setCliDireccion("avenida perdicion");
		clientes.setCliTelefono("122221");

		session.getTransaction().begin();
		session.save(clientes);
		session.getTransaction().commit();

		session.close();
	}

	/**
	 * Consulta el cliente
	 */
	@Test
	public void testB() {

		Session session = sessionFactory.openSession();

		Clientes clientes = (Clientes) session.get(Clientes.class, 1234L);

		assertNotNull("El cliente no existe", clientes);

		log.info("" + clientes.getCliNombre());
		log.info("" + clientes.getCliDireccion());
		log.info("" + clientes.getCliTelefono());
		log.info("" + clientes.getCliCedula());

		session.close();
	}

	/**
	 * Modificar Cliente
	 */
	@Test
	public void testC() {

		Session session = sessionFactory.openSession();

		Clientes clientes = (Clientes) session.get(Clientes.class, 1234L);
		assertNotNull("El cliente no existe", clientes);

		clientes.setCliDireccion("Avenida Siempre Viva 123");
		clientes.setCliNombre("Homero J Simpson");
		clientes.setCliTelefono("55555555");

		session.getTransaction().begin();
		session.update(clientes);
		session.getTransaction().commit();

		session.close();
	}

	/**
	 * Borrar Cliente
	 */
	@Test
	public void testD() {

		Session session = sessionFactory.openSession();

		// String hql="delete FROM Clientes cli WHERE cli.cliNombre=:nombre";

		Clientes clientes = (Clientes) session.get(Clientes.class, 1234L);
		assertNotNull("El cliente no existe", clientes);

		session.getTransaction().begin();
		session.delete(clientes);
		session.getTransaction().commit();

		session.close();
	}

}
