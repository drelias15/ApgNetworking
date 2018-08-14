package ApgNetworking.models;

import javax.persistence.*;

@Entity
public class ApgUserCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;



    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "apguser_id")
    private ApgUser apguser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    private Course course;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ApgUser getApguser() {
        return apguser;
    }

    public void setApguser(ApgUser apguser) {
        this.apguser = apguser;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
