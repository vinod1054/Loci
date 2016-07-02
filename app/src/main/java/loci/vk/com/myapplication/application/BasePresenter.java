package loci.vk.com.myapplication.application;

/**
 * Created by vinod on 15/2/16.
 */
public interface BasePresenter {
    void setView(Object view);
    void clearView();
    void closeRealm();
}
