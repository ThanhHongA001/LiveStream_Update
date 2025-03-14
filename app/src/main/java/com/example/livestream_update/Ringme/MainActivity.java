package com.example.livestream_update.Ringme;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.vtm.databinding.RmActivityMainBinding;
import com.vtm.ringme.ExtraHomeFragment;
import com.vtm.ringme.HomeNewAdapter;
import com.vtm.ringme.api.HomeApi;
import com.vtm.ringme.api.response.LiveStreamResponse;
import com.vtm.ringme.base.BaseAdapter;
import com.vtm.ringme.common.api.http.HttpCallBack;
import com.vtm.ringme.livestream.listener.HomeListener;
import com.vtm.ringme.model.HomePage;
import com.vtm.ringme.values.Constants;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements HomeListener {

    private HomeNewAdapter adapter;
    private RmActivityMainBinding binding;

    private ArrayList<HomePage> listHomePage;
    private HomeApi mApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=RmActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (listHomePage == null) {
            listHomePage = new ArrayList<>();
        } else {
            listHomePage.clear();
        }
        mApi = HomeApi.getInstance();



        adapter = new HomeNewAdapter(this, this);
        BaseAdapter.setupVerticalRecycler(this, binding.rcvList, null, adapter, true);
        adapter.setItems(listHomePage);


        listHomePage=getListDefault();
        callApiGetListLivestream();

    }

    private ArrayList<HomePage> getListDefault() {
        ArrayList<HomePage> list = new ArrayList<>();
        list.add(0, new HomePage(1, Constants.Intent.TYPE_LIVE_STREAM, 1));
        list.add(1, new HomePage(2, Constants.Intent.TYPE_PACKGAE, 2));
        list.add(2, new HomePage(3, Constants.Intent.TYPE_MOVIE, 3));
        list.add(3, new HomePage(4, Constants.Intent.TYPE_VIDEO, 4));
        list.add(4, new HomePage(5, Constants.Intent.TYPE_GAME_DASH, 5));
        list.add(5, new HomePage(6, Constants.Intent.TYPE_GAME, 6));
        list.add(6, new HomePage(7, Constants.Intent.TYPE_MUSIC, 7));
        return list;
    }


    private void callApiGetListLivestream() {
        mApi.getListLivestream(1, 0, new HttpCallBack() {
            @Override
            public void onSuccess(String data) throws Exception {
                try {
                    Gson gson = new Gson();
                    LiveStreamResponse response = gson.fromJson(data, LiveStreamResponse.class);
                    if (response != null && response.getListLivStream() != null) {
                        if (listHomePage != null) {

                            for (int i = 0; i < listHomePage.size(); i++) {

                                if (listHomePage.get(i).getCategory().equals(Constants.Intent.TYPE_LIVE_STREAM)) {
                                    listHomePage.get(i).setListLive(response.getListLivStream());

                                    adapter.setItems(listHomePage);
                                    adapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String message) {
                super.onFailure(message);
            }
        });
    }

    @Override
    public void onClickNextButton(String type) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ExtraHomeFragment extraHomeFragment = new ExtraHomeFragment(Constants.Intent.stream);
        fragmentTransaction.replace(binding.homeFragmentContainer.getId(), extraHomeFragment);
        fragmentTransaction.addToBackStack("extraHomeFragment");
        fragmentTransaction.commit();

    }

    @Override
    public void onClickNotifyMe(String id, long time, int positon, boolean notify) {

    }


}