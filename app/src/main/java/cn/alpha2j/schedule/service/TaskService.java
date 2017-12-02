package cn.alpha2j.schedule.service;

import java.util.List;

import cn.alpha2j.schedule.entity.Task;

/**
 * @author alpha
 */
public interface TaskService {
    /**
     * 增加新的Task
     * @param task
     * @return 增加成功的话返回task的id, 失败返回-1
     */
    long addTask(Task task);

    /**
     * 查找属于当天的所有Task
     * @return 如果没有找到, 那么返回的 list.size() 为空
     */
    List<Task> findAllForToday();

    /**
     * 查找属于当天的所有未完成的Task
     * @return 如果没有找到, 那么返回的 list.size() 为空
     */
    List<Task> findAllUnfinishedForToday();

    /**
     * 查找属于当天的所有已经完成的Task
     * @return 如果没有找到, 那么返回的 list.size() 为空
     */
    List<Task> findAllFinishedForToday();

    /**
     * 将该Task的状态设置为已完成
     * @param task
     */
    void setDone(Task task);

    /**
     * 将Task的状态设置为未完成
     * @param task
     */
    void setUnDone(Task task);
}