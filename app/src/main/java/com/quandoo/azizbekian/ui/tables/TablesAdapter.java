package com.quandoo.azizbekian.ui.tables;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quandoo.azizbekian.R;
import com.quandoo.azizbekian.data.model.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on Aug 03, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
public class TablesAdapter extends RecyclerView.Adapter<TablesAdapter.TablesViewHolder> {

    private static final String PLACEHOLDER_TABLE_NUMBER = "%d";

    private List<Table> mTables;
    private final TablesPresenter mTablesPresenter;

    @Inject
    public TablesAdapter(TablesPresenter tablesPresenter) {
        mTablesPresenter = tablesPresenter;
        mTables = new ArrayList<>();
    }

    public void setTables(List<Table> tables) {
        mTables = tables;
    }

    @Override
    public TablesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_table, parent, false);
        return new TablesViewHolder(itemView);
    }

    @Override public void onBindViewHolder(TablesViewHolder holder, int position) {
        Table table = mTables.get(position);
        holder.tableNumber.setText(String.format(Locale.getDefault(),
                PLACEHOLDER_TABLE_NUMBER, position));
        holder.itemView.setBackgroundResource(table.isAvailable()
                ? R.drawable.ripple_table_available : R.drawable.ripple_table_not_available);
    }

    @Override public int getItemCount() {
        return mTables.size();
    }

    class TablesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.table_number) TextView tableNumber;

        public TablesViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override public void onClick(View view) {
            mTablesPresenter.onTableClicked(getAdapterPosition());
        }
    }
}
