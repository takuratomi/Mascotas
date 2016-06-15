package co.edu.usbcali.mascota.test;

import static org.junit.Assert.fail;

import java.util.List;

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
public class TestHQLCliente {

	private static final Logger log = LoggerFactory.getLogger(TestHQLCliente.class);

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

	@Test
	public void atestConsultarClientes() {
		Session session = sessionFactory.openSession();
		String hql = "SELECT cli FROM Clientes cli";
		List<Clientes> losClientes = session.createQuery(hql).list();
		for (Clientes clientes : losClientes) {
			log.info("" + clientes.getCliNombre());
			log.info("" + clientes.getCliDireccion());
			log.info("" + clientes.getCliTelefono());
			log.info("" + clientes.getCliCedula());
		}
		session.close();
	}

	@Test
	public void btestConsultarClientesPorNombre() {
		Session session = sessionFactory.openSession();
		String hql = "SELECT cli FROM Clientes cli WHERE cli.cliNombre=:nombre";
		List<Clientes> losClientes = session.createQuery(hql).setString("nombre", "William Navarro").list();

		for (Clientes clientes : losClientes) {
			log.info("" + clientes.getCliNombre());
			log.info("" + clientes.getCliDireccion());
			log.info("" + clientes.getCliTelefono());
			log.info("" + clientes.getCliCedula());
		}

		session.close();
	}

	@Test
	public void ctestConsultarClientesPorNombreLike() {
		Session session = sessionFactory.openSession();
		String hql = "SELECT cli FROM Clientes cli WHERE cli.cliNombre LIKE:nombre";
		List<Clientes> losClientes = session.createQuery(hql).setString("nombre", "%W%").list();

		for (Clientes clientes : losClientes) {
			log.info("" + clientes.getCliNombre());
			log.info("" + clientes.getCliDireccion());
			log.info("" + clientes.getCliTelefono());
			log.info("" + clientes.getCliCedula());
		}

		session.close();
	}

	@Test
	public void dtestConsultarCuentasFuncionesMatematicas() {
		Session session = sessionFactory.openSession();
		String hql = "SELECT MAX(cue.cueSaldo),MIN(cue.cueSaldo),AVG(cue.cueSaldo),COUNT(cue),SUM(cue.cueSaldo) FROM Cuentas cue";
		Object arreglo[] = (Object[]) session.createQuery(hql).uniqueResult();

		log.info("Maximo:" + arreglo[0]);
		log.info("Minimo:" + arreglo[1]);
		log.info("Promedio:" + arreglo[2]);
		log.info("Numero de cuentas:" + arreglo[3]);
		log.info("Suma:" + arreglo[4]);

		for (Object object : arreglo) {
			log.info(object.toString());
		}

		session.close();
	}

}
