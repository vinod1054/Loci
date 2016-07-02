package loci.vk.com.myapplication.TestPurpose;

import loci.vk.com.myapplication.application.BasePresenter;

/**
 * Created by vinod on 15/2/16.
 */
public interface UsersPresenter extends BasePresenter{
    void onUserClick(int id);
    void onAddNewUserClick();
}
