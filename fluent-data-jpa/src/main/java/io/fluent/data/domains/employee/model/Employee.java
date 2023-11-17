	package io.fluent.data.domains.employee.model;
	
	import jakarta.persistence.*;
	import jakarta.validation.constraints.*;


	@Entity
	@Table(name = "employees")
	public class Employee {

		@EmbeddedId
		private EmployeeIdentity id;
	
		@NotNull
		@Size(min = 2, message = "First Name should have at least 2 characters")
		private String firstName;
		
		@NotNull
		@Size(min = 2, message = "Last Name should have at least 2 characters")
		private String lastName;
		
		@Email
		@NotBlank
		private String emailId;
		
		@NotNull
		@Size(min = 2, message = "Passport should have at least 2 characters")
		private String passportNumber;
	
		public Employee() {
	
		}
	
		public Employee(String firstName, String lastName, String emailId) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.emailId = emailId;
		}
	

		@Column(name = "first_name", nullable = false)
		public String getFirstName() {
			return firstName;
		}
	
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
	
		@Column(name = "last_name", nullable = false)
		public String getLastName() {
			return lastName;
		}
	
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
	
		@Column(name = "email_address", nullable = false)
		public String getEmailId() {
			return emailId;
		}
	
		public void setEmailId(String emailId) {
			this.emailId = emailId;
		}
	
		@Column(name = "passport_number", nullable = false)
		public String getPassportNumber() {
			return passportNumber;
		}
	
		public void setPassportNumber(String passportNumber) {
			this.passportNumber = passportNumber;
		}

		public EmployeeIdentity getId() {
			return id;
		}

		public void setId(EmployeeIdentity id) {
			this.id = id;
		}
	}
