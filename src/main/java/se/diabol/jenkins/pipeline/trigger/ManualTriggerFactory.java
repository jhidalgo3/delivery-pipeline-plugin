package se.diabol.jenkins.pipeline.trigger;

import au.com.centrumsystems.hudson.plugin.buildpipeline.trigger.BuildPipelineTrigger;
import hudson.model.AbstractProject;
import jenkins.model.Jenkins;
import se.diabol.jenkins.pipeline.domain.trigger.ManualTrigger;
import se.diabol.jenkins.pipeline.util.ProjectUtil;


/**
 * Created by patbos on 25/03/14.
 */
public class ManualTriggerFactory {
    public static ManualTrigger getManualTrigger(AbstractProject<?, ?> project, AbstractProject<?, ?> downstream) {

         BuildPipelineTrigger bppTrigger = downstream.getPublishersList().get(BuildPipelineTrigger.class);
         if (bppTrigger != null) {
             String names = bppTrigger.getDownstreamProjectNames();
             if (ProjectUtil.getProjectList(names, Jenkins.getInstance(), null).contains(project)) {
                 return new BPPManualTrigger();
             }
         }
         return null;
     }

}
