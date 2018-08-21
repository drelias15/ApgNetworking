package ApgNetworking.models;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Collection;

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

	private String crn;
	private String description;
	private Boolean active;

	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
	public Collection<UserCourse> userCourses;

	public Course() {
	}

	public Course(@Size(min = 1, max = 20) String name, @Size(min = 1, max =
			20) String semester, String crn, String description, Boolean
			active) {
		this.name = name;
		this.semester = semester;
		this.crn = crn;
		this.description = description;
		this.active = active;
	}

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

	public String getCrn() {
		return crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Collection<UserCourse> getUserCourses() {
		return userCourses;
	}

	public void setUserCourses(Collection<UserCourse> userCourses) {
		this.userCourses = userCourses;
	}
}
