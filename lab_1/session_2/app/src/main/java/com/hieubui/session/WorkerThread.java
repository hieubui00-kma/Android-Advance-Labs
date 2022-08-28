package com.hieubui.session;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WorkerThread extends Thread {
    private boolean isRunning = false;

    public WorkerThread() {
    }

    public WorkerThread(@Nullable Runnable target) {
        super(target);
    }

    public WorkerThread(@Nullable ThreadGroup group, @Nullable Runnable target) {
        super(group, target);
    }

    public WorkerThread(@NonNull String name) {
        super(name);
    }

    public WorkerThread(@Nullable ThreadGroup group, @NonNull String name) {
        super(group, name);
    }

    public WorkerThread(@Nullable Runnable target, @NonNull String name) {
        super(target, name);
    }

    public WorkerThread(@Nullable ThreadGroup group, @Nullable Runnable target, @NonNull String name) {
        super(group, target, name);
    }

    public WorkerThread(@Nullable ThreadGroup group, @Nullable Runnable target, @NonNull String name, long stackSize) {
        super(group, target, name, stackSize);
    }

    @Override
    public synchronized void start() {
        isRunning = true;
        super.start();
    }

    @Override
    public void interrupt() {
        super.interrupt();
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
