package com.makanza.job_portals.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
@Entity
@Table(name="jobs")
public class Job {
	   @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String title;
	    private String department;
	    private String location;
	    private Double salary;
	    private int vacancies;
	    private String status;
	    
	    @OneToMany(mappedBy="job", cascade=CascadeType.ALL, orphanRemoval = true)
	    private List<Application> applications = new ArrayList<>();
		public Job() {
			super();
			// TODO Auto-generated constructor stub
		}
		public Job(String title, String department, String location, Double salary, int vacancies, String status) {
			super();
			this.title = title;
			this.department = department;
			this.location = location;
			this.salary = salary;
			this.vacancies=vacancies;
			this.status=status;
			
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getDepartment() {
			return department;
		}
		public void setDepartment(String department) {
			this.department = department;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public Double getSalary() {
			return salary;
		}
		public void setSalary(Double salary) {
			this.salary = salary;
		}
		public List<Application> getApplications() {
			return applications;
		}
		public void setApplications(List<Application> applications) {
			this.applications = applications;
		}
		public int getVacancies() {
			return vacancies;
		}
		public void setVacancies(int vacancies) {
			this.vacancies = vacancies;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		
	    
	   
}
