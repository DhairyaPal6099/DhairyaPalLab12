package dhairya.pal.n01576099.lab12;

public class CourseItemModel {
    private int courseId;
    private String courseName;
    private String courseDescription;

    public CourseItemModel() {}

    public CourseItemModel(int courseId, String courseName, String courseDescription) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
    }

    public int getCourseId() {
        return this.courseId;
    }
    public String getCourseName() {
        return this.courseName;
    }

    public String getCourseDescription() {
        return this.courseDescription;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }
}
