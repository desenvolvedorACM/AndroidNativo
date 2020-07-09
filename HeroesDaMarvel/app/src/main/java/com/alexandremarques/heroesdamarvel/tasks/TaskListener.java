package com.alexandremarques.heroesdamarvel.tasks;

public interface TaskListener {
    public void onSuccess(String result);

    public void onError(String result);
}
