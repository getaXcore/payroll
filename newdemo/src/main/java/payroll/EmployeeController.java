package payroll;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class EmployeeController {
	
	private final EmployeeRepository repository;
	private final EmployeeModelAssembler assembler;
	
	public EmployeeController(EmployeeRepository repository, EmployeeModelAssembler assembler) {
		this.repository = repository;
		this.assembler = assembler;
	}
	
	//Aggregate root
	
	@GetMapping("/employees")
	CollectionModel<EntityModel<Employee>>all(){
		//old way
		/*List<EntityModel<Employee>> employees = repository.findAll().stream()
				.map(employee->EntityModel.of(employee,
						linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
						linkTo(methodOn(EmployeeController.class).all()).withRel("employees")))
						.collect(Collectors.toList());
						*/
		//new way
		List<EntityModel<Employee>> employees = repository.findAll().stream()
				.map(assembler::toModel)
				.collect(Collectors.toList());
		
		return CollectionModel.of(employees,
				linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
	}
	
	@PostMapping("/employees")
	ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee) {
		EntityModel<Employee> entityModel = assembler.toModel(repository.save(newEmployee));
		
		return ResponseEntity //for old client will not error to access for get name, although in the employee already add firstname and lastname element
				.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(entityModel);
	}
	
	//Single item
	
	@GetMapping("/employees/{id}")
	EntityModel<Employee> one(@PathVariable Long id) {
		Employee employee = repository.findById(id)
				.orElseThrow(()->new EmployeeNotFoundException(id));
		
		//old way
		/*return EntityModel.of(employee,
				linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel(),
				linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
				*/
		//new way
		return assembler.toModel(employee);
	}
	
	@PutMapping("/employees/{id}")
	ResponseEntity<?> replacEmployee(@RequestBody Employee newEmployee,@PathVariable Long id) {
		Employee updateEmployee = repository.findById(id)
				.map(employee->{
					employee.setName(newEmployee.getName());
					employee.setRole(newEmployee.getRole());
					return repository.save(employee);
				})
				.orElseGet(()->{
					newEmployee.setId(id);
					return repository.save(newEmployee);
				});
		
		EntityModel<Employee> entityModel = assembler.toModel(updateEmployee);
		
		return ResponseEntity //for old client will not error to update name instead firstname and lastname element
				.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(entityModel);
	}
	
	@DeleteMapping("/employees/{id}")
	ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
		repository.deleteById(id);
		
		return ResponseEntity.noContent().build();
	}
}
