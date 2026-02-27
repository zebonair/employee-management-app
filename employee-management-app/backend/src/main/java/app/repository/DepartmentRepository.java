package app.repository;

import app.dto.DepartmentCountDto;
import app.model.Department;
import app.model.Employee;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@ApplicationScoped
public class DepartmentRepository {

    @PersistenceContext
    EntityManager em;

    public List<Department> listAllOrdered() {
        return em.createQuery("from Department d order by d.name", Department.class)
                .getResultList();
    }

    public Department find(Long id) {
        return em.find(Department.class, id);
    }

    public void persist(Department d) {
        em.persist(d);
    }

    public void delete(Department d) {
        em.remove(d);
    }

    public List<Employee> listEmployees(Department d) {
        return em.createQuery("from Employee e where e.department = :d order by e.fullName", Employee.class)
                .setParameter("d", d)
                .getResultList();
    }

    public int unassignEmployees(Department d) {
        return em.createQuery("update Employee e set e.department = null where e.department = :d")
                .setParameter("d", d)
                .executeUpdate();
    }

    public List<DepartmentCountDto> countsPerDepartment() {
        return em.createQuery(
                "select new app.dto.DepartmentCountDto(d.id, d.name, count(e.id)) " +
                        "from Department d left join Employee e on e.department = d " +
                        "group by d.id, d.name order by d.name",
                DepartmentCountDto.class
        ).getResultList();
    }

    public Long countUnassigned() {
        return em.createQuery("select count(e.id) from Employee e where e.department is null", Long.class)
                .getSingleResult();
    }

    public Department findByNameIgnoreCase(String name) {
        if (name == null) return null;
        return em.createQuery(
                        "from Department d where lower(d.name) = :n", Department.class)
                .setParameter("n", name.trim().toLowerCase())
                .getResultStream()
                .findFirst()
                .orElse(null);
    }
}