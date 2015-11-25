package ch.ethz.inf.mytodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import ch.ethz.inf.mytodo.io.IO;
import ch.ethz.inf.mytodo.model.Category;
import ch.ethz.inf.mytodo.model.ToDo;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private Category mCurrentCategory = Category.Personal;
    private ArrayList mDataset;
    private IO io;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Navigation Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Navigation View
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //RecyclerView Setup
        RecyclerViewSetup();

        //SwipeSetup
        DismissOnSwipeSetup();

        //FloatingButton Setup
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ToDoActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }


    private void RecyclerViewSetup() {

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Load data from file system
        io = new IO();
        mDataset = io.LoadData(getBaseContext(),mCurrentCategory);

        // Specify an adapter
        mAdapter = new ToDoAdapter(mDataset,this, mCurrentCategory);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Snackbar.make(mRecyclerView, "Replace with settings action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Result from ToDoActivity

        if (resultCode == RESULT_OK){

            ToDo toDo = new ToDo(data.getStringExtra("title"),data.getStringExtra("date"), data.getBooleanExtra("done", false));

            if (requestCode == 0){
                //Create
                mDataset.add(toDo);
                mAdapter.notifyDataSetChanged();

            }else if (requestCode == 1){
                //Edit
                int position = data.getIntExtra("position",0);
                mDataset.set(position, toDo);
                mAdapter.notifyItemChanged(position);
            }
        }

        io.WriteData(mDataset, getBaseContext(), mCurrentCategory);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_personal) {
            mCurrentCategory = Category.Personal;
        } else if (id == R.id.nav_eth) {
            mCurrentCategory = Category.ETH;
        } else if (id == R.id.nav_tv_shows) {
            mCurrentCategory = Category.TVShows;
        } else if (id == R.id.nav_books) {
            mCurrentCategory = Category.Books;
        }


        //Reload dataset and new adpater
        mDataset = io.LoadData(getBaseContext(), mCurrentCategory);
        mAdapter = new ToDoAdapter(mDataset,this,mCurrentCategory);
        mRecyclerView.setAdapter(mAdapter);

        //Close Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void DismissOnSwipeSetup() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove item
                int position = mRecyclerView.getChildAdapterPosition(viewHolder.itemView);
                mDataset.remove(position);
                io.WriteData(mDataset, getBaseContext() , mCurrentCategory);

                //Animation
                mAdapter.notifyItemRemoved(position);

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);

        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }





}
