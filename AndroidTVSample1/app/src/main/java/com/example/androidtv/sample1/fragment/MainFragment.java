package com.example.androidtv.sample1.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.BrowseSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.FocusHighlight;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.PageRow;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.leanback.widget.VerticalGridPresenter;
import androidx.preference.PreferenceManager;

import com.example.androidtv.sample1.R;
import com.example.androidtv.sample1.activity.WebViewActivity;
import com.example.androidtv.sample1.models.Card;
import com.example.androidtv.sample1.models.CardRow;
import com.example.androidtv.sample1.presenters.CardPresenterSelector;
import com.example.androidtv.sample1.utils.Utils;
import com.google.gson.Gson;


public class MainFragment extends BrowseSupportFragment {

    private static final long HEADER_ID_1 = 1;
    private static final String HEADER_NAME_1 = "メイン";
    private static final long HEADER_ID_2 = 2;
    private static final String HEADER_NAME_2 = "設定";

    private BackgroundManager mBackgroundManager;

    private ArrayObjectAdapter mRowsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupUi();
        loadData();

        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        getMainFragmentRegistry().registerFragment(PageRow.class,
                new PageRowFragmentFactory(mBackgroundManager));
    }

    private void setupUi() {
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);
        setBrandColor(getResources().getColor(R.color.navigation_background));
        setTitle("TVアプリサンプル1");

        prepareEntranceTransition();
    }

    private void loadData() {
        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        setAdapter(mRowsAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                createRows();
                startEntranceTransition();
            }
        }, 2000);
    }

    private void createRows() {
        HeaderItem headerItem1 = new HeaderItem(HEADER_ID_1, HEADER_NAME_1);
        PageRow pageRow1 = new PageRow(headerItem1);
        mRowsAdapter.add(pageRow1);

        HeaderItem headerItem2 = new HeaderItem(HEADER_ID_2, HEADER_NAME_2);
        PageRow pageRow2 = new PageRow(headerItem2);
        mRowsAdapter.add(pageRow2);
    }

    private static class PageRowFragmentFactory extends FragmentFactory {
        private final BackgroundManager mBackgroundManager;

        PageRowFragmentFactory(BackgroundManager backgroundManager) {
            this.mBackgroundManager = backgroundManager;
        }

        @Override
        public Fragment createFragment(Object rowObj) {
            Row row = (Row)rowObj;
            mBackgroundManager.setDrawable(null);
            if (row.getHeaderItem().getId() == HEADER_ID_1) {
                return new SampleFragmentA();
            } else if (row.getHeaderItem().getId() == HEADER_ID_2) {
                return new SamplePreferenceFragment();
            }

            throw new IllegalArgumentException(String.format("Invalid row %s", rowObj));
        }
    }

    public static class SampleFragmentA extends GridFragment {
        private static final int COLUMNS = 4;
        private final int ZOOM_FACTOR = FocusHighlight.ZOOM_FACTOR_SMALL;
        private ArrayObjectAdapter mAdapter;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setupAdapter();
            loadData();
            getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());
        }


        private void setupAdapter() {
            VerticalGridPresenter presenter = new VerticalGridPresenter(ZOOM_FACTOR);
            presenter.setNumberOfColumns(COLUMNS);
            setGridPresenter(presenter);

            CardPresenterSelector cardPresenter = new CardPresenterSelector(getActivity());
            mAdapter = new ArrayObjectAdapter(cardPresenter);
            setAdapter(mAdapter);

            setOnItemViewClickedListener(new OnItemViewClickedListener() {
                @Override
                public void onItemClicked(
                        Presenter.ViewHolder itemViewHolder,
                        Object item,
                        RowPresenter.ViewHolder rowViewHolder,
                        Row row) {
                    Card card = (Card)item;
                    /*
                    Toast.makeText(getActivity(),
                            "Clicked on "+card.getTitle(),
                            Toast.LENGTH_SHORT).show();
                     */
                    Intent intent = new Intent(getActivity().getApplicationContext(), WebViewActivity.class);
                    startActivity(intent);
                }
            });
        }

        private void loadData() {
            String json = Utils.inputStreamToString(getResources().openRawResource(
                    R.raw.grid_example));
            CardRow cardRow = new Gson().fromJson(json, CardRow.class);
            mAdapter.addAll(0, cardRow.getCards());
        }
    }

    public static class SamplePreferenceFragment extends Fragment implements BrowseSupportFragment.MainFragmentAdapterProvider {

        private MainFragmentAdapter mMainFragmentAdapter = new MainFragmentAdapter(this);

        @Override
        public MainFragmentAdapter getMainFragmentAdapter() {
            return mMainFragmentAdapter;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getMainFragmentAdapter().getFragmentHost().showTitleView(false);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.preference_fragment, container, false);

            EditText editText = (EditText) view.findViewById(R.id.webview_url);

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String url = sp.getString("webview_url", "");
            if (url != "") {
                editText.setText(url);
            }

            Button registButton = (Button) view.findViewById(R.id.regist);
            registButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String newUrl = String.valueOf(editText.getText());
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("webview_url", newUrl);
                    editor.apply();
                }
            });

            return view;
        }
    }
}