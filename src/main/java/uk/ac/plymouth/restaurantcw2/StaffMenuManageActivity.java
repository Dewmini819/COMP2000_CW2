package uk.ac.plymouth.restaurantcw2;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StaffMenuManageActivity extends AppCompatActivity {

    private ListView list;
    private ArrayAdapter<String> adapter;
    private List<MenuDao.MenuItem> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        if (!"staff".equalsIgnoreCase(SessionManager.getRole(this))) {
            finish();
            return;
        }

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(24, 24, 24, 24);

        Button btnAdd = new Button(this);
        btnAdd.setText("Add Menu Item");

        list = new ListView(this);

        root.addView(btnAdd);
        root.addView(list);

        setContentView(root);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        list.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> showAddDialog());

        list.setOnItemClickListener((p, v, pos, id) -> {
            if (pos >= 0 && pos < items.size()) showDeleteDialog(items.get(pos));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    private void load() {
        MenuDao dao = new MenuDao(this);
        items = dao.all();

        List<String> rows = new ArrayList<>();
        for (MenuDao.MenuItem m : items) {
            rows.add(m.name + "  £" + String.format(Locale.UK, "%.2f", m.price));
        }

        adapter.clear();
        adapter.addAll(rows);
        adapter.notifyDataSetChanged();
    }

    private void showAddDialog() {
        EditText name = new EditText(this);
        name.setHint("Item name");

        EditText price = new EditText(this);
        price.setHint("Price");
        price.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.addView(name);
        box.addView(price);

        new AlertDialog.Builder(this)
                .setTitle("Add Menu Item")
                .setView(box)
                .setPositiveButton("Add", (d, w) -> {
                    String n = name.getText().toString().trim();
                    String pStr = price.getText().toString().trim();
                    if (n.isEmpty() || pStr.isEmpty()) return;

                    double pVal;
                    try {
                        pVal = Double.parseDouble(pStr);
                    } catch (Exception e) {
                        return;
                    }

                    new MenuDao(this).add(n, pVal);
                    load();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showDeleteDialog(MenuDao.MenuItem m) {
        new AlertDialog.Builder(this)
                .setTitle("Delete item?")
                .setMessage(m.name)
                .setPositiveButton("Delete", (d, w) -> {
                    new MenuDao(this).delete(m.id);
                    load();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
