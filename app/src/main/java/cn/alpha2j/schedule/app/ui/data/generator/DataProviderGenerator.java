package cn.alpha2j.schedule.app.ui.data.generator;

import android.support.annotation.NonNull;

import cn.alpha2j.schedule.app.ui.data.provider.DataProvider;

/**
 * @author alpha
 */
public interface DataProviderGenerator {

    /**
     * 生成DataProvider
     *
     * @return DataProvider
     */
    @NonNull
    DataProvider generate();
}
