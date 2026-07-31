package ua.gomin.messenger.alerts;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONObject;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalFragment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Pair;

public class AirAlertStatsActivity extends UniversalFragment {

    private final List<Pair<Integer, String>> regions = AirAlertRegions.getAllAsInt();

    private Map<Integer, Boolean> cachedResults = new HashMap<>();
    private long lastFetchTime = 0L;
    private boolean isLoading = false;
    private SwipeRefreshLayout swipeRefresh = null;
    private boolean dataLoaded = false;
    private boolean isDestroyed = false;

    @Override
    public CharSequence getTitle() {
        return LocaleController.getString("AirAlertStats_Header", R.string.AirAlertStats_Header);
    }

    @Override
    public View createView(Context context) {
        View baseView = super.createView(context);

        swipeRefresh = new SwipeRefreshLayout(context);
        swipeRefresh.addView(baseView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        swipeRefresh.setColorSchemeColors(Theme.getColor(Theme.key_windowBackgroundWhiteBlueHeader));
        swipeRefresh.setProgressBackgroundColorSchemeColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        swipeRefresh.setOnRefreshListener(() -> {
            cachedResults.clear();
            lastFetchTime = 0L;
            loadData();
        });
        
        fragmentView = swipeRefresh;

        loadData();
        return swipeRefresh;
    }

    @Override
    public void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
        if (isLoading && cachedResults.isEmpty()) {
            items.add(buildLoadingItem());
            return;
        }
        if (cachedResults.isEmpty() && dataLoaded) {
            items.add(buildErrorItem());
            return;
        }
        if (cachedResults.isEmpty()) return;

        items.add(UItem.asShadow(LocaleController.getString("AirAlertStats_Header", R.string.AirAlertStats_Header)));
        for (Pair<Integer, String> region : regions) {
            int id = region.first;
            String name = region.second;
            boolean hasAlert = cachedResults.containsKey(id) && cachedResults.get(id);
            items.add(buildRegionItem(id, name, hasAlert));
        }
    }

    @Override
    public void onClick(UItem item, View view, int position, float x, float y) {}

    @Override
    public boolean onLongClick(UItem item, View view, int position, float x, float y) {
        return false;
    }

    private void loadData() {
        if (System.currentTimeMillis() - lastFetchTime < 30000 && !cachedResults.isEmpty()) {
            if (listView != null) {
                listView.adapter.update(false);
            }
            return;
        }
        isLoading = true;
        dataLoaded = false;
        if (swipeRefresh != null) {
            swipeRefresh.setRefreshing(true);
        }
        if (listView != null) {
            listView.adapter.update(false);
        }
        fetchAllRegions();
    }

    private void fetchAllRegions() {
        Utilities.globalQueue.postRunnable(() -> {
            Map<Integer, Boolean> results = new HashMap<>();
            try {
                URL url = new URL("https://air-alert-backend.vova28061991.workers.dev/status/all");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                if (connection.getResponseCode() == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();

                    JSONObject json = new JSONObject(sb.toString());
                    for (Pair<Integer, String> region : regions) {
                        results.put(region.first, json.optBoolean(String.valueOf(region.first), false));
                    }
                }
            } catch (Exception e) {
                FileLog.e(e);
            }

            AndroidUtilities.runOnUIThread(() -> {
                if (isDestroyed || getParentActivity() == null) return;
                cachedResults = results;
                lastFetchTime = System.currentTimeMillis();
                isLoading = false;
                dataLoaded = true;
                if (swipeRefresh != null) {
                    swipeRefresh.setRefreshing(false);
                }
                if (listView != null) {
                    listView.adapter.update(false);
                }
            });
        });
    }

    private UItem buildRegionItem(int id, String name, boolean hasAlert) {
        Context ctx = getContext();
        if (ctx == null) ctx = ApplicationLoader.applicationContext;
        return UItem.asCustom(id, createRegionView(ctx, name, hasAlert));
    }

    private View createRegionView(Context context, String name, boolean hasAlert) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        layout.setPadding(
            AndroidUtilities.dp(20f), AndroidUtilities.dp(12f),
            AndroidUtilities.dp(20f), AndroidUtilities.dp(12f)
        );

        TextView text = new TextView(context);
        text.setText(name);
        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18f);
        text.setTextColor(hasAlert ? 0xFFE53935 : Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        text.setTypeface(AndroidUtilities.getTypeface("fonts/gomin_medium.ttf"));
        text.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        layout.addView(text, LayoutHelper.createLinear(0, LayoutHelper.WRAP_CONTENT, 1f));

        TextView status = new TextView(context);
        status.setText(LocaleController.getString(
            hasAlert ? "AirAlertStats_StatusAlert" : "AirAlertStats_StatusQuiet",
            hasAlert ? R.string.AirAlertStats_StatusAlert : R.string.AirAlertStats_StatusQuiet
        ));
        status.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12f);
        status.setTextColor(hasAlert ? 0xFFE53935 : 0xFF4CAF50);
        status.setTypeface(AndroidUtilities.getTypeface("fonts/gomin_medium.ttf"));
        status.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        layout.addView(status, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        return layout;
    }

    private UItem buildLoadingItem() {
        Context ctx = getContext();
        if (ctx == null) ctx = ApplicationLoader.applicationContext;

        LinearLayout layout = new LinearLayout(ctx);
        layout.setGravity(Gravity.CENTER);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(0, AndroidUtilities.dp(48f), 0, AndroidUtilities.dp(48f));

        ProgressBar spinner = new ProgressBar(ctx);
        layout.addView(spinner, LayoutHelper.createLinear(48, 48));

        TextView label = new TextView(ctx);
        label.setText(LocaleController.getString("AirAlertStats_Loading", R.string.AirAlertStats_Loading));
        label.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f);
        label.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));
        label.setGravity(Gravity.CENTER);
        label.setPadding(0, AndroidUtilities.dp(16f), 0, 0);
        layout.addView(label, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        return UItem.asCustom(layout);
    }

    private UItem buildErrorItem() {
        Context ctx = getContext();
        if (ctx == null) ctx = ApplicationLoader.applicationContext;

        LinearLayout layout = new LinearLayout(ctx);
        layout.setGravity(Gravity.CENTER);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(0, AndroidUtilities.dp(48f), 0, AndroidUtilities.dp(48f));

        TextView label = new TextView(ctx);
        label.setText(LocaleController.getString("AirAlertStats_Error", R.string.AirAlertStats_Error));
        label.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f);
        label.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));
        label.setGravity(Gravity.CENTER);
        layout.addView(label, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        TextView retry = new TextView(ctx);
        retry.setText(LocaleController.getString("AirAlertStats_Retry", R.string.AirAlertStats_Retry));
        retry.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f);
        retry.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueHeader));
        retry.setGravity(Gravity.CENTER);
        retry.setPadding(0, AndroidUtilities.dp(16f), 0, 0);
        retry.setOnClickListener(v -> loadData());
        layout.addView(retry, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        return UItem.asCustom(layout);
    }

    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        isDestroyed = true;
    }
}
