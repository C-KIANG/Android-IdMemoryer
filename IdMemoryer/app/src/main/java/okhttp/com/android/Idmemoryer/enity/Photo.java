package okhttp.com.android.Idmemoryer.enity;

public class Photo {
    private Integer id;
    private String name;
    private String url;
    private Boolean selectResult;

    public Photo(String name) {
        this.name = name;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getSelectResult() {
        return selectResult;
    }

    public void setSelectResult(Boolean selectResult) {
        this.selectResult = selectResult;
    }
}
