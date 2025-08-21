package com.makanza.job_portals.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="applications")
public class Application {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

    private String candidateName;
    private String email;
    
    private String username;
    @Column(length = 2000)
    private String resumeText;

    private int resumeScore;
    private int locationPoints;
    private int finalScore;
    
    private String status="PENDING";
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="myuser_id", nullable=false)
    private MyUser user;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="job_id", nullable=false)
    private Job job;

	public Application() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Application(String candidateName, String email, String  username,String status, String resumeText, int resumeScore, int locationPoints,
			int finalScore, Job job) {
		super();
		this.candidateName = candidateName;
		this.email = email;
		this.resumeText = resumeText;
		this.username=username;
		this.status=status;
		this.resumeScore = resumeScore;
		this.locationPoints = locationPoints;
		this.finalScore = finalScore;
		this.job = job;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCandidateName() {
		return candidateName;
	}

	public void setCandidateName(String candidateName) {
		this.candidateName = candidateName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getResumeText() {
		return resumeText;
	}

	public void setResumeText(String resumeText) {
		this.resumeText = resumeText;
	}

	public int getResumeScore() {
		return resumeScore;
	}

	public void setResumeScore(int resumeScore) {
		this.resumeScore = resumeScore;
	}

	public int getLocationPoints() {
		return locationPoints;
	}

	public void setLocationPoints(int locationPoints) {
		this.locationPoints = locationPoints;
	}

	public int getFinalScore() {
		return finalScore;
	}

	public void setFinalScore(int finalScore) { 
		this.finalScore = finalScore;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public MyUser getUser() {
		return user;
	}

	public void setUser(MyUser user) {
		this.user = user;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
    
}
