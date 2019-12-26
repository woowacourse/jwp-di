package nextstep.jdbc;

public class KeyHolder {
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "KeyHolder{" +
                "id=" + id +
                '}';
    }
}
