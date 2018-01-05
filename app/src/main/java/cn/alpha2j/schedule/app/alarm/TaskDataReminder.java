package cn.alpha2j.schedule.app.alarm;

import android.util.Log;

import cn.alpha2j.schedule.app.ui.data.provider.TaskDataProvider;
import cn.alpha2j.schedule.time.ScheduleDateTime;

/**
 * @author alpha
 */
public class TaskDataReminder {

    private static final String TAG = "TaskDataReminder";

    private TaskReminder mTaskReminder;

    public TaskDataReminder() {
        mTaskReminder = new TaskReminder();
    }

    public TaskDataReminder(TaskReminder taskReminder) {
        this.mTaskReminder = taskReminder;
    }

    public void remind(TaskDataProvider.TaskData taskData) {

//        如果传入的taskData为空或者该taskData的task为空, 或者task根本不需要提醒, 那么就直接返回
        if(taskData == null || taskData.getTask() == null || !taskData.getTask().isAlarm()) {
            return;
        }

        if(shouldRemind(taskData.getTask().getTaskAlarmDateTime())) {
            mTaskReminder.remindTask(taskData.getTask());
//            TODO: 测试完删除
            long now = ScheduleDateTime.now().getEpochMillisecond();
            Log.d(TAG, "remind: 添加了任务提醒, id: " + taskData.getTask().getId() + " 时间: " + now);
        }
    }

    public void cancelRemind(TaskDataProvider.TaskData taskData) {

//        这里不判断是否提醒, 不管是否提醒, 都用相应的id来取消提醒, 因为如果一个任务本来已经设置了提醒,
//        且已经添加到提醒管理器中了, 但是又取消了提醒, 那么这时候task的isAlarm可能为false了,
//        那么如果这里进行taskData.getTask().isAlarm()判断的话, 返回false那么已经设置了提醒的那个task就不能取消了.
        if(taskData == null || taskData.getTask() == null) {
            Log.d(TAG, "cancelRemindTaskData: 参数taskData为null, 结束方法执行");
            return;
        }

        mTaskReminder.cancelRemindTask(taskData.getTask());
        Log.d(TAG, "cancelRemindTaskData: 取消了任务提醒, id: "  + taskData.getTask().getId());
    }

    private boolean shouldRemind(ScheduleDateTime scheduleDateTime) {

        if(scheduleDateTime == null) {
            return false;
        }

        long now = ScheduleDateTime.now().getEpochMillisecond();
        long remindTime = scheduleDateTime.getEpochMillisecond();

        //当前时间比提醒时间晚或相同, 不进行提醒
        if (now >= remindTime) {
            return false;
        } else {
            return true;
        }
    }
}
