package se.diabol.jenkins.pipeline.domain.trigger;

import hudson.model.ItemGroup;
import hudson.model.TopLevelItem;

public interface ManualTrigger {
    void triggerManual(String projectName, String upstreamName,
                       String buildId, ItemGroup<? extends TopLevelItem> itemGroup) throws TriggerException;

}
