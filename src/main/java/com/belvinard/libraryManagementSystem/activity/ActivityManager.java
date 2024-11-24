package com.belvinard.libraryManagementSystem.activity;

import org.springframework.context.annotation.Bean;

import java.util.Stack;

public class ActivityManager {
    private Stack<Activity> activityStack = new Stack<>();
    private final int MAX_CAPACITY = 10; // Nombre maximal d'activités à conserver

    // Ajouter une activité à la pile
    public void addActivity(Activity activity) {
        if (activityStack.size() >= MAX_CAPACITY) {
            activityStack.remove(0); // Supprime la plus ancienne activité si la capacité est atteinte
        }
        activityStack.push(activity);
    }

    @Bean
    public ActivityManager activityManager() {
        return new ActivityManager();
    }


    // Afficher les activités récentes
    public void displayRecentActivities() {
        if (activityStack.isEmpty()) {
            System.out.println("No recent activities to display.");
        } else {
            System.out.println("Recent Activities:");
            for (int i = activityStack.size() - 1; i >= 0; i--) {
                System.out.println(activityStack.get(i));
            }
        }
    }
}
