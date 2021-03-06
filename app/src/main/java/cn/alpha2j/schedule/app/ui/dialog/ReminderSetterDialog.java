package cn.alpha2j.schedule.app.ui.dialog;

import android.app.Dialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.alpha2j.schedule.R;
import cn.alpha2j.schedule.app.ui.entity.TimeType;

/**
 *
 * @author alpha
 *         Created on 2018/2/25.
 */
public class ReminderSetterDialog extends DialogFragment {

    private View mRootView;
    private Switch mSwitch;
    private ImageView mImageView;
    private TextView mTextView;
    private Spinner mTimeSpinner;
    private Spinner mTypeSpinner;

    private ReminderWrapper mReminderWrapper;

    private OnReminderSetListener mOnReminderSetListener;

    public ReminderSetterDialog() {

        mReminderWrapper = new ReminderWrapper();
    }

    public static ReminderSetterDialog newInstance(ReminderWrapper reminderWrapper) {

        ReminderSetterDialog dialog = new ReminderSetterDialog();
        dialog.setReminderWrapper(reminderWrapper);

        return dialog;
    }

    public void setReminderWrapper(ReminderWrapper reminderWrapper) {
        mReminderWrapper = reminderWrapper;
    }

    public void setOnReminderSetListener(OnReminderSetListener onReminderSetListener) {
        mOnReminderSetListener = onReminderSetListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        initView();
        initViewData();

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(mRootView)
                .setPositiveButton("确定", (dialog, id) -> {
//                    如果设置了监听器那么回调
                    if(mOnReminderSetListener != null) {
                        mOnReminderSetListener.onReminderSet(mReminderWrapper);
                    }
                })
                .setNegativeButton("取消", (dialog, id) -> {
                    ReminderSetterDialog.this.getDialog().cancel();
                });

        return dialogBuilder.create();
    }

    private void initView() {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        mRootView = inflater.inflate(R.layout.dialog_reminder_time_setter, null);

        mSwitch = mRootView.findViewById(R.id.s_reminder_dialog_alarm_control);
        mImageView = mRootView.findViewById(R.id.iv_reminder_dialog_alarm_icon);
        mTextView = mRootView.findViewById(R.id.tv_reminder_dialog_alarm_show);
        mTimeSpinner = mRootView.findViewById(R.id.spinner_reminder_dialog_time_num);
        mTypeSpinner = mRootView.findViewById(R.id.spinner_reminder_dialog_time_type);
    }

    private void initViewData() {

//        初始化视图数据, 开关是否打开, 显示多少分钟等
        mSwitch.setChecked(mReminderWrapper.isRemind());
        mSwitch.setOnCheckedChangeListener(((compoundButton, b) -> {
            setViewEnable();
        }));

//        初始化文字显示数据
        setDateText();

//        初始化下拉框数据
        List<Integer> spinnerTimeList = new ArrayList<>();
        for (int i = 0; i <= 60; i++) {
            spinnerTimeList.add(i);
        }
        ArrayAdapter<Integer> spinnerTimeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, spinnerTimeList);
        spinnerTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTimeSpinner.setAdapter(spinnerTimeAdapter);
        mTimeSpinner.setSelection(mReminderWrapper.getNum());
        mTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mReminderWrapper.setNum(spinnerTimeAdapter.getItem(i));
                setDateText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        List<String> spinnerTypeList = new ArrayList<>();
        spinnerTypeList.add(TimeType.MINUTE);
        spinnerTypeList.add(TimeType.HOUR);
        spinnerTypeList.add(TimeType.DAY);
        ArrayAdapter<String> spinnerTypeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, spinnerTypeList);
        spinnerTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTypeSpinner.setAdapter(spinnerTypeAdapter);
        switch (mReminderWrapper.getTimeType()) {
            case TimeType.MINUTE:
                mTypeSpinner.setSelection(0);
                break;
            case TimeType.HOUR:
                mTypeSpinner.setSelection(1);
                break;
            case TimeType.DAY:
                mTypeSpinner.setSelection(2);
                break;
            default:
                mTypeSpinner.setSelection(0);
        }
        mTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0 :
                        mReminderWrapper.setTimeType(TimeType.MINUTE);
                        break;
                    case 1:
                        mReminderWrapper.setTimeType(TimeType.HOUR);
                        break;
                    case 2:
                        mReminderWrapper.setTimeType(TimeType.DAY);
                        break;
                    default:
                        mReminderWrapper.setTimeType(TimeType.MINUTE);
                }
                setDateText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        setViewEnable();
    }


    private void setDateText() {

        mTextView.setText(getResources().getString(R.string.task_add_string_reminder_text, mReminderWrapper.getNum(), mReminderWrapper.getTimeType()));
    }

    private void setViewEnable() {

//        现将reminderWrapper的状态设置为开关状态
        mReminderWrapper.setRemind(mSwitch.isChecked());

        if (mSwitch.isChecked()) {
//            如果开关是开的, 那么设置图标为alarm
            mImageView.setImageResource(R.drawable.ic_alarm);
            mTextView.getPaint().setFlags(0);
            mTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
//            且打开下拉选择
            mTimeSpinner.setEnabled(true);
            mTypeSpinner.setEnabled(true);
        } else {
//            如果开关是关的, 那么
//            设置图标为alarm off
            mImageView.setImageResource(R.drawable.ic_alarm_off);
            mTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            mTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGreyDark));
//            且下拉选择禁用
            mTimeSpinner.setEnabled(false);
            mTypeSpinner.setEnabled(false);
        }
    }

    /**
     * 当设置完提醒后, 点击ok按钮退出时回调
     */
    public interface OnReminderSetListener {

        /**
         * 点击ok按钮时调用这个方法
         * @param reminderWrapper
         */
        void onReminderSet(ReminderWrapper reminderWrapper);
    }

    public static class ReminderWrapper {

        /**
         * 是否提醒
         */
        private boolean remind;
        /**
         * 提醒的时间数
         */
        private int num;
        /**
         * 提醒的时间类型
         */
        private String mTimeType;

        public ReminderWrapper() {
//            默认为不提醒, 且时间类型为分钟
            remind = false;
            num = 0;
            mTimeType = TimeType.MINUTE;
        }

        public boolean isRemind() {
            return remind;
        }

        public void setRemind(boolean remind) {
            this.remind = remind;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getTimeType() {
            return mTimeType;
        }

        public void setTimeType(String timeType) {
            if(TimeType.isTimeType(timeType)) {
                mTimeType = timeType;
            } else {
                mTimeType = TimeType.MINUTE;
            }
        }

        /**
         * 如果设置了提醒, 那么返回提醒的类型和数量构成的毫秒数
         */
        public long getResultAsEpochMills() {
//            如果不提醒那么返回0
            if(remind) {
                switch (mTimeType) {
                    case TimeType.MINUTE:
                        return num * 60 * 1000;
                    case TimeType.HOUR:
                        return num * 60 * 60 * 1000;
                    case TimeType.DAY:
                        return num * 24 * 60 * 60 * 1000;
                    default:
                        return num * 60 * 1000;
                }
            } else {
                return 0;
            }
        }
    }
}
