package com.quandoo.azizbekian.test.common;

import android.content.Context;

import com.quandoo.azizbekian.BaseApplication;
import com.quandoo.azizbekian.QuandooApplication;
import com.quandoo.azizbekian.data.DataManager;
import com.quandoo.azizbekian.test.common.injection.component.DaggerTestComponent;
import com.quandoo.azizbekian.test.common.injection.component.TestComponent;
import com.quandoo.azizbekian.test.common.injection.module.ApplicationTestModule;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * Created on Aug 06, 2016.
 *
 * @author Andranik Azizbekian (andranik.azizbekyan@gmail.com)
 */
public class TestComponentRule implements TestRule {

    private final TestComponent mTestComponent;
    private final Context mContext;

    public TestComponentRule(Context context) {
        mContext = context;
        QuandooApplication application = QuandooApplication.get(context);
        mTestComponent = DaggerTestComponent.builder()
                .applicationTestModule(new ApplicationTestModule(application))
                .build();
    }

    public DataManager getMockDataManager() {
        return mTestComponent.getDataManager();
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                BaseApplication application = QuandooApplication.get(mContext);
                application.setComponent(mTestComponent);
                base.evaluate();
                application.setComponent(null);
            }
        };
    }
}
