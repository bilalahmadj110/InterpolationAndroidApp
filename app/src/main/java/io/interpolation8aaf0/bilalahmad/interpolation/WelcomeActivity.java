package io.interpolation8aaf0.bilalahmad.interpolation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import io.interpolation8aaf0.bilalahmad.interpolation.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static io.interpolation8aaf0.bilalahmad.interpolation.R.*;

public class WelcomeActivity extends AppCompatActivity {
    HorizontalScrollView horizontalScrollView;
    ArrayList<LinearLayout> views = new ArrayList<>();
    ArrayList<TextBoxes> textBoxes = new ArrayList<>();
    double ans;
    AppCompatImageButton addButton, removeButton, clearButton;
    CommonClass commonClass = new CommonClass();
    AppCompatButton calculate;
    TextView notification;
    EditText calculateAt;
    LinearLayout mainLayout;
    Spinner dropDownSelection;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        horizontalScrollView = findViewById(id.scroller);

        mainLayout = findViewById(id.add_entry_to);

        dropDownSelection = findViewById(id.select_method);
        notification = findViewById(id.notification);
        addButton = findViewById(id.add_button);
        removeButton = findViewById(id.remove_button);
        clearButton = findViewById(id.clear_button);
        calculate = findViewById(id.calculate);
        calculateAt = findViewById(id.calculate_at);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEntry();
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeEntry();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearEntries();
            }
        });

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    if (dropDownSelection.getSelectedItemPosition() == 5)
                        calculateLagrange();
                    else if (dropDownSelection.getSelectedItemPosition() == 0)
                        calculateForward();
                    else if (dropDownSelection.getSelectedItemPosition() == 1)
                        calculateBackward();
                    else {
                        commonClass.showDialog(
                                Html.fromHtml("Coming soon!"),
                                Html.fromHtml(
                                        "This calculation isn't available yet, will be available in coming versions."), drawable.ic_error_outline_black_24dp,
                                WelcomeActivity.this);
                    }

                }
            }
        });
        if (savedInstanceState != null) {
            calculateAt.setText(savedInstanceState.getString("calculate-at", ""));
            int totalSize = savedInstanceState.getInt("size", 0);
            for (int i = 0; i < totalSize; i++)
                addEntry();
            changeAllText(savedInstanceState, totalSize);
        } else {
            addEntry();
        }
    }

    public void changeAllText(Bundle saved, int size) {
        for (int i = 0; i < size; i++) {
            ((EditText) findViewById(textBoxes.get(i).getYId())).setText(saved.getString("-" + (i + 1), ""));
            ((EditText) findViewById(textBoxes.get(i).getXId())).setText(saved.getString("" + (i + 1), ""));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case id.help:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addEntry() {
        count++;
        LayoutInflater inflater = getLayoutInflater();
        View otherView = inflater.inflate(layout.xy_container, null);
        LinearLayout layout = otherView.findViewById(id.add_entry_from);
        ((TextView) otherView.findViewById(id.entry_no)).setText("Entry - " + count);
        int x = count;
        int y = count * 1000;
        TextBoxes textBoxes1 = new TextBoxes(x, y);
        otherView.findViewById(id.x_value).setId(x);
        otherView.findViewById(id.y_value).setId(y);

        textBoxes.add(textBoxes1);
        ((ViewGroup) layout.getParent()).removeView(layout);
        mainLayout.addView(layout);

        Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_right_in);
        animation.setStartOffset(10);
        layout.startAnimation(animation);
        views.add(layout);
        horizontalScrollView.postDelayed(new Runnable() {
            public void run() {
                horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        }, 100L);
    }

    public void removeEntry() {
        if (count > 1) {
            mainLayout.removeView(views.get(count - 1));
            views.remove(count - 1);
            textBoxes.remove(count - 1);
            count--;
        }
    }

    public void clearEntries() {
        while (count > 1)
            removeEntry();
        ((EditText) findViewById(textBoxes.get(0).getXId())).setText("");
        ((EditText) findViewById(textBoxes.get(0).getYId())).setText("");

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("size", views.size());
        outState.putString("calculate-at", calculateAt.getText().toString());
        for (int i = 0; i < views.size(); i++) {
            outState.putString("" + (i + 1), ((EditText) findViewById(textBoxes.get(i).getXId())).getText().toString());
            outState.putString("-" + (i + 1), ((EditText) findViewById(textBoxes.get(i).getYId())).getText().toString());
        }
    }

    public boolean validate(EditText textBox) {
        try {
            if (TextUtils.isEmpty(textBox.getText().toString()))
                return true;
            Double.parseDouble(textBox.getText().toString());
        } catch (Exception e) {
            return true;
        }
        return false;
    }

    public boolean validateInput() {
        boolean x = false, y = false, find = false;
        int i;
        for (i = 0; i < count; i++) {
            if (validate((EditText) findViewById(textBoxes.get(i).getXId()))) {
                x = true;
                break;
            } else if (validate((EditText) findViewById(textBoxes.get(i).getYId()))) {
                y = true;
                break;
            } else if (validate((EditText) (findViewById(id.calculate_at)))) {
                find = true;
                break;
            }
        }
        if (!(!x && (!y && !find)))
            commonClass.showDialog(
                    Html.fromHtml("<b>Field Error</b>"),
                    Html.fromHtml("Please enter valid x/y values in all the required fields!"),
                    drawable.ic_error_outline_black_24dp, WelcomeActivity.this);
        if (x)
            findViewById(textBoxes.get(i).getXId()).requestFocus();
        else if (y)
            findViewById(textBoxes.get(i).getYId()).requestFocus();
        else if (find)
            findViewById(id.calculate_at).requestFocus();
        return (!x && (!y && !find));
    }

    public void calculateBackward() {
        NewtonBackwardFormula backwardFormula = new NewtonBackwardFormula();
        ans = backwardFormula.calculateBackward(getX(), getY(), Double.parseDouble(
                ((EditText) (findViewById(id.calculate_at))).getText().toString()));
        commonClass.showDialog(
                Html.fromHtml("<b>Newton Backward Result</b>"),
                Html.fromHtml(
                        "The value of f(x) at x = " + ((EditText) (findViewById(id.calculate_at))).getText().toString() +
                                " is <b>" + ans + "</b>."), drawable.ic_info_black_24dp,
                WelcomeActivity.this);
        saveValues();
    }

    public void calculateForward() {
        NewtonForwardFormula forwardFormula = new NewtonForwardFormula();
        ans = forwardFormula.calculateForward(getX(), getY(), Double.parseDouble(
                ((EditText) (findViewById(id.calculate_at))).getText().toString()));
        commonClass.showDialog(
                Html.fromHtml("<b>Newton Forward Result</b>"),
                Html.fromHtml(
                        "The value of y at x = " + ((EditText) (findViewById(id.calculate_at))).getText().toString() +
                                " is <b>" + ans + "</b>."), drawable.ic_info_black_24dp,
                WelcomeActivity.this);
        saveValues();
    }

    public double[] getX() {
        double[] xValues = new double[count];
        for (int i = 0; i < count; i++) {
            xValues[i] = Double.parseDouble(((EditText) findViewById(textBoxes.get(i).getXId())).getText().toString());
//            yValues[i] = Double.parseDouble(((EditText) findViewById(textBoxes.get(i).getYId())).getText().toString());
        }
        return xValues;
    }

    public double[] getY() {
        double[] yValues = new double[count];
        for (int i = 0; i < count; i++) {
//            xValues[i] = Double.parseDouble(((EditText) findViewById(textBoxes.get(i).getXId())).getText().toString());
            yValues[i] = Double.parseDouble(((EditText) findViewById(textBoxes.get(i).getYId())).getText().toString());
        }
        return yValues;
    }

    public void calculateLagrange() {

        ans = LagrangeFormula.calculateLagrangeValue(
                getX(), getY(), Double.parseDouble(((EditText) (findViewById(id.calculate_at))).getText().toString()));
        commonClass.showDialog(
                Html.fromHtml("Lagrange Result"),
                Html.fromHtml(
                        "The value of f(x) at x = " + ((EditText) (findViewById(id.calculate_at))).getText().toString() +
                                " is <b>" + ans + "</b>."), drawable.ic_info_black_24dp,
                WelcomeActivity.this);
        saveValues();
    }

    //   This method would be called when you calculate some input...
    public void saveValues() {
        double[] xValues = new double[count];
        double[] yValues = new double[count];
        Map<String, Object> user = new HashMap<>();

        for (int i = 0; i < count; i++) {
            xValues[i] = Double.parseDouble(((EditText) findViewById(textBoxes.get(i).getXId())).getText().toString());
            yValues[i] = Double.parseDouble(((EditText) findViewById(textBoxes.get(i).getYId())).getText().toString());
        }

    }
}

