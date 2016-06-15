package co.edu.usbcali.mascota.test;

import static org.junit.Assert.*;

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
import org.postgresql.util.LruCache.CreateAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.usbcali.modelo.Clientes;
import co.edu.usbcali.modelo.Mascotas;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestConsultaMascotas {

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

	/* mascotas por cliente */
	@Test
	public void atestConsultarMascotasPorCliente() {

		String hql = "SELECT masc from Mascotas masc where masc.clientes.cliCedula=:cedulaCli";
		Long codigoCliente = 10L;
		Session session = sessionFactory.openSession();
		List<Mascotas> lasMascotas = session.createQuery(hql).setLong("cedulaCli", codigoCliente).list();

		for (Mascotas mascotas : lasMascotas) {
			log.info("" + mascotas.getMasCodigo());
			log.info("" + mascotas.getMasEdad());
			log.info("" + mascotas.getMasNombre());
		}

		session.close();
	}

	@Test
	public void btestConsultarMascotasPorNombre() {

		String hql = "SELECT masc FROM Mascotas masc WHERE masc.masNombre=:masNombre";
		String nombre_mascota = "CALBA";
		Session session = sessionFactory.openSession();
		List<Mascotas> lasMascotas = session.createQuery(hql).setString("masNombre", nombre_mascota).list();
		for (Mascotas mascotas : lasMascotas) {
			log.info("" + mascotas.getMasCodigo());
			log.info("" + mascotas.getMasEdad());
			log.info("" + mascotas.getMasNombre());
		}

		session.close();
	}
	
	@Test
	public void ctestConsultarClientesMascotasPorVacuna() {
		String vacu_nombre="POLIO";
		String hql = "SELECT cli FROM Clientes cli, Mascotas masc, VacunasMascotas vamas, Vacunas vacu WHERE cli.cliCedula = masc.clientes.cliCedula AND masc.masCodigo = vamas.id.masCodigo AND vamas.id.vacCodigo = vacu.vacCodigo AND vacu.vacNombre =:nombre GROUP BY(cli.cliCedula) ORDER BY(cli.cliCedula)";

		Session session = sessionFactory.openSession();
		List<Clientes> losClientes = session.createQuery(hql).setString("nombre",vacu_nombre).list();
		for (Clientes clientes : losClientes) {
			log.info("codigoCli: "+clientes.getCliCedula()+" nombre: "+clientes.getCliNombre());
						
		}
		
		
		session.close();
	}

	@Test
	public void dtestConsultarMascotasMasEdad3() {
		String hql = "SELECT masc.masCodigo, masc.masNombre,masc.masEdad, masc.clientes.cliCedula, cli.cliNombre,cli.cliDireccion,cli.cliTelefono FROM Mascotas masc, Clientes cli WHERE masc.clientes.cliCedula = cli.cliCedula and masc.masEdad>3L";

		Session session = sessionFactory.openSession();
		List<Object[]> lasMascotas = session.createQuery(hql).list();

		log.info(
				"codMascota    ||   nombreMascota    || edadMascota   || codCliente   || nombreCliente   ||  direccionCliente   ||   telefonoCliente");
		for (Object[] mascota : lasMascotas) {
			log.info(mascota[0].toString() + " ||  " + mascota[1].toString() + " ||  " + mascota[2].toString() + " ||  "
					+ mascota[3].toString() + " ||  " + mascota[4].toString() + " ||  " + mascota[5].toString()
					+ " ||  " + mascota[6].toString());
		}
		session.close();
	}

	@Test
	public void eTestConsultaMasDeTresMascotas() {
		/*
		 * select cli_cedula, count(mas_codigo) as numeroMascotas from mascotas
		 * group by(cli_cedula) having (count(mas_codigo)>3) order
		 * by(cli_cedula) asc
		 * 
		 */
		Session session = sessionFactory.openSession();

		String hql = "SELECT cli FROM Clientes cli, Mascotas masc, VacunasMascotas vamas, Vacunas vacu WHERE cli.cliCedula = masc.clientes.cliCedula AND masc.masCodigo = vamas.id.masCodigo AND vamas.id.vacCodigo = vacu.vacCodigo AND vacu.vacNombre = 'POLIO' GROUP BY(cli.cliCedula) ORDER BY(cli.cliCedula)";

		List<Clientes> losClientes = session.createQuery(hql).list();
		
		for (Clientes clientes : losClientes) {
			log.info("codigoCli:"+clientes.getCliCedula()+" nombre:"+clientes.getCliNombre());
		}

		session.close();
	}

	@Test
	public void ftestConsultarMascotasPorNombreLike() {
		Session session = sessionFactory.openSession();

		String hql = "SELECT masc FROM Mascotas masc WHERE masc.masNombre LIKE:masNombre";
		List<Mascotas> lasMascotas = session.createQuery(hql).setString("masNombre", "P%").list();

		for (Mascotas mascotas : lasMascotas) {
			log.info("" + mascotas.getMasCodigo());
			log.info("" + mascotas.getMasEdad());
			log.info("" + mascotas.getMasNombre());
		}

		session.close();
	}

	@Test
	public void gtestConsultaEdadMayorMascotas() {
		Session session = sessionFactory.openSession();
		String hql = "SELECT MAX(masEdad) from Mascotas masc";
		Object obj = (Object) session.createQuery(hql).uniqueResult();

		log.info("maximoEdad:" + obj);

		session.close();
	}

	@Test
	public void htestConsultarPromedioEdadesMascotas() {
		Session session = sessionFactory.openSession();

		String hql = "SELECT avg(masc.masEdad) FROM Mascotas masc";
		Object obj = (Object) session.createQuery(hql).uniqueResult();
		log.info("Promedio:" + obj);

		session.close();
	}

}
