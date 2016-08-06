package com.quandoo.azizbekian.ui.main;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.quandoo.azizbekian.R;
import com.quandoo.azizbekian.data.model.Customer;
import com.quandoo.azizbekian.test.common.TestComponentRule;
import com.quandoo.azizbekian.test.common.TestDataFactory;
import com.quandoo.azizbekian.util.RecyclerViewInteraction;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.util.List;

import rx.Observable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.when;

/**
 * Created on Aug 06, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    public final TestComponentRule component =
            new TestComponentRule(InstrumentationRegistry.getTargetContext());

    public final ActivityTestRule<MainActivity> main =
            new ActivityTestRule<MainActivity>(MainActivity.class, false, false) {
                @Override
                protected Intent getActivityIntent() {
                    // Override the default intent so we pass a false flag for syncing so it doesn't
                    // start a sync service in the background that would affect  the behaviour of
                    // this test.
                    return MainActivity.getStartIntent(InstrumentationRegistry.getTargetContext(),
                            false);
                }
            };

    // TestComponentRule needs to go first to make sure the Dagger ApplicationTestComponent is set
    // in the Application before any Activity is launched.
    @Rule
    public final TestRule chain = RuleChain.outerRule(component).around(main);

    @Test
    public void listOfCustomersShows() {
        List<Customer> customers = TestDataFactory.MOCK_CUSTOMER_LIST;
        when(component
                .getMockDataManager()
                .getCustomers(anyBoolean()))
                .thenReturn(Observable.just(customers));

        main.launchActivity(null);

        int position = 0;
        for (Customer customer : customers) {
            onView(withId(R.id.recycler_customers))
                    .perform(RecyclerViewActions.scrollToPosition(position));
            String name = String.format(CustomersAdapter.PLACEHOLDER_CUSTOMER_NAME,
                    customer.customerFirstName(),
                    customer.customerLastName());
            onView(withText(name))
                    .check(matches(isDisplayed()));
            position++;
        }
    }

    @Test
    public void sortListFromSearch() {
        List<Customer> customers = TestDataFactory.MOCK_CUSTOMER_LIST;
        String query = "a";
        when(component
                .getMockDataManager()
                .getCustomers(anyBoolean()))
                .thenReturn(Observable.just(customers));

        main.launchActivity(null);

        onView(withId(R.id.search)).perform(click());
        onView(withId(R.id.search_src_text))
                .perform(typeText(query));

        RecyclerViewInteraction.
                <Customer>onRecyclerView(withId(R.id.recycler_customers))
                .withItems(MainPresenter.filter(customers, query))
                .check((customer, view, e) -> matches(hasDescendant(
                        withText(
                                String.format(CustomersAdapter.PLACEHOLDER_CUSTOMER_NAME,
                                        customer.customerFirstName(),
                                        customer.customerLastName()))))
                        .check(view, e));
    }

}