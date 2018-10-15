package org.ligi.fast;

import android.content.Context;
import android.util.Log;

import org.ligi.fast.model.AppInfo;
import org.ligi.fast.model.DynamicAppInfoList;
import org.ligi.fast.settings.AndroidFASTSettings;
import org.ligi.fast.settings.FASTSettings;

import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.dashbench.api.DefaultBenchmarkEndedCallback;
import br.ufpe.cin.dashbench.api.IBenchmark;
import br.ufpe.cin.dashbench.configuration.BenchmarkConfiguration;

public class Benchmark implements IBenchmark {
    private DynamicAppInfoList dynList;
    private List<AppInfo> notEmpty;
    private List<AppInfo> empty;

    public Benchmark(FASTSettings settings,Context ctx) {
        this.notEmpty = new ArrayList<AppInfo>();
        String separator = ";;";
        for(int i = 0; i < 1000; i++) {
            //example: a;;b;;i;;c;;0
            String cache_str = String.format("a%sb%s%s%sc%s0",separator,separator,String.valueOf(i),separator,separator);
            this.notEmpty.add(new AppInfo(ctx, cache_str));
        }
        this.empty = new ArrayList<AppInfo>();
        this.dynList = new DynamicAppInfoList(empty, settings);
    }

    @Override
    public String getName() {
        return "FastSearch";
    }

    @Override
    public void runWarmUp() {
        for (int i = 0; i < BenchmarkConfiguration.getInstance().getNumberOfWarmUpIterations(); i++) {
            this.dynList.update(this.notEmpty);
            this.dynList.update(this.empty);
        }
    }

    @Override
    public void runBenchmark() {
        for (int i = 0; i < BenchmarkConfiguration.getInstance().getNumberOfIterations(); i++) {
            this.dynList.update(this.notEmpty);
            this.dynList.update(this.empty);
        }
        (new DefaultBenchmarkEndedCallback()).execute();
    }
}
