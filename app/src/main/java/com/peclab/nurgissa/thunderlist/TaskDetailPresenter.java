package com.peclab.nurgissa.thunderlist;


import java.util.ArrayList;
import java.util.List;

public class TaskDetailPresenter implements TaskDetailContract.Presenter, TaskDetailContract.Interactor.OnFinishedListener {
    private static final String HINT_NOTE = "Add a note..";
    private static final String HINT_SCHEDULE = "Set Reminder";
    private static final String HINT_EDIT_VALUE = "Task title..";
    private static final String HINT_ADD_SUBTASK = "Add a subtask";

    private List<TaskDetail> taskDetails;
    private TaskDetailContract.View view;
    private TaskDetailContract.Interactor interactor;

    public TaskDetailPresenter(TaskDetailContract.View view, TaskDetailContract.Interactor interactor) {
        this.view = view;
        this.interactor = interactor;
        taskDetails = new ArrayList<>();
    }

    @Override
    public void bindTaskTitleViewToValue(TaskDetailContract.TaskTitleAdapterView adapterView, int position) {
        TaskDetail td = taskDetails.get(position);
        adapterView.setImage(td.getImage());

        if (td.getText().equals(HINT_EDIT_VALUE)) {
            adapterView.setTextHint(td.getText());
        } else {
            adapterView.setText(td.getText());
        }
    }

    @Override
    public void bindBasicViewToValue(TaskDetailContract.BasicAdapterView adapterView, int position) {
        TaskDetail td = taskDetails.get(position);
        adapterView.setText(td.getText());
        adapterView.setImage(td.getImage());
    }

    @Override
    public void bindSubtaskViewToValue(TaskDetailContract.SubtaskAdapterView adapterView, int position) {
        TaskDetail td = taskDetails.get(position);
        adapterView.setText(td.getText());
    }

    @Override
    public int getDetailTaskItemCount() {
        return taskDetails.size();
    }

    @Override
    public int getViewType(int position) {
        return taskDetails.get(position).getViewType();
    }

    @Override
    public void addSubtask(String value) {
        TaskDetail tdSubtask = new TaskDetail();

        tdSubtask.setText(value);
        tdSubtask.setViewType(TaskDetail.VIEW_TYPE_SUBTASK);

        taskDetails.add(tdSubtask);

        view.notifySubtaskAddedToDetailTask();
    }

    @Override
    public void onCreateFinished() {
        System.out.println("Show toast -> task created!");
    }

    @Override
    public void initializeTaskDetail(String title) {
        Task task = null;
        if (title != null) {
            task = interactor.getByTitle(title);
        }

        populateDetailViewItemEditValue(task);
        populateDetailViewItemSchedule(task);
        populateDetailViewItemNote(task);
        populateDetailViewItemSubtask();
    }

    private void populateDetailViewItemEditValue(Task task) {
        TaskDetail td = new TaskDetail();

        if (task != null && task.getTitle() != null) {
            td.setId(task.getId());
            td.setText(task.getTitle());
        } else {
            td.setText(HINT_EDIT_VALUE);
        }
        td.setViewType(TaskDetail.VIEW_TYPE_EDIT_VALUE);
        td.setImage(TaskDetail.IMAGE_EDIT_VALUE);

        taskDetails.add(0, td);
    }

    private void populateDetailViewItemSchedule(Task task) {
        TaskDetail td = new TaskDetail();

        if (task != null && task.getSchedule() != null) {
            td.setText(task.getSchedule());

        } else {
            td.setText(HINT_SCHEDULE);
        }
        td.setViewType(TaskDetail.VIEW_TYPE_SCHEDULE);
        td.setImage(TaskDetail.IMAGE_SCHEDULE);

        taskDetails.add(1, td);
    }

    private void populateDetailViewItemNote(Task task) {
        TaskDetail td = new TaskDetail();

        if (task != null && task.getNote() != null) {
            td.setText(task.getTitle());
        } else {
            td.setText(HINT_NOTE);
        }
        td.setViewType(TaskDetail.VIEW_TYPE_NOTE);
        td.setImage(TaskDetail.IMAGE_NOTE);

        taskDetails.add(2, td);
    }

    private void populateDetailViewItemSubtask() {
        TaskDetail td = new TaskDetail();

        td.setText(HINT_ADD_SUBTASK);
        td.setViewType(TaskDetail.VIEW_TYPE_ADD_SUBTASK);
        td.setImage(TaskDetail.IMAGE_ADD_SUBTASK);

        taskDetails.add(3, td);
    }

    @Override
    public void saveTaskDetail() {
        Task task = new Task();

        for (TaskDetail td : taskDetails) {

            switch (td.getViewType()) {
                case TaskDetail.VIEW_TYPE_EDIT_VALUE:
                    task.setId(td.getId());
                    task.setTitle(td.getText());
                    break;

                case TaskDetail.VIEW_TYPE_SCHEDULE:
                    task.setSchedule(td.getText());
                    break;

                case TaskDetail.VIEW_TYPE_NOTE:
                    task.setNote(td.getText());
                    break;
            }
        }

//        if (task.getId() != 0) {
//            interactor.saveById(task, task.getId());
//        } else {
//            interactor.save(task);
//        }
    }
}
