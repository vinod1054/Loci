package loci.vk.com.myapplication.application;

import io.realm.RealmObject;

/**
 * Created by vinod on 15/2/16.
 */
public class BaseModel extends RealmObject {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
