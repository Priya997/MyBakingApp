package com.priyankaj.mybakingapp.activities;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.priyankaj.mybakingapp.R;
import com.priyankaj.mybakingapp.adapters.StepsAdapter;
import com.priyankaj.mybakingapp.fragments.IngredientsFragement;
import com.priyankaj.mybakingapp.fragments.VideosFragment;
import com.priyankaj.mybakingapp.models.IngredientsModel;
import com.priyankaj.mybakingapp.models.MainModel;
import com.priyankaj.mybakingapp.models.StepsModel;
import com.priyankaj.mybakingapp.utils.Constants;
import com.priyankaj.mybakingapp.utils.OnItemTouchListener;
import com.priyankaj.mybakingapp.widgets.HomeWidget;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.priyankaj.mybakingapp.utils.Constants.MyPREFERENCES;

public class ListActivity extends AppCompatActivity {

    @BindView(R.id.ingredientsviewtext)
    TextView ingredientsnumber;
    @BindView(R.id.stepsrecyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.ingredientcardview)
    CardView cardView;
    @BindView(R.id.addtowidget)
    TextView addtowidget;

    private StepsAdapter adapter;
    private ArrayList<StepsModel> stepList;
    private ArrayList<IngredientsModel> ingredientsList;
    private MainModel recipeModel;
    private boolean twoPane;
    private int position;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            recipeModel = bundle.getParcelable("data");
            position = bundle.getInt("position");
            assert recipeModel != null;
            String recipeName = recipeModel.getHome_name();
            getSupportActionBar().setTitle(recipeName);

        }
        final SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        addtowidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit = sharedpreferences.edit();
                edit.putString(Constants.RECEIPE_NAME, recipeModel.getHome_name());
                edit.putInt(Constants.RECEIPE_POSITION, position);
                edit.commit();
                Intent intent = new Intent(getApplicationContext(), HomeWidget.class);
                intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
                int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), HomeWidget.class));
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                sendBroadcast(intent);


                //  HomeWidget myWidget = new HomeWidget();
                //  myWidget.onUpdate(getApplicationContext(), AppWidgetManager.getInstance(getApplicationContext()),ids);

                Toast.makeText(getApplicationContext(), "Added To Widget", Toast.LENGTH_SHORT).show();

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        stepList = recipeModel.getSteps();
        ingredientsList = recipeModel.getIngredients();
        adapter = new StepsAdapter(stepList, this);
        recyclerView.setAdapter(adapter);
        final FragmentManager fragmentManager = getSupportFragmentManager();

        twoPane = findViewById(R.id.fulldetailsframelayout) != null;

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (twoPane) {
                    Log.v("inside frag create 3 ", "lol");
                    IngredientsFragement ingredientsFragement = new IngredientsFragement();
                    ingredientsFragement.setList(ingredientsList);
                    fragmentManager.beginTransaction()
                            .replace(R.id.fulldetailsframelayout, ingredientsFragement)
                            .commit();

                } else {
                    Log.v("inside frag create ins", "lol");
                    Bundle ingredients_bundel = new Bundle();
                    ingredients_bundel.putParcelableArrayList("ingredient_bundle", ingredientsList);
                    Intent i1 = new Intent(getApplicationContext(), DetailsActivity.class);
                    i1.putExtras(ingredients_bundel);
                    startActivity(i1);

                }
            }
        });

        recyclerView.addOnItemTouchListener(new OnItemTouchListener(this, new OnItemTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (twoPane) {
                    if (savedInstanceState == null) {
                        Log.v("inside frag create ", "lol");
                        VideosFragment videosFragment = new VideosFragment();
                        videosFragment.setPosition(position);
                        videosFragment.setStepList(stepList);
                        fragmentManager.beginTransaction()
                                .replace(R.id.fulldetailsframelayout, videosFragment)
                                .commit();
                    }


                } else {
                    Log.v("inside frag create 2 ", "lol");
                    Bundle steps_bundle = new Bundle();
                    steps_bundle.putParcelableArrayList("steps_bundle", stepList);
                    steps_bundle.putInt("position", position);
                    Intent i2 = new Intent(getApplicationContext(), DetailsActivity.class);
                    i2.putExtras(steps_bundle);
                    startActivity(i2);
                }
            }
        }));
    }
}
