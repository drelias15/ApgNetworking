package ApgNetworking.models;

import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Entity
@Table(name="APGUser")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Email
	private String email;
	@Size(min=1, max=20)
	private String firstName;
	@Size(min=1, max=20)
	private String lastName;
	@Size(min=1, max=20)
	private String username;
	private String password;
	private String picUrl;
	private String linkedIn;
	private String github;
	private boolean enabled;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Collection<Role> roles;

	@OneToMany(mappedBy = "apguser", cascade = CascadeType.ALL)
	public Collection<Post> posts;

	@OneToMany(mappedBy = "apguser", cascade = CascadeType.ALL)
	public Collection<UserCourse> userCourses;

	public User(){

	}

	public User(String email, String firstName, String lastName, String username, String password, boolean enabled) {
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.enabled = enabled;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Collection<Role> getRoles() {
		return roles;
	}
	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getLinkedIn() {
		return linkedIn;
	}
	public void setLinkedIn(String linkedIn) {
		this.linkedIn = linkedIn;
	}
	public String getGithub() {
		//if left of github is not https then append https or http
		return github;
	}
	public void setGithub(String github) {
		this.github = github;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public void addRole(Role role)
	{
		this.roles.add(role);
	}

	public Collection<Post> getPosts() {
		return posts;
	}

	public void setPosts(Collection<Post> posts) {
		this.posts = posts;
	}

	public Collection<UserCourse> getUserCourses() {
		return userCourses;
	}

	public void setUserCourses(Collection<UserCourse> userCourses) {
		this.userCourses = userCourses;
	}
}
