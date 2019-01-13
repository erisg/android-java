package bog.skate.eris.goskate;

public class Post {
    private String postImage;
    private String description;
    private String description1;
    private String getDescription2;

    public Post(String postImage, String description, String description1, String getDescription2) {

        this.postImage = postImage;
        this.description = description;
        this.description1 = description1;
        this.getDescription2 = getDescription2;
    }

    public Post() {
        }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription1() {
        return description1;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }

    public String getGetDescription2() {
        return getDescription2;
    }

    public void setGetDescription2(String getDescription2) {
        this.getDescription2 = getDescription2;
    }
}
