package ApgNetworking.models;

import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Table(name="APGPost")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime posteddate;
    private String title;
    private String link;

    @Size(min=3, max=140)
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    private Course course;

    public Post() {
    }

    public Post(String title, String link, @Size(min = 3, max = 140) String content) {
        this.title = title;
        this.link = link;
        this.content = content;
        LocalDateTime current = LocalDateTime.now();
        this.posteddate = current;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getPosteddate() {
        return posteddate;
    }

    public void setPosteddate() {
        LocalDateTime current = LocalDateTime.now();
        this.posteddate = current;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
