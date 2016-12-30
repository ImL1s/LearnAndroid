package com.demo.safeBodyGuard.manager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by iml1s-macpro on 2016/12/29.
 */

public class TimerManager
{
    private static TimerManager mInstance;

    /*
     * 閒置中的timer cache pool
     */
    private Stack<Timer> idleTimerStack = new Stack<>();

    /*
     * 正在運作中的timer cache pool
     */
    private HashSet<Timer> busyTimerSet = new HashSet<>();

    private HashMap<Timer,List<TimerManagerTask>> timerTaskMap = new HashMap<>();


    public static TimerManager getInstance()
    {
        return mInstance;
    }

    static
    {
        mInstance = new TimerManager();
    }

    private void TimeManager(){}


    /*
     * 安排一項延遲任務
     */
    public void schedule(Runnable runnableTask, long delay)
    {
        TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
                runnableTask.run();
            }
        };

        schedule(task,delay);
    }


    /*
     * 安排一項延遲任務
     */
    public void schedule(TimerTask task, long delay)
    {
        Timer timer;

        if(idleTimerStack.size() > 0)
        {
            timer = idleTimerStack.pop();
            timer.cancel();
            timer.purge();
        }
        else
        {
            timer = new Timer();
        }

        TimerManagerTask realTask = new TimerManagerTask(task);

        busyTimerSet.add(timer);
        timer.schedule(realTask, delay);
        realTask.setRelativeTimer(timer);

        if(timerTaskMap.containsKey(timer))
        {
            timerTaskMap.get(timer).add(realTask);
        }
        else
        {
            LinkedList<TimerManagerTask> taskLinkedList = new LinkedList<>();
            taskLinkedList.add(realTask);
            timerTaskMap.put(timer,taskLinkedList);
        }
    }

    public void scheduleMutiTask(long interval,TimerTask... tasks)
    {
        // TODO scheduleMutiTask
    }

    /*
     * Task任務執行完成後,回調放回緩存池
     */
    public void onTaskFinish(TimerManagerTask timerTask)
    {
        Timer relativeTimer = timerTask.getRelativeTimer();
        relativeTimer.cancel();
        relativeTimer.purge();
        busyTimerSet.remove(relativeTimer);
        idleTimerStack.add(relativeTimer);

        if(timerTaskMap.containsKey(relativeTimer))timerTaskMap.remove(relativeTimer);
    }


    /*
     * TimerManager所使用的TimerTask
     */
    private class TimerManagerTask extends TimerTask
    {
        private Timer relativeTimer = null;                                     // 這個Task相關的Timer
        private TimerTask timerTask = null;                                     // 實際執行的Task
        private boolean isRelease = false;                                      // 標誌是否已經被釋放了,防止重複釋放

        TimerManagerTask(TimerTask task)
        {
            this.timerTask = task;
        }

        @Override
        public void run()
        {
            timerTask.run();
            release();
        }

        /*
         * Task完成後釋放回緩存池
         */
        final void release()
        {
            if(!isRelease)
            {
                TimerManager.getInstance().onTaskFinish(this);
                isRelease = true;
            }
        }

        Timer getRelativeTimer()
        {
            return relativeTimer;
        }


        void setRelativeTimer(Timer relativeTimer)
        {
            this.relativeTimer = relativeTimer;
        }
    }
}
