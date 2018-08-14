package ApgNetworking.models;

import java.util.Collection;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name="APGCourse")
public class Course {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Size(min=1, max=20)
	private String name;

	@Size(min=1, max=20)
	private String semester;

	private boolean active;
	private String crn;

	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
	public Collection<UserCourse> userCourses;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSemester() {
		return semester;
	}
	public void setSemester(String semester) {
		this.semester = semester;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}

	public String getCrn() {
		return crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public Collection<UserCourse> getUserCourses() {
		return userCourses;
	}

	public void setUserCourses(Collection<UserCourse> userCourses) {
		this.userCourses = userCourses;
	}
}
