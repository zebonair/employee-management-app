package app.repository;

import app.model.Employee;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class EmployeeRepository {

    @PersistenceContext
    EntityManager em;

    public Employee find(Long id) {
        return em.find(Employee.class, id);
    }

    public void persist(Employee e) {
        em.persist(e);
    }

    public void delete(Employee e) {
        em.remove(e);
    }

    public List<Object[]> searchByNameLike(String q) {
        return em.createQuery(
                        "select e.id, e.fullName, d.id, d.name from Employee e " +
                                "left join e.department d " +
                                "where lower(e.fullName) like :q order by e.fullName",
                        Object[].class)
                .setParameter("q", "%" + q.toLowerCase() + "%")
                .getResultList();
    }
}