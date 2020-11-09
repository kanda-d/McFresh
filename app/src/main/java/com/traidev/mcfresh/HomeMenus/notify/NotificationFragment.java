package com.traidev.mcfresh.HomeMenus.notify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.traidev.mcfresh.Home;
import com.traidev.mcfresh.R;
import com.traidev.mcfresh.Utility.Main_Interface;
import com.traidev.mcfresh.Utility.Network.RetrofitClient;
import com.traidev.mcfresh.Utility.SharedPrefManager;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<NotifyViewModel> notify;
    private NotifyAdapter adapter;
    private Main_Interface main_interface;
    private GifImageView progressImg;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notify, container, false);

       // progressImg = root.findViewById(R.id.updateImgProgress);


        recyclerView = root.findViewById(R.id.all_notify);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        fetchNotification();

        return root;
    }


    public void fetchNotification()
    {
        SharedPrefManager sh = new SharedPrefManager(getActivity());

        main_interface = RetrofitClient.getApiClient().create(Main_Interface.class);

        Call<List<NotifyViewModel>> call = main_interface.getNotifications(sh.getsUser().getUid());

        call.enqueue(new Callback<List<NotifyViewModel>>() {
            @Override
            public void onResponse(Call<List<NotifyViewModel>> call, Response<List<NotifyViewModel>> response) {

                    notify = response.body();
                    adapter = new NotifyAdapter(notify,getActivity());
                    recyclerView.setAdapter(adapter);

                //progressImg.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<List<NotifyViewModel>> call, Throwable t) {

            }
        });
    }

}