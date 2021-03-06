package cn.alpha2j.schedule.app.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.facebook.stetho.Stetho;

import java.util.concurrent.ConcurrentHashMap;

import cn.alpha2j.schedule.R;
import cn.alpha2j.schedule.app.service.AlarmService;
import cn.alpha2j.schedule.app.ui.dialog.TaskFastAddBottomDialog;
import cn.alpha2j.schedule.app.ui.fragment.BaseFragment;
import cn.alpha2j.schedule.app.ui.fragment.TaskOverviewFragment;
import cn.alpha2j.schedule.app.ui.fragment.TaskTodayFragment;

/**
 * @author alpha
 *         Created on 2017/11/4.
 */
public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private FloatingActionButton mFloatingActionButton;

    private TaskTodayFragment mTaskTodayFragment;
    private TaskOverviewFragment mTaskOverviewFragment;
    private final ConcurrentHashMap<String, BaseFragment> mMapOfAddedFragments = new ConcurrentHashMap<>();

    private String mCurrentFragmentTag;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected String getToolbarTitle() {
        return getResources().getString(R.string.activity_toolbar_title_main);
    }

    @Override
    protected void initActivity(@Nullable Bundle savedInstanceState) {

//        集成Stetho
        Stetho.initializeWithDefaults(this);

//        初始化view域
        initViews();

//        初始化服务
        Intent remindServiceIntent = new Intent(this, AlarmService.class);
        startService(remindServiceIntent);

//        改变homeAsUp图标
        changeDisplayHomeAsUpIcon();

//        初始化NavigationView的数据(初始化头部, 设置列表选中项, 设置监听器)
        setNavigationViewHeader();
        setNavigationViewItemSelectedListener();

//        设置FloatingActionButton的监听器
        setFloatingActionButtonListener();

//        初始化当前Fragment
        initFragment(savedInstanceState);

//        设置NavigationView选中项
        setSelectedNavigationItem();
    }

    private void initViews() {

        mDrawerLayout = findViewById(R.id.dl_home_activity_container);
        mNavigationView = findViewById(R.id.nv_home_sidebar);
        mFloatingActionButton = findViewById(R.id.fab_home_add_task_btn);
    }

    private void changeDisplayHomeAsUpIcon() {

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
    }

    private void setNavigationViewHeader() {

        View view = mNavigationView.getHeaderView(0);
        FrameLayout frameLayout = view.findViewById(R.id.fl_nav_header_container);
        Glide.with(this).load(R.drawable.nav_header_bg).asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Drawable drawable = new BitmapDrawable(resource);
                        frameLayout.setBackground(drawable);
                    }
                });
    }

    private void setNavigationViewItemSelectedListener() {

        mNavigationView.setNavigationItemSelectedListener(this);
    }

    private void setFloatingActionButtonListener() {

        mFloatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TaskAddActivity.class);
            startActivity(intent);
        });
    }

    private void initFragment(@Nullable Bundle savedInstanceState) {

//        Activity被系统销毁前显示的是哪个Fragment, 将它设置为当前Fragment然后显示出来.
        if(savedInstanceState == null) {
            mCurrentFragmentTag = FC.FRAGMENT_TAG_TASK_TODAY;
        } else {
            mCurrentFragmentTag = savedInstanceState.getString(FC.FRAGMENT_TAG);
        }

        displayFragment(mCurrentFragmentTag);
    }

    /**
     * 设置draw里面的选中项
     */
    private void setSelectedNavigationItem() {

        if (mCurrentFragmentTag == null) {
            mNavigationView.setCheckedItem(R.id.activity_main_menu_task_today_item);
        }

        switch (mCurrentFragmentTag) {
            case FC.FRAGMENT_TAG_TASK_TODAY:
                mNavigationView.setCheckedItem(R.id.activity_main_menu_task_today_item);
                break;
            case FC.FRAGMENT_TAG_TASK_OVERVIEW:
                mNavigationView.setCheckedItem(R.id.activity_main_menu_task_overview_item);
                break;
            default:
                mNavigationView.setCheckedItem(R.id.activity_main_menu_task_today_item);
        }
    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_main_toolbar_menu, menu);

        return true;
    }

    //    todo: 这个方法还需要重写
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.activity_main_menu_task_today_item:

                mCurrentFragmentTag = FC.FRAGMENT_TAG_TASK_TODAY;
                displayFragment(mCurrentFragmentTag);
                break;
            case R.id.activity_main_menu_task_overview_item:

//                mCurrentFragmentTag = FC.FRAGMENT_TAG_TASK_OVERVIEW;
//                displayFragment(mCurrentFragmentTag);
                Intent taskOverviewActivityIntent = new Intent(this, TaskOverviewActivity.class);
                startActivity(taskOverviewActivityIntent);
                break;
            case R.id.activity_main_menu_task_statistics_item:

                Intent statisticsActivityIntent = new Intent(this, TaskStatisticsActivity.class);
                startActivity(statisticsActivityIntent);
                overridePendingTransition(R.anim.animation_bottom_in, R.anim.animation_no);
                break;
            case R.id.activity_main_menu_settings_item:

                Intent settingsActivityIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsActivityIntent);
                break;
            case R.id.activity_main_menu_about_item:

                Intent aboutActivityIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutActivityIntent);
                break;
            default:
        }

        mDrawerLayout.closeDrawers();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
//            HomeAsUp按钮的id就是 android.R.id.home
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.activity_main_menu_add_item:
                TaskFastAddBottomDialog addTaskBottomDialog = new TaskFastAddBottomDialog();
                addTaskBottomDialog.setOnTaskCreatedListener(task -> {
                    if (mTaskTodayFragment != null) {
                        mTaskTodayFragment.notifyNewTaskAdd(task);
                    }
                });
                addTaskBottomDialog.show(getSupportFragmentManager());
                break;
            default:
        }

        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

//        Activity从不可见状态变为可见状态时, 需要将navigation的item设置为可见时的那个值
        setSelectedNavigationItem();

//        调用TaskTodayFragment的更新方法, 更新Fragment的数据
        if (mTaskTodayFragment != null) {
            mTaskTodayFragment.refreshData();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

//        将当前fragment 的类型传入
//        做if判断好像有点臃余, 不过能增加健壮性
        if(mCurrentFragmentTag != null) {
            outState.putString(FC.FRAGMENT_TAG, mCurrentFragmentTag);
        } else {
            outState.putString(FC.FRAGMENT_TAG, FC.FRAGMENT_TAG_TASK_TODAY);
        }
    }

    /**
     * 将接口简化
     */
    private interface FC extends BaseFragment.FragmentConstant {}

    private void displayFragment(String fragmentTag) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (fragmentTag) {
            case FC.FRAGMENT_TAG_TASK_TODAY :
                if(mTaskTodayFragment == null) {
                    mTaskTodayFragment = new TaskTodayFragment();
                    mMapOfAddedFragments.put(FC.FRAGMENT_TAG_TASK_TODAY, mTaskTodayFragment);
                }

                if(mTaskTodayFragment.isAdded()) {
                    transaction.show(mTaskTodayFragment);
                } else {
                    transaction.add(R.id.fl_home_fragment_container, mTaskTodayFragment, FC.FRAGMENT_TAG_TASK_TODAY);
                }

                hideOthersFragments(fragmentTag);
                break;
            case FC.FRAGMENT_TAG_TASK_OVERVIEW :
                if(mTaskOverviewFragment == null) {
                    mTaskOverviewFragment = new TaskOverviewFragment();
                    mMapOfAddedFragments.put(FC.FRAGMENT_TAG_TASK_OVERVIEW, mTaskOverviewFragment);
                }

                if(mTaskOverviewFragment.isAdded()) {
                    transaction.show(mTaskOverviewFragment);
                } else {
                    transaction.add(R.id.fl_home_fragment_container, mTaskOverviewFragment, FC.FRAGMENT_TAG_TASK_OVERVIEW);
                }

                hideOthersFragments(fragmentTag);
                break;
            default:
        }

        transaction.commit();
    }

    private void hideOthersFragments(String fragmentTag) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        for (ConcurrentHashMap.Entry<String, BaseFragment> entry : mMapOfAddedFragments.entrySet()) {
            if (!entry.getKey().equals(fragmentTag)) {
                Fragment fragment = entry.getValue();

                if(fragment != null) {
                    if(fragment.isAdded()) {
                        transaction.hide(fragment);
                    }
                }
            }
        }

        transaction.commit();
    }
}
