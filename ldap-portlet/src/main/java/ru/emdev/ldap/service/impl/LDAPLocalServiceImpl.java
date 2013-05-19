package ru.emdev.ldap.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import org.apache.directory.server.constants.ServerDNConstants;
import org.apache.directory.server.core.DefaultDirectoryService;
import org.apache.directory.server.core.DirectoryService;
import org.apache.directory.server.core.partition.Partition;
import org.apache.directory.server.core.partition.impl.btree.jdbm.JdbmIndex;
import org.apache.directory.server.core.partition.impl.btree.jdbm.JdbmPartition;
import org.apache.directory.server.core.partition.ldif.LdifPartition;
import org.apache.directory.server.core.schema.SchemaPartition;
import org.apache.directory.server.ldap.LdapServer;
import org.apache.directory.server.protocol.shared.transport.TcpTransport;
import org.apache.directory.server.xdbm.Index;
import org.apache.directory.shared.ldap.entry.ServerEntry;
import org.apache.directory.shared.ldap.name.DN;
import org.apache.directory.shared.ldap.schema.SchemaManager;
import org.apache.directory.shared.ldap.schema.ldif.extractor.SchemaLdifExtractor;
import org.apache.directory.shared.ldap.schema.loader.ldif.LdifSchemaLoader;
import org.apache.directory.shared.ldap.schema.manager.impl.DefaultSchemaManager;
import org.apache.directory.shared.ldap.schema.registries.SchemaLoader;

import ru.emdev.ldap.service.base.LDAPLocalServiceBaseImpl;
import ru.emdev.ldap.util.EmDevSchemaLdifExtractor;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.PropsUtil;

/**
 * The implementation of the LDAP local service.
 */
public class LDAPLocalServiceImpl extends LDAPLocalServiceBaseImpl {
	private static final Log log = LogFactoryUtil
			.getLog(LDAPLocalServiceImpl.class);
	private static final int PORT_NUM = 10389;

	private DirectoryService directoryService;
	private LdapServer ldapServer;

	@Override
	public void startLDAPServer() throws SystemException {
		if (ldapServer != null) {
			log.warn("LDAP Server already started");
			return;
		}

		try {
			// Use liferay's DATA to store LDAP Info
			String pathToLDAP = PropsUtil.get("liferay.home") + "/data/ldap";

			// create directory
			File workingDir = new File(pathToLDAP);
			if (!workingDir.exists()) {
				workingDir.mkdirs();
			}

			initDirectoryService(workingDir);

			ldapServer = new LdapServer();
			ldapServer.setDirectoryService(directoryService);

			// Set LDAP port to 10389
			int portNum = PORT_NUM;
			TcpTransport ldapTransport = new TcpTransport(portNum);
			ldapServer.setTransports(ldapTransport);

			ldapServer.start();

			log.info("LDAP server started on port " + portNum);
		} catch (Exception ex) {
			log.error("Cannot start LDAP server", ex);
			ldapServer = null;
			directoryService = null;
			throw new SystemException("Cannot start LDAP server", ex);
		}
	}

	@Override
	public void stopLDAPServer() throws SystemException {
		if (ldapServer != null) {
			try {
				ldapServer.stop();
				directoryService.shutdown();

				ldapServer = null;
				directoryService = null;
			} catch (Exception ex) {
				throw new SystemException("Cannot stop LDAP server", ex);
			}
		}
	}

	@Override
	public boolean isLDAPServerStarted() {
		return ldapServer != null;
	}

	// followed code got from
	// http://svn.apache.org/repos/asf/directory/documentation/samples/trunk/embedded-sample/src/main/java/org/apache/directory/seserver/EmbeddedADSVer157.java
	/**
	 * Add a new partition to the server
	 * 
	 * @param partitionId
	 *            The partition Id
	 * @param partitionDn
	 *            The partition DN
	 * @return The newly added partition
	 * @throws Exception
	 *             If the partition can't be added
	 */
	private Partition addPartition(String partitionId, String partitionDn)
			throws Exception {
		// Create a new partition named 'foo'.
		JdbmPartition partition = new JdbmPartition();
		partition.setId(partitionId);
		partition.setPartitionDir(new File(directoryService
				.getWorkingDirectory(), partitionId));
		partition.setSuffix(partitionDn);
		directoryService.addPartition(partition);

		return partition;
	}

	/**
	 * Add a new set of index on the given attributes
	 * 
	 * @param partition
	 *            The partition on which we want to add index
	 * @param attrs
	 *            The list of attributes to index
	 */
	private void addIndex(Partition partition, String... attrs) {
		// Index some attributes on the apache partition
		HashSet<Index<?, ServerEntry, Long>> indexedAttributes = new HashSet<Index<?, ServerEntry, Long>>();

		for (String attribute : attrs) {
			indexedAttributes
					.add(new JdbmIndex<String, ServerEntry>(attribute));
		}

		((JdbmPartition) partition).setIndexedAttributes(indexedAttributes);
	}

	/**
	 * initialize the schema manager and add the schema partition to dieectory
	 * service
	 * 
	 * @throws Exception
	 *             if the schema LDIF files are not found on the classpath
	 */
	private void initSchemaPartition() throws Exception {
		SchemaPartition schemaPartition = directoryService.getSchemaService()
				.getSchemaPartition();

		// Init the LdifPartition
		LdifPartition ldifPartition = new LdifPartition();
		String workingDirectory = directoryService.getWorkingDirectory()
				.getPath();
		ldifPartition.setWorkingDirectory(workingDirectory + "/schema");

		// Extract the schema on disk (a brand new one) and load the registries
		File schemaRepository = new File(workingDirectory, "schema");
		SchemaLdifExtractor extractor = new EmDevSchemaLdifExtractor(new File(
				workingDirectory));

		try {
			extractor.extractOrCopy();
		} catch (IOException ioe) {
			// The schema has already been extracted, bypass
		}

		schemaPartition.setWrappedPartition(ldifPartition);

		SchemaLoader loader = new LdifSchemaLoader(schemaRepository);
		SchemaManager schemaManager = new DefaultSchemaManager(loader);
		directoryService.setSchemaManager(schemaManager);

		// We have to load the schema now, otherwise we won't be able
		// to initialize the Partitions, as we won't be able to parse
		// and normalize their suffix DN
		schemaManager.loadAllEnabled();

		schemaPartition.setSchemaManager(schemaManager);

		List<Throwable> errors = schemaManager.getErrors();

		if (errors.size() != 0) {
			throw new Exception("Schema load failed : " + errors);
		}
	}

	private void initDirectoryService(File workDir) throws Exception {
		// Initialize the LDAP service
		directoryService = new DefaultDirectoryService();
		directoryService.setWorkingDirectory(workDir);

		// first load the schema
		initSchemaPartition();

		// then the system partition
		// this is a MANDATORY partition
		Partition systemPartition = addPartition("system",
				ServerDNConstants.SYSTEM_DN);
		directoryService.setSystemPartition(systemPartition);

		// Disable the ChangeLog system
		directoryService.getChangeLog().setEnabled(false);
		directoryService.setDenormalizeOpAttrsEnabled(true);

		Partition apachePartition = addPartition("apache", "dc=apache,dc=org");

		// Index some attributes on the apache partition
		addIndex(apachePartition, "objectClass", "ou", "uid");

		// And start the service
		directoryService.startup();

		// Inject the apache root entry
		if (!directoryService.getAdminSession().exists(
				apachePartition.getSuffixDn())) {
			DN dnApache = new DN("dc=Apache,dc=Org");
			ServerEntry entryApache = directoryService.newEntry(dnApache);
			entryApache.add("objectClass", "top", "domain", "extensibleObject");
			entryApache.add("dc", "Apache");
			directoryService.getAdminSession().add(entryApache);
		}

		// We are all done !
	}

}
