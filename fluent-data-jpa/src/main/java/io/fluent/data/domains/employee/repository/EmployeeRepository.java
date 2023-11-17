	package io.fluent.data.domains.employee.repository;
	
	import io.fluent.data.domains.employee.model.Employee;
	import org.springframework.data.jpa.repository.JpaRepository;
	import org.springframework.stereotype.Repository;

	import java.util.List;


	@Repository
	public interface EmployeeRepository extends JpaRepository<Employee, Long>{
		List<Employee> findByEmployeeIdentityCompanyId(String companyId);

	}
