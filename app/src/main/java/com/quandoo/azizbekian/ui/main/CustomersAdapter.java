package com.quandoo.azizbekian.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quandoo.azizbekian.R;
import com.quandoo.azizbekian.data.model.Customer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on Aug 03, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
public class CustomersAdapter extends RecyclerView.Adapter<CustomersAdapter.CustomerViewHolder> {

    public static final String PLACEHOLDER_CUSTOMER_NAME = "%s %s";

    private List<Customer> mCustomers;
    private final MainPresenter mMainPresenter;

    @Inject
    public CustomersAdapter(MainPresenter mainPresenter) {
        mMainPresenter = mainPresenter;
        mCustomers = new ArrayList<>();
    }

    public void setCustomers(List<Customer> customers) {
        mCustomers = customers;
    }

    @Override
    public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_customer, parent, false);
        return new CustomerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomerViewHolder holder, int position) {
        Customer customer = mCustomers.get(position);
        holder.customerName.setText(String.format(PLACEHOLDER_CUSTOMER_NAME,
                customer.customerFirstName(), customer.customerLastName()));

    }

    @Override public int getItemCount() {
        return mCustomers.size();
    }

    public void animateTo(List<Customer> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<Customer> newModels) {
        for (int i = mCustomers.size() - 1; i >= 0; i--) {
            final Customer model = mCustomers.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Customer> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Customer model = newModels.get(i);
            if (!mCustomers.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Customer> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Customer model = newModels.get(toPosition);
            final int fromPosition = mCustomers.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public Customer removeItem(int position) {
        final Customer model = mCustomers.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Customer model) {
        mCustomers.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Customer model = mCustomers.remove(fromPosition);
        mCustomers.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    class CustomerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.customer_name) TextView customerName;

        public CustomerViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override public void onClick(View view) {
            mMainPresenter.onCustomerClicked(mCustomers.get(getAdapterPosition()));
        }
    }
}
