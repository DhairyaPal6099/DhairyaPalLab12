package dhairya.pal.n01576099.lab12;

public class CourseItemModel {
    private String courseName;
    private String courseDescription;

    public CourseItemModel() {}

    public CourseItemModel(String courseName, String courseDescription) {
        this.courseName = courseName;
        this.courseDescription = courseDescription;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public String getCourseDescription() {
        return this.courseDescription;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }
}
