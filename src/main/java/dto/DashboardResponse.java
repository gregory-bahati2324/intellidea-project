package dto;

import java.util.List;

public class DashboardResponse {

    public List<Stat> stats;
    public List<Activity> activity;
    public int profileCompletion;

    public static class Stat {
        public String label;
        public String value;
        public String change;
    }

    public static class Activity {
        public String user;
        public String action;
        public String time;
    }
}