package space.dotcat.assistant.screen.roomList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import space.dotcat.assistant.R;
import space.dotcat.assistant.content.Room;
import space.dotcat.assistant.screen.general.BaseActivity;
import space.dotcat.assistant.screen.general.BaseActivityWithSettingsMenu;
import space.dotcat.assistant.screen.general.LoadingDialog;
import space.dotcat.assistant.screen.general.LoadingView;
import space.dotcat.assistant.screen.roomDetail.RoomDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.arturvasilov.rxloader.LoaderLifecycleHandler;

public class RoomsActivity extends BaseActivityWithSettingsMenu implements RoomsView, RoomsAdapter.OnItemClick,
        SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recyclerViewRooms)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.tv_error_message)
    TextView mErrorMessage;

    private LoadingView mLoadingView;

    private RoomsAdapter mAdapter;

    private RoomsPresenter mPresenter;

    public static void start(@NonNull Activity activity) {
        Intent intent = new Intent(activity, RoomsActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        if(getSupportActionBar() != null)
            getSupportActionBar().setTitle(getString(R.string.app_name));

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_red_light,
                android.R.color.holo_green_light);

        mLoadingView = LoadingDialog.view(getSupportFragmentManager());

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, getCountOfColumns()));

        mAdapter = new RoomsAdapter(new ArrayList<>(), this);

        mRecyclerView.setAdapter(mAdapter);

        LifecycleHandler lifecycleHandler = LoaderLifecycleHandler.create(this,getSupportLoaderManager());
        mPresenter = new RoomsPresenter(lifecycleHandler, this);
        mPresenter.init();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mPresenter.unsubscribe();
    }

    @Override
    public void onItemClick(@NonNull Room room) {
        mPresenter.onItemClick(room);
    }

    @Override
    public void showRooms(@NonNull List<Room> rooms) {
        if (!rooms.isEmpty()) {
            if(getSnackBar() != null){
                if(getSnackBar().isShown()){
                    getSnackBar().dismiss();
                }
            }

            mRecyclerView.setVisibility(View.VISIBLE);
            mErrorMessage.setVisibility(View.INVISIBLE);
            mAdapter.ChangeDataSet(rooms);
        } else {
            mRecyclerView.setVisibility(View.INVISIBLE);
            mErrorMessage.setVisibility(View.VISIBLE);
            mErrorMessage.setText(getString(R.string.error_empty_rooms));
        }
    }

    @Override
    public void showRoomDetail(@NonNull Room room) {
        RoomDetailsActivity.start(this, room);
    }

    @Override
    public void showError(Throwable throwable) {
        super.showBaseError(throwable, mSwipeRefreshLayout);
    }

    @Override
    public void showLoading() {
        mLoadingView.showLoading();
    }

    @Override
    public void hideLoading() {
        mLoadingView.hideLoading();
    }

    @Override
    public void onRefresh() {
        mPresenter.reloadData();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private int getCountOfColumns() {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return 3;

        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return 2;

        } else
            return 0;
    }
}
