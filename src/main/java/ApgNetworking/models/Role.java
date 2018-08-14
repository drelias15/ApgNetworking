package ApgNetworking.models;

import java.util.Collection;

import javax.persistence.*;


@Entity
@Table(name="APGRole")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String role;
	@ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY)
	private Collection<ApgUser> users;
	public Role(String role) {
		this.role = role;
	}
	public Role() {
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Collection<ApgUser> getUsers() {
		return users;
	}
	public void setUsers(Collection<ApgUser> users) {
		this.users = users;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

}
