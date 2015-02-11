package com.example.amado.taskit;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;


public class TaskListActivity extends ActionBarActivity {
    private static final int EDIT_TASK= 10;
    private static final int CREATE_TASK= 20;
    private static final String TAG = "TaskListActivity";
    private int mLastPositionClicked;
    private taskAdapter mAdapter;
    private ArrayList<Task> mTasks;
    private ListView mlistView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        mTasks = new ArrayList<Task>();
        mTasks.add(new Task());
        mTasks.get(0).setName("Task 1");
        mTasks.get(0).setDueDate(new Date());
        mTasks.add(new Task());
        mTasks.get(1).setName("Task 2");
        mTasks.get(1).setDone(true);
        mTasks.add(new Task());
        mTasks.get(2).setName("Task 3");


        mlistView = (ListView)findViewById(R.id.List);
        mAdapter = new taskAdapter(mTasks);
        mlistView.setAdapter(mAdapter);
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mLastPositionClicked = position;
                Intent i = new Intent(TaskListActivity.this, TaskActivity.class);
                Task task = (Task) parent.getAdapter().getItem(position);
                i.putExtra(TaskActivity.EXTRA, task);
                startActivityForResult(i, EDIT_TASK);
            }
        });
        mlistView.getSelectedItemPosition();
        mlistView.setChoiceMode(mlistView.CHOICE_MODE_MULTIPLE_MODAL);
        mlistView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                getMenuInflater().inflate(R.menu.menu_task_list_context, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                int id = item.getItemId();
                SparseBooleanArray positions =mlistView.getCheckedItemPositions();
                if (id == R.id.delete_task) {
                    for (int i = positions.size()- 1; i >=0 ; i--) {
                        if (positions.valueAt(i)) {
                            mTasks.remove(positions.keyAt(i));
                        }
                    }
                    mode.finish();
                    mAdapter.notifyDataSetChanged();
                    return true;

                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case EDIT_TASK:
                if (resultCode == RESULT_OK) {
                    Task task = (Task) data.getSerializableExtra(TaskActivity.EXTRA);
                    mTasks.set(mLastPositionClicked, task);
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case CREATE_TASK:
                if (resultCode == RESULT_OK) {
                    Task task = (Task) data.getSerializableExtra(TaskActivity.EXTRA);
                    mTasks.add(task);
                    mAdapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }
    }




    private class taskAdapter extends ArrayAdapter<Task>{
        taskAdapter(ArrayList<Task> tasks){
            super(TaskListActivity.this, R.layout.task_list_row, R.id.task_item_name, tasks);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView =  super.getView(position, convertView, parent);
            Task task = getItem(position);
            TextView taskName = (TextView)convertView.findViewById(R.id.task_item_name);
            taskName.setText(task.getName());
            CheckBox donebox = (CheckBox)convertView.findViewById(R.id.task_item_done);
            donebox.setChecked(task.isDone());
            return convertView;
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_task) {
            Intent i = new Intent(TaskListActivity.this, TaskActivity.class);
            startActivityForResult(i,CREATE_TASK );
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.menu_task_list_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete_task){
            AdapterView.AdapterContextMenuInfo menuInfo =(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
            mTasks.remove(menuInfo.position);
            mAdapter.notifyDataSetChanged();
            return true;
        }
        return super.onContextItemSelected(item);
    }
}
