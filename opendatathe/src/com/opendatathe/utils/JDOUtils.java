package com.opendatathe.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.google.appengine.api.datastore.Key;

/**
 * 
 * @author <a href="mailto:diogo@sertaogames.com">Diogo Vinícius</a>
 * 
 */
public class JDOUtils {

	public static final PersistenceManagerFactory pmfInstance = JDOHelper.getPersistenceManagerFactory("transactions-optional");

	private PersistenceManager pm;

	private Transaction tx;

	public void beginTransaction() {
		tx = pm.currentTransaction();
		tx.begin();
	}

	public void commit() {
		tx.commit();
	}

	public JDOUtils() {
		pm = pmfInstance.getPersistenceManager();
		// pm.setDetachAllOnCommit(true);
		// pm.setCopyOnAttach(false);
	}

	public <T> T save(T entity) throws IOException {
		return pm.makePersistent(entity);
	}

	public <T> void delete(T entity) throws IOException {
		pm.deletePersistent(entity);
	}

	public <T> void deleteById(Class<T> cls, long id) throws IOException {
		T obj = pm.getObjectById(cls, id);
		delete(obj);
	}

	public <T> T detatch(T entity) throws IOException {
		return pm.detachCopy(entity);
	}

	public <T> void refresh(T entity) throws IOException {
		pm.refresh(entity);
	}

	public <T> void saveALL(List<T> entitys) throws IOException {
		pm.makePersistentAll(entitys);
	}

	public <T> T findById(Class<T> cls, Object key) {
		try {
			return pm.getObjectById(cls, key);
		} catch (JDOObjectNotFoundException e) {
			e.printStackTrace();
		} finally {
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(Class<T> clazz) {
		String query = "select from " + clazz.getName();
		return ((List<T>) pm.newQuery(query).execute());
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findByAttribute(Class<T> class1, String attribute, String value) {
		// TODO mudar para prepared query
		String query = "select from " + class1.getName() + " where " + attribute + " == \"" + value + "\"";
		List<T> list = (List<T>) pm.newQuery(query).execute();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findByAttributeInAnyPosition(Class<T> class1, String attribute, String value) {
		// TODO mudar para prepared query
		String query = "select from " + class1.getName() + " where " + attribute + " == \"%" + value + "%\"";
		System.out.println(query);
		List<T> list = (List<T>) pm.newQuery(query).execute();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findByAttribute(Class<T> class1, String attribute, Boolean value) {
		// TODO mudar para prepared query
		String query = "select from " + class1.getName() + " where " + attribute + " == " + value;
		List<T> list = (List<T>) pm.newQuery(query).execute();
		return list;
	}
	
//	/**
//	 * Percorre o ultimo parametro adicionando-os alternativamente como par chave-valor na busca.
//	 * @param class1
//	 * @param sequence
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public <T> List<T> findByAttributes(Class<T> class1, String... sequence) {
//		// TODO mudar para prepared query
//		String query = "select from " + class1.getName() + " where ";// + attribute + " == \"" + value + "\"";\
//		for (int i = 0; i < sequence.length;) {
//			query = query.concat(sequence[i]+" == \""+ sequence[i+1]+"\"");
//			i = i + 2;
//		}
//		List<T> list = (List<T>) pm.newQuery(query).execute();
//		return list;
//	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findByAttribute(Class<T> class1, String attribute, Long long1) {
		// TODO mudar para prepared query
		String query = "select from " + class1.getName() + " where " + attribute + " == " + long1;
		List<T> list = (List<T>) pm.newQuery(query).execute();
		return list;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findByAttributes(Class<T> clazz, Map<String, Object> params) {
		String query = "select from " + clazz.getName();
		if (!params.isEmpty()) {
			query += " where ";
			Object[] k = params.keySet().toArray();
			for (int i = 0; i < params.size(); i++) {
				Object key = k[i];
				if (i > 0) {
					query += " && ";
				}
				query += key + " == " + params.get(key);
			}
		}
		List<T> list = (List<T>) pm.newQuery(query).execute();
		return list;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findByAttribute(Class<T> clazz, String attribute, Key value) {
		Query q = pm.newQuery(clazz);
		q.setFilter(attribute + " == value");
		q.declareParameters(Key.class.getName() + " value");

		List<T> list = (List<T>) q.execute(value);

		return list;
	}

	@SuppressWarnings("unchecked")
	public <T> T findFirstByAttribute(Class<T> clazz, String attribute, String value) {
		// TODO mudar pra prepared query
		String query = "select from " + clazz.getName() + " where " + attribute + " == \"" + value + "\"";
		List<T> list = (List<T>) pm.newQuery(query).execute();
		boolean objectNotFound = list == null || list.isEmpty();
		return objectNotFound ? null : list.get(0);
	}

	public void close() {
		pm.close();
	}

	public PersistenceManager getPm() {
		return pm;
	}

}
