package cn.alpha2j.schedule.app.ui.data.generator;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import cn.alpha2j.schedule.app.ui.data.AbstractDataProvider;
import cn.alpha2j.schedule.app.ui.data.TaskDataProvider;
import cn.alpha2j.schedule.app.ui.data.decorator.FinishedTaskDataProviderDecorator;
import cn.alpha2j.schedule.data.Task;
import cn.alpha2j.schedule.data.service.TaskService;
import cn.alpha2j.schedule.data.service.impl.TaskServiceImpl;

/**
 * @author alpha
 */
public class TodayFinishedDataProviderGenerator implements DataProviderGenerator {

    @NonNull
    @Override
    public AbstractDataProvider create() {

        TaskService taskService = TaskServiceImpl.getInstance();
        List<Task> finishedTasks = taskService.findAllFinishedForToday();
        List<TaskDataProvider.TaskData> dataset = new ArrayList<>();
        for (Task task : finishedTasks) {
            TaskDataProvider.TaskData data = new TaskDataProvider.TaskData(task, false);
            dataset.add(data);
        }

        return new TaskDataProvider(dataset);
    }
}