package se.diabol.jenkins.pipeline.trigger;

import au.com.centrumsystems.hudson.plugin.buildpipeline.BuildPipelineView;
import au.com.centrumsystems.hudson.plugin.buildpipeline.DownstreamProjectGridBuilder;
import hudson.model.ItemGroup;
import hudson.model.TopLevelItem;
import se.diabol.jenkins.pipeline.domain.trigger.ManualTrigger;

public class BPPManualTrigger implements ManualTrigger {
    @Override
    public void triggerManual(String projectName, String upstreamName, String buildId, ItemGroup<? extends TopLevelItem> itemGroup) {
        BuildPipelineView view = new BuildPipelineView("", "", new DownstreamProjectGridBuilder(upstreamName), "1", false, "");
        view.triggerManualBuild(Integer.parseInt(buildId), projectName, upstreamName);
    }

}
