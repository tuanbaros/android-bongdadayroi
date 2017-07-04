package app.com.bongdadayroi.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import app.com.bongdadayroi.R;
import app.com.bongdadayroi.activities.AdsActivity;
import app.com.bongdadayroi.activities.EXOMediaActivity;
import app.com.bongdadayroi.adapters.MyAdapter;
import app.com.bongdadayroi.adapters.MyListAdapter;
import app.com.bongdadayroi.models.Category;
import app.com.bongdadayroi.models.CategoryItem;
import app.com.bongdadayroi.models.Data;
import app.com.bongdadayroi.models.MyData;
import app.com.bongdadayroi.models.MyVideo;
import app.com.bongdadayroi.myapp.Config;
import app.com.bongdadayroi.utils.API;
import app.com.bongdadayroi.utils.NewAPI;

public class DrawerFragment extends Fragment {
    private ProgressBar progressBar;
    private ListView listView;
    private LinearLayout llTry;
    private LayoutInflater layoutInflater;
    View footer;
    private CategoryItem categoryItem;
    private int index;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_drawer, container, false);
        index = getArguments().getInt("position");
        if (index > 2) {
            categoryItem = Category.getInstance().getData()[index - 3];
        }
        layoutInflater = LayoutInflater.from(getContext());
        footer = layoutInflater.inflate(R.layout.image_load_more, null);
        setOnClick();
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        listView = (ListView) rootView.findViewById(R.id.lvResult);
        llTry = (LinearLayout) rootView.findViewById(R.id.llTry);
        Button button = (Button) rootView.findViewById(R.id.btRetry);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index > 2) {
                    requestDetailCategory(index, categoryItem.getId());
                }
            }
        });
        setUpListView(index);
        return rootView;
    }

    private void setOnClick() {
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMore(index);
            }
        });
    }

    private void loadMore(int position) {
        MyData myData = MyData.getInstance();
        listView.removeFooterView(footer);
        footer = layoutInflater.inflate(R.layout.progressbar_load_more, null);
        listView.addFooterView(footer);
        String url;
        switch (position) {
            case 0:
                break;
            case 1:
                url = API.LOAD_MORE_LATEST_URL +
                    myData.getArrayNew().get(myData.getArrayNew().size() - 1).getPost_id();
                requestLoadMore(url, 1);
                break;
            case 2:
                url = API.LOAD_MORE_TOPVIEW_URL +
                    myData.getArrayMost().get(myData.getArrayMost().size() - 1).getPost_id();
                requestLoadMore(url, 2);
                break;
            default:
                ArrayList<MyVideo> arrayList = myData.getArrayListData().get(position - 3);
                int last_id = Integer.parseInt(arrayList.get(arrayList.size() - 1).getPost_id());
                url = API.LOAD_MORE_CATEGORY_URL + categoryItem.getId() + "&last_id=" + last_id;
                requestLoadMore(url, position);
                break;
        }
    }

    private void setUpListView(int position) {
        MyData myData = MyData.getInstance();
        switch (position) {
            case 0:
                break;
            case 1:
                progressBar.setVisibility(View.GONE);
                llTry.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                listView.addFooterView(footer);
                listView.setAdapter(new MyListAdapter(getActivity(), myData.getArrayNew()));
                break;
            case 2:
                progressBar.setVisibility(View.GONE);
                llTry.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                listView.addFooterView(footer);
                listView.setAdapter(new MyListAdapter(getActivity(), myData.getArrayMost()));
                break;
            default:
                if (myData.getArrayListData().get(position - 3).size() < 1) {
                    requestDetailCategory(position, categoryItem.getId());
                } else {
                    progressBar.setVisibility(View.GONE);
                    llTry.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    listView.addFooterView(footer);
                    listView.setAdapter(new MyListAdapter(getActivity(),
                        myData.getArrayListData().get(position - 3)));
                }
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), EXOMediaActivity.class);
                intent.putExtra("video", (MyVideo) listView.getAdapter().getItem(position));
                startActivity(intent);
            }
        });
    }

    private void requestDetailCategory(final int position, final String categoryId) {
        HashMap<String, String> params = new HashMap<>();
        params.put(NewAPI.PARAM_APP_ID, Config.APP_ID);
        params.put(NewAPI.PARAM_TYPE, "detail-category");
        params.put("category_id", categoryId);
        AndroidNetworking.get(NewAPI.HOST_VIDEO_API)
            .addQueryParameter(params)
            .setTag("detail-category")
            .setPriority(Priority.MEDIUM)
            .doNotCacheResponse()
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    Gson gson = new Gson();
                    MyData myData = MyData.getInstance();
                    Data data = gson.fromJson(response.toString(), Data.class);
                    Collections.addAll(myData.getArrayListData().get(position - 3), data.getData());
                    progressBar.setVisibility(View.GONE);
                    llTry.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    MyAdapter.getInstance().setMyListAdapter(new MyListAdapter(getActivity(),
                        myData.getArrayListData().get(position - 3)));
                    if (MyAdapter.getInstance().getMyListAdapter().getCount() > 0) {
                        listView.addFooterView(footer);
                    }
                    listView.setAdapter(MyAdapter.getInstance().getMyListAdapter());
                }

                @Override
                public void onError(ANError ANError) {
                    progressBar.setVisibility(View.GONE);
                    llTry.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }
            });
    }

    private void requestLoadMore(final String url, final int postion) {
        AndroidNetworking.get(url)
            .setPriority(Priority.MEDIUM)
            .doNotCacheResponse()
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    Gson gson = new Gson();
                    MyData myData = MyData.getInstance();
                    Data data = gson.fromJson(response.toString(), Data.class);
                    if (data.getData() != null) {
                        if (data.getData().length > 0) {
                            switch (postion) {
                                case 0:
                                    break;
                                case 1:
                                    Collections.addAll(myData.getArrayNew(), data.getData());
                                    break;
                                case 2:
                                    Collections.addAll(myData.getArrayMost(), data.getData());
                                    break;
                                default:
                                    Collections.addAll(myData.getArrayListData().get(postion - 3),
                                        data.getData());
                                    break;
                            }
                        } else {
                            Toast.makeText(getContext(), "No video more!", Toast.LENGTH_SHORT)
                                .show();
                        }
                    } else {
                        Toast.makeText(getContext(), "No video more!", Toast.LENGTH_SHORT).show();
                    }
                    listView.removeFooterView(footer);
                    footer = (View) layoutInflater.inflate(R.layout.image_load_more, null);
                    listView.addFooterView(footer);
                    setOnClick();
                }

                @Override
                public void onError(ANError ANError) {
                    listView.removeFooterView(footer);
                    footer = layoutInflater.inflate(R.layout.image_load_more, null);
                    listView.addFooterView(footer);
                    setOnClick();
                    Toast.makeText(getContext(), "Network error!", Toast.LENGTH_SHORT).show();
                }
            });
    }
}
